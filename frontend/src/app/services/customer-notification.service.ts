import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CustomerNotification } from '../models/customer-notification.model';

@Injectable({
  providedIn: 'root'
})
export class CustomerNotificationService {
  private apiUrl = 'http://localhost:8080/api/notifications';

  constructor(private http: HttpClient) { }

  getAllNotifications(): Observable<CustomerNotification[]> {
    return this.http.get<CustomerNotification[]>(this.apiUrl);
  }

  getNotificationById(id: number): Observable<CustomerNotification> {
    return this.http.get<CustomerNotification>(`${this.apiUrl}/${id}`);
  }

  getNotificationsByCustomerId(customerId: string): Observable<CustomerNotification[]> {
    return this.http.get<CustomerNotification[]>(`${this.apiUrl}/customer/${customerId}`);
  }

  getNotificationsByStatus(status: boolean): Observable<CustomerNotification[]> {
    return this.http.get<CustomerNotification[]>(`${this.apiUrl}/notified/${status}`);
  }

  getNotificationsByType(type: string): Observable<CustomerNotification[]> {
    return this.http.get<CustomerNotification[]>(`${this.apiUrl}/type/${type}`);
  }

  getExpiredNotifications(): Observable<CustomerNotification[]> {
    return this.http.get<CustomerNotification[]>(`${this.apiUrl}/expired`);
  }

  createNotification(notification: CustomerNotification): Observable<CustomerNotification> {
    return this.http.post<CustomerNotification>(this.apiUrl, notification);
  }

  updateNotification(id: number, notification: CustomerNotification): Observable<CustomerNotification> {
    return this.http.put<CustomerNotification>(`${this.apiUrl}/${id}`, notification);
  }

  markAsNotified(id: number): Observable<CustomerNotification> {
    return this.http.patch<CustomerNotification>(`${this.apiUrl}/${id}/mark-notified`, {});
  }

  markAsUnnotified(id: number): Observable<CustomerNotification> {
    return this.http.patch<CustomerNotification>(`${this.apiUrl}/${id}/mark-unnotified`, {});
  }

  deleteNotification(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}

