package az.hamburgit.hamburg.academy.website.mapper

import az.hamburg.it.hamburg.academy.website.dao.entity.ReviewEntity
import az.hamburg.it.hamburg.academy.website.dao.entity.SpecializationEntity
import az.hamburg.it.hamburg.academy.website.model.request.create.CreateReviewRequest
import az.hamburg.it.hamburg.academy.website.model.request.update.UpdateReviewRequest
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import org.springframework.mock.web.MockMultipartFile
import spock.lang.Specification

import static az.hamburg.it.hamburg.academy.website.mapper.ReviewMapper.REVIEW_MAPPER
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.ACTIVE
import static az.hamburg.it.hamburg.academy.website.model.enums.Status.IN_PROGRESS

class ReviewMapperTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    // buildReviewEntity
    def "TestBuildReviewEntity"() {
        given:
        def mockFile = new MockMultipartFile(
                "imageFile",
                "review.png",
                "image/png",
                "test-content".bytes
        )
        def createReviewRequest = new CreateReviewRequest()
        def specializationEntity = random.nextObject(SpecializationEntity)
        createReviewRequest.setImageFile(mockFile)
        createReviewRequest.setSpecializationId(specializationEntity.id)

        when:
        def reviewEntity = REVIEW_MAPPER.buildReviewEntity(createReviewRequest, specializationEntity)

        then:
        createReviewRequest.studentName == reviewEntity.studentName
        createReviewRequest.studentSurname == reviewEntity.studentSurname
        createReviewRequest.comment == reviewEntity.comment
        createReviewRequest.rating == reviewEntity.rating
        createReviewRequest.specializationId == reviewEntity.specialization.id
        createReviewRequest.imageFile.originalFilename == reviewEntity.imagePath
        ACTIVE == reviewEntity.status
    }

    // buildReviewResponse
    def "TestBuildReviewResponse"() {
        given:
        def reviewEntity = random.nextObject(ReviewEntity)

        when:
        def reviewResponse = REVIEW_MAPPER.buildReviewResponse(reviewEntity)

        then:
        reviewResponse.id == reviewEntity.id
        reviewResponse.studentName == reviewEntity.studentName
        reviewResponse.studentSurname == reviewEntity.studentSurname
        reviewResponse.comment == reviewEntity.comment
        reviewResponse.imagePath == reviewEntity.imagePath
        reviewResponse.rating == reviewEntity.rating
        reviewResponse.status == reviewEntity.status
        reviewResponse.createdAt == reviewEntity.createdAt
        reviewResponse.updatedAt == reviewEntity.updatedAt
        reviewResponse.specializationId == reviewEntity.specialization.id
    }

    // updateReview
    def "TestUpdateReview"() {
        given:
        def mockFile = new MockMultipartFile(
                "imageFile",
                "graduate.png",
                "image/png",
                "test-content".bytes
        )
        def reviewEntity = random.nextObject(ReviewEntity)
        def updateReviewRequest = new UpdateReviewRequest()
        updateReviewRequest.setImageFile(mockFile)
        updateReviewRequest.setSpecializationId(reviewEntity.specialization.id)
        updateReviewRequest.setComment(reviewEntity.comment)
        updateReviewRequest.setStudentName(reviewEntity.studentName)
        updateReviewRequest.setStudentSurname(reviewEntity.studentSurname)
        updateReviewRequest.setRating(reviewEntity.rating)

        when:
        REVIEW_MAPPER.updateReview(reviewEntity, updateReviewRequest)

        then:
        updateReviewRequest.studentName == reviewEntity.studentName
        updateReviewRequest.studentSurname == reviewEntity.studentSurname
        updateReviewRequest.comment == reviewEntity.comment
        updateReviewRequest.imageFile.originalFilename == reviewEntity.imagePath
        updateReviewRequest.rating == reviewEntity.rating
        updateReviewRequest.specializationId == reviewEntity.specialization.id
        IN_PROGRESS == reviewEntity.status
    }

}
