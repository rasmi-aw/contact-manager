package com.beastwall.backend.contact.infra.persistence.repository;

import com.beastwall.backend.contact.domain.model.entity.Contact;
import com.beastwall.backend.contact.application.port.out.ContactRepositoryPort;
import com.beastwall.backend.contact.infra.persistence.entity.ContactEntityJpa;
import com.beastwall.backend.contact.infra.persistence.mapper.ContactMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class ContactRepositoryJpaAdapter implements ContactRepositoryPort {

    private ContactJpaRepository contactRepository;

    private ContactMapper contactMapper;

    @Override
    public List<Contact> getContacts(long fromId, int limit) {
        return contactRepository
                .findContactsByIdLimitedResults(fromId, limit)
                .stream()
                .map(contactMapper::mapToDomainModel)
                .toList();
    }

    @Override
    public Optional<Contact> getContactById(long id) {
        return contactRepository
                .findById(id)
                .map(c -> contactMapper.mapToDomainModel(c));
    }

    @Override
    public Optional<Contact> saveContact(Contact contact) {
        ContactEntityJpa contactEntityJpa = contactRepository.save(contactMapper.mapToPersistenceModel(contact));
        return Optional.of(contactMapper.mapToDomainModel(contactEntityJpa));
    }

    @Override
    public List<Contact> saveAll(List<Contact> contacts) {
        List<ContactEntityJpa> persisted = contactRepository.saveAll(contacts.stream().map(contactMapper::mapToPersistenceModel).toList());
        return persisted.stream().map(contactMapper::mapToDomainModel).toList();
    }

    @Override
    public void deleteContactById(long id) {
        contactRepository.deleteById(id);
    }
}
