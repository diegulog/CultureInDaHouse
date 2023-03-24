package edu.uoc.epcsd.showcatalog;

import edu.uoc.epcsd.showcatalog.domain.service.UserService;
import edu.uoc.epcsd.showcatalog.infrastructure.security.JWTAuthenticationFilter;
import edu.uoc.epcsd.showcatalog.infrastructure.security.JWTAuthorizationFilter;
import edu.uoc.epcsd.showcatalog.infrastructure.security.JwtProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Properties;

@EnableGlobalMethodSecurity(
        securedEnabled = true,
        prePostEnabled = true
)
@Configuration
public class WebConfig extends WebSecurityConfigurerAdapter {
    private final UserService userService;
    private final JwtProperties jwtProperties;

    public WebConfig(UserService userService, JwtProperties jwtProperties) {
        super();
        this.userService = userService;
        this.jwtProperties = jwtProperties;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // no sessions
                .and()
                .authorizeRequests()
                .antMatchers("/error/**","/**").permitAll()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtProperties))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtProperties));
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        return provider;
    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
