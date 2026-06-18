package com.beastwall.backend.contact.v1.infra.persistence.repository;

import com.beastwall.backend.contact.v1.infra.persistence.entity.ContactEntityJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactJpaRepository extends JpaRepository<ContactEntityJpa, Long> {

    @Query(value = "SELECT * FROM contact c WHERE c.id >= :fromId LIMIT :limit", nativeQuery = true)
    List<ContactEntityJpa> findContactsByIdLimitedResults(long fromId, long limit);
}
