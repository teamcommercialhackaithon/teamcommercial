import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CustomerDevice } from '../models/customer-device.model';
import { Page } from '../models/page.model';

@Injectable({
  providedIn: 'root'
})
export class CustomerDeviceService {
  private apiUrl = 'http://localhost:8080/api/customer-devices';

  constructor(private http: HttpClient) { }

  getAllCustomerDevices(page: number = 0, size: number = 25): Observable<Page<CustomerDevice>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<Page<CustomerDevice>>(this.apiUrl, { params });
  }

  getCustomerDeviceById(id: number): Observable<CustomerDevice> {
    return this.http.get<CustomerDevice>(`${this.apiUrl}/${id}`);
  }

  getCustomerDevicesByCustomerId(customerId: string): Observable<CustomerDevice[]> {
    return this.http.get<CustomerDevice[]>(`${this.apiUrl}/customer/${customerId}`);
  }

  getCustomerDevicesBySerial(serial: string): Observable<CustomerDevice[]> {
    return this.http.get<CustomerDevice[]>(`${this.apiUrl}/serial/${serial}`);
  }

  getCustomerDevicesByStatus(status: string): Observable<CustomerDevice[]> {
    return this.http.get<CustomerDevice[]>(`${this.apiUrl}/status/${status}`);
  }

  createCustomerDevice(customerDevice: CustomerDevice): Observable<CustomerDevice> {
    return this.http.post<CustomerDevice>(this.apiUrl, customerDevice);
  }

  updateCustomerDevice(id: number, customerDevice: CustomerDevice): Observable<CustomerDevice> {
    return this.http.put<CustomerDevice>(`${this.apiUrl}/${id}`, customerDevice);
  }

  deleteCustomerDevice(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}

