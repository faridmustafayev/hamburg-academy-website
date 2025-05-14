package az.hamburgit.hamburg.academy.website.mapper

import az.hamburg.it.hamburg.academy.website.dao.entity.UserEntity
import az.hamburg.it.hamburg.academy.website.model.request.RegistrationRequest
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

import static az.hamburg.it.hamburg.academy.website.mapper.UserMapper.USER_MAPPER
import static az.hamburg.it.hamburg.academy.website.model.enums.Role.ADMIN

class UserMapperTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    // mapRegistrationRequestToEntity
    def "TestMapRegistrationRequestEntity"() {
        given:
        def registrationRequest = random.nextObject(RegistrationRequest)

        when:
        def userEntity = USER_MAPPER.mapRegistrationRequestToEntity(registrationRequest)

        then:
        registrationRequest.email == userEntity.username
        registrationRequest.password == userEntity.password
        ADMIN == userEntity.role
    }

    // mapEntityToResponse
    def "TestMapEntityResponse"() {
        given:
        def userEntity = random.nextObject(UserEntity)

        when:
        def userResponse = USER_MAPPER.mapEntityToResponse(userEntity)

        then:
        userResponse.id == userEntity.id
        userResponse.email == userEntity.username
        userResponse.role == userEntity.role
    }
}
