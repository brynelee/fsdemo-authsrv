package com.xdorg1.authsrv.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /*auth
                .inMemoryAuthentication()
                .withUser("dahai").password("666666").roles("USER")
                .and()
                .withUser("xiaodong.li").password("123456").roles("USER", "ADMIN");*/
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        auth.parentAuthenticationManager(authenticationManagerBean());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
/*
        http.authorizeRequests()
            .antMatchers("/","/oauth/**","/login").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/login")
            .permitAll()
            .and()
            .logout()
            .permitAll();
*/

        //http.csrf().disable();

/*      //原来的设置
        http
                .requestMatchers().antMatchers("/oauth/**","/login/**","/logout/**")
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/**").authenticated()
                .and()
                .formLogin()
                .loginPage("/oauth/login")
                .loginProcessingUrl("/oauth/authorize")
                .permitAll(); //新增login form支持用户登录及授权
*/


/*        //不拦截 oauth 开放的资源
        http.requestMatchers()
                .anyRequest()
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/**").permitAll();
*/

        http
                // 必须配置，不然OAuth2的http配置不生效----不明觉厉
                .requestMatchers()
                //.antMatchers("/auth/login", "/auth/authorize","/oauth/authorize")
                .antMatchers("/auth/**","/oauth/**")
                .and()
                .authorizeRequests()
                // 自定义页面或处理url是，如果不配置全局允许，浏览器会提示服务器将页面转发多次
                .antMatchers("/auth/login", "/auth/authorize")
                .permitAll()
                .anyRequest()
                .authenticated();

        // 表单登录
        http.formLogin()
                // 登录页面
                .loginPage("/auth/login")
                // 登录处理url
                .loginProcessingUrl("/auth/authorize");
        http.httpBasic().disable();

        //http.logout().permitAll();

/*
        http

                .logout(l -> l
                        .logoutSuccessUrl("/exit").permitAll()
                );
*/
        http.logout().logoutSuccessHandler(logoutSuccessHandler);

    }


}
