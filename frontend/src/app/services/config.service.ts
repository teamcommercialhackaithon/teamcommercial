import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Config } from '../models/config.model';
import { Page } from '../models/page.model';

@Injectable({
  providedIn: 'root'
})
export class ConfigService {
  private apiUrl = 'http://localhost:8080/api/configs';

  constructor(private http: HttpClient) { }

  getAllConfigs(page: number = 0, size: number = 25): Observable<Page<Config>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<Page<Config>>(this.apiUrl, { params });
  }

  getConfigById(id: number): Observable<Config> {
    return this.http.get<Config>(`${this.apiUrl}/${id}`);
  }

  getConfigsByFullOutageNotification(enabled: boolean): Observable<Config[]> {
    return this.http.get<Config[]>(`${this.apiUrl}/full-outage/${enabled}`);
  }

  getConfigsByPartialOutageNotification(enabled: boolean): Observable<Config[]> {
    return this.http.get<Config[]>(`${this.apiUrl}/partial-outage/${enabled}`);
  }

  getConfigsByStartStopNotification(enabled: boolean): Observable<Config[]> {
    return this.http.get<Config[]>(`${this.apiUrl}/start-stop/${enabled}`);
  }

  createConfig(config: Config): Observable<Config> {
    return this.http.post<Config>(this.apiUrl, config);
  }

  updateConfig(id: number, config: Config): Observable<Config> {
    return this.http.put<Config>(`${this.apiUrl}/${id}`, config);
  }

  toggleFullOutageNotification(id: number): Observable<Config> {
    return this.http.patch<Config>(`${this.apiUrl}/${id}/toggle-full-outage`, {});
  }

  togglePartialOutageNotification(id: number): Observable<Config> {
    return this.http.patch<Config>(`${this.apiUrl}/${id}/toggle-partial-outage`, {});
  }

  toggleStartStopNotification(id: number): Observable<Config> {
    return this.http.patch<Config>(`${this.apiUrl}/${id}/toggle-start-stop`, {});
  }

  deleteConfig(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}

