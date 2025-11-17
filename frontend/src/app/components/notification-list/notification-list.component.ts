import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CustomerNotificationService } from '../../services/customer-notification.service';
import { CustomerNotification } from '../../models/customer-notification.model';

@Component({
  selector: 'app-notification-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './notification-list.component.html',
  styleUrls: ['./notification-list.component.css']
})
export class NotificationListComponent implements OnInit {
  notifications: CustomerNotification[] = [];
  selectedNotification: CustomerNotification | null = null;
  isEditing = false;
  isCreating = false;
  loading = false;
  errorMessage = '';
  successMessage = '';
  filterStatus: string = 'all';

  newNotification: CustomerNotification = {
    customerId: '',
    macAddress: '',
    ipAddress: '',
    serial: '',
    startDate: '',
    endDate: '',
    type: '',
    notified: false
  };

  constructor(private notificationService: CustomerNotificationService) { }

  ngOnInit(): void {
    this.loadNotifications();
  }

  loadNotifications(): void {
    this.loading = true;
    this.errorMessage = '';
    this.notificationService.getAllNotifications().subscribe({
      next: (data) => {
        this.notifications = data;
        this.loading = false;
      },
      error: (error) => {
        this.errorMessage = 'Failed to load notifications. Make sure the backend is running.';
        this.loading = false;
        console.error('Error loading notifications:', error);
      }
    });
  }

  getFilteredNotifications(): CustomerNotification[] {
    if (this.filterStatus === 'all') {
      return this.notifications;
    } else if (this.filterStatus === 'notified') {
      return this.notifications.filter(n => n.notified);
    } else if (this.filterStatus === 'unnotified') {
      return this.notifications.filter(n => !n.notified);
    }
    return this.notifications;
  }

  showCreateForm(): void {
    this.isCreating = true;
    this.isEditing = false;
    this.selectedNotification = null;
    const today = new Date().toISOString().split('T')[0];
    this.newNotification = {
      customerId: '',
      macAddress: '',
      ipAddress: '',
      serial: '',
      startDate: today,
      endDate: '',
      type: '',
      notified: false
    };
    this.clearMessages();
  }

  createNotification(): void {
    this.notificationService.createNotification(this.newNotification).subscribe({
      next: (notification) => {
        this.notifications.push(notification);
        this.isCreating = false;
        this.successMessage = 'Notification created successfully!';
        this.clearMessages(3000);
      },
      error: (error) => {
        this.errorMessage = 'Failed to create notification.';
        console.error('Error creating notification:', error);
      }
    });
  }

  editNotification(notification: CustomerNotification): void {
    this.selectedNotification = { ...notification };
    this.isEditing = true;
    this.isCreating = false;
    this.clearMessages();
  }

  updateNotification(): void {
    if (this.selectedNotification && this.selectedNotification.notificationId) {
      this.notificationService.updateNotification(this.selectedNotification.notificationId, this.selectedNotification).subscribe({
        next: (updatedNotification) => {
          const index = this.notifications.findIndex(n => n.notificationId === updatedNotification.notificationId);
          if (index !== -1) {
            this.notifications[index] = updatedNotification;
          }
          this.isEditing = false;
          this.selectedNotification = null;
          this.successMessage = 'Notification updated successfully!';
          this.clearMessages(3000);
        },
        error: (error) => {
          this.errorMessage = 'Failed to update notification.';
          console.error('Error updating notification:', error);
        }
      });
    }
  }

  toggleNotified(notification: CustomerNotification): void {
    if (!notification.notificationId) return;

    const service = notification.notified 
      ? this.notificationService.markAsUnnotified(notification.notificationId)
      : this.notificationService.markAsNotified(notification.notificationId);

    service.subscribe({
      next: (updatedNotification) => {
        const index = this.notifications.findIndex(n => n.notificationId === updatedNotification.notificationId);
        if (index !== -1) {
          this.notifications[index] = updatedNotification;
        }
        this.successMessage = `Notification marked as ${updatedNotification.notified ? 'notified' : 'unnotified'}!`;
        this.clearMessages(2000);
      },
      error: (error) => {
        this.errorMessage = 'Failed to update notification status.';
        console.error('Error updating notification status:', error);
      }
    });
  }

  deleteNotification(id: number | undefined): void {
    if (!id) return;
    
    if (confirm('Are you sure you want to delete this notification?')) {
      this.notificationService.deleteNotification(id).subscribe({
        next: () => {
          this.notifications = this.notifications.filter(n => n.notificationId !== id);
          this.successMessage = 'Notification deleted successfully!';
          this.clearMessages(3000);
        },
        error: (error) => {
          this.errorMessage = 'Failed to delete notification.';
          console.error('Error deleting notification:', error);
        }
      });
    }
  }

  cancelEdit(): void {
    this.isEditing = false;
    this.isCreating = false;
    this.selectedNotification = null;
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

  isExpired(notification: CustomerNotification): boolean {
    if (!notification.endDate) return false;
    const endDate = new Date(notification.endDate);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    return endDate < today;
  }
}

