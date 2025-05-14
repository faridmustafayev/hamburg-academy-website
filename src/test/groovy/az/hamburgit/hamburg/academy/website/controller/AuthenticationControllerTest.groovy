package az.hamburgit.hamburg.academy.website.controller

import az.hamburg.it.hamburg.academy.website.controller.AuthenticationController
import az.hamburg.it.hamburg.academy.website.exception.ErrorHandler
import az.hamburg.it.hamburg.academy.website.model.jwt.RefreshTokenRequest
import az.hamburg.it.hamburg.academy.website.model.request.LoginRequest
import az.hamburg.it.hamburg.academy.website.model.response.LoginResponse
import az.hamburg.it.hamburg.academy.website.service.abstraction.AuthenticationService
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.http.HttpHeaders.AUTHORIZATION
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

class AuthenticationControllerTest extends Specification {
    AuthenticationService authenticationService
    AuthenticationController authenticationController
    MockMvc mockMvc

    def setup() {
        authenticationService = Mock()
        authenticationController = new AuthenticationController(authenticationService)
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController)
                .setControllerAdvice(ErrorHandler.class)
                .build()
    }

    // login
    def "TestLogin"() {
        given:
        def url = "/v1/auth/login"

        def loginRequest = LoginRequest.builder()
                .email("hamburgAcademy@gmail.com")
                .password("password2002#")
                .build()

        def loginResponse = LoginResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build()

        def jsonLoginRequest =
                """
                   {
                       "email": "hamburgAcademy@gmail.com",
                       "password": "password2002#"
                   }
                """

        def jsonLoginResponse =
                """
                   {
                       "accessToken": "accessToken",
                       "refreshToken": "refreshToken"
                   }
                """

        when:
        def jsonResponse = mockMvc.perform(
                post(url)
                        .contentType(APPLICATION_JSON)
                        .header(AUTHORIZATION, "")
                        .content(jsonLoginRequest)
        ).andReturn()

        then:
        1 * authenticationService.login(loginRequest) >> loginResponse
        jsonResponse.response.status == OK.value()
        JSONAssert.assertEquals(jsonLoginResponse.toString(), jsonResponse.response.contentAsString.toString(), true)
    }

    // refresh
    def "TestRefresh"() {
        given:
        def url = "/v1/auth/refresh"

        def refreshRequest = RefreshTokenRequest.builder()
                .refreshToken("refreshToken")
                .build()

        def loginResponse = LoginResponse.builder()
                .refreshToken("refreshTokenNew")
                .accessToken("accessTokenNew")
                .build()

        def jsonRefreshRequest =
                """
                   {
                       "refreshToken": "refreshToken"
                   }
                """

        def jsonLoginResponse =
                """
                   {
                       "refreshToken": "refreshTokenNew",
                       "accessToken": "accessTokenNew"
                   }
                """

        when:
        def jsonResponse = mockMvc.perform(
                post(url)
                        .contentType(APPLICATION_JSON)
                        .header(AUTHORIZATION, "")
                        .content(jsonRefreshRequest)
        ).andReturn()

        then:
        1 * authenticationService.refresh(refreshRequest) >> loginResponse
        jsonResponse.response.status == OK.value()
        JSONAssert.assertEquals(jsonLoginResponse.toString(), jsonResponse.response.contentAsString.toString(), true)
    }
}
