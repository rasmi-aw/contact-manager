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
    return this.http.get<Contact[]>(this.apiUrl+"/many/0");
  }

  getById(id: number): Observable<Contact> {
    return this.http.get<Contact>(`${this.apiUrl}/${id}`);
  }

  create(contact: Contact): Observable<Contact> {
    return  this.update(contact.id? contact.id:-1,contact);
    // return this.http.post<Contact>(this.apiUrl, contact);
  }

  update(id: number, contact: Contact): Observable<Contact> {
    return this.http.put<Contact>(`${this.apiUrl}`, contact);
  }

  delete(id: unknown): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  importCsv(file: unknown): Observable<Contact[]> {
    // const formData = new FormData();
    // formData.append('file', file);
    return this.http.post<Contact[]>(`${this.apiUrl}/import`, null);
  }
}
