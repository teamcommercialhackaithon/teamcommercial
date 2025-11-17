import { Component } from '@angular/core';
import { RouterOutlet, RouterLink } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink],
  template: `
    <div class="app-header">
      <h1>Team Commercial</h1>
      <nav class="nav-menu">
        <a routerLink="/products" routerLinkActive="active" class="nav-link">Products</a>
        <a routerLink="/customers" routerLinkActive="active" class="nav-link">Customers</a>
        <a routerLink="/notifications" routerLinkActive="active" class="nav-link">Notifications</a>
        <a routerLink="/events" routerLinkActive="active" class="nav-link">Events</a>
        <a routerLink="/messages" routerLinkActive="active" class="nav-link">Messages</a>
        <a routerLink="/configs" routerLinkActive="active" class="nav-link">Config</a>
      </nav>
    </div>
    <div class="container">
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
  `]
})
export class AppComponent {
  title = 'Team Commercial';
}

