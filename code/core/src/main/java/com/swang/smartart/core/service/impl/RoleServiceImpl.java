package com.swang.smartart.core.service.impl;

import com.swang.smartart.core.exception.MissingRequiredFieldException;
import com.swang.smartart.core.exception.NoSuchEntityException;
import com.swang.smartart.core.exception.NotUniqueException;
import com.swang.smartart.core.persistence.dao.RoleDao;
import com.swang.smartart.core.persistence.entity.Role;
import com.swang.smartart.core.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service needs to check if the parameters passed in are null or empty.
 * Service also takes care of Transactional management.
 * Created by swang on 3/9/2015.
 */
@Service("roleService")
public class RoleServiceImpl implements RoleService {

    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
    @Autowired
    private RoleDao roleDao;

    /**
     * Find Role by name.
     *
     * @param name
     * @return
     * @throws NoSuchEntityException
     */
    @Override
    public Role findByName(String name) throws NoSuchEntityException {
        // check parameters
        if (StringUtils.isBlank(name)) {
            logger.info("Name is blank.");
            return null;
        }
        // call dao
        return roleDao.findByName(name);
    }

    /**
     * Find Role by code.
     *
     * @param code
     * @return
     * @throws NoSuchEntityException
     */
    @Override
    public Role findByCode(String code) throws NoSuchEntityException {
        if (StringUtils.isBlank(code)) {
            logger.info("Code is blank.");
            return null;
        }
        return roleDao.findByCode(code);
    }

    /**
     * Create a new Role.
     * Name and Code must not be null and must be unique.
     * The newly created object will be active by default.
     *
     * @param role
     * @return
     */
    @Transactional
    @Override
    public Role create(Role role) throws MissingRequiredFieldException,
            NotUniqueException {
        if (role == null) {
            throw new MissingRequiredFieldException("Role is null.");
        }
        if (StringUtils.isBlank(role.getName()) ||
                StringUtils.isBlank(role.getCode()))
            throw new MissingRequiredFieldException("Name or code is blank.");

        // check uniqueness
        if (roleDao.findByName(role.getName()) != null) {
            throw new NotUniqueException("Role with name " + role.getName() +
                    " already exists.");
        }
        if (roleDao.findByCode(role.getCode()) != null) {
            throw new NotUniqueException("Role with code " + role.getName() +
                    " already exists.");
        }
        role.setActive(true);
        // pass all checks, create the object
        return roleDao.create(role);
    }

    /**
     * Get Role by id.
     *
     * @param id
     * @return
     * @throws NoSuchEntityException
     */
    @Override
    public Role get(Long id) throws NoSuchEntityException {
        if (id == null) {
            throw new NoSuchEntityException("Id is null.");
        }
        if (roleDao.get(id) == null) {
            throw new NoSuchEntityException("Role with id " + id +
                    " does not exist.");
        }
        return roleDao.get(id);
    }

    /**
     * Update an existing Role.
     * Must check unique fields if are unique.
     *
     * @param role
     * @return
     * @throws MissingRequiredFieldException
     * @throws NotUniqueException
     */
    @Transactional
    @Override
    public Role update(Role role) throws MissingRequiredFieldException,
            NotUniqueException {
        // checking missing fields
        if (role == null) {
            throw new MissingRequiredFieldException("Role is null.");
        }
        if (role.getId() == null) {
            throw new MissingRequiredFieldException("Role id is null.");
        }
        if (StringUtils.isBlank(role.getName()) ||
                StringUtils.isBlank(role.getCode())) {
            throw new MissingRequiredFieldException("Role name or code is blank.");
        }
        // checking uniqueness
        Role namedRole = roleDao.findByName(role.getName());
        if (namedRole != null &&
                !namedRole.getId().equals(role.getId())) {
            throw new NotUniqueException("Role with name " + role.getName() +
                    " already exists.");
        }
        Role codedRole = roleDao.findByCode(role.getCode());
        if (codedRole != null &&
                !codedRole.getId().equals(role.getId())) {
            throw new NotUniqueException("Role with code " + role.getCode() +
                    " already exists.");
        }
        return roleDao.update(role);
    }

    /**
     * Delete an existing Role.
     *
     * @param id
     * @return
     * @throws NoSuchEntityException
     */
    @Transactional
    @Override
    public Role delete(Long id) throws NoSuchEntityException {
        if (id == null) {
            throw new NoSuchEntityException("Id is null.");
        }
        Role role = roleDao.get(id);
        if (role == null) {
            throw new NoSuchEntityException("Role with id " + id +
                    " does not exist.");
        }
        roleDao.delete(id);
        return role;
    }

    @Override
    public List<Role> getAll() {
        return roleDao.getAll();
    }

    @Override
    public Long countAll() {
        return roleDao.countAll();
    }

    /**
     * Test if T is blank for the query.
     *
     * @param role null return false, all required fields are null return false.
     * @return
     */
    @Override
    public Boolean isBlank(Role role) {
        return role == null && role.getActive() == null &&
                role.getId() == null && StringUtils.isBlank(role.getName()) &&
                StringUtils.isBlank(role.getCode());
    }


}
