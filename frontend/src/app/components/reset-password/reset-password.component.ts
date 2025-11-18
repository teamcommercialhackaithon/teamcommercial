import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { ResetPasswordRequest } from '../../models/user.model';

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {
  request: ResetPasswordRequest = {
    token: '',
    newPassword: '',
    captchaVerified: false
  };

  confirmPassword = '';
  error = '';
  success = '';
  loading = false;

  constructor(
    private authService: AuthService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Get token from query parameters
    this.route.queryParams.subscribe(params => {
      this.request.token = params['token'] || '';
      if (!this.request.token) {
        this.error = 'Invalid or missing reset token';
      }
    });
  }

  onCaptchaChange(event: any): void {
    this.request.captchaVerified = event.target.checked;
  }

  onSubmit(): void {
    this.error = '';
    this.success = '';

    if (!this.request.token) {
      this.error = 'Invalid reset token';
      return;
    }

    if (!this.request.newPassword) {
      this.error = 'Please enter a new password';
      return;
    }

    if (this.request.newPassword.length < 6) {
      this.error = 'Password must be at least 6 characters';
      return;
    }

    if (this.request.newPassword !== this.confirmPassword) {
      this.error = 'Passwords do not match';
      return;
    }

    if (!this.request.captchaVerified) {
      this.error = 'Please verify that you are human';
      return;
    }

    this.loading = true;

    this.authService.resetPassword(this.request).subscribe({
      next: (response) => {
        this.loading = false;
        this.success = response;
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2000);
      },
      error: (err) => {
        this.loading = false;
        this.error = typeof err.error === 'string' ? err.error : 'Failed to reset password. Please try again.';
      }
    });
  }
}

