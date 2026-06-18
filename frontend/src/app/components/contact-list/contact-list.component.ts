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

  @ViewChild(MatSort) sort!: MatSort;

  constructor() {
    effect(() => {
      this.dataSource.data = this.store.contacts() as Contact[];
    });
  }

  ngOnInit(): void {
    this.store.loadAll();
    this.dataSource.sort = this.sort;
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
      this.store.importCsv(fileList[0]);
    }
  }
}
