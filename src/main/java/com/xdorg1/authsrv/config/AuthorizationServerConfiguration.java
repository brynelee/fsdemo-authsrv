package com.xdorg1.authsrv.config;

import com.xdorg1.authsrv.service.impl.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/*
spring.security.oauth2.client.authorization-uri=/oauth/authorize
spring.security.oauth2.client.token-uri=/oauth/token

/oauth/authorize
/oauth/token
/oauth/check_token
/oauth/confirm_access
/oauth/error
/oauth/token_key
*/

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AuthenticationManager authenticationManager;

    //密码加密
    @Autowired
    private PasswordEncoder passwordEncoder;

/*    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public TokenStore tokenStore(){
        //使用Redis存储Token
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        //设置redis token存储的前缀
        redisTokenStore.setPrefix("auth-token:");
        return redisTokenStore;
    }*/

/*    @Bean
    public DefaultTokenServices tokenServices(){
        DefaultTokenServices tokenServices = new DefaultTokenServices();

        //配置token存储
        tokenServices.setTokenStore(tokenStore());
        //开启支持refresh_token，此处如果之前没有配置，启动服务后再配置重启服务，可能会导致不返回token的问题，解决方式：清除redis对应token存储
        tokenServices.setSupportRefreshToken(true);
        //复用refresh_token
        tokenServices.setReuseRefreshToken(true);
        //token有效期，设置12小时
        tokenServices.setAccessTokenValiditySeconds(12 * 60 * 60);
        //refresh_token有效期，设置一周
        tokenServices.setRefreshTokenValiditySeconds(7 * 24 * 60 * 60);
        return tokenServices;
    }*/

    /**
     * 自定义用户信息
     *
     * @return
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("test-secret");
        return converter;
    }

    @Bean
    public JwtTokenStore jwtTokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                //.passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder())
                .withClient("webclient1")
                .secret(passwordEncoder.encode("time4@FUN"))
                .authorizedGrantTypes("refresh_token", "password", "client_credentials")
                .scopes("all", "read", "write")

                .and().withClient("webclient2").authorities("authorization_code", "refresh_token")
                .secret(passwordEncoder.encode("time4@FUN"))
                .authorizedGrantTypes("authorization_code")
                .scopes("all", "read", "write")
                .accessTokenValiditySeconds(7200)
                .refreshTokenValiditySeconds(10000)
                .redirectUris("http://localhost:8084/callback", "http://localhost:8084/signin")

                .and().withClient("webclient3")
                .secret(passwordEncoder.encode("time4@FUN"))
                .authorizedGrantTypes("implicit")
                .scopes("all", "read", "write")
                .accessTokenValiditySeconds(7200)
                .refreshTokenValiditySeconds(10000)
                .redirectUris("http://localhost:8084/callback", "http://localhost:8084/signin")

                .and().withClient("fsdemo-usercenter")
                .secret(passwordEncoder.encode("time4@FUN"))
                .authorizedGrantTypes("refresh_token", "password", "client_credentials")
                .scopes("all", "read", "write")

                .and().withClient("fsdemo-frontend").authorities("authorization_code", "refresh_token")
                .secret(passwordEncoder.encode("time4@FUN"))
                .authorizedGrantTypes("authorization_code")
                .scopes("all", "read", "write")
                .accessTokenValiditySeconds(7200)
                .refreshTokenValiditySeconds(10000)
                .redirectUris("http://fsdemo-usercenter:8081/usercenter/auth")

                .and().withClient("fsdemoapp").authorities("authorization_code", "refresh_token")
                .secret(passwordEncoder.encode("time4@FUN"))
                .authorizedGrantTypes("authorization_code")
                .scopes("all", "read", "write")
                .accessTokenValiditySeconds(7200)
                .refreshTokenValiditySeconds(10000)
                .redirectUris("http://fsdemo-usercenter:8081/usercenter/auth");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(authenticationManager)
                //for JWT begin
                .tokenStore(jwtTokenStore())
                .accessTokenConverter(accessTokenConverter())
                //for JWT end
                .userDetailsService(userDetailsService());
                //配置token的服务和存储
                //for RedisTokenStore begin
//                .tokenServices(tokenServices())
//                .tokenStore(tokenStore());
                //for RedisTokenStore end
        // 最后一个参数为替换之后授权页面的url
        endpoints.pathMapping("/oauth/confirm_access","/custom/confirm_access");
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
//        //curl -i -X POST -H "Accept: application/json" -u "client_1:123456" http://localhost:5000/oauth/check_token?token=a1478d56-ebb8-4f21-b4b6-8a9602df24ec
        security.tokenKeyAccess("permitAll()")         //url:/oauth/token_key,exposes public key for token verification if using JWT tokens
                .checkTokenAccess("isAuthenticated()") //url:/oauth/check_token allow check token
                .allowFormAuthenticationForClients();

        //受信任的资源服务能够获取到公有密匙
//        oauthServer.tokenKeyAccess("isAnonymous() || hasAuthority('ROLE_TRUSTED_CLIENT')")
//                   .checkTokenAccess("hasAuthority('ROLE_TRUSTED_CLIENT')");
    }

}
