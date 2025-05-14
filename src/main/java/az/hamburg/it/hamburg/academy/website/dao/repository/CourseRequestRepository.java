package az.hamburg.it.hamburg.academy.website.dao.repository;

import az.hamburg.it.hamburg.academy.website.dao.entity.CourseRequestEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CourseRequestRepository extends JpaRepository<CourseRequestEntity, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM CourseRequestEntity cre WHERE cre.id = :id")
    void deleteByIdCustom(Long id);

    @Override
    @EntityGraph(attributePaths = {"specializations"})
    List<CourseRequestEntity> findAll();

    @Override
    @EntityGraph(attributePaths = {"specializations"})
    Optional<CourseRequestEntity> findById(Long id);
}
