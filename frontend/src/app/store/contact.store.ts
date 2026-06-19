import { inject } from '@angular/core';
import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { catchError, pipe, switchMap, tap } from 'rxjs';
import { Contact } from '../models/contact.model';
import { ContactService } from '../services/contact.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';

export interface ContactState {
  contacts: Contact[];
  loading: boolean;
  error: string | null;
}

const initialState: ContactState = {
  contacts: [],
  loading: false,
  error: null,
};

export const ContactStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withMethods((store, contactService = inject(ContactService), snackBar = inject(MatSnackBar), router = inject(Router)) => ({

    loadAll: rxMethod<void>(
      pipe(
        tap(() => patchState(store, { loading: true })),
        switchMap(() =>
          contactService.getAll().pipe(
            tap((contacts) => patchState(store, { contacts, loading: false })),
            catchError((err) => {
              patchState(store, { error: err.message, loading: false });
              throw err;
            })
          )
        )
      )
    ),

    addContact: rxMethod<Contact>(
      pipe(
        switchMap((contact) =>
          contactService.create(contact).pipe(
            tap((newContact) => {
              patchState(store, { contacts: [...store.contacts(), newContact] });
              snackBar.open('Contact created successfully!', 'Close', { duration: 3000 });
              router.navigate(['/contacts']);
            }),
            catchError((err) => {
              snackBar.open('Error creating contact', 'Close', { duration: 3000 });
              throw err;
            })
          )
        )
      )
    ),

    updateContact: rxMethod<{ id: number; contact: Contact }>(
      pipe(
        switchMap(({ id, contact }) =>
          contactService.update(contact).pipe(
            tap((updatedContact) => {
              patchState(store, {
                contacts: store.contacts().map((c) => (c.id === id ? updatedContact : c)),
              });
              snackBar.open('Contact updated successfully!', 'Close', { duration: 3000 });
              router.navigate(['/contacts']);
            }),
            catchError((err) => {
              snackBar.open('Error updating contact', 'Close', { duration: 3000 });
              throw err;
            })
          )
        )
      )
    ),

    deleteContact: rxMethod<number>(
      pipe(
        switchMap((id) =>
          contactService.delete(id).pipe(
            tap(() => {
              patchState(store, { contacts: store.contacts().filter((c) => c.id !== id) });
              snackBar.open('Contact removed.', 'Close', { duration: 3000 });
            }),
            catchError((err) => {
              snackBar.open('Error deleting contact', 'Close', { duration: 3000 });
              throw err;
            })
          )
        )
      )
    ),

    importCsv: rxMethod<File>(
      pipe(
        tap(() => patchState(store, { loading: true })),
        switchMap((file) =>
          contactService.importCsv(file).pipe(
            tap((newContacts) => {
              patchState(store, { contacts: [...store.contacts(), ...newContacts], loading: false });
              snackBar.open('CSV Data Imported Successfully!', 'Close', { duration: 3000 });
            }),
            catchError((err) => {
              patchState(store, { loading: false });
              snackBar.open('CSV Import failed', 'Close', { duration: 3000 });
              throw err;
            })
          )
        )
      )
    ),
  }))
);
