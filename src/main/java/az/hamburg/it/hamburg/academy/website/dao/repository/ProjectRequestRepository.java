package az.hamburg.it.hamburg.academy.website.dao.repository;

import az.hamburg.it.hamburg.academy.website.dao.entity.ProjectRequestEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ProjectRequestRepository extends JpaRepository<ProjectRequestEntity, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM ProjectRequestEntity pre WHERE pre.id = :id")
    void deleteByIdCustom(Long id);

    @Override
    @NotNull
    @EntityGraph(attributePaths = {"services"})
    List<ProjectRequestEntity> findAll();

    @NotNull
    @Override
    @EntityGraph(attributePaths = {"services"})
    Optional<ProjectRequestEntity> findById(@NotNull Long id);

}
