package az.hamburg.it.hamburg.academy.website.dao.repository;

import az.hamburg.it.hamburg.academy.website.dao.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM ReviewEntity re WHERE re.id = :id")
    void deleteByIdCustom(Long id);
}
