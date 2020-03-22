package com.xdorg1.authsrv.service.impl;

import com.xdorg1.authsrv.model.UserEntity;
import com.xdorg1.authsrv.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.List;

public class UserDetailsServiceImpl implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String userName) {

        logger.info("UserDetailsServiceImpl loadUserByUsername was called with userName as: " + userName);

        UserEntity userEntity = userService.findByUsername(userName);
        if (null == userEntity) {
            throw new UsernameNotFoundException(userName);
        }

        //todo: based on the user inforamtion and his/her role get the authorities, currently it's a demo implementation

        // List<SimpleGrantedAuthority> authorities = new ArrayList<>();
//        for (SysRole role : sysUser.getRoleList()) {
//            for (SysPermission permission : role.getPermissionList()) {
//                authorities.add(new SimpleGrantedAuthority(permission.getCode()));
//            }
//        }
        return new User(userEntity.getUsername(), userEntity.getPassword(), getAuthority());
    }

    private List getAuthority() {
        return Arrays.asList(
                new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("ROLE_USER"),
                new SimpleGrantedAuthority("ROLE_MANAGER"));
    }

}
