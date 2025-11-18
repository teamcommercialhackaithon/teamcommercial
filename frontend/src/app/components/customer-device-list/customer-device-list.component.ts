import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CustomerDeviceService } from '../../services/customer-device.service';
import { CustomerDevice } from '../../models/customer-device.model';

@Component({
  selector: 'app-customer-device-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './customer-device-list.component.html',
  styleUrls: ['./customer-device-list.component.css']
})
export class CustomerDeviceListComponent implements OnInit {
  customerDevices: CustomerDevice[] = [];
  selectedCustomerDevice: CustomerDevice | null = null;
  isEditing = false;
  isCreating = false;
  loading = false;
  errorMessage = '';
  successMessage = '';
  searchText = '';
  filterStatus = 'all';

  newCustomerDevice: CustomerDevice = {
    customerId: '',
    serial: '',
    controllerDevice: '',
    status: ''
  };

  constructor(private customerDeviceService: CustomerDeviceService) { }

  ngOnInit(): void {
    this.loadCustomerDevices();
  }

  loadCustomerDevices(): void {
    this.loading = true;
    this.errorMessage = '';
    this.customerDeviceService.getAllCustomerDevices().subscribe({
      next: (data) => {
        this.customerDevices = data;
        this.loading = false;
      },
      error: (error) => {
        this.errorMessage = 'Failed to load customer devices. Make sure the backend is running.';
        this.loading = false;
        console.error('Error loading customer devices:', error);
      }
    });
  }

  getFilteredCustomerDevices(): CustomerDevice[] {
    let filtered = this.customerDevices;

    // Filter by status
    if (this.filterStatus !== 'all') {
      filtered = filtered.filter(device => device.status === this.filterStatus);
    }

    // Filter by search text
    if (this.searchText) {
      const searchLower = this.searchText.toLowerCase();
      filtered = filtered.filter(device =>
        device.customerId?.toLowerCase().includes(searchLower) ||
        device.serial?.toLowerCase().includes(searchLower) ||
        device.controllerDevice?.toLowerCase().includes(searchLower) ||
        device.status?.toLowerCase().includes(searchLower)
      );
    }

    return filtered;
  }

  getUniqueStatuses(): string[] {
    const statuses = this.customerDevices
      .map(device => device.status)
      .filter((status, index, self) => status && self.indexOf(status) === index);
    return statuses as string[];
  }

  showCreateForm(): void {
    this.isCreating = true;
    this.isEditing = false;
    this.selectedCustomerDevice = null;
    this.newCustomerDevice = {
      customerId: '',
      serial: '',
      controllerDevice: '',
      status: ''
    };
    this.clearMessages();
  }

  createCustomerDevice(): void {
    if (!this.newCustomerDevice.customerId || !this.newCustomerDevice.status) {
      this.errorMessage = 'Customer ID and Status are required fields.';
      return;
    }

    this.customerDeviceService.createCustomerDevice(this.newCustomerDevice).subscribe({
      next: (device) => {
        this.customerDevices.unshift(device);
        this.isCreating = false;
        this.successMessage = 'Customer device created successfully!';
        this.clearMessages(3000);
      },
      error: (error) => {
        this.errorMessage = 'Failed to create customer device. ' + (error.error?.message || '');
        console.error('Error creating customer device:', error);
      }
    });
  }

  editCustomerDevice(device: CustomerDevice): void {
    this.selectedCustomerDevice = { ...device };
    this.isEditing = true;
    this.isCreating = false;
    this.clearMessages();
  }

  updateCustomerDevice(): void {
    if (this.selectedCustomerDevice && this.selectedCustomerDevice.id) {
      if (!this.selectedCustomerDevice.customerId || !this.selectedCustomerDevice.status) {
        this.errorMessage = 'Customer ID and Status are required fields.';
        return;
      }

      this.customerDeviceService.updateCustomerDevice(this.selectedCustomerDevice.id, this.selectedCustomerDevice).subscribe({
        next: (updatedDevice) => {
          const index = this.customerDevices.findIndex(d => d.id === updatedDevice.id);
          if (index !== -1) {
            this.customerDevices[index] = updatedDevice;
          }
          this.isEditing = false;
          this.selectedCustomerDevice = null;
          this.successMessage = 'Customer device updated successfully!';
          this.clearMessages(3000);
        },
        error: (error) => {
          this.errorMessage = 'Failed to update customer device. ' + (error.error?.message || '');
          console.error('Error updating customer device:', error);
        }
      });
    }
  }

  deleteCustomerDevice(id: number | undefined): void {
    if (!id) return;

    if (confirm('Are you sure you want to delete this customer device?')) {
      this.customerDeviceService.deleteCustomerDevice(id).subscribe({
        next: () => {
          this.customerDevices = this.customerDevices.filter(d => d.id !== id);
          this.successMessage = 'Customer device deleted successfully!';
          this.clearMessages(3000);
        },
        error: (error) => {
          this.errorMessage = 'Failed to delete customer device. ' + (error.error?.message || '');
          console.error('Error deleting customer device:', error);
        }
      });
    }
  }

  cancelEdit(): void {
    this.isEditing = false;
    this.isCreating = false;
    this.selectedCustomerDevice = null;
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
