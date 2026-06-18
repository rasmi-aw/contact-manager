package com.beastwall.backend.contact.v1.application.service;

import com.beastwall.backend.contact.v1.application.port.in.ContactUseCase;
import com.beastwall.backend.contact.v1.application.port.out.ContactRepositoryPort;
import com.beastwall.backend.contact.v1.domain.model.entity.Contact;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class ContactService implements ContactUseCase {

    private ContactRepositoryPort contactRepository;

    @Override
    public List<Contact> getContacts(long fromId, int limit) {
        return contactRepository.getContacts(fromId, limit);
    }

    @Override
    public Optional<Contact> getContactById(long id) {
        return contactRepository.getContactById(id);
    }

    @Override
    @Transactional
    public Optional<Contact> saveContact(Contact contact) {
        return contactRepository.saveContact(contact);
    }

    @Override
    @Transactional
    public void deleteContactById(long id) {
        contactRepository.deleteContactById(id);
    }

}
