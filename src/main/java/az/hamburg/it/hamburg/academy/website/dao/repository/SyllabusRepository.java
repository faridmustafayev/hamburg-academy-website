package az.hamburg.it.hamburg.academy.website.dao.repository;

import az.hamburg.it.hamburg.academy.website.dao.entity.SyllabusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface SyllabusRepository extends JpaRepository<SyllabusEntity, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM SyllabusEntity se WHERE se.id = :id")
    void deleteByCustomId(Long id);
}
