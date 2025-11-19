import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { User } from '../../models/user.model';

@Component({
  selector: 'app-user-management',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.css']
})
export class UserManagementComponent implements OnInit {
  users: User[] = [];
  filteredUsers: User[] = [];
  loading = false;
  error = '';
  success = '';

  showForm = false;
  isEditing = false;
  currentUser: User = this.getEmptyUser();
  searchTerm = '';
  
  // Pagination properties
  currentPage = 0;
  pageSize = 25;
  totalPages = 0;
  totalElements = 0;
  Math = Math;

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.loading = true;
    this.error = '';

    this.userService.getAllUsers(this.currentPage, this.pageSize).subscribe({
      next: (data) => {
        this.users = data.content;
        this.filteredUsers = data.content;
        this.totalPages = data.totalPages;
        this.totalElements = data.totalElements;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load users';
        console.error('Error loading users:', err);
        this.loading = false;
      }
    });
  }
  
  // Pagination methods
  goToPage(page: number): void {
    this.currentPage = page;
    this.loadUsers();
  }
  
  nextPage(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      this.loadUsers();
    }
  }
  
  previousPage(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.loadUsers();
    }
  }
  
  getPageNumbers(): number[] {
    const pages: number[] = [];
    const maxPagesToShow = 5;
    let startPage = Math.max(0, this.currentPage - 2);
    let endPage = Math.min(this.totalPages - 1, startPage + maxPagesToShow - 1);
    
    if (endPage - startPage < maxPagesToShow - 1) {
      startPage = Math.max(0, endPage - maxPagesToShow + 1);
    }
    
    for (let i = startPage; i <= endPage; i++) {
      pages.push(i);
    }
    return pages;
  }

  searchUsers(): void {
    if (!this.searchTerm.trim()) {
      this.filteredUsers = this.users;
      return;
    }

    const term = this.searchTerm.toLowerCase();
    this.filteredUsers = this.users.filter(user =>
      user.firstName.toLowerCase().includes(term) ||
      user.lastName.toLowerCase().includes(term) ||
      user.username.toLowerCase().includes(term) ||
      user.emailId.toLowerCase().includes(term)
    );
  }

  showAddForm(): void {
    this.isEditing = false;
    this.currentUser = this.getEmptyUser();
    this.showForm = true;
    this.error = '';
    this.success = '';
  }

  editUser(user: User): void {
    this.isEditing = true;
    this.currentUser = { ...user, password: '' }; // Don't show password
    this.showForm = true;
    this.error = '';
    this.success = '';
  }

  cancelForm(): void {
    this.showForm = false;
    this.currentUser = this.getEmptyUser();
    this.error = '';
    this.success = '';
  }

  saveUser(): void {
    this.error = '';
    this.success = '';

    if (!this.validateUser()) {
      return;
    }

    if (this.isEditing) {
      this.updateUser();
    } else {
      this.createUser();
    }
  }

  private validateUser(): boolean {
    if (!this.currentUser.firstName || !this.currentUser.lastName) {
      this.error = 'First name and last name are required';
      return false;
    }

    if (!this.currentUser.username) {
      this.error = 'Username is required';
      return false;
    }

    if (!this.isEditing && !this.currentUser.password) {
      this.error = 'Password is required for new users';
      return false;
    }

    if (!this.currentUser.emailId) {
      this.error = 'Email is required';
      return false;
    }

    if (this.currentUser.password && this.currentUser.password.length < 6) {
      this.error = 'Password must be at least 6 characters';
      return false;
    }

    return true;
  }

  private createUser(): void {
    this.loading = true;

    this.userService.createUser(this.currentUser).subscribe({
      next: (user) => {
        this.success = 'User created successfully';
        this.loading = false;
        this.showForm = false;
        this.loadUsers();
      },
      error: (err) => {
        this.error = typeof err.error === 'string' ? err.error : 'Failed to create user';
        this.loading = false;
      }
    });
  }

  private updateUser(): void {
    this.loading = true;

    this.userService.updateUser(this.currentUser.userId, this.currentUser).subscribe({
      next: (user) => {
        this.success = 'User updated successfully';
        this.loading = false;
        this.showForm = false;
        this.loadUsers();
      },
      error: (err) => {
        this.error = typeof err.error === 'string' ? err.error : 'Failed to update user';
        this.loading = false;
      }
    });
  }

  deleteUser(user: User): void {
    if (!confirm(`Are you sure you want to delete user "${user.username}"?`)) {
      return;
    }

    this.loading = true;
    this.error = '';

    this.userService.deleteUser(user.userId).subscribe({
      next: () => {
        this.success = 'User deleted successfully';
        this.loading = false;
        this.loadUsers();
      },
      error: (err) => {
        this.error = typeof err.error === 'string' ? err.error : 'Failed to delete user';
        this.loading = false;
      }
    });
  }

  toggleUserStatus(user: User): void {
    this.loading = true;
    this.error = '';

    this.userService.toggleUserStatus(user.userId).subscribe({
      next: (updatedUser) => {
        this.success = `User ${updatedUser.active ? 'activated' : 'deactivated'} successfully`;
        this.loading = false;
        this.loadUsers();
      },
      error: (err) => {
        this.error = 'Failed to toggle user status';
        this.loading = false;
      }
    });
  }

  private getEmptyUser(): User {
    return {
      userId: 0,
      firstName: '',
      lastName: '',
      username: '',
      password: '',
      emailId: '',
      phoneNumber: '',
      active: true
    };
  }
}

