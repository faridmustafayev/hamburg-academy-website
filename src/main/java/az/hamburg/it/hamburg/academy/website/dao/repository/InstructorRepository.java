package az.hamburg.it.hamburg.academy.website.dao.repository;

import az.hamburg.it.hamburg.academy.website.dao.entity.InstructorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface InstructorRepository extends JpaRepository<InstructorEntity, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM InstructorEntity ie WHERE ie.id = :id")
    void deleteByIdCustom(Long id);
}
