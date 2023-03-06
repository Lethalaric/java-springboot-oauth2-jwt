package com.haris.oauth2.sample.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
@EnableAuthorizationServer
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {

    @Value("${oauth2.clientId}")
    private String clientId;

    @Value("${oauth2.secret}")
    private String secret;

    @Value("${oauth2.accessTokenValiditySeconds}")
    private int accessTokenValidityInSeconds;

    @Value("${oauth2.refreshTokenValiditySeconds}")
    private int refreshTokenValidityInSeconds;

    @Value("${oauth2.authorizedGrantTypes}")
    private String[] authorizedGrantTypes;

    private AuthenticationManager authenticationManager;

    private PasswordEncoder passwordEncoder;

    private UserDetailsService userDetailsService;

    @Bean
    JwtAccessTokenConverter accessTokenConverter() {
        return new JwtAccessTokenConverter();
    }

    public OAuth2Config(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(clientId)
                .secret(passwordEncoder.encode(secret))
                .refreshTokenValiditySeconds(refreshTokenValidityInSeconds)
                .accessTokenValiditySeconds(accessTokenValidityInSeconds)
                .authorizedGrantTypes(authorizedGrantTypes)
                .scopes("read", "write")
                .resourceIds("api");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .accessTokenConverter(accessTokenConverter())
                .userDetailsService(userDetailsService)
                .authenticationManager(authenticationManager);
    }
}
