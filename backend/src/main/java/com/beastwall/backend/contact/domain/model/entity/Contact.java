package com.beastwall.backend.contact.domain.model.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Contact {
    private final Long id;
    private final String firstname;
    private final String lastname;
    private final String phone;
    private final String email;

    public Contact(Long id, String firstname, String lastname, String phone, String email) {
        if (firstname == null || firstname.isBlank()) {
            throw new IllegalArgumentException("Firstname is required");
        }
        if (lastname == null || lastname.isBlank()) {
            throw new IllegalArgumentException("Lastname is required");
        }
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.email = email;
    }
}