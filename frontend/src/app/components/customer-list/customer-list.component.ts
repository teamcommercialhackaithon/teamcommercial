import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CustomerService } from '../../services/customer.service';
import { Customer } from '../../models/customer.model';

@Component({
  selector: 'app-customer-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './customer-list.component.html',
  styleUrls: ['./customer-list.component.css']
})
export class CustomerListComponent implements OnInit {
  customers: Customer[] = [];
  selectedCustomer: Customer | null = null;
  isEditing = false;
  isCreating = false;
  loading = false;
  errorMessage = '';
  successMessage = '';

  newCustomer: Customer = {
    customerName: '',
    customerId: 0,
    streetNumber: '',
    streetName: '',
    unitNumber: '',
    city: '',
    state: '',
    postCode: '',
    emailId: '',
    mobileNumber: '',
    communicationPreference: '',
    serial: ''
  };

  constructor(private customerService: CustomerService) { }

  ngOnInit(): void {
    this.loadCustomers();
  }

  loadCustomers(): void {
    this.loading = true;
    this.errorMessage = '';
    this.customerService.getAllCustomers().subscribe({
      next: (data) => {
        this.customers = data;
        this.loading = false;
      },
      error: (error) => {
        this.errorMessage = 'Failed to load customers. Make sure the backend is running.';
        this.loading = false;
        console.error('Error loading customers:', error);
      }
    });
  }

  showCreateForm(): void {
    this.isCreating = true;
    this.isEditing = false;
    this.selectedCustomer = null;
    this.newCustomer = {
      customerName: '',
      customerId: 0,
      streetNumber: '',
      streetName: '',
      unitNumber: '',
      city: '',
      state: '',
      postCode: '',
      emailId: '',
      mobileNumber: '',
      communicationPreference: '',
      serial: ''
    };
    this.clearMessages();
  }

  createCustomer(): void {
    this.customerService.createCustomer(this.newCustomer).subscribe({
      next: (customer) => {
        this.customers.push(customer);
        this.isCreating = false;
        this.successMessage = 'Customer created successfully!';
        this.clearMessages(3000);
        this.newCustomer = {
          customerName: '',
          customerId: 0,
          streetNumber: '',
          streetName: '',
          unitNumber: '',
          city: '',
          state: '',
          postCode: '',
          emailId: '',
          mobileNumber: '',
          communicationPreference: '',
          serial: ''
        };
      },
      error: (error) => {
        this.errorMessage = 'Failed to create customer.';
        console.error('Error creating customer:', error);
      }
    });
  }

  editCustomer(customer: Customer): void {
    this.selectedCustomer = { ...customer };
    this.isEditing = true;
    this.isCreating = false;
    this.clearMessages();
  }

  updateCustomer(): void {
    if (this.selectedCustomer && this.selectedCustomer.customerId) {
      this.customerService.updateCustomer(this.selectedCustomer.customerId, this.selectedCustomer).subscribe({
        next: (updatedCustomer) => {
          const index = this.customers.findIndex(c => c.customerId === updatedCustomer.customerId);
          if (index !== -1) {
            this.customers[index] = updatedCustomer;
          }
          this.isEditing = false;
          this.selectedCustomer = null;
          this.successMessage = 'Customer updated successfully!';
          this.clearMessages(3000);
        },
        error: (error) => {
          this.errorMessage = 'Failed to update customer.';
          console.error('Error updating customer:', error);
        }
      });
    }
  }

  deleteCustomer(customerId: number | undefined): void {
    if (!customerId) return;
    
    if (confirm('Are you sure you want to delete this customer?')) {
      this.customerService.deleteCustomer(customerId).subscribe({
        next: () => {
          this.customers = this.customers.filter(c => c.customerId !== customerId);
          this.successMessage = 'Customer deleted successfully!';
          this.clearMessages(3000);
        },
        error: (error) => {
          this.errorMessage = 'Failed to delete customer.';
          console.error('Error deleting customer:', error);
        }
      });
    }
  }

  cancelEdit(): void {
    this.isEditing = false;
    this.isCreating = false;
    this.selectedCustomer = null;
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

