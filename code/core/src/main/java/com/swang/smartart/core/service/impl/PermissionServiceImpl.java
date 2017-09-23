package com.swang.smartart.core.service.impl;

import com.swang.smartart.core.exception.MissingRequiredFieldException;
import com.swang.smartart.core.exception.NoSuchEntityException;
import com.swang.smartart.core.exception.NotUniqueException;
import com.swang.smartart.core.persistence.dao.PermissionDao;
import com.swang.smartart.core.persistence.entity.Permission;
import com.swang.smartart.core.service.PermissionService;
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
@Service("permissionService")
public class PermissionServiceImpl implements PermissionService {

    private static final Logger logger = LoggerFactory.getLogger(PermissionServiceImpl.class);
    @Autowired
    private PermissionDao permissionDao;

    /**
     * Find Permission by name.
     *
     * @param name
     * @return
     * @throws com.swang.smartart.core.exception.NoSuchEntityException
     */
    @Override
    public Permission findByName(String name) throws NoSuchEntityException {
        // check parameters
        if (StringUtils.isBlank(name)) {
            logger.info("Name is blank.");
            return null;
        }
        // call dao
        return permissionDao.findByName(name);
    }

    /**
     * Find Permission by code.
     *
     * @param code
     * @return
     * @throws com.swang.smartart.core.exception.NoSuchEntityException
     */
    @Override
    public Permission findByCode(String code) throws NoSuchEntityException {
        if (StringUtils.isBlank(code)) {
            logger.info("Code is blank.");
            return null;
        }
        return permissionDao.findByCode(code);
    }

    /**
     * Create a new Permission.
     * Name and Code must not be null and must be unique.
     * The newly created object will be active by default.
     *
     * @param permission
     * @return
     */
    @Transactional
    @Override
    public Permission create(Permission permission) throws MissingRequiredFieldException,
            NotUniqueException {
        if (permission == null) {
            throw new MissingRequiredFieldException("Permission is null.");
        }
        if (StringUtils.isBlank(permission.getName()) ||
                StringUtils.isBlank(permission.getCode()))
            throw new MissingRequiredFieldException("Name or code is blank.");

        // check uniqueness
        if (permissionDao.findByName(permission.getName()) != null) {
            throw new NotUniqueException("Permission with name " + permission.getName() +
                    " already exists.");
        }
        if (permissionDao.findByCode(permission.getCode()) != null) {
            throw new NotUniqueException("Permission with code " + permission.getName() +
                    " already exists.");
        }
        permission.setActive(true);
        // pass all checks, create the object
        return permissionDao.create(permission);
    }

    /**
     * Get Permission by id.
     *
     * @param id
     * @return
     * @throws com.swang.smartart.core.exception.NoSuchEntityException
     */
    @Override
    public Permission get(Long id) throws NoSuchEntityException {
        if (id == null) {
            throw new NoSuchEntityException("Id is null.");
        }
        if (permissionDao.get(id) == null) {
            throw new NoSuchEntityException("Permission with id " + id +
                    " does not exist.");
        }
        return permissionDao.get(id);
    }

    /**
     * Update an existing Permission.
     * Must check unique fields if are unique.
     *
     * @param permission
     * @return
     * @throws com.swang.smartart.core.exception.MissingRequiredFieldException
     * @throws com.swang.smartart.core.exception.NotUniqueException
     */
    @Transactional
    @Override
    public Permission update(Permission permission) throws MissingRequiredFieldException,
            NotUniqueException {
        // checking missing fields
        if (permission == null) {
            throw new MissingRequiredFieldException("Permission is null.");
        }
        if (permission.getId() == null) {
            throw new MissingRequiredFieldException("Permission id is null.");
        }
        if (StringUtils.isBlank(permission.getName()) ||
                StringUtils.isBlank(permission.getCode())) {
            throw new MissingRequiredFieldException("Permission name or code is blank.");
        }
        // checking uniqueness
        Permission namedPermission = permissionDao.findByName(permission.getName());
        if (namedPermission != null &&
                !namedPermission.getId().equals(permission.getId())) {
            throw new NotUniqueException("Permission with name " + permission.getName() +
                    " already exists.");
        }
        Permission codedPermission = permissionDao.findByCode(permission.getCode());
        if (codedPermission != null &&
                !codedPermission.getId().equals(permission.getId())) {
            throw new NotUniqueException("Permission with code " + permission.getCode() +
                    " already exists.");
        }
        return permissionDao.update(permission);
    }

    /**
     * Delete an existing Permission.
     *
     * @param id
     * @return
     * @throws com.swang.smartart.core.exception.NoSuchEntityException
     */
    @Transactional
    @Override
    public Permission delete(Long id) throws NoSuchEntityException {
        if (id == null) {
            throw new NoSuchEntityException("Id is null.");
        }
        Permission permission = permissionDao.get(id);
        if (permission == null) {
            throw new NoSuchEntityException("Permission with id " + id +
                    " does not exist.");
        }
        permissionDao.delete(id);
        return permission;
    }

    @Override
    public List<Permission> getAll() {
        return permissionDao.getAll();
    }

    @Override
    public Long countAll() {
        return permissionDao.countAll();
    }
}
