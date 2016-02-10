package com.swang.smartart.core.service.impl;

import com.swang.smartart.core.exception.MissingRequiredFieldException;
import com.swang.smartart.core.exception.NoSuchEntityException;
import com.swang.smartart.core.exception.NotUniqueException;
import com.swang.smartart.core.persistence.dao.UserStatusDao;
import com.swang.smartart.core.persistence.entity.UserStatus;
import com.swang.smartart.core.service.UserStatusService;

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
@Service("userStatusService")
public class UserStatusServiceImpl implements UserStatusService {

    private static final Logger logger = LoggerFactory.getLogger(UserStatusServiceImpl.class);
    @Autowired
    private UserStatusDao userStatusDao;

    /**
     * Find UserStatus by name.
     *
     * @param name
     * @return
     * @throws NoSuchEntityException
     */
    @Override
    public UserStatus findByName(String name) throws NoSuchEntityException {
        // check parameters
        if (StringUtils.isBlank(name)) {
            logger.info("Name is blank.");
            return null;
        }
        // call dao
        return userStatusDao.findByName(name);
    }

    /**
     * Find UserStatus by code.
     *
     * @param code
     * @return
     * @throws NoSuchEntityException
     */
    @Override
    public UserStatus findByCode(String code) throws NoSuchEntityException {
        if (StringUtils.isBlank(code)) {
            logger.info("Code is blank.");
            return null;
        }
        return userStatusDao.findByCode(code);
    }

    /**
     * Create a new UserStatus.
     * Name and Code must not be null and must be unique.
     * The newly created object will be active by default.
     *
     * @param userStatus
     * @return
     */
    @Transactional
    @Override
    public UserStatus create(UserStatus userStatus) throws MissingRequiredFieldException,
            NotUniqueException {
        if (userStatus == null) {
            throw new MissingRequiredFieldException("UserStatus is null.");
        }
        if (StringUtils.isBlank(userStatus.getName()) ||
                StringUtils.isBlank(userStatus.getCode()))
            throw new MissingRequiredFieldException("Name or code is blank.");

        // check uniqueness
        if (userStatusDao.findByName(userStatus.getName()) != null) {
            throw new NotUniqueException("UserStatus with name " + userStatus.getName() +
                    " already exists.");
        }
        if (userStatusDao.findByCode(userStatus.getCode()) != null) {
            throw new NotUniqueException("UserStatus with code " + userStatus.getName() +
                    " already exists.");
        }
        userStatus.setActive(true);
        // pass all checks, create the object
        return userStatusDao.create(userStatus);
    }

    /**
     * Get UserStatus by id.
     *
     * @param id
     * @return
     * @throws NoSuchEntityException
     */
    @Override
    public UserStatus get(Long id) throws NoSuchEntityException {
        if (id == null) {
            throw new NoSuchEntityException("Id is null.");
        }
        if (userStatusDao.get(id) == null) {
            throw new NoSuchEntityException("UserStatus with id " + id +
                    " does not exist.");
        }
        return userStatusDao.get(id);
    }

    /**
     * Update an existing UserStatus.
     * Must check unique fields if are unique.
     *
     * @param userStatus
     * @return
     * @throws MissingRequiredFieldException
     * @throws NotUniqueException
     */
    @Transactional
    @Override
    public UserStatus update(UserStatus userStatus) throws MissingRequiredFieldException,
            NotUniqueException {
        // checking missing fields
        if (userStatus == null) {
            throw new MissingRequiredFieldException("UserStatus is null.");
        }
        if (userStatus.getId() == null) {
            throw new MissingRequiredFieldException("UserStatus id is null.");
        }
        if (StringUtils.isBlank(userStatus.getName()) ||
                StringUtils.isBlank(userStatus.getCode())) {
            throw new MissingRequiredFieldException("UserStatus name or code is blank.");
        }
        // checking uniqueness
        UserStatus namedUserStatus = userStatusDao.findByName(userStatus.getName());
        if (namedUserStatus != null &&
                !namedUserStatus.getId().equals(userStatus.getId())) {
            throw new NotUniqueException("UserStatus with name " + userStatus.getName() +
                    " already exists.");
        }
        UserStatus codedUserStatus = userStatusDao.findByCode(userStatus.getCode());
        if (codedUserStatus != null &&
                !codedUserStatus.getId().equals(userStatus.getId())) {
            throw new NotUniqueException("UserStatus with code " + userStatus.getCode() +
                    " already exists.");
        }
        return userStatusDao.update(userStatus);
    }

    /**
     * Delete an existing UserStatus.
     *
     * @param id
     * @return
     * @throws NoSuchEntityException
     */
    @Transactional
    @Override
    public UserStatus delete(Long id) throws NoSuchEntityException {
        if (id == null) {
            throw new NoSuchEntityException("Id is null.");
        }
        UserStatus userStatus = userStatusDao.get(id);
        if (userStatus == null) {
            throw new NoSuchEntityException("UserStatus with id " + id +
                    " does not exist.");
        }
        userStatusDao.delete(id);
        return userStatus;
    }

    @Override
    public List<UserStatus> getAll() {
        return userStatusDao.getAll();
    }

    @Override
    public Long countAll() {
        return userStatusDao.countAll();
    }
}
