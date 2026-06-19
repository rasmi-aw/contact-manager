package com.beastwall.backend.contact.infra.config;

import com.beastwall.backend.contact.application.port.in.ContactUseCase;
import com.beastwall.backend.contact.application.port.out.ContactRepositoryPort;
import com.beastwall.backend.contact.application.service.ContactService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContactServiceConfiguration {

    @Bean
    public ContactUseCase contactUseCase(ContactRepositoryPort contactRepositoryPort) {
        return new ContactService(contactRepositoryPort);
    }
}
