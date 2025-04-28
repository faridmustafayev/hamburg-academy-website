package az.hamburg.it.hamburg.academy.website.service.specification;

import az.hamburg.it.hamburg.academy.website.dao.entity.GraduateEntity;
import az.hamburg.it.hamburg.academy.website.model.criteria.GraduateCriteria;
import az.hamburg.it.hamburg.academy.website.util.PredicateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import static az.hamburg.it.hamburg.academy.website.dao.entity.GraduateEntity.Fields.fullName;
import static az.hamburg.it.hamburg.academy.website.util.PredicateUtil.applyLikePattern;

@AllArgsConstructor(staticName = "of")
public class GraduateSpecification implements Specification<GraduateEntity> {
    private GraduateCriteria graduateCriteria;

    @Override
    public Predicate toPredicate(Root<GraduateEntity> root,
                                 CriteriaQuery<?> query,
                                 CriteriaBuilder cb) {
        var predicates = PredicateUtil.builder()
                .addNullSafety(graduateCriteria.getFullName(),
                        nameSurname -> cb.like(root.get(fullName), applyLikePattern(nameSurname)))
                .build();

        return cb.and(predicates);
    }
}