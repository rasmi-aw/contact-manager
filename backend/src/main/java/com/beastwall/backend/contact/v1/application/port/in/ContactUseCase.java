package com.beastwall.backend.contact.v1.application.port.in;

import com.beastwall.backend.contact.v1.domain.model.entity.Contact;

import java.util.List;
import java.util.Optional;

public interface ContactUseCase {
    List<Contact> getContacts(long fromId, int limit);
    Optional<Contact> getContactById(long id);
    Optional<Contact> saveContact(Contact contact);
    void deleteContactById(long id);
}