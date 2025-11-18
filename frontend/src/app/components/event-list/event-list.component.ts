import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EventService } from '../../services/event.service';
import { Event } from '../../models/event.model';

@Component({
  selector: 'app-event-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './event-list.component.html',
  styleUrls: ['./event-list.component.css']
})
export class EventListComponent implements OnInit {
  events: Event[] = [];
  selectedEvent: Event | null = null;
  isEditing = false;
  isCreating = false;
  loading = false;
  errorMessage = '';
  successMessage = '';
  filterType: string = 'all';
  viewPayload: string | null = null;

  newEvent: Event = {
    type: '',
    macAddress: '',
    serial: '',
    message: '',
    date: '',
    payload: ''
  };

  constructor(private eventService: EventService) { }

  ngOnInit(): void {
    this.loadEvents();
  }

  loadEvents(): void {
    this.loading = true;
    this.errorMessage = '';
    this.eventService.getAllEvents().subscribe({
      next: (data) => {
        this.events = data;
        this.loading = false;
      },
      error: (error) => {
        this.errorMessage = 'Failed to load events. Make sure the backend is running.';
        this.loading = false;
        console.error('Error loading events:', error);
      }
    });
  }

  getFilteredEvents(): Event[] {
    if (this.filterType === 'all' || this.filterType === '') {
      return this.events;
    }
    return this.events.filter(e => e.type === this.filterType);
  }

  getUniqueTypes(): string[] {
    const types = this.events
      .map(e => e.type)
      .filter((type): type is string => type !== undefined && type !== null && type !== '');
    return Array.from(new Set(types));
  }

  showCreateForm(): void {
    this.isCreating = true;
    this.isEditing = false;
    this.selectedEvent = null;
    const now = new Date();
    const localDateTime = new Date(now.getTime() - now.getTimezoneOffset() * 60000).toISOString().slice(0, 16);
    this.newEvent = {
      type: '',
      macAddress: '',
      serial: '',
      message: '',
      date: localDateTime,
      payload: ''
    };
    this.clearMessages();
  }

  createEvent(): void {
    this.eventService.createEvent(this.newEvent).subscribe({
      next: (event) => {
        this.events.unshift(event); // Add to beginning of list
        this.isCreating = false;
        this.successMessage = 'Event created successfully!';
        this.clearMessages(3000);
      },
      error: (error) => {
        this.errorMessage = 'Failed to create event.';
        console.error('Error creating event:', error);
      }
    });
  }

  editEvent(event: Event): void {
    this.selectedEvent = { ...event };
    this.isEditing = true;
    this.isCreating = false;
    this.clearMessages();
  }

  updateEvent(): void {
    if (this.selectedEvent && this.selectedEvent.eventId) {
      this.eventService.updateEvent(this.selectedEvent.eventId, this.selectedEvent).subscribe({
        next: (updatedEvent) => {
          const index = this.events.findIndex(e => e.eventId === updatedEvent.eventId);
          if (index !== -1) {
            this.events[index] = updatedEvent;
          }
          this.isEditing = false;
          this.selectedEvent = null;
          this.successMessage = 'Event updated successfully!';
          this.clearMessages(3000);
        },
        error: (error) => {
          this.errorMessage = 'Failed to update event.';
          console.error('Error updating event:', error);
        }
      });
    }
  }

  deleteEvent(id: number | undefined): void {
    if (!id) return;
    
    if (confirm('Are you sure you want to delete this event?')) {
      this.eventService.deleteEvent(id).subscribe({
        next: () => {
          this.events = this.events.filter(e => e.eventId !== id);
          this.successMessage = 'Event deleted successfully!';
          this.clearMessages(3000);
        },
        error: (error) => {
          this.errorMessage = 'Failed to delete event.';
          console.error('Error deleting event:', error);
        }
      });
    }
  }

  viewPayloadDetails(payload: string | undefined): void {
    if (payload) {
      this.viewPayload = payload;
    }
  }

  closePayloadView(): void {
    this.viewPayload = null;
  }

  cancelEdit(): void {
    this.isEditing = false;
    this.isCreating = false;
    this.selectedEvent = null;
    this.clearMessages();
  }

  clearMessages(delay: number = 0): void {
    if (delay > 0) {
      setTimeout(() => {
        this.errorMessage = '';
        this.successMessage = '';
      }, delay);
    } else {
      this.errorMessage = '';
      this.successMessage = '';
    }
  }
}

