package com.levinzonr.ezpad.controllers

import com.levinzonr.ezpad.domain.model.TokenResponse
import com.levinzonr.ezpad.domain.payload.EmailLoginPayload
import com.levinzonr.ezpad.domain.payload.FacebookLogin
import com.levinzonr.ezpad.domain.payload.FacebookUser
import com.levinzonr.ezpad.domain.payload.GoogleUser
import com.levinzonr.ezpad.services.UserService
import com.levinzonr.ezpad.utils.baseUrl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.social.facebook.api.impl.FacebookTemplate
import org.springframework.web.client.RestTemplate
import javax.validation.Valid
import java.net.InetAddress
import java.net.URI
import java.net.URL
import javax.inject.Inject
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED
import org.apache.catalina.manager.StatusTransformer.setContentType
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.jwt.JwtHelper.headers
import org.springframework.util.MultiValueMap
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import java.nio.charset.Charset
import java.util.*


@RestController
@RequestMapping("/auth")
class AuthenticationController {

    companion object {
        var fields = arrayOf("id", "email", "first_name", "last_name")
    }

    @Autowired
    private lateinit var userService: UserService

    @PostMapping("/facebook")
    fun loginViaFacebook(
            request: HttpServletRequest,
            @Valid @RequestBody facebookLogin: FacebookLogin) : TokenResponse {

        val fbTemplate = FacebookTemplate(facebookLogin.token)
        val fbUser = fbTemplate.fetchObject("me", FacebookUser::class.java, *(fields))
        val user = userService.processFacebookUser(fbUser)
        return RestAuthHelper.authRedirect(request.baseUrl, user.email, fbUser.id.toString()).also {
            it.user = user.toResponse()
        }
    }

    @CrossOrigin(origins = ["http://localhost:3000"])
    @PostMapping("/email")
    fun loginViaEmail(request: HttpServletRequest, @Valid @RequestBody emailLoginPayload: EmailLoginPayload) : TokenResponse {
        return RestAuthHelper.authRedirect(request.baseUrl, emailLoginPayload.email, emailLoginPayload.password).also {
            it.user = userService.getUserEmail(emailLoginPayload.email).toResponse()
        }
    }
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping("/email")
    fun test() {
        println("tsts")
    }

    @PostMapping("/google")
    fun loginViaGoogle(request: HttpServletRequest,
                       @Valid @RequestBody facebookLogin: FacebookLogin) {

        val uri = UriComponentsBuilder.fromHttpUrl("https://www.googleapis.com/oauth2/v3/tokenInfo")
                .queryParam("id_token", facebookLogin.token)

        print("${uri.toUriString()}")
        val restTemplate = RestTemplate()
        val user = restTemplate.getForEntity(uri.toUriString(), GoogleUser::class.java)
        print(user)



    }


}