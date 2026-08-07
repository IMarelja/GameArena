package hr.algebra.gamearena.api.config;

import hr.algebra.gamearena.api.filter.JwtAuthenticationFilter;
import hr.algebra.gamearena.api.filter.UnconfiguredEndpointDenier;
import hr.algebra.gamearena.api.model.user.Role;
import hr.algebra.gamearena.api.service.jwt.IJwtService;
import hr.algebra.gamearena.api.service.user.IUserService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(IUserService userService, IJwtService jwtService) {
        return new JwtAuthenticationFilter(userService, jwtService);
    }

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtAuthenticationFilterRegistration(
            JwtAuthenticationFilter jwtAuthenticationFilter)
    {
        FilterRegistrationBean<JwtAuthenticationFilter> registration = new FilterRegistrationBean<>(jwtAuthenticationFilter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            SecurityErrorHandler securityErrorHandler,
            UnconfiguredEndpointDenier unconfiguredEndpointDenier)
    {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/error").permitAll()

                        // Any authenticated user
                        .requestMatchers(HttpMethod.GET, "/api/user/me").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/team/for/me").authenticated()

                        // Admin Role
                        .requestMatchers(HttpMethod.GET, "/api/user/{id}/full").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/api/games").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/api/games/**").hasRole(Role.ADMIN.name())
                        .requestMatchers("/api/log/**").hasRole(Role.ADMIN.name())

                        // Anonymous
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/user", "/api/user/{id}").permitAll()
                        .requestMatchers("/api/games/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/team", "/api/team/{id}").permitAll()

                        // Anything not listed above is closed by default
                        .anyRequest().access(unconfiguredEndpointDenier))

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(securityErrorHandler)
                        .accessDeniedHandler(securityErrorHandler))

                .build();
    }
}
