package az.hamburg.it.hamburg.academy.website.mapper;

import az.hamburg.it.hamburg.academy.website.dao.entity.GraduateEntity;
import az.hamburg.it.hamburg.academy.website.dao.entity.SpecializationEntity;
import az.hamburg.it.hamburg.academy.website.model.request.create.CreateGraduateRequest;
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateGraduateRequest;
import az.hamburg.it.hamburg.academy.website.model.response.GraduateResponse;
import az.hamburg.it.hamburg.academy.website.model.response.PageableResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

import static az.hamburg.it.hamburg.academy.website.mapper.DiplomaMapper.DIPLOMA_MAPPER;
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.ACTIVE;
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.IN_PROGRESS;

public enum GraduateMapper {
    GRADUATE_MAPPER;

    public GraduateEntity buildGraduateEntity(CreateGraduateRequest request, List<SpecializationEntity> specializations) {
        return GraduateEntity.builder()
                .fullName(request.getFullName())
                .specializations(specializations)
                .status(ACTIVE)
                .imagePath(request.getImageFile() != null ? request.getImageFile().getOriginalFilename() : null)
                .build();
    }

    public GraduateResponse buildGraduateResponse(GraduateEntity graduate) {
        return GraduateResponse.builder()
                .id(graduate.getId())
                .fullName(graduate.getFullName())
                .imagePath(graduate.getImagePath())
                .status(graduate.getStatus())
                .diplomas(
                        graduate.getDiplomas().stream()
                                .map(DIPLOMA_MAPPER::buildDiplomaResponse)
                                .collect(Collectors.toList())
                )
                .specializationIds(
                        graduate.getSpecializations().stream()
                                .map(SpecializationEntity::getId)
                                .collect(Collectors.toList())
                )
                .createdAt(graduate.getCreatedAt())
                .updatedAt(graduate.getUpdatedAt())
                .build();
    }

    public PageableResponse<GraduateResponse> mapPageableGraduateResponse(Page<GraduateEntity> graduatePage) {
        var graduateResponse = graduatePage.stream()
                .map(GRADUATE_MAPPER::buildGraduateResponse)
                .collect(Collectors.toList());

        return PageableResponse.<GraduateResponse>builder()
                .content(graduateResponse)
                .hasNextPage(graduatePage.hasNext())
                .totalElements(graduatePage.getTotalElements())
                .lastPageNumber(graduatePage.getTotalPages())
                .build();
    }

    public void updateGraduate(GraduateEntity graduate, UpdateGraduateRequest request) {
        if (request.getFullName() != null && !request.getFullName().trim().isEmpty()) {
            graduate.setFullName(request.getFullName());
        }

        if (request.getImageFile() != null) {
            graduate.setImagePath(request.getImageFile().getOriginalFilename());
        }

        if (request.getSpecializationIds() != null) {
            List<SpecializationEntity> specializations = request.getSpecializationIds().stream()
                    .map(id -> SpecializationEntity.builder()
                            .id(id)
                            .build())
                    .collect(Collectors.toList());
            graduate.setSpecializations(specializations);
        }

        graduate.setStatus(IN_PROGRESS);
    }
}
