package az.hamburg.it.hamburg.academy.website.mapper;

import az.hamburg.it.hamburg.academy.website.dao.entity.SyllabusEntity;
import az.hamburg.it.hamburg.academy.website.model.response.SyllabusResponse;

public enum SyllabusMapper {
    SYLLABUS_MAPPER;

    public SyllabusResponse buildSyllabusResponse(SyllabusEntity syllabus) {
        return SyllabusResponse.builder()
                .id(syllabus.getId())
                .imagePath(syllabus.getImagePath())
                .pdfPath(syllabus.getPdfPath())
                .status(syllabus.getStatus())
                .createdAt(syllabus.getCreatedAt())
                .updatedAt(syllabus.getUpdatedAt())
                .build();
    }

}