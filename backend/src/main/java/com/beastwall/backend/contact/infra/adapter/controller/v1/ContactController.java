package com.beastwall.backend.contact.infra.adapter.controller.v1;

import com.beastwall.backend.contact.application.port.in.ContactUseCase;
import com.beastwall.backend.contact.domain.model.entity.Contact;
import com.beastwall.backend.contact.infra.adapter.controller.v1.dto.ContactDTO;
import com.beastwall.backend.contact.infra.persistence.mapper.ContactMapper;
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
    private final ContactMapper contactMapper;

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

    /**
     * Add or Update, i could add PostMapping as well, but i don't mind since it's a small Api
     */
    @PutMapping
    public ResponseEntity<ContactDTO> saveContact(@Valid @RequestBody ContactDTO contactDTO) {
        Optional<Contact> response = contactUseCase.saveContact(contactDTO.toDomain());
        return response.map(contact -> ResponseEntity.ok(ContactDTO.fromDomain(contact))).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("/batch")
    public ResponseEntity<?> saveAll(@Valid @RequestBody List<ContactDTO> contacts) {
        List<Contact> response = contactUseCase.saveAll(contacts.stream().map(ContactDTO::toDomain).toList());
        if (response == null || response.isEmpty())
            return ResponseEntity.badRequest().body("Error in your one of the contacts");
        else
            return ResponseEntity.ok(response.stream().map(ContactDTO::fromDomain));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteContactById(@PathVariable long id) {
        contactUseCase.deleteContactById(id);
        return ResponseEntity.ok("Contact(" + id + ") Deleted Successfully");
    }
}