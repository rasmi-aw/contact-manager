package com.beastwall.backend.contact.application.port.out;

import com.beastwall.backend.contact.domain.model.entity.Contact;

import java.util.List;
import java.util.Optional;

public interface ContactRepositoryPort {

    /**
     * returns A limited list of contacts starting from a given @NonNull Id
     *
     * @param fromId: starting Id included
     * @param limit:  number of contacts to return
     */
    List<Contact> getContacts(long fromId, int limit);

    Optional<Contact> getContactById(long id);

    Optional<Contact> saveContact(Contact contact);

    List<Contact> saveAll(List<Contact> contact);

    void deleteContactById(long id);
}
