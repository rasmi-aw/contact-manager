package com.beastwall.backend.contact.v1.infra.adapter.controller.v1;

import com.beastwall.backend.contact.v1.application.port.in.ContactUseCase;
import com.beastwall.backend.contact.v1.domain.model.entity.Contact;
import com.beastwall.backend.contact.v1.infra.adapter.controller.v1.dto.ContactDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/contacts")
@AllArgsConstructor
public class ContactController {

    private final ContactUseCase contactUseCase;

    @GetMapping("/many/{fromId}")
    public ResponseEntity<List<ContactDTO>> getContacts(
            @PathVariable long fromId,
            @RequestParam(required = false, defaultValue = "20") int limit) {

        List<ContactDTO> contacts = contactUseCase.getContacts(fromId, limit).stream()
                .map(ContactDTO::fromDomain)
                .toList();
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/one/{id}")
    public ResponseEntity<ContactDTO> getContactById(@PathVariable long id) {
        return contactUseCase.getContactById(id)
                .map(ContactDTO::fromDomain)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping
    public ResponseEntity<ContactDTO> saveContact(@Valid @RequestBody ContactDTO contactDTO) {
        Optional<Contact> response = contactUseCase.saveContact(contactDTO.toDomain());
        System.out.println();
        return response.map(contact -> ResponseEntity.ok(ContactDTO.fromDomain(contact))).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteContactById(@PathVariable long id) {
        contactUseCase.deleteContactById(id);
        return ResponseEntity.ok("Contact(" + id + ") Deleted Successfully");
    }
}