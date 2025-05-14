package az.hamburgit.hamburg.academy.website.mapper


import az.hamburg.it.hamburg.academy.website.model.jwt.AuthPayloadDto
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

import static az.hamburg.it.hamburg.academy.website.mapper.TokenMapper.TOKEN_MAPPER
import static az.hamburg.it.hamburg.academy.website.model.constant.AuthConstant.ISSUER

class TokenMapperTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    // buildAccessTokenClaimsSet
    def "TestBuildAccessTokenClaimsSet"() {
        given:
        def authPayloadDto = random.nextObject(AuthPayloadDto)
        def date = random.nextObject(Date)

        when:
        def accessTokenClaimsSet = TOKEN_MAPPER.buildAccessTokenClaimsSet(authPayloadDto, date)

        then:
        authPayloadDto.userId == accessTokenClaimsSet.userId
        authPayloadDto.username == accessTokenClaimsSet.username
        Math.abs(new Date().time - accessTokenClaimsSet.createdTime.time) < 1000
        date == accessTokenClaimsSet.expirationTime
        ISSUER == accessTokenClaimsSet.iss
    }

    // buildRefreshTokenClaimsSet
    def "TestBuildRefreshTokenClaimSet"() {
        given:
        def authPayloadDto = random.nextObject(AuthPayloadDto)
        def refreshTokenExpirationCount = random.nextObject(Integer)
        def expirationTime = random.nextObject(Date)

        when:
        def refreshTokenClaimsSet = TOKEN_MAPPER.buildRefreshTokenClaimsSet(authPayloadDto, refreshTokenExpirationCount, expirationTime)

        then:
        ISSUER == refreshTokenClaimsSet.iss
        authPayloadDto.userId == refreshTokenClaimsSet.userId
        authPayloadDto.username == refreshTokenClaimsSet.username
        expirationTime == refreshTokenClaimsSet.expirationTime
        refreshTokenExpirationCount == refreshTokenClaimsSet.count
    }
}
