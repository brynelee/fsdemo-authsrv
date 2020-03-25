package com.xdorg1.authsrv.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;

import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @Description: 用户成功退出
 * @ProjectName: spring-parent
 * @Package: com.yaomy.security.handler.AjaxLogoutSuccessHandler
 * @Date: 2019/7/1 15:39
 * @Version: 1.0
 */

@Component
public class UserLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        //String accessToken = request.getParameter("access_token");
        response.setContentType("application/json;charset=UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString("退出成功"));
    }
}

