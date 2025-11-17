import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MessageService } from '../../services/message.service';
import { Message } from '../../models/message.model';

@Component({
  selector: 'app-message-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './message-list.component.html',
  styleUrls: ['./message-list.component.css']
})
export class MessageListComponent implements OnInit {
  messages: Message[] = [];
  selectedMessage: Message | null = null;
  isEditing = false;
  isCreating = false;
  loading = false;
  errorMessage = '';
  successMessage = '';
  filterType: string = 'all';
  searchText: string = '';

  newMessage: Message = {
    messageType: '',
    messageText: ''
  };

  constructor(private messageService: MessageService) { }

  ngOnInit(): void {
    this.loadMessages();
  }

  loadMessages(): void {
    this.loading = true;
    this.errorMessage = '';
    this.messageService.getAllMessages().subscribe({
      next: (data) => {
        this.messages = data;
        this.loading = false;
      },
      error: (error) => {
        this.errorMessage = 'Failed to load messages. Make sure the backend is running.';
        this.loading = false;
        console.error('Error loading messages:', error);
      }
    });
  }

  getFilteredMessages(): Message[] {
    let filtered = this.messages;

    // Filter by type
    if (this.filterType !== 'all' && this.filterType !== '') {
      filtered = filtered.filter(m => m.messageType === this.filterType);
    }

    // Filter by search text
    if (this.searchText && this.searchText.trim() !== '') {
      const search = this.searchText.toLowerCase();
      filtered = filtered.filter(m => 
        m.messageText.toLowerCase().includes(search) ||
        (m.messageType && m.messageType.toLowerCase().includes(search))
      );
    }

    return filtered;
  }

  getUniqueTypes(): string[] {
    const types = this.messages
      .map(m => m.messageType)
      .filter((type): type is string => type !== undefined && type !== null && type !== '');
    return Array.from(new Set(types));
  }

  showCreateForm(): void {
    this.isCreating = true;
    this.isEditing = false;
    this.selectedMessage = null;
    this.newMessage = {
      messageType: '',
      messageText: ''
    };
    this.clearMessages();
  }

  createMessage(): void {
    this.messageService.createMessage(this.newMessage).subscribe({
      next: (message) => {
        this.messages.unshift(message); // Add to beginning of list
        this.isCreating = false;
        this.successMessage = 'Message created successfully!';
        this.clearMessages(3000);
        this.newMessage = {
          messageType: '',
          messageText: ''
        };
      },
      error: (error) => {
        this.errorMessage = 'Failed to create message.';
        console.error('Error creating message:', error);
      }
    });
  }

  editMessage(message: Message): void {
    this.selectedMessage = { ...message };
    this.isEditing = true;
    this.isCreating = false;
    this.clearMessages();
  }

  updateMessage(): void {
    if (this.selectedMessage && this.selectedMessage.messageId) {
      this.messageService.updateMessage(this.selectedMessage.messageId, this.selectedMessage).subscribe({
        next: (updatedMessage) => {
          const index = this.messages.findIndex(m => m.messageId === updatedMessage.messageId);
          if (index !== -1) {
            this.messages[index] = updatedMessage;
          }
          this.isEditing = false;
          this.selectedMessage = null;
          this.successMessage = 'Message updated successfully!';
          this.clearMessages(3000);
        },
        error: (error) => {
          this.errorMessage = 'Failed to update message.';
          console.error('Error updating message:', error);
        }
      });
    }
  }

  deleteMessage(id: number | undefined): void {
    if (!id) return;
    
    if (confirm('Are you sure you want to delete this message?')) {
      this.messageService.deleteMessage(id).subscribe({
        next: () => {
          this.messages = this.messages.filter(m => m.messageId !== id);
          this.successMessage = 'Message deleted successfully!';
          this.clearMessages(3000);
        },
        error: (error) => {
          this.errorMessage = 'Failed to delete message.';
          console.error('Error deleting message:', error);
        }
      });
    }
  }

  cancelEdit(): void {
    this.isEditing = false;
    this.isCreating = false;
    this.selectedMessage = null;
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

