package com.swang.smartart.core.persistence.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Domain class for role.
 * <p>
 * 100: Admin
 * 200: Merchant Admin
 * 201: Merchant Operator
 * <p>
 * Created by swang on 2/17/2015.
 */
@Entity
@Table(name = "ROLES")
public class Role implements Serializable {

    static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ROLE_ID")
    private Long id;

    @Column(name = "ROLE_NAME", length = 64, nullable = false)
    private String name;

    @Column(name = "ROLE_DESCRIPTION", length = 255)
    private String description;

    @Column(name = "ROLE_ACTIVE", nullable = false)
    private Boolean active;

    @Column(name = "ROLE_CODE", nullable = false)
    private String code;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "ROLE_PERMISSION_MAPPINGS",
            joinColumns = {@JoinColumn(name = "RPMP_ROLE_ID", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "RPMP_PRMS_ID", nullable = false,
                    updatable = false)}
    )
    private Set<Permission> permissions;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles")
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

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
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
}
