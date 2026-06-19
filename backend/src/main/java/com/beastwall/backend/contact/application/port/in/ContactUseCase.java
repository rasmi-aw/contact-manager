package com.beastwall.backend.contact.application.port.in;

import com.beastwall.backend.contact.domain.model.entity.Contact;

import java.util.List;
import java.util.Optional;

public interface ContactUseCase {
    List<Contact> getContacts(long fromId, int limit);
    Optional<Contact> getContactById(long id);
    Optional<Contact> saveContact(Contact contact);
    List<Contact> saveAll(List<Contact> contact);
    void deleteContactById(long id);
}