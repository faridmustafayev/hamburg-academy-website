package az.hamburg.it.hamburg.academy.website.dao.entity;

import az.hamburg.it.hamburg.academy.website.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
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

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
@FieldDefaults(level = PRIVATE)
@Entity
@Table(name = "syllabuses")
@Where(clause = "status <> 'DELETED'")
public class SyllabusEntity {
    @Id
    Long id;

    String imagePath;

    String pdfPath;

    @Enumerated(STRING)
    Status status;

    @CreationTimestamp
    LocalDateTime createdAt;

    @UpdateTimestamp
    LocalDateTime updatedAt;

    @OneToOne(fetch = LAZY)
    @MapsId
    @JoinColumn(name = "id")
    @JsonBackReference
    SpecializationEntity specialization;
}
