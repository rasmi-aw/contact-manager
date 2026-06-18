import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';

import { Contact } from '../../models/contact.model';
import { ContactStore } from '../../store/contact.store';

@Component({
  selector: 'app-contact-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatCardModule],
  templateUrl: './contact-form.component.html'
})
export class ContactFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  readonly store = inject(ContactStore);
  private route = inject(ActivatedRoute);

  contactForm!: FormGroup;
  isEditMode = false;
  contactId?: number;

  ngOnInit(): void {
    this.contactForm = this.fb.group({
      firstname: ['', Validators.required],
      lastname: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', Validators.required]
    });

    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.isEditMode = true;
      this.contactId = +idParam;

      const contact = (this.store.contacts() as Contact[]).find((c: Contact) => c.id === this.contactId);
      if (contact) {
        this.contactForm.patchValue(contact);
      }
    }
  }

  onSubmit(): void {
    if (this.contactForm.invalid) return;

    if (this.isEditMode && this.contactId) {
      this.store.updateContact({ id: this.contactId, contact: this.contactForm.value });
    } else {
      this.store.addContact(this.contactForm.value);
    }
  }
}
