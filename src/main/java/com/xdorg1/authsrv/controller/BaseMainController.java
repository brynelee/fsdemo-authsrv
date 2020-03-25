package com.xdorg1.authsrv.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BaseMainController {

    //@Autowired
    //private BootSecurityProperties properties;

    @GetMapping("/auth/login")
    public String loginPage(Model model){
        //model.addAttribute("loginProcessUrl",properties.getLoginProcessUrl());
        model.addAttribute("loginProcessUrl", "/auth/authorize");
        return "base-login";
    }

}
