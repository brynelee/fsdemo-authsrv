package com.xdorg1.authsrv.service.impl;

import com.xdorg1.authsrv.exception.UserFriendlyException;
import com.xdorg1.authsrv.model.UserEntity;
import com.xdorg1.authsrv.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${fsdemo.usercenter.userinfourl}")
    private String userInfoURL;

    /*
    @Autowired
    private UserRepository userRepository;

     */

    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * 根据用户名查询单个用户信息
     *
     * @param userName
     * @return
     */
    @Override
    public UserEntity findByUsername(String userName) {

        logger.info("UserServiceImpl findByUsername was called with userName as: " + userName);

        //using restful api to get userentity information /usercenter/user?username={username}
        //input: String username
        //output: username, password, mobile, userid, status, email
        Map<String, String> urlVariables = new HashMap<String, String>();
        urlVariables.put("username", userName);
        String name = urlVariables.get("username");

        logger.info("UserServiceImpl findByUsername URL parameter username as: " + name);

        RestTemplate restTemplate = new RestTemplate();

        UserEntity user = restTemplate.getForObject(userInfoURL, UserEntity.class, urlVariables);

        logger.info("UserServiceImpl findByUsername query to usercenter with {} and got UserEntity {}", userName, user);

        //UserEntity user = userRepository.findByUsername(userName);

        //临时测试使用
/*        UserEntity user = new UserEntity();

        user.setUsername(userName);
        user.setPassword(passwordEncoder.encode("666666"));
        user.setMobile("13088888888");
        user.setUserId(2);
        user.setStatus(1);
        user.setEmail("dahai666@sina.com");*/

        //temporarily not encoding password in usercenter database
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user == null) {
            throw new UserFriendlyException("用户不存在!");
        }
        return user;
    }

}
