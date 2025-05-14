package az.hamburg.it.hamburg.academy.website.mapper;

import az.hamburg.it.hamburg.academy.website.dao.entity.DiplomaEntity;
import az.hamburg.it.hamburg.academy.website.dao.entity.GraduateEntity;
import az.hamburg.it.hamburg.academy.website.model.request.create.CreateDiplomaRequest;
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateDiplomaRequest;
import az.hamburg.it.hamburg.academy.website.model.response.DiplomaResponse;

import static az.hamburg.it.hamburg.academy.website.model.enums.Status.ACTIVE;
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.IN_PROGRESS;

public enum DiplomaMapper {
    DIPLOMA_MAPPER;

    public DiplomaEntity buildDiplomaEntity(CreateDiplomaRequest request, GraduateEntity graduate) {
        return DiplomaEntity.builder()
                .diplomaNumber(request.getDiplomaNumber())
                .graduate(graduate)
                .status(ACTIVE)
                .build();
    }

    public void updateDiploma(DiplomaEntity diploma, UpdateDiplomaRequest request) {
        if (request.getDiplomaNumber() != null && !request.getDiplomaNumber().trim().isEmpty()) {
            diploma.setDiplomaNumber(request.getDiplomaNumber());
        }

        if (request.getGraduateId() != null) {
            diploma.getGraduate().setId(request.getGraduateId());
        }

        diploma.setStatus(IN_PROGRESS);
    }

    public DiplomaResponse buildDiplomaResponse(DiplomaEntity diploma) {
        return DiplomaResponse.builder()
                .id(diploma.getId())
                .diplomaPath(diploma.getDiplomaPath())
                .diplomaNumber(diploma.getDiplomaNumber())
                .status(diploma.getStatus())
                .createdAt(diploma.getCreatedAt())
                .updatedAt(diploma.getUpdatedAt())
                .build();
    }
}
