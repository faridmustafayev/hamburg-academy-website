package az.hamburg.it.hamburg.academy.website.util;

import az.hamburg.it.hamburg.academy.website.exception.AuthenticationException;
import az.hamburg.it.hamburg.academy.website.model.jwt.AccessTokenClaimsSet;
import az.hamburg.it.hamburg.academy.website.model.jwt.RefreshTokenClaimsSet;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Date;

import static az.hamburg.it.hamburg.academy.website.exception.ExceptionConstants.USER_UNAUTHORIZED;
import static az.hamburg.it.hamburg.academy.website.model.constant.AuthConstant.KEY_SIZE;
import static az.hamburg.it.hamburg.academy.website.model.constant.AuthConstant.RSA;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class JwtUtil {
    ObjectMapper objectMapper = new ObjectMapper();

    public KeyPair generateKeyPair() {
        try {
            var keyPairGen = KeyPairGenerator.getInstance(RSA);
            keyPairGen.initialize(KEY_SIZE);
            return keyPairGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            log.error("ActionLog.generateKeyPair.error no such algorithm", e);
            throw new AuthenticationException(USER_UNAUTHORIZED.getCode(), USER_UNAUTHORIZED.getMessage(), 401);
        }
    }

    public <T> String generateToken(T tokenClaimSet, PrivateKey privateKey) {
        SignedJWT signedJWT;
        try {
            signedJWT = generateSignedJWT(objectMapper.writeValueAsString(tokenClaimSet), privateKey);
        } catch (Exception e) {
            log.error("ActionLog.generateToken.error cannot generate token", e);
            throw new AuthenticationException(USER_UNAUTHORIZED.getCode(), USER_UNAUTHORIZED.getMessage(), 401);
        }
        return signedJWT.serialize();
    }

    public void verifyToken(String token, RSAPublicKey publicKey) {
        try {
            var signedJwt = SignedJWT.parse(token);
            var verifier = new RSASSAVerifier(publicKey);

            if (!signedJwt.verify(verifier)) {
                log.error("ActionLog.verifyToken.error can't verify signedJwt");
                throw new AuthenticationException(USER_UNAUTHORIZED.getCode(), USER_UNAUTHORIZED.getMessage(), 401);
            }
        } catch (ParseException | JOSEException e) {
            log.error("ActionLog.verifyToken.error can't parse token ", e);
            throw new AuthenticationException(USER_UNAUTHORIZED.getCode(), USER_UNAUTHORIZED.getMessage(), 401);
        }
    }

    public Date generateSessionExpirationTime(Integer expirationMinutes) {
        return new Date(System.currentTimeMillis() + expirationMinutes * 60 * 1_000);
    }

    public boolean isRefreshTokenTimeExpired(RefreshTokenClaimsSet refreshTokenClaimsSet) {
        return refreshTokenClaimsSet.getExpirationTime().before(new Date());
    }

    public boolean isRefreshTokenCountExpired(RefreshTokenClaimsSet refreshTokenClaimsSet) {
        return refreshTokenClaimsSet.getCount() <= 0;
    }

    public AccessTokenClaimsSet getClaimsFromAccessToken(String token) {

        AccessTokenClaimsSet claimsSet;
        try {
            claimsSet = objectMapper.readValue(getClaimsFromToken(token).toString(), AccessTokenClaimsSet.class);
        } catch (IOException | ParseException e) {
            log.error("ActionLog.getClaimsFromAccessToken.error can't parse access token", e);
            throw new AuthenticationException(USER_UNAUTHORIZED.getCode(), USER_UNAUTHORIZED.getMessage(), 401);
        }
        return claimsSet;
    }

    public RefreshTokenClaimsSet getClaimsFromRefreshToken(String token) {
        RefreshTokenClaimsSet claimsSet;
        try {
            var claimsAsText = getClaimsFromToken(token).toString();
            log.info(claimsAsText);
            claimsSet = objectMapper.readValue(claimsAsText, RefreshTokenClaimsSet.class);
        } catch (IOException | ParseException e) {
            log.error(e.getMessage());
            log.error("ActionLog.getClaimsFromRefreshToken.error can't parse refresh token", e);
            throw new AuthenticationException(USER_UNAUTHORIZED.getCode(), USER_UNAUTHORIZED.getMessage(), 401);
        }
        return claimsSet;
    }

    public boolean isTokenExpired(Date expirationTime) {
        return expirationTime.before(new Date());
    }

    private JWTClaimsSet getClaimsFromToken(String token) throws ParseException {
        return SignedJWT.parse(token).getJWTClaimsSet();
    }

    private SignedJWT generateSignedJWT(String tokenClaimSetJson,
                                        PrivateKey privateKey) throws JOSEException, ParseException {
        var jwtClaimsSet = JWTClaimsSet.parse(tokenClaimSetJson);
        var header = new JWSHeader(JWSAlgorithm.RS256);
        var signedJWT = new SignedJWT(header, jwtClaimsSet);
        var signer = new RSASSASigner(privateKey);
        signedJWT.sign(signer);
        return signedJWT;
    }
}