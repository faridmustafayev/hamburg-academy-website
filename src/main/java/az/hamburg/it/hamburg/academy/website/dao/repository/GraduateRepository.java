package az.hamburg.it.hamburg.academy.website.dao.repository;

import az.hamburg.it.hamburg.academy.website.dao.entity.GraduateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface GraduateRepository extends JpaRepository<GraduateEntity, Long>, JpaSpecificationExecutor<GraduateEntity> {
    @Modifying
    @Transactional
    @Query("DELETE FROM GraduateEntity ge WHERE ge.id = :id")
    void deleteByIdCustom(Long id);

    @Override
    @EntityGraph(attributePaths = {"diplomas", "specializations"})
    Page<GraduateEntity> findAll(Specification<GraduateEntity> specification, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"diplomas", "specializations"})
    Optional<GraduateEntity> findById(Long id);
}
