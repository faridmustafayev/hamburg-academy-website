package az.hamburg.it.hamburg.academy.website.mapper;

import az.hamburg.it.hamburg.academy.website.dao.entity.ContactEntity;
import az.hamburg.it.hamburg.academy.website.model.request.create.CreateContactRequest;
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateContactRequest;
import az.hamburg.it.hamburg.academy.website.model.response.ContactResponse;

import static az.hamburg.it.hamburg.academy.website.model.enums.Status.ACTIVE;
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.IN_PROGRESS;

public enum ContactMapper {
    CONTACT_MAPPER;

    public ContactEntity buildContactEntity(CreateContactRequest request) {
        return ContactEntity.builder()
                .address(request.getAddress())
                .mapUrl(request.getMapUrl())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .linkedinUrl(request.getLinkedinUrl())
                .instagramUrl(request.getInstagramUrl())
                .tiktokUrl(request.getTiktokUrl())
                .status(ACTIVE)
                .build();
    }

    public ContactResponse buildContactResponse(ContactEntity contactEntity) {
        return ContactResponse.builder()
                .id(contactEntity.getId())
                .address(contactEntity.getAddress())
                .mapUrl(contactEntity.getMapUrl())
                .email(contactEntity.getEmail())
                .phoneNumber(contactEntity.getPhoneNumber())
                .linkedinUrl(contactEntity.getLinkedinUrl())
                .instagramUrl(contactEntity.getInstagramUrl())
                .tiktokUrl(contactEntity.getTiktokUrl())
                .status(contactEntity.getStatus())
                .createdAt(contactEntity.getCreatedAt())
                .updatedAt(contactEntity.getUpdatedAt())
                .build();
    }

    public void updateContact(ContactEntity contactEntity, UpdateContactRequest request) {
        if (request.getAddress() != null && !contactEntity.getAddress().trim().isEmpty()) {
            contactEntity.setAddress(request.getAddress());
        }

        if (request.getMapUrl() != null && !contactEntity.getMapUrl().trim().isEmpty()) {
            contactEntity.setMapUrl(request.getMapUrl());
        }

        if (request.getEmail() != null && !contactEntity.getEmail().trim().isEmpty()) {
            contactEntity.setEmail(request.getEmail());
        }

        if (request.getPhoneNumber() != null && !contactEntity.getPhoneNumber().trim().isEmpty()) {
            contactEntity.setPhoneNumber(request.getPhoneNumber());
        }

        if (request.getLinkedinUrl() != null && !contactEntity.getLinkedinUrl().trim().isEmpty()) {
            contactEntity.setLinkedinUrl(request.getLinkedinUrl());
        }

        if (request.getInstagramUrl() != null && !contactEntity.getInstagramUrl().trim().isEmpty()) {
            contactEntity.setInstagramUrl(request.getInstagramUrl());
        }

        if (request.getTiktokUrl() != null && !contactEntity.getTiktokUrl().trim().isEmpty()) {
            contactEntity.setTiktokUrl(request.getTiktokUrl());
        }

        contactEntity.setStatus(IN_PROGRESS);
    }
}
