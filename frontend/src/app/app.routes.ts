import { Routes } from '@angular/router';
import { ProductListComponent } from './components/product-list/product-list.component';
import { CustomerListComponent } from './components/customer-list/customer-list.component';
import { NotificationListComponent } from './components/notification-list/notification-list.component';
import { EventListComponent } from './components/event-list/event-list.component';
import { MessageListComponent } from './components/message-list/message-list.component';
import { ConfigListComponent } from './components/config-list/config-list.component';

export const routes: Routes = [
  { path: '', redirectTo: '/products', pathMatch: 'full' },
  { path: 'products', component: ProductListComponent },
  { path: 'customers', component: CustomerListComponent },
  { path: 'notifications', component: NotificationListComponent },
  { path: 'events', component: EventListComponent },
  { path: 'messages', component: MessageListComponent },
  { path: 'configs', component: ConfigListComponent }
];

