import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CustomerNotification } from '../models/customer-notification.model';
import { Page } from '../models/page.model';

@Injectable({
  providedIn: 'root'
})
export class CustomerNotificationService {
  private apiUrl = 'http://localhost:8080/api/notifications';

  constructor(private http: HttpClient) { }

  getAllNotifications(page: number = 0, size: number = 25): Observable<Page<CustomerNotification>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<Page<CustomerNotification>>(this.apiUrl, { params });
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

