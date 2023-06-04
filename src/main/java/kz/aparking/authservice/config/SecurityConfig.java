package kz.aparking.authservice.config;

import kz.aparking.authservice.jwt.JwtAuthenticationEntryPoint;
import kz.aparking.authservice.jwt.JwtAuthenticationFilter;
import kz.aparking.authservice.jwt.JwtTokenUtil;
import kz.aparking.authservice.jwt.JwtUserDetailsService;
import kz.aparking.authservice.services.TokenBlacklistService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements SecurityConfigurer<DefaultSecurityFilterChain, HttpSecurity> {
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService jwtUserDetailsService;
    private final TokenBlacklistService tokenBlacklistService;


    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                          JwtTokenUtil jwtTokenUtil,
                          JwtUserDetailsService jwtUserDetailsService,
                          TokenBlacklistService tokenBlacklistService) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenUtil, jwtUserDetailsService, tokenBlacklistService);
    }
    @Override
    public void init(HttpSecurity http) {
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/auth/**", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .requestMatchers(new OrRequestMatcher(
                        new AntPathRequestMatcher("/authenticate"),
                        new AntPathRequestMatcher("/register")))
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}

