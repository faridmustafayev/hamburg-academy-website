package az.hamburg.it.hamburg.academy.website.dao.repository;

import az.hamburg.it.hamburg.academy.website.dao.entity.DiplomaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface DiplomaRepository extends JpaRepository<DiplomaEntity, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM DiplomaEntity de WHERE de.id = :id")
    void deleteByIdCustom(Long id);
}
