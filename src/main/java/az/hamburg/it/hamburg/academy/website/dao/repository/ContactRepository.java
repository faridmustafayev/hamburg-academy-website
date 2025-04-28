package az.hamburg.it.hamburg.academy.website.dao.repository;

import az.hamburg.it.hamburg.academy.website.dao.entity.ContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ContactRepository extends JpaRepository<ContactEntity, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM ContactEntity ce WHERE ce.id = :id")
    void deleteByIdCustom(Long id);
}
