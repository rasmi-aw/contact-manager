package com.beastwall.backend.contact.v1.infra.persistence.mapper;

import com.beastwall.backend.contact.v1.domain.model.entity.Contact;
import com.beastwall.backend.contact.v1.infra.persistence.entity.ContactEntityJpa;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContactMapper {

    Contact mapToDomainModel(ContactEntityJpa contact);
    ContactEntityJpa mapToPersistenceModel(Contact contact);

}
