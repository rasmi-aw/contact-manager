package com.beastwall.backend.contact.v1.infra.config;

import com.beastwall.backend.contact.v1.application.port.in.ContactUseCase;
import com.beastwall.backend.contact.v1.application.port.out.ContactRepositoryPort;
import com.beastwall.backend.contact.v1.application.service.ContactService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContactServiceConfiguration {

    @Bean
    public ContactUseCase contactUseCase(ContactRepositoryPort contactRepositoryPort) {
        return new ContactService(contactRepositoryPort);
    }
}
