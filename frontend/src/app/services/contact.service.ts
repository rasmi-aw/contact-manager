import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Contact } from '../models/contact.model';

@Injectable({
  providedIn: 'root'
})
export class ContactService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/v1/contacts';

  getAll(): Observable<Contact[]> {
    return this.http.get<Contact[]>(this.apiUrl + "/many/0?limit=1000");
  }

  getById(id: number): Observable<Contact> {
    return this.http.get<Contact>(`${this.apiUrl}/${id}`);
  }
  createOrUpdate(contact: Contact): Observable<Contact> {
    return this.http.put<Contact>(`${this.apiUrl}`, contact);
  }

  delete(id: unknown): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' });
  }

  importCsv(contacts: Contact[]): Observable<Contact[]> {
    return this.http.post<Contact[]>(`${this.apiUrl}/batch`, contacts);
  }
}
