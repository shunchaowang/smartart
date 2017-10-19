package com.swang.smartart.core.persistence.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by swang on 2/17/2015.
 */
@Entity
@Table(name = "PERMISSIONS")
public class Permission implements Serializable {

    static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PRMS_ID")
    private Long id;

    @Column(name = "PRMS_NAME", length = 64, nullable = false)
    private String name;

    @Column(name = "PRMS_DESCRIPTION", length = 255)
    private String description;

    @Column(name = "PRMS_ACTIVE", nullable = false)
    private Boolean active;

    @Column(name = "PRMS_CODE", nullable = false)
    private String code;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "ROLE_PERMISSION_MAPPINGS",
            joinColumns = {@JoinColumn(name = "RPMP_PRMS_ID", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "RPMP_ROLE_ID", nullable = false, updatable
                    = false)}
    )
    private Set<Role> roles;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions")
    private Set<User> users;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, optional = true)
    @JoinColumn(name = "PRMS_MENU_ID", nullable = true)
    private MenuItem menuItem;

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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
