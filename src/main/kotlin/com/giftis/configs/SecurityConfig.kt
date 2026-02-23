package com.giftis.configs

import com.giftis.exceptions.user.UserNotFoundException
import com.giftis.security.infrastructure.security.filters.JwtFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val accessDeniedHandler: AccessDeniedHandler,
    private val authenticationEntryPoint: AuthenticationEntryPoint,
    private val corsConfigurationSource: CorsConfigurationSource,
    private val jwtFilter: JwtFilter
) {

    @Bean
    fun userDetailsService(): UserDetailsService = UserDetailsService {
        _ -> throw UserNotFoundException()
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain = http
        .csrf { csrf -> csrf.disable() }
        .cors {
            cors -> cors.configurationSource(corsConfigurationSource)
        }
        .authorizeHttpRequests { auth -> auth
            .requestMatchers("/v1/auth/**").permitAll()
            .requestMatchers("/v1/user").permitAll()
            .requestMatchers("/v1/links/**").authenticated()
            .requestMatchers("/v1/wish/**").authenticated()
            .anyRequest().permitAll()
        }
        .sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        .exceptionHandling { exceptionHandling -> exceptionHandling
            .accessDeniedHandler(accessDeniedHandler)
            .authenticationEntryPoint(authenticationEntryPoint)
        }
        .addFilterBefore(
            jwtFilter,
            UsernamePasswordAuthenticationFilter::class.java
        )
        .build()

}