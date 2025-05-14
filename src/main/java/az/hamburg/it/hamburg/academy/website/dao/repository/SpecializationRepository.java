package az.hamburg.it.hamburg.academy.website.dao.repository;

import az.hamburg.it.hamburg.academy.website.dao.entity.SpecializationEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface SpecializationRepository extends JpaRepository<SpecializationEntity, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM SpecializationEntity se WHERE se.id = :id")
    void deleteByIdCustom(Long id);

    @NotNull
    @Override
    @EntityGraph(attributePaths = {
            "instructors", "reviews", "graduates", "courseRequests", "syllabus",
            "graduates.diplomas", "graduates.specializations", "courseRequests.specializations"
    })
    List<SpecializationEntity> findAll();

    @NotNull
    @Override
    @EntityGraph(attributePaths = {
            "instructors", "reviews", "graduates", "courseRequests", "syllabus",
            "graduates.diplomas", "graduates.specializations", "courseRequests.specializations"
    })
    Optional<SpecializationEntity> findById(@NotNull Long id);
}
