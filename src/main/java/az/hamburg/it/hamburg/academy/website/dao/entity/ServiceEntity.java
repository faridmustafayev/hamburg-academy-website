package az.hamburg.it.hamburg.academy.website.dao.entity;

import az.hamburg.it.hamburg.academy.website.model.enums.ServiceType;
import az.hamburg.it.hamburg.academy.website.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = PRIVATE)
@Entity
@Table(name = "services")
@Where(clause = "status <> 'DELETED'")
public class ServiceEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;

    @Enumerated(STRING)
    ServiceType name;

    String description;

    @Enumerated(STRING)
    Status status;

    @CreationTimestamp
    LocalDateTime createdAt;

    @UpdateTimestamp
    LocalDateTime updatedAt;

    @ManyToMany(cascade = PERSIST)
    @JoinTable(
            name = "service_project_requests",
            joinColumns = @JoinColumn(name = "service_id"),
            inverseJoinColumns = @JoinColumn(name = "project_request_id")
    )
    @JsonBackReference
    Set<ProjectRequestEntity> projectRequests;
}