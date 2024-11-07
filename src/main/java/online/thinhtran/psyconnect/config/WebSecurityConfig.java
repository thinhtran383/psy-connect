package online.thinhtran.psyconnect.config;

import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.components.JwtFilter;
import online.thinhtran.psyconnect.entities.User;
import org.apache.tomcat.Jar;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.messaging.Message;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableWebMvc
@EnableMethodSecurity
public class WebSecurityConfig {
    @Value("${api.base-path}")
    private String apiPrefix;
    private final JwtFilter jwtFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.authorizeHttpRequests(request -> {
            request
                    .requestMatchers(
                            // swagger
                            "/swagger-ui/**",
                            "/v3/api-docs/",
                            "/v3/api-docs/**",
                            "/api-docs",
                            "/api-docs/**",
                            "/swagger-resources",
                            "/swagger-resources/**",
                            "/configuration/ui",
                            "/configuration/security",
                            "/swagger-ui/**",
                            "/swagger-ui.html",

                            // login
                            String.format("%s/auth/login", apiPrefix),
                            String.format("%s/auth/register", apiPrefix),

                            // chat
                            "/socket.io/**",
                            "/socket.io",
                            "/ws/**",
                            "/ws",
                            "/user",
                            "/user/**",
                            "/info",
                            "/info/**"
                    ).permitAll();
        });

        httpSecurity.authorizeHttpRequests(request -> request.anyRequest().authenticated());
        httpSecurity
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider);

        return httpSecurity.build();
    }
}
