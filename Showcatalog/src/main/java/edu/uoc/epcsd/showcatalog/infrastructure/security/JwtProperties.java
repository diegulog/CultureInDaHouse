package edu.uoc.epcsd.showcatalog.infrastructure.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtProperties {
    @Value(value = "${jwt-security.secret}")
    public String secret;
}
