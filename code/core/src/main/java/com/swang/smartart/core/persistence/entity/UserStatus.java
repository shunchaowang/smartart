package com.swang.smartart.core.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Set;

/**
 * Domain class for the status of the user.
 * <p/>
 * 100: Normal
 * 400: Deactivated
 * 501: Frozen
 * <p/>
 * Created by swang on 2/17/2015.
 */
@Entity
@Table(name = "USER_STATUSES")
public class UserStatus implements Serializable {

    static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "USST_ID")
    private Long id;

    @Column(name = "USST_NAME", length = 32, nullable = false)
    private String name;

    @Column(name = "USST_DESCRIPTION", length = 255)
    private String description;

    @Column(name = "USST_ACTIVE", nullable = false)
    private Boolean active;

    @Column(name = "USST_CODE", nullable = false)
    private String code;

    @OneToMany(mappedBy = "userStatus")
    private Set<User> users;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
