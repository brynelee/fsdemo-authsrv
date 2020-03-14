package com.xdorg1.authsrv.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/authsrv")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/users")
    public Map<String, Object> users(OAuth2Authentication user) {

        logger.info("UserController - users() called with user: " + user);

        //SecurityContext userContext = SecurityContextHolder.getContext();

        //logger.info("UserController users() - userContext: " + userContext);

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put(
                "user",
                user.getUserAuthentication().getPrincipal());
        userInfo.put("authorities",
                AuthorityUtils.authorityListToSet(
                        user.getUserAuthentication().getAuthorities()));

        return userInfo;
    }
}
