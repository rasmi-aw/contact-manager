import { Component, OnInit, ViewChild, inject, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';

import { Contact } from '../../models/contact.model';
import { ContactStore } from '../../store/contact.store';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import {ContactService} from '../../services/contact.service';

@Component({
  selector: 'app-contact-list',
  standalone: true,
  imports: [
    CommonModule, RouterModule, MatTableModule, MatSortModule,
    MatFormFieldModule, MatInputModule, MatButtonModule, MatIconModule, MatDialogModule
  ],
  templateUrl: './contact-list.component.html'
})
export class ContactListComponent implements OnInit {
  readonly store = inject(ContactStore);
  private dialog = inject(MatDialog);

  displayedColumns: string[] = ['firstname', 'lastname', 'email', 'phone', 'actions'];
  dataSource = new MatTableDataSource<Contact>([]);

  // Using a setter ensures the sort property links up whenever the view instantiates it
  private matSort!: MatSort;

  @ViewChild(MatSort) set sort(sort: MatSort) {
    this.matSort = sort;
    this.dataSource.sort = this.matSort;
  }

  constructor(private contactService: ContactService) {
    effect(() => {
      this.dataSource.data = this.store.contacts() as Contact[];
      // Re-assign here to force MatTableDataSource to recalculate sorting boundaries on signal mutations
      if (this.matSort) {
        this.dataSource.sort = this.matSort;
      }
    });
  }

  ngOnInit(): void {
    this.store.loadAll();
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  onDelete(id: number): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: { message: 'Are you sure you want to permanently delete this contact?' }
    });

    dialogRef.afterClosed().subscribe((confirmed: boolean) => {
      if (confirmed) {
        this.store.deleteContact(id);
      }
    });
  }

  exportToCsv(): void {
    const data = this.store.contacts() as Contact[];
    if (data.length === 0) return;

    const headers = ['First Name', 'Last Name', 'Email', 'Phone'];
    const csvRows = [
      headers.join(','),
      ...data.map((c: Contact) => `"${c.firstname}","${c.lastname}","${c.email}","${c.phone}"`)
    ];

    const blob = new Blob([csvRows.join('\n')], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.setAttribute('href', url);
    link.setAttribute('download', 'contacts_export.csv');
    link.style.visibility = 'hidden';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }



  onFileSelected(event: Event): void {
    const element = event.currentTarget as HTMLInputElement;
    const fileList: FileList | null = element.files;

    if (fileList && fileList.length > 0) {
      const file = fileList[0];
      const reader = new FileReader();

      reader.onload = (e: ProgressEvent<FileReader>) => {
        const text = e.target?.result as string;
        if (!text) return;

        const lines = text.split('\n');
        const contacts: Contact[] = [];

        // Start at index 1 to skip the header line if present ("First Name, Last Name...")
        for (let i = 1; i < lines.length; i++) {
          const line = lines[i].trim();
          if (!line) continue; // Skip empty rows

          // Split by comma while stripping enclosing double quotes
          const columns = line.split(',').map(val => val.replace(/^"|"$/g, '').trim());

          // Ensure we have enough columns before processing
          if (columns.length >= 4) {
            contacts.push({
              id: null as any, // Explicitly sending null for backend database sequence generation
              firstname: columns[0],
              lastname: columns[1],
              email: columns[2],
              phone: columns[3],
            });
          }
        }

        if (contacts.length > 0) {
          // Pass the structural JSON array directly to your service/store method
          this.contactService.importCsvList(contacts);
        }
      };

      reader.readAsText(file);
    }
  }
}
