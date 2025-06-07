package com.ggomg.imagebff.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.ggomg.imagebff.auth.JsonUsernamePasswordAuthenticationConverter
import com.ggomg.imagebff.auth.security.jwt.config.JwtProperties
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationFilter
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.security.web.session.HttpSessionEventPublisher
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

private val logger = KotlinLogging.logger {}

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(JwtProperties::class)
class SessionSecurityConfig(
    private val objectMapper: ObjectMapper,
) {


    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration()
        config.allowedOrigins = listOf("http://localhost:3000")
        config.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        config.allowedHeaders = listOf("*")
        config.allowCredentials = true

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        return source
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }


    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun sessionRegistry(): SessionRegistryImpl = SessionRegistryImpl()


    @Bean
    fun httpSessionEventPublisher(): HttpSessionEventPublisher = HttpSessionEventPublisher()

    @Bean
    fun jsonUsernamePasswordAuthenticationConverter(): JsonUsernamePasswordAuthenticationConverter {
        return JsonUsernamePasswordAuthenticationConverter(objectMapper)
    }

    @Bean
    fun defaultFilterChain(http: HttpSecurity, authManager: AuthenticationManager): SecurityFilterChain {

        val authenticationFilter =
            AuthenticationFilter(authManager, jsonUsernamePasswordAuthenticationConverter()).apply {
                setRequestMatcher(AntPathRequestMatcher("/auth/login", "POST"))
                setSuccessHandler(jsonAuthenticationSuccessHandler())
                setFailureHandler(jsonAuthenticationFailureHandler())
            }
        
        http
            .cors { it.configurationSource(corsConfigurationSource()) }
            .csrf { it.disable() }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            }
            .securityContext {
                it.securityContextRepository(HttpSessionSecurityContextRepository())
            }
            .authorizeHttpRequests {
                it.requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/auth/**",
                    "/login" // 명시적 permitAll 추가
                ).permitAll().anyRequest().authenticated()
            }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .addFilterAt(authenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun jsonAuthenticationSuccessHandler(): AuthenticationSuccessHandler =
        AuthenticationSuccessHandler { request, response, authentication ->
            logger.info("Authentication success for user={}", authentication.name)

            // 세션에 SecurityContext 저장
            val context = SecurityContextHolder.createEmptyContext().apply {
                this.authentication = authentication
            }
            HttpSessionSecurityContextRepository().saveContext(context, request, response)

            response.apply {
                status = HttpStatus.OK.value()
                contentType = MediaType.APPLICATION_JSON_VALUE
            }
            objectMapper.writeValue(
                response.outputStream, mapOf(
                    "status" to "success",
                    "user" to authentication.name
                )
            )
        }

    @Bean
    fun jsonAuthenticationFailureHandler(): AuthenticationFailureHandler =
        AuthenticationFailureHandler { request, response, exception ->
            logger.error("Authentication failure: {}", exception.message, exception)

            response.apply {
                status = HttpStatus.UNAUTHORIZED.value()
                contentType = MediaType.APPLICATION_JSON_VALUE
            }
            objectMapper.writeValue(
                response.writer, mapOf(
                    "status" to "failure",
                    "error" to exception.message
                )
            )
        }

}