import { Component, OnInit, inject, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';

import { Contact } from '../../models/contact.model';
import { ContactStore } from '../../store/contact.store';

@Component({
  selector: 'app-contact-detail',
  standalone: true,
  imports: [CommonModule, RouterModule, MatCardModule, MatButtonModule],
  templateUrl : "./contact-detail.component.html",
})
export class ContactDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  readonly store = inject(ContactStore);

  contactId = 0;

  // Explicitly typing the collection array up-front guarantees parameter recognition
  contact = computed(() => (this.store.contacts() as Contact[]).find((c: Contact) => c.id === this.contactId));

  ngOnInit(): void {
    this.contactId = +this.route.snapshot.paramMap.get('id')!;
  }
}
