import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Customer } from '../models/customer.model';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private apiUrl = 'http://localhost:8080/api/customers';

  constructor(private http: HttpClient) { }

  getAllCustomers(): Observable<Customer[]> {
    return this.http.get<Customer[]>(this.apiUrl);
  }

  getCustomerByCustomerId(customerId: number): Observable<Customer> {
    return this.http.get<Customer>(`${this.apiUrl}/${customerId}`);
  }

  getCustomerByEmail(emailId: string): Observable<Customer> {
    return this.http.get<Customer>(`${this.apiUrl}/email/${emailId}`);
  }

  createCustomer(customer: Customer): Observable<Customer> {
    return this.http.post<Customer>(this.apiUrl, customer);
  }

  updateCustomer(customerId: number, customer: Customer): Observable<Customer> {
    return this.http.put<Customer>(`${this.apiUrl}/${customerId}`, customer);
  }

  deleteCustomer(customerId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${customerId}`);
  }

  searchCustomers(name: string): Observable<Customer[]> {
    return this.http.get<Customer[]>(`${this.apiUrl}/search?name=${name}`);
  }

  getCustomersByCity(city: string): Observable<Customer[]> {
    return this.http.get<Customer[]>(`${this.apiUrl}/city/${city}`);
  }

  getCustomersByState(state: string): Observable<Customer[]> {
    return this.http.get<Customer[]>(`${this.apiUrl}/state/${state}`);
  }

  getCustomersByCommunicationPreference(preference: string): Observable<Customer[]> {
    return this.http.get<Customer[]>(`${this.apiUrl}/communication-preference/${preference}`);
  }
}

