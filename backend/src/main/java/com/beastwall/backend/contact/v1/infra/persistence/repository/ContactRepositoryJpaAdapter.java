package com.beastwall.backend.contact.v1.infra.persistence.repository;

import com.beastwall.backend.contact.v1.domain.model.entity.Contact;
import com.beastwall.backend.contact.v1.application.port.out.ContactRepositoryPort;
import com.beastwall.backend.contact.v1.infra.persistence.entity.ContactEntityJpa;
import com.beastwall.backend.contact.v1.infra.persistence.mapper.ContactMapper;
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
    public void deleteContactById(long id) {
        contactRepository.deleteById(id);
    }
}
