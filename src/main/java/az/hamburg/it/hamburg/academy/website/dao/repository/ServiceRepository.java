package az.hamburg.it.hamburg.academy.website.dao.repository;

import az.hamburg.it.hamburg.academy.website.dao.entity.ServiceEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM ServiceEntity se WHERE se.id = :id")
    void deleteByIdCustom(Long id);

    @Override
    @NotNull
    @EntityGraph(attributePaths = {"projectRequests", "projectRequests.services"})
    List<ServiceEntity> findAll();

    @NotNull
    @Override
    @EntityGraph(attributePaths = {"projectRequests", "projectRequests.services"})
    Optional<ServiceEntity> findById(@NotNull Long id);

}
