package com.beastwall.backend;

import com.beastwall.backend.contact.infra.adapter.controller.v1.dto.ContactDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ContactControllerITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private ContactDTO existingContact;

    @BeforeEach
    void setUp() throws Exception {
        ContactDTO seed = new ContactDTO(
                null,
                "John",
                "Doe",
                "0612345678",
                "john.doe@example.com"
        );

        String jsonResponse = mockMvc.perform(
                        put("/api/v1/contacts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(seed)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        existingContact = objectMapper.readValue(jsonResponse, ContactDTO.class);
    }

    @ParameterizedTest
    @MethodSource("provideValidContactLists")
    void importContacts_ShouldReturnSavedContacts_WhenPayloadIsValid(List<ContactDTO> payload) throws Exception {
        mockMvc.perform(
                        post("/api/v1/contacts/batch")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk());
    }

    static Stream<Arguments> provideValidContactLists() {
        return Stream.of(
                Arguments.of(List.of(
                        new ContactDTO(null, "Abdel", "Alpha", "0711223344", "abdel@beastwall.com"),
                        new ContactDTO(null, "Marie", "Dupont", "0655667788", "marie.dupont@example.com")
                )),
                Arguments.of(List.of(
                        new ContactDTO(null, "Jane", "Smith", "0687654321", "jane.smith@example.com")
                ))
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidContacts")
    void saveContact_ShouldReturnBadRequest_WhenConstraintsAreViolated(ContactDTO invalidContact) throws Exception {
        mockMvc.perform(
                        put("/api/v1/contacts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidContact)))
                .andExpect(status().isBadRequest());
    }

    static Stream<Arguments> provideInvalidContacts() {
        return Stream.of(
                Arguments.of(new ContactDTO(null, "", "Missing Firstname", "123456", "valid@email.com")),
                Arguments.of(new ContactDTO(null, "Missing", "Email", "123456", "invalid-email-format")),
                Arguments.of(new ContactDTO(null, "Valid", "", "", "valid@email.com"))
        );
    }

    @Test
    void getContactById_ShouldReturnContact_WhenIdExists() throws Exception {
        mockMvc.perform(get("/api/v1/contacts/one/{id}", existingContact.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingContact.getId()))
                .andExpect(jsonPath("$.firstname").value("John"))
                .andExpect(jsonPath("$.lastname").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void getContactById_ShouldReturnNotFound_WhenIdDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/v1/contacts/one/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void getContacts_ShouldReturnPaginatedList() throws Exception {
        mockMvc.perform(
                        get("/api/v1/contacts/many/{fromId}", 0)
                                .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void deleteContactById_ShouldDeleteContact() throws Exception {
        mockMvc.perform(delete("/api/v1/contacts/{id}", existingContact.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/contacts/one/{id}", existingContact.getId()))
                .andExpect(status().isNotFound());
    }
}