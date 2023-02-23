package com.haris.oauth2.sample.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;
import java.util.Base64;

@Configuration
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {

    private final String PRIVATE_KEY_BASE64 = "LS0tLS1CRUdJTiBSU0EgUFJJVkFURSBLRVktLS0tLQpNSUlFb2dJQkFBS0NBUUVBeUgrcDVZSlZLdVd6QUhwRkJka2w0WjZSMnhad1ZMK0cvUzY2SmdGbVlMc3lKZm1NCmFRazJMOHpqdnZ0UmZDcGNHLy9jYVVTZ1VlTDFMUnZRaGRSaEc3elFxY2tjTkdFb0hoSVAyU2YxcEJwMmV1QnQKUW5QUEtrc1lHaUd4MHpwMGx2ZExrOW84c25scXBlY0dHTlZ0TUdXUmxNNHdQcGFGMklxUUt0T2lOUEZ2V1h3OAppNllQUzZBMWxvSkJZZGkwb2VzMUNCQjZ0RHlzRjJRRGhwbkg5eUVibU1lVEJ2L0ZTa0pscVE0NmU3dXo0Q0VECmRmblJYWlZxQkU1SXl6N3JDSFFBek5SSGVMVlBra294UEtTZk14QUkwSHAyQWUwaFdSdXhQOWRYbkI3UFFLdloKS2lxbTRwMkw0UGM1MUZBSWI1bWF5b1hXTCtHYWY5akdES093SXdJREFRQUJBb0lCQUV3RENsZzJseTlTTWhERwpTaEN6aUljRzVPa1lrckd1Z3ZETm5FajRVdVh0VWsvUFVnb29NZURoWHA1ekRTdHpxOFA1dG9nNk9rV1JNNzVNClF6dlpqbzlEZ0NBK1hIRUhQSHhhQUtRbFlCMDg0eG9tcDRWLy9sUmNiK0RJVVhCeFF1NVJCdGdnN3M0QzlnbTcKa2pZc0E5OUxRbXdWMTNpQy9HOWg2Tkl6dGRVMzN3d09iSnErKzJKcERVZmRRMzhCemNsS1pKb0M4eGlscnNITgpUOGFMVkVZRmdxeVhCSXhNUkxzQ1VkeVVTdU5Ib3lUK2xsZFc1c3B3WExKSmQzTEpLSXB5NnFxWlA0bSs0MUFuCk1PTDdiRUVzQXp0ejh0eXNJWHpiQmRQTFZqVmtmZTgwTEtlbk1PNS81dFJLSlVTWTdXNVBzSjZrM3RBWVg5WXgKZUJqMTNsRUNnWUVBOFdKaFFFVzBBRVpmY3RvRzdLcE5Sa2t2cm9qV3VtelFIWGE5bWg1QVpIYnFvSERKWjBROApZNVJWb3dtengzcEJlc093SUJKRTE2U0dtaUU4NEVLbVNJbGNHckRqeFc5Smx6ZGsvTW14WmkvM1JCanBxWUxLCmhPZlhNYlJSMzROcTVjb3E0NjVxY3VoWDREb2hwRkhyNi92cGNwU0hjU2NqNmQ4bHMrNEpKc2NDZ1lFQTFLT0gKZEJ4eWlxWFduaFp5TUNNSm8rOFplVGpRK1ZnQkxqYUdDdWZhZ05vUHNSQWMwb1RwMHRtbGZoTVVqbFhCSUkvTgpKd0JvN3oyaGl0allaRVB0ZXY4N25xdDlBVEVsaGFGeXc0cTA2a21YSUVJeWFuRkJxZ01qaW1yRDAyQ3lPcWhKCit5YnZLUVNXYjU4b2twM2c4cHRvSUZDa3pTRmxMeERwVTJzalg4VUNnWUI2a1pnTVNOaVQ0eWdJYThsa1Z6MlIKMlF2TE5sVENxUW9zUytVTzR4bVRhZ2JOblRmSHBoQjhDQkJNRDFzb2lWM0NkcWR5UVpTUFkwMnQ2ZjY5YVZxQwpMcTJFS0JvZ1lOZ2ZYdE42d1BmQmljMm0ySGVkeVhvSWJxa0k1aVZFdDdpS0lsUVE5TG04eTFvRDA4RE5iWU84CmVsZ2FCRzJaK1dQMUY0aFBpeWsrZFFLQmdBWlBpQ0NaQzRrejNNZTZVTTgrNnZWVGprVDd1Ni82dmFZNmRnWHkKMVpoSFlIblh5Wk14NEtLaVZQZ0Y1dkZHZUVIWUlyV0tuSktsR0pzL1RrVERnMVBJS1dQUElSbWM5TWtmZm9GeApKMmRnb2J5U1dsNndWOHVNNWt3Z1dRbDM4REl2ZG1xZXdEQk9jNHlDZzdpK09NUGFldCtpcFJGMWNVUlJVeXRuCm5aZ2xBb0dBUmVWWExqUmg4UHROVUg2MnQzc2VQMUVzMEtMVUQzRjBZeXdibjVCSUdSUW5rTHlVRURDWTgwWHgKdDVUQUlySjdLVGVDU3NNQlpBcWFvNzdkVDhSUmhHNkFuMW1xOFRBc2ZBTTFVUExsT3k4VGpyaUxFOVJtOFV2RgpHMWJGK0wxcU5LVk9Cd2tsaUltcmVvRXVQa0plMm1TSlB1UkdsdGwybmNmY293bSswUFk9Ci0tLS0tRU5EIFJTQSBQUklWQVRFIEtFWS0tLS0tCg==";
    private final String PUBLIC_KEY_BASE64 = "LS0tLS1CRUdJTiBQVUJMSUMgS0VZLS0tLS0KTUlJQklqQU5CZ2txaGtpRzl3MEJBUUVGQUFPQ0FROEFNSUlCQ2dLQ0FRRUF5SCtwNVlKVkt1V3pBSHBGQmRrbAo0WjZSMnhad1ZMK0cvUzY2SmdGbVlMc3lKZm1NYVFrMkw4emp2dnRSZkNwY0cvL2NhVVNnVWVMMUxSdlFoZFJoCkc3elFxY2tjTkdFb0hoSVAyU2YxcEJwMmV1QnRRblBQS2tzWUdpR3gwenAwbHZkTGs5bzhzbmxxcGVjR0dOVnQKTUdXUmxNNHdQcGFGMklxUUt0T2lOUEZ2V1h3OGk2WVBTNkExbG9KQllkaTBvZXMxQ0JCNnREeXNGMlFEaHBuSAo5eUVibU1lVEJ2L0ZTa0pscVE0NmU3dXo0Q0VEZGZuUlhaVnFCRTVJeXo3ckNIUUF6TlJIZUxWUGtrb3hQS1NmCk14QUkwSHAyQWUwaFdSdXhQOWRYbkI3UFFLdlpLaXFtNHAyTDRQYzUxRkFJYjVtYXlvWFdMK0dhZjlqR0RLT3cKSXdJREFRQUIKLS0tLS1FTkQgUFVCTElDIEtFWS0tLS0t";
    private final String CLIENT_ID = "oauth2-clientid";
    private final String CLIENT_SECRET = "oauth2-clientsecret";

    @Qualifier("AuthenticationManagerBean")
    @Autowired
    private AuthenticationManager authenticationManager;

    @Bean
    public JwtAccessTokenConverter tokenEnhancer() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(Arrays.toString(Base64.getDecoder().decode(PRIVATE_KEY_BASE64)));
        jwtAccessTokenConverter.setVerifierKey(Arrays.toString(Base64.getDecoder().decode(PUBLIC_KEY_BASE64)));
        return jwtAccessTokenConverter;
    }

    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(tokenEnhancer());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()
                .withClient(CLIENT_ID)
                .secret(CLIENT_SECRET)
                .scopes("read", "write")
                .authorizedGrantTypes("password", "refresh_token")
                .accessTokenValiditySeconds(20_000)
                .refreshTokenValiditySeconds(25_000);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(authenticationManager)
                .tokenStore(tokenStore())
                .tokenEnhancer(tokenEnhancer());
    }
}
