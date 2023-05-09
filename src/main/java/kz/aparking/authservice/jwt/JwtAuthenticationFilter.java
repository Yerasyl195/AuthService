package kz.aparking.authservice.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.aparking.authservice.logout.TokenBlacklistService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService jwtUserDetailsService;
    private final TokenBlacklistService tokenBlacklistService;


    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil, JwtUserDetailsService jwtUserDetailsService, TokenBlacklistService tokenBlacklistService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.tokenBlacklistService = tokenBlacklistService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");

        String phoneNumber = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            if (jwtToken != null && !tokenBlacklistService.isBlacklisted(jwtToken)) {
                try {
                    phoneNumber = jwtTokenUtil.getPhoneNumberFromToken(jwtToken);
                } catch (Exception e) {
                    logger.error("Unable to get JWT Token");
                }
            }
        }

        if (phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(phoneNumber);

            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}

//    @Override
//    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain) throws jakarta.servlet.ServletException, IOException {
//        final String requestTokenHeader = request.getHeader("Authorization");
//
//        String phoneNumber = null;
//        String jwtToken = null;
//
//        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ") && !tokenBlacklistService.isBlacklisted(jwtToken)) {
//            jwtToken = requestTokenHeader.substring(7);
//            try {
//                phoneNumber = jwtTokenUtil.getPhoneNumberFromToken(jwtToken);
//            } catch (Exception e) {
//                logger.error("Unable to get JWT Token");
//            }
//        }
//
//        if (phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(phoneNumber);
//
//            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
//                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
