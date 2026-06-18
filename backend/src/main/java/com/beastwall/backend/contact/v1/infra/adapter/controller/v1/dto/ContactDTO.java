package com.beastwall.backend.contact.v1.infra.adapter.controller.v1.dto;

import com.beastwall.backend.contact.v1.domain.model.entity.Contact;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ContactDTO {

    private Long id;

    @NotBlank(message = "firstname is required")
    private String firstname;

    @NotBlank(message = "lastname is required")
    private String lastname;

    @Size(min = 10, max = 14, message = "Wrong phone format, it has to contain between 10 and 14 digits")
    private String phone;

    @Email(message = "not valid email address xxx@yyy.domain")
    private String email;

    public static ContactDTO fromDomain(Contact contact) {
        return ContactDTO.builder()
                .id(contact.getId())
                .firstname(contact.getFirstname())
                .lastname(contact.getLastname())
                .phone(contact.getPhone())
                .email(contact.getEmail())
                .build();
    }

    public Contact toDomain() {
        return Contact.builder()
                .id(this.id)
                .firstname(this.firstname)
                .lastname(this.lastname)
                .phone(this.phone)
                .email(this.email)
                .build();
    }

    @Override
    public String toString() {
        return "ContactDTO{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}