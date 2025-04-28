package az.hamburg.it.hamburg.academy.website.dao.entity;

import az.hamburg.it.hamburg.academy.website.model.enums.SpecializationType;
import az.hamburg.it.hamburg.academy.website.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.Set;

import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = PRIVATE)
@Entity
@Table(name = "specializations")
@Where(clause = "status <> 'DELETED'")
public class SpecializationEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;

    @Enumerated(STRING)
    SpecializationType name;

    String description;

    @Enumerated(STRING)
    Status status;

    @CreationTimestamp
    LocalDateTime createdAt;

    @UpdateTimestamp
    LocalDateTime updatedAt;

    @OneToMany(mappedBy = "specialization", cascade = {PERSIST, MERGE})
    @JsonBackReference
    Set<InstructorEntity> instructors;

    @OneToMany(mappedBy = "specialization", cascade = {PERSIST, MERGE})
    @JsonBackReference
    Set<ReviewEntity> reviews;

    @ManyToMany(cascade = PERSIST)
    @JoinTable(
            name = "specialization_graduates",
            joinColumns = @JoinColumn(name = "specialization_id"),
            inverseJoinColumns = @JoinColumn(name = "graduate_id")
    )
    @JsonBackReference
    Set<GraduateEntity> graduates;

    @ManyToMany(cascade = PERSIST)
    @JoinTable(
            name = "specialization_course_requests",
            joinColumns = @JoinColumn(name = "specialization_id"),
            inverseJoinColumns = @JoinColumn(name = "course_request_id")
    )
    @JsonBackReference
    Set<CourseRequestEntity> courseRequests;

    @OneToOne(mappedBy = "specialization", fetch = LAZY, cascade = {PERSIST, MERGE})
    @JsonBackReference
    SyllabusEntity syllabus;
}