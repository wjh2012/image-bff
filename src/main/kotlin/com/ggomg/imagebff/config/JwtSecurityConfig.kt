package com.ggomg.imagebff.config

import com.ggomg.imagebff.common.auth.filter.JwtAuthenticationFilter
import com.ggomg.imagebff.common.auth.filter.RegistrationTemporaryTokenFilter
import com.ggomg.imagebff.common.auth.jwt.JwtTokenService
import com.ggomg.imagebff.common.auth.oauth2.OAuth2AuthenticationSuccessHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.context.NullSecurityContextRepository


@Configuration
@EnableWebSecurity
class JwtSecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val registrationTemporaryTokenFilter: RegistrationTemporaryTokenFilter,
    private val oAuth2AuthenticationSuccessHandler: OAuth2AuthenticationSuccessHandler,
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun registerFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .securityMatcher("/oauth2/sign-up/**")
            .csrf { it.disable() }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .securityContext { it.securityContextRepository(NullSecurityContextRepository()) } // ✅ 세션 저장 방지
            .oauth2Login(Customizer.withDefaults())
            .authorizeHttpRequests {
                it
                    .requestMatchers(
                        "/oauth2/sign-up/**"
                    ).permitAll()
                    .anyRequest().authenticated()
            }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .addFilterBefore(registrationTemporaryTokenFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun defaultFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .securityMatcher("/**")
            .csrf { it.disable() }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .securityContext { it.securityContextRepository(NullSecurityContextRepository()) } // ✅ 세션 저장 방지
            .oauth2Login(Customizer.withDefaults())
            .authorizeHttpRequests {
                it
                    .requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/auth/**"
                    ).permitAll()
                    .anyRequest().authenticated()
            }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .oauth2Login {
                it
                    .successHandler(oAuth2AuthenticationSuccessHandler)
                    .failureUrl("http://localhost:3000/login?error=missing_email")
            }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)


        return http.build()
    }


}