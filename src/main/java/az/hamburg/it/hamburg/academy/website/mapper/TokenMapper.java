package az.hamburg.it.hamburg.academy.website.mapper;


import az.hamburg.it.hamburg.academy.website.model.jwt.AccessTokenClaimsSet;
import az.hamburg.it.hamburg.academy.website.model.jwt.AuthPayloadDto;
import az.hamburg.it.hamburg.academy.website.model.jwt.RefreshTokenClaimsSet;

import java.util.Date;

import static az.hamburg.it.hamburg.academy.website.model.constant.AuthConstant.ISSUER;

public enum TokenMapper {
    TOKEN_MAPPER;

    public AccessTokenClaimsSet buildAccessTokenClaimsSet(AuthPayloadDto dto, Date expirationTime) {
        return AccessTokenClaimsSet.builder()
                .iss(ISSUER)
                .userId(dto.getUserId())
                .username(dto.getUsername())
                .createdTime(new Date())
                .expirationTime(expirationTime)
                .build();
    }

    public RefreshTokenClaimsSet buildRefreshTokenClaimsSet(AuthPayloadDto dto,
                                                            int refreshTokenExpirationCount,
                                                            Date expirationTime) {
        return RefreshTokenClaimsSet.builder()
                .iss(ISSUER)
                .userId(dto.getUserId())
                .username(dto.getUsername())
                .expirationTime(expirationTime)
                .count(refreshTokenExpirationCount)
                .build();
    }
}