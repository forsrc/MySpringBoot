package com.forsrc.boot.authserver.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

//@Configuration
@EnableAuthorizationServer
@EnableConfigurationProperties({ AuthorizationServerProperties.class })
@Order(77)
public class Oauth2ServerJdbcConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthorizationServerProperties authorizationServerProperties;
    @Autowired
    private DataSource dataSource;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // @formatter:off
//        clients
//            .inMemory()
//            .withClient("forsrc")
//            .resourceIds("forsrc")
//            .secret("forsrc")
//            .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
//            .scopes("read", "write")
//            .accessTokenValiditySeconds((int) TimeUnit.MINUTES.toSeconds(30))
//            .autoApprove(true)
//            ;
       // @formatter:on
        clients.withClientDetails(myClientDetailsService());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer()));
        // @formatter:off
        endpoints
            .authorizationCodeServices(jdbcAuthorizationCodeServices())
            .tokenEnhancer(tokenEnhancerChain)
            .tokenStore(jdbcTokenStore())
            .approvalStore(jdbcApprovalStore())
            .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
            .authenticationManager(authenticationManager)
        ;
        // @formatter:on
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // @formatter:off
        security
            .tokenKeyAccess("permitAll()")
            .checkTokenAccess("isAuthenticated()");
            ;
        // @formatter:on
    }

    @Bean
    public ClientCredentialsTokenEndpointFilter checkTokenEndpointFilter() {
        ClientCredentialsTokenEndpointFilter filter = new ClientCredentialsTokenEndpointFilter("/oauth/check_token");
        filter.setAuthenticationManager(authenticationManager);
        filter.setAllowOnlyPost(true);
        return filter;
    }

    @Bean
    public DataSourceInitializer dataSourceInitializer(final DataSource dataSource) {
        final DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(databasePopulator());
        return initializer;
    }

    private DatabasePopulator databasePopulator() {
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("classpath:schema.sql"));
        populator.addScript(new ClassPathResource("classpath:data.sql"));
        return populator;
    }

    @Bean("jdbcTokenServices")
    @Primary
    public DefaultTokenServices jdbcTokenServices() {
        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(jdbcTokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        // return new CustomTokenEnhancer();
        return new TokenEnhancer() {
            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
                final Map<String, Object> additionalInfo = new HashMap<>();
                additionalInfo.put("organization", authentication.getName() + UUID.randomUUID());
                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
                return accessToken;
            }
        };
    }

    @Bean
    public JdbcTokenStore jdbcTokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Bean
    public AuthorizationCodeServices jdbcAuthorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    @Bean
    public ApprovalStore jdbcApprovalStore() {
        TokenApprovalStore tokenApprovalStore = new TokenApprovalStore();
        tokenApprovalStore.setTokenStore(jdbcTokenStore());
        return tokenApprovalStore;
    }

    //@FrameworkEndpoint
    public static class RevokeTokenEndpoint {
        @Resource(name = "jdbcTokenServices")
        private ConsumerTokenServices jdbcTokenServices;

        @RequestMapping(method = RequestMethod.DELETE, value = "/oauth/token")
        @ResponseBody
        public void revokeToken(HttpServletRequest request) {
            String authorization = request.getHeader("Authorization");
            if (authorization != null && authorization.contains("Bearer")) {
                String tokenId = authorization.substring("Bearer".length() + 1);
                jdbcTokenServices.revokeToken(tokenId);
            }
        }
    }

    @Bean
    public ClientDetailsService myClientDetailsService() throws Exception {
        return new ClientDetailsService() {
            private final JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(dataSource);

            @Override
            public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
                return jdbcClientDetailsService.loadClientByClientId(clientId);
            }
        };
    }
}
