package com.swang.smartart.manage.config;

import com.swang.smartart.core.persistence.entity.Permission;
import com.swang.smartart.manage.web.controller.UserResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by swang on 5/19/2015.
 * Since this bean is initiated during spring config phase, we cannot have resource or autowired
 * annotation here, we need to add the needed call to static controller UserResource and reference
 * UserResource from here.
 */
@Component("customPermissionEvaluator")
public class CustomPermissionEvaluatorImpl implements CustomPermissionEvaluator {

    private static final Logger logger =
            LoggerFactory.getLogger(CustomPermissionEvaluatorImpl.class);

    //    @Resource
//    private CustomUserDetailsService customUserDetailsService;

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Object target, Object permission) {
        logger.warn("evaluate permission: " + permission);
        SecurityUser user = getLogin(authentication);
        Permission permissionObject = getPermission(permission);
        if (user == null || permissionObject == null) {
            return false;
        }
        return hasPermission(user, permissionObject);
    }

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Serializable targetId, String targetType, Object permission) {
        return false;
    }

    private Boolean hasPermission(SecurityUser user, Permission permission) {

        if (user.getPermissions().contains(permission)) {
            return true;
        }
        return false;
    }

    private SecurityUser getLogin(Authentication authentication) {
        return UserResource.getAuthenticatedUser(authentication);
    }

    private Permission getPermission(Object permission) {
        return UserResource.getPermission((String) permission);
    }
}
