import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterLink, Router } from '@angular/router';
import { AuthService } from './services/auth.service';
import { AuthResponse } from './models/user.model';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink],
  template: `
    <div class="app-header" *ngIf="isLoggedIn">
      <div class="header-content">
        <h1>Team Commercial</h1>
        <div class="user-info">
          <span class="user-name">👤 {{ currentUser?.firstName }} {{ currentUser?.lastName }}</span>
          <button (click)="logout()" class="btn-logout" title="Logout">
            🚪 Logout
          </button>
        </div>
      </div>
      <nav class="nav-menu">
        <a routerLink="/dashboard" routerLinkActive="active" class="nav-link">📊 Dashboard</a>
        <a routerLink="/customers" routerLinkActive="active" class="nav-link">Customers</a>
        <a routerLink="/notifications" routerLinkActive="active" class="nav-link">Notifications</a>
        <a routerLink="/events" routerLinkActive="active" class="nav-link">Events</a>
        <a routerLink="/messages" routerLinkActive="active" class="nav-link">Messages</a>
        <a routerLink="/configs" routerLinkActive="active" class="nav-link">Config</a>
        <a routerLink="/users" routerLinkActive="active" class="nav-link">👥 Users</a>
      </nav>
    </div>
    <div class="container" [class.no-header]="!isLoggedIn">
      <router-outlet></router-outlet>
    </div>
  `,
  styles: [`
    .app-header {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      padding: 20px;
      color: white;
      margin: -20px -20px 20px -20px;
      border-radius: 12px 12px 0 0;
    }
    
    .app-header h1 {
      margin: 0 0 15px 0;
      color: white;
      text-align: center;
    }
    
    .nav-menu {
      display: flex;
      justify-content: center;
      gap: 20px;
    }
    
    .nav-link {
      color: white;
      text-decoration: none;
      padding: 10px 20px;
      border-radius: 6px;
      transition: all 0.3s ease;
      font-weight: 600;
      background: rgba(255, 255, 255, 0.1);
    }
    
    .nav-link:hover {
      background: rgba(255, 255, 255, 0.2);
      transform: translateY(-2px);
    }
    
    .nav-link.active {
      background: white;
      color: #667eea;
    }
    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 15px;
    }
    
    .user-info {
      display: flex;
      align-items: center;
      gap: 15px;
    }
    
    .user-name {
      font-weight: 600;
      font-size: 1em;
    }
    
    .btn-logout {
      background: rgba(255, 255, 255, 0.2);
      color: white;
      border: 2px solid white;
      padding: 8px 16px;
      border-radius: 6px;
      cursor: pointer;
      font-weight: 600;
      transition: all 0.3s ease;
    }
    
    .btn-logout:hover {
      background: white;
      color: #667eea;
    }
    
    .container.no-header {
      margin: 0;
      padding: 0;
    }
    
    @media (max-width: 768px) {
      .header-content {
        flex-direction: column;
        gap: 10px;
      }
      
      .nav-menu {
        flex-wrap: wrap;
        gap: 10px;
      }
      
      .nav-link {
        font-size: 0.9em;
        padding: 8px 12px;
      }
    }
  `]
})
export class AppComponent implements OnInit {
  title = 'Team Commercial';
  isLoggedIn = false;
  currentUser: AuthResponse | null = null;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Subscribe to authentication state
    this.authService.currentUser.subscribe(user => {
      this.isLoggedIn = !!user;
      this.currentUser = user;
    });
  }

  logout(): void {
    if (confirm('Are you sure you want to logout?')) {
      this.authService.logout();
      this.router.navigate(['/login']);
    }
  }
}

