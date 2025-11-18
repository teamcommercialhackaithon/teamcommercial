import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { LoginRequest } from '../../models/user.model';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginRequest: LoginRequest = {
    username: '',
    password: '',
    captchaVerified: false
  };

  error = '';
  loading = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {
    // Redirect to dashboard if already logged in
    if (this.authService.isLoggedIn) {
      this.router.navigate(['/dashboard']);
    }
  }

  onCaptchaChange(event: any): void {
    this.loginRequest.captchaVerified = event.target.checked;
  }

  onSubmit(): void {
    this.error = '';

    if (!this.loginRequest.username || !this.loginRequest.password) {
      this.error = 'Please enter username and password';
      return;
    }

    if (!this.loginRequest.captchaVerified) {
      this.error = 'Please verify that you are human';
      return;
    }

    this.loading = true;

    this.authService.login(this.loginRequest).subscribe({
      next: (response) => {
        this.loading = false;
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.loading = false;
        this.error = typeof err.error === 'string' ? err.error : 'Login failed. Please check your credentials.';
      }
    });
  }
}

