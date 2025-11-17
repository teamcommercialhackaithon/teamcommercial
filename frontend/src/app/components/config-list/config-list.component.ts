import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ConfigService } from '../../services/config.service';
import { Config } from '../../models/config.model';

@Component({
  selector: 'app-config-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './config-list.component.html',
  styleUrls: ['./config-list.component.css']
})
export class ConfigListComponent implements OnInit {
  configs: Config[] = [];
  selectedConfig: Config | null = null;
  isEditing = false;
  isCreating = false;
  loading = false;
  errorMessage = '';
  successMessage = '';

  newConfig: Config = {
    waitTime: 0,
    enableFullOutageNotification: false,
    partialOutageNotification: false,
    startStopNotification: false
  };

  constructor(private configService: ConfigService) { }

  ngOnInit(): void {
    this.loadConfigs();
  }

  loadConfigs(): void {
    this.loading = true;
    this.errorMessage = '';
    this.configService.getAllConfigs().subscribe({
      next: (data) => {
        this.configs = data;
        this.loading = false;
      },
      error: (error) => {
        this.errorMessage = 'Failed to load configurations. Make sure the backend is running.';
        this.loading = false;
        console.error('Error loading configs:', error);
      }
    });
  }

  showCreateForm(): void {
    this.isCreating = true;
    this.isEditing = false;
    this.selectedConfig = null;
    this.newConfig = {
      waitTime: 0,
      enableFullOutageNotification: false,
      partialOutageNotification: false,
      startStopNotification: false
    };
    this.clearMessages();
  }

  createConfig(): void {
    this.configService.createConfig(this.newConfig).subscribe({
      next: (config) => {
        this.configs.push(config);
        this.isCreating = false;
        this.successMessage = 'Configuration created successfully!';
        this.clearMessages(3000);
      },
      error: (error) => {
        this.errorMessage = 'Failed to create configuration.';
        console.error('Error creating config:', error);
      }
    });
  }

  editConfig(config: Config): void {
    this.selectedConfig = { ...config };
    this.isEditing = true;
    this.isCreating = false;
    this.clearMessages();
  }

  updateConfig(): void {
    if (this.selectedConfig && this.selectedConfig.configId) {
      this.configService.updateConfig(this.selectedConfig.configId, this.selectedConfig).subscribe({
        next: (updatedConfig) => {
          const index = this.configs.findIndex(c => c.configId === updatedConfig.configId);
          if (index !== -1) {
            this.configs[index] = updatedConfig;
          }
          this.isEditing = false;
          this.selectedConfig = null;
          this.successMessage = 'Configuration updated successfully!';
          this.clearMessages(3000);
        },
        error: (error) => {
          this.errorMessage = 'Failed to update configuration.';
          console.error('Error updating config:', error);
        }
      });
    }
  }

  toggleFullOutage(config: Config): void {
    if (!config.configId) return;
    this.configService.toggleFullOutageNotification(config.configId).subscribe({
      next: (updatedConfig) => {
        const index = this.configs.findIndex(c => c.configId === updatedConfig.configId);
        if (index !== -1) {
          this.configs[index] = updatedConfig;
        }
        this.successMessage = 'Full outage notification toggled!';
        this.clearMessages(2000);
      },
      error: (error) => {
        this.errorMessage = 'Failed to toggle full outage notification.';
        console.error('Error toggling:', error);
      }
    });
  }

  togglePartialOutage(config: Config): void {
    if (!config.configId) return;
    this.configService.togglePartialOutageNotification(config.configId).subscribe({
      next: (updatedConfig) => {
        const index = this.configs.findIndex(c => c.configId === updatedConfig.configId);
        if (index !== -1) {
          this.configs[index] = updatedConfig;
        }
        this.successMessage = 'Partial outage notification toggled!';
        this.clearMessages(2000);
      },
      error: (error) => {
        this.errorMessage = 'Failed to toggle partial outage notification.';
        console.error('Error toggling:', error);
      }
    });
  }

  toggleStartStop(config: Config): void {
    if (!config.configId) return;
    this.configService.toggleStartStopNotification(config.configId).subscribe({
      next: (updatedConfig) => {
        const index = this.configs.findIndex(c => c.configId === updatedConfig.configId);
        if (index !== -1) {
          this.configs[index] = updatedConfig;
        }
        this.successMessage = 'Start/Stop notification toggled!';
        this.clearMessages(2000);
      },
      error: (error) => {
        this.errorMessage = 'Failed to toggle start/stop notification.';
        console.error('Error toggling:', error);
      }
    });
  }

  deleteConfig(id: number | undefined): void {
    if (!id) return;
    
    if (confirm('Are you sure you want to delete this configuration?')) {
      this.configService.deleteConfig(id).subscribe({
        next: () => {
          this.configs = this.configs.filter(c => c.configId !== id);
          this.successMessage = 'Configuration deleted successfully!';
          this.clearMessages(3000);
        },
        error: (error) => {
          this.errorMessage = 'Failed to delete configuration.';
          console.error('Error deleting config:', error);
        }
      });
    }
  }

  cancelEdit(): void {
    this.isEditing = false;
    this.isCreating = false;
    this.selectedConfig = null;
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

