package com.swang.smartart.manage.web.controller;

import com.swang.smartart.core.exception.NoSuchEntityException;
import com.swang.smartart.core.persistence.entity.MenuCategory;
import com.swang.smartart.core.persistence.entity.MenuItem;
import com.swang.smartart.core.persistence.entity.Permission;
import com.swang.smartart.core.persistence.entity.User;
import com.swang.smartart.core.service.PermissionService;
import com.swang.smartart.core.service.UserService;
import com.swang.smartart.manage.config.SecurityUser;
import com.swang.smartart.manage.web.vo.navigation.Menu;
import com.swang.smartart.manage.web.vo.navigation.SubMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by swang on 3/11/2015.
 */
@Controller
public class UserResource {

    private static final Logger logger = LoggerFactory.getLogger(UserResource.class);

    private static UserService userService;
    private static PermissionService permissionService;

    public static SecurityUser getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            User loginUser = userService.findByUsername(username);
            return new SecurityUser(loginUser);
        }
        return null;
    }

    public static SecurityUser getAuthenticatedUser(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            User loginUser = userService.findByUsername(username);
            return new SecurityUser(loginUser);
        }
        return null;
    }

    public static List<Menu> getMenu() {
        SecurityUser securityUser = getCurrentUser();
        if (securityUser == null) {
            return null;
        }
        HashMap<String, Menu> menuMap = new HashMap<>();
        for (Permission permission : securityUser.getPermissions()) {
            MenuItem menuItem = permission.getMenuItem();
            if (menuItem == null) {
                continue;
            }
            MenuCategory menuCategory = menuItem.getMenuCategory();
            if (menuCategory == null) {
                continue;
            }
            if (!menuMap.containsKey(menuCategory.getName())) {
                menuMap.put(menuCategory.getName(), new Menu(menuCategory));
            }
            SubMenu subMenu = new SubMenu(menuItem);
            if (menuMap.get(menuCategory.getName()).getSubMenus().contains(subMenu)) {
                continue;
            }
            menuMap.get(menuCategory.getName()).getSubMenus().add(subMenu);
        }

        List<Menu> menus = new ArrayList<>(menuMap.values());
        Collections.sort(menus);
        for (Menu menu : menus) {
            Collections.sort(menu.getSubMenus());
        }
        return menus;
    }

    public static Permission getPermission(String permission) {
        if (permission == null || permission.equals("")) {
            return null;
        }
        Permission permissionObject = null;
        try {
            permissionObject = permissionService.findByName(permission);
        } catch (NoSuchEntityException e) {
            logger.debug(e.getMessage());
            return null;
        }
        return permissionObject;
    }

    @Autowired
    public void setUserService(UserService userService) {
        UserResource.userService = userService;
    }

    @Resource
    public void setPermissionService(PermissionService permissionService) {
        UserResource.permissionService = permissionService;
    }
}
