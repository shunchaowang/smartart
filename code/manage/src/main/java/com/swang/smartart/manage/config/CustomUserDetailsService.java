package com.swang.smartart.manage.config;

import com.swang.smartart.core.persistence.entity.User;
import com.swang.smartart.core.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Create a customer UserDetailsService to inject to spring context.
 * Since our user role schema is not following spring security poc, we need to
 * create the customer service to do the authentication.
 * Created by swang on 3/12/2015.
 */
@Component
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        logger.debug("username: " + username);
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username " + username + " not found.");
        }
        return new SecurityUser(user);
    }
}


