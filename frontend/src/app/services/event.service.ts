import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Event } from '../models/event.model';
import { Page } from '../models/page.model';

@Injectable({
  providedIn: 'root'
})
export class EventService {
  private apiUrl = 'http://localhost:8080/api/events';

  constructor(private http: HttpClient) { }

  getAllEvents(page: number = 0, size: number = 25, processed?: boolean): Observable<Page<Event>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    
    if (processed !== undefined) {
      params = params.set('processed', processed.toString());
    }
    
    return this.http.get<Page<Event>>(this.apiUrl, { params });
  }

  getEventById(id: number): Observable<Event> {
    return this.http.get<Event>(`${this.apiUrl}/${id}`);
  }

  getEventsByType(type: string): Observable<Event[]> {
    return this.http.get<Event[]>(`${this.apiUrl}/type/${type}`);
  }

  getEventsByMacAddress(macAddress: string): Observable<Event[]> {
    return this.http.get<Event[]>(`${this.apiUrl}/mac/${macAddress}`);
  }

  getEventsBySerial(serial: string): Observable<Event[]> {
    return this.http.get<Event[]>(`${this.apiUrl}/serial/${serial}`);
  }

  getRecentEvents(): Observable<Event[]> {
    return this.http.get<Event[]>(`${this.apiUrl}/recent`);
  }

  createEvent(event: Event): Observable<Event> {
    return this.http.post<Event>(this.apiUrl, event);
  }

  updateEvent(id: number, event: Event): Observable<Event> {
    return this.http.put<Event>(`${this.apiUrl}/${id}`, event);
  }

  deleteEvent(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}

