package com.swang.smartart.manage.web.vo;

import com.swang.smartart.core.persistence.entity.User;
import org.springframework.context.i18n.LocaleContextHolder;

import java.text.DateFormat;
import java.util.Locale;

/**
 * Created by swang on 3/16/2015.
 */
public class UserCommand {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String remark;
    private Boolean active;
    private String createdTime;
    private String updatedTime;
    // relationships
    private Long userStatus;
    private String userStatusName;

    public UserCommand() {
    }

    public UserCommand(User user) {
        id = user.getId();
        username = user.getUsername();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getEmail();
        active = user.getActive();
        remark = user.getRemark();

        if (user.getUserStatus() != null) {
            userStatus = user.getUserStatus().getId();
            userStatusName = user.getUserStatus().getName();
        }
        Locale locale = LocaleContextHolder.getLocale();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
        createdTime = dateFormat.format(user.getCreatedTime());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Long getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Long userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserStatusName() {
        return userStatusName;
    }

    public void setUserStatusName(String userStatusName) {
        this.userStatusName = userStatusName;
    }
}
