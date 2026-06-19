package com.beastwall.backend.contact.application.service;

import com.beastwall.backend.contact.application.port.in.ContactUseCase;
import com.beastwall.backend.contact.application.port.out.ContactRepositoryPort;
import com.beastwall.backend.contact.domain.model.entity.Contact;
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
    public List<Contact> saveAll(List<Contact> contacts) {
        return contactRepository.saveAll(contacts);
    }

    @Override
    @Transactional
    public void deleteContactById(long id) {
        contactRepository.deleteContactById(id);
    }

}
