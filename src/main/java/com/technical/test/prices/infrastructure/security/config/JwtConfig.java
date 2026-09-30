package com.technical.test.prices.infrastructure.security.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

/**
 * Signing of the JWT access tokens (RS256).
 */
@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfig {

    private static final String KEY_ALGORITHM = "RSA";
    private static final int KEY_SIZE = 2048; //Minimum recommended for RSA

    /**
     * RSA key pair generated in memory at startup: no secret is stored in the repository, at the cost of invalidating
     * the issued tokens on every restart.
     */
    @Bean
    public KeyPair jwtKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        generator.initialize(KEY_SIZE);
        return generator.generateKeyPair();
    }

    @Bean
    public JwtEncoder jwtEncoder(KeyPair jwtKeyPair) {
        return NimbusJwtEncoder.withKeyPair((RSAPublicKey) jwtKeyPair.getPublic(), (RSAPrivateKey) jwtKeyPair.getPrivate())
                .build();
    }
}
