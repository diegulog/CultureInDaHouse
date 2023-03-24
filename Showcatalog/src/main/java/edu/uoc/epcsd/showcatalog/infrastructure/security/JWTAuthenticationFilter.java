package edu.uoc.epcsd.showcatalog.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authManager;
    private final JwtProperties jwtProperties;

    public JWTAuthenticationFilter(@NotNull AuthenticationManager authManager, JwtProperties jwtProperties) {
        super();
        this.authManager = authManager;
        this.jwtProperties = jwtProperties;
    }
    @Override
    @NotNull
    public Authentication attemptAuthentication(@NotNull HttpServletRequest req, @Nullable HttpServletResponse res) throws AuthenticationException {
        if (!req.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + req.getMethod());
        }
        String username = obtainUsername(req);
        String password = obtainPassword(req);
        return authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>()));

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        List<String> claims = new ArrayList<>();
        for (GrantedAuthority authority : authResult.getAuthorities()) {
            claims.add(authority.getAuthority());
        }
        User user = (User) authResult.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC512(jwtProperties.secret);
        String token = JWT.create()
                .withIssuer("auth0")
                .withSubject(user.getUsername())
                .withClaim("auth", claims)
                .sign(algorithm);
        response.addHeader("Authorization", "Bearer " + token);
    }
}
