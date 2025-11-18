import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { PasswordResetRequest } from '../../models/user.model';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent {
  request: PasswordResetRequest = {
    emailId: '',
    captchaVerified: false
  };

  error = '';
  success = '';
  loading = false;

  constructor(private authService: AuthService) {}

  onCaptchaChange(event: any): void {
    this.request.captchaVerified = event.target.checked;
  }

  onSubmit(): void {
    this.error = '';
    this.success = '';

    if (!this.request.emailId) {
      this.error = 'Please enter your email address';
      return;
    }

    if (!this.request.captchaVerified) {
      this.error = 'Please verify that you are human';
      return;
    }

    this.loading = true;

    this.authService.requestPasswordReset(this.request).subscribe({
      next: (response) => {
        this.loading = false;
        this.success = response;
        this.request.emailId = '';
        this.request.captchaVerified = false;
      },
      error: (err) => {
        this.loading = false;
        this.error = typeof err.error === 'string' ? err.error : 'Failed to send reset link. Please try again.';
      }
    });
  }
}

