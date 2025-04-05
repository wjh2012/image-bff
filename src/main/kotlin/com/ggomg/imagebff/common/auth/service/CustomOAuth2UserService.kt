package com.ggomg.imagebff.common.auth.service

import com.ggomg.imagebff.common.auth.jwt.JwtTokenService
import com.ggomg.imagebff.user.entity.AuthType
import com.ggomg.imagebff.user.entity.User
import com.ggomg.imagebff.user.entity.UserRole
import com.ggomg.imagebff.user.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class CustomOAuth2UserService(
    private val userRepository: UserRepository,
    private val jwtTokenService: JwtTokenService
) : OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val delegate = DefaultOAuth2UserService()
        val oAuth2User = delegate.loadUser(userRequest)

        val registrationId = userRequest.clientRegistration.registrationId
        val userNameAttributeName = userRequest.clientRegistration
            .providerDetails
            .userInfoEndpoint
            .userNameAttributeName

        val attributes = oAuth2User.attributes
        val email = attributes["email"] as? String ?: ""

        val user = userRepository.findByEmail(email)
            ?: run {
                val newUser = User(
                    name = attributes["name"] as? String ?: "",
                    email = email,
                    password = "",
                    authType = AuthType.GOOGLE,
                    userRole = UserRole.ROLE_USER
                )
                userRepository.save(newUser)
            }

        val token = jwtTokenService.generateToken(user.email)

        return DefaultOAuth2User(
            setOf(SimpleGrantedAuthority("ROLE_USER")),
            attributes,
            userNameAttributeName
        )
    }
}