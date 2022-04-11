package com.example.store.filter;

import com.example.store.exception.InvalidSessionException;
import com.example.store.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomBeforeAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final UserService userService;

    public CustomBeforeAuthenticationFilter(AuthenticationManager authenticationManager,
                                            AuthenticationSuccessHandler authenticationSuccessHandler,
                                            UserService userService) {
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/**"));
        setAuthenticationManager(authenticationManager);
        setAuthenticationSuccessHandler(authenticationSuccessHandler);
        this.userService = userService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
    throws AuthenticationException {

        String sessionId = request.getHeader("Session-Id");

        UserDetails userBySessionId = userService.getUserBySessionId(sessionId);
        if (userBySessionId == null) {
            throw new InvalidSessionException("Wrong session id!");
        }


        var authenticate = new UsernamePasswordAuthenticationToken(
                userBySessionId,
                null,
                userBySessionId.getAuthorities()
        );

        authenticate.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        return authenticate;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }



}