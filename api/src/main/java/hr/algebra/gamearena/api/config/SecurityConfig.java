package hr.algebra.gamearena.api.config;

import hr.algebra.gamearena.api.filter.JwtAuthenticationFilter;
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

/**
 * The single place that answers "what protects this endpoint?".
 * <p>
 * {@link JwtAuthenticationFilter} only establishes <em>who</em> is calling; every rule about
 * <em>where</em> that caller may go is declared below.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(IUserService userService, IJwtService jwtService) {
        return new JwtAuthenticationFilter(userService, jwtService);
    }

    /**
     * Spring Boot registers every {@link jakarta.servlet.Filter} bean with the servlet container on top
     * of wherever else it is used. The JWT filter belongs to the security chain alone, so the
     * container-level registration is switched off here to stop it running twice.
     */
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
            SecurityErrorHandler securityErrorHandler) throws Exception
    {
        return http
                // Stateless bearer-token API: no session to fixate and no cookie to forge, so the
                // CSRF token and the session machinery would only get in the way.
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Rules are matched top to bottom and the first hit wins, so the specific paths
                // must come before the templated ones - /api/user/me would otherwise be swallowed
                // by /api/user/{id}.
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/error").permitAll()

                        // Anonymous: no token needed
                        .requestMatchers("/api/auth/**").permitAll()

                        // Any authenticated user
                        .requestMatchers(HttpMethod.GET, "/api/user/me").authenticated()

                        // Role based
                        .requestMatchers(HttpMethod.GET, "/api/user/{id}/full").hasRole(Role.ADMIN.name())

                        // Anonymous, but declared last: {id} also matches "me", so the narrower
                        // rules above have to get their chance first.
                        .requestMatchers(HttpMethod.GET, "/api/user", "/api/user/{id}").permitAll()

                        // Anything not listed above is closed by default
                        .anyRequest().authenticated())

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(securityErrorHandler)
                        .accessDeniedHandler(securityErrorHandler))

                .build();
    }
}
