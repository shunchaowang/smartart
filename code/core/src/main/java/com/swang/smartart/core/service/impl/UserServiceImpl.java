package com.swang.smartart.core.service.impl;

import com.swang.smartart.core.exception.MissingRequiredFieldException;
import com.swang.smartart.core.exception.NoSuchEntityException;
import com.swang.smartart.core.exception.NotUniqueException;
import com.swang.smartart.core.persistence.dao.UserDao;
import com.swang.smartart.core.persistence.dao.UserStatusDao;
import com.swang.smartart.core.persistence.entity.Role;
import com.swang.smartart.core.persistence.entity.User;
import com.swang.smartart.core.persistence.entity.UserStatus;
import com.swang.smartart.core.service.UserService;
import com.swang.smartart.core.util.ResourceProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by swang on 3/12/2015.
 * Modified by Linly on 3/15/2015.
 */
@Service("userService")
public class UserServiceImpl extends GenericQueryServiceImpl<User, Long> implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserDao userDao;
    @Resource
    private UserStatusDao userStatusDao;

    /**
     * Find user by the unique username of email format.
     * Username is stored at merchantIdentity/username, on manage it's username without merchant.
     *
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {

        if (StringUtils.isBlank(username)) {
            logger.debug("Email is blank.");
            return null;
        }
        CriteriaBuilder builder = userDao.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root);

        Path<String> path = root.get("username");
        Predicate predicate = builder.equal(path, username);
        query.where(predicate);
        return userDao.findSingle(query);
    }

    @Override
    public User findByEmail(String email) {
        if (StringUtils.isBlank(email)) {
            logger.debug("Email is blank.");
            return null;
        }
        CriteriaBuilder builder = userDao.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root);

        Path<String> path = root.get("email");
        Predicate predicate = builder.equal(path, email);
        query.where(predicate);
        return userDao.findSingle(query);
    }

    @Transactional
    @Override
    public User create(User user) throws MissingRequiredFieldException, NotUniqueException {
        if (user == null) {
            throw new MissingRequiredFieldException("User is null.");
        }
        if (StringUtils.isBlank(user.getUsername())) {
            throw new MissingRequiredFieldException("User username is blank.");
        }
        if (StringUtils.isBlank(user.getPassword())) {
            throw new MissingRequiredFieldException("User password is blank.");
        }
        if (StringUtils.isBlank(user.getEmail())) {
            throw new MissingRequiredFieldException("User email is blank.");
        }
        if (StringUtils.isBlank(user.getFirstName())) {
            throw new MissingRequiredFieldException("User first name is blank.");
        }
        if (StringUtils.isBlank(user.getLastName())) {
            throw new MissingRequiredFieldException("User last name is blank.");
        }
        if (user.getUserStatus() == null) {
            throw new MissingRequiredFieldException("User status is null.");
        }
        user.setActive(true);
        user.setCreatedTime(Calendar.getInstance().getTime());
        return userDao.create(user);
    }

    @Override
    public User get(Long id) throws NoSuchEntityException {
        if (id == null) {
            throw new NoSuchEntityException("Id is null.");
        }
        if (userDao.get(id) == null) {
            throw new NoSuchEntityException("User with id " + id + " does not exist.");
        }
        return userDao.get(id);
    }

    /**
     * Update a user.
     * Username is not allowed to change.
     *
     * @param user
     * @return
     * @throws MissingRequiredFieldException
     * @throws NotUniqueException
     */
    @Transactional
    @Override
    public User update(User user) throws MissingRequiredFieldException, NotUniqueException {
        if (user == null) {
            throw new MissingRequiredFieldException("User is null.");
        }
        if (user.getId() == null) {
            throw new MissingRequiredFieldException("User id is null.");
        }
        if (StringUtils.isBlank(user.getUsername())) {
            throw new MissingRequiredFieldException("User username is blank.");
        }
        if (StringUtils.isBlank(user.getPassword())) {
            throw new MissingRequiredFieldException("User password is blank.");
        }
        if (StringUtils.isBlank(user.getEmail())) {
            throw new MissingRequiredFieldException("User email is blank.");
        }
        if (StringUtils.isBlank(user.getFirstName())) {
            throw new MissingRequiredFieldException("User first name is blank.");
        }
        if (StringUtils.isBlank(user.getLastName())) {
            throw new MissingRequiredFieldException("User last name is blank.");
        }
        if (user.getUserStatus() == null) {
            throw new MissingRequiredFieldException("User status is null.");
        }

        // set updated time if not set
        user.setUpdatedTime(Calendar.getInstance().getTime());
        return userDao.update(user);
    }

    @Transactional
    @Override
    public User delete(Long id) throws NoSuchEntityException {
        if (id == null) {
            throw new NoSuchEntityException("Id is null.");
        }
        User user = userDao.get(id);
        if (user == null) {
            throw new NoSuchEntityException("User with id " + id + " does not exist.");
        }
        userDao.delete(id);
        return user;
    }

    @Override
    public List<User> getAll() {
        return userDao.getAll();
    }

    @Override
    public Long countAll() {
        return userDao.countAll();
    }

    /**
     * Archive a user.
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public User archiveUser(Long id) throws NoSuchEntityException {
        if (id == null) {
            throw new NoSuchEntityException("Id is null.");
        }
        User user = userDao.get(id);
        if (user == null) {
            throw new NoSuchEntityException("User with id " + id + " does not exist.");
        }
        user.setActive(false);
        return userDao.update(user);
    }

    /**
     * Restore a user.
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public User restoreUser(Long id) throws NoSuchEntityException {
        if (id == null) {
            throw new NoSuchEntityException("Id is null.");
        }
        User user = userDao.get(id);
        if (user == null) {
            throw new NoSuchEntityException("User with id " + id + " does not exist.");
        }
        user.setActive(true);
        return userDao.update(user);
    }

    /**
     * Freeze a user.
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public User freezeUser(Long id) throws NoSuchEntityException {
        if (id == null) {
            throw new NoSuchEntityException("Id is null.");
        }
        User user = userDao.get(id);
        if (user == null) {
            throw new NoSuchEntityException("User with id " + id +
                    " does not exist.");
        }
        UserStatus userStatus = userStatusDao.findByCode(ResourceProperties
                .USER_STATUS_FROZEN_CODE);
        user.setUserStatus(userStatus);
        user = userDao.update(user);
        return user;
    }

    /**
     * Unfreeze a user.
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public User unfreezeUser(Long id) throws NoSuchEntityException {
        if (id == null) {
            throw new NoSuchEntityException("Id is null.");
        }
        User user = userDao.get(id);
        if (user == null) {
            throw new NoSuchEntityException("User with id " + id +
                    " does not exist.");
        }
        UserStatus userStatus = userStatusDao.findByCode(ResourceProperties
                .USER_STATUS_NORMAL_CODE);
        user.setUserStatus(userStatus);
        user = userDao.update(user);
        return user;
    }

    /**
     * Find all T from criteria with offset and max records.
     *
     * @param criteriaQuery the criteria contains all query
     * @param start         the offset of the result
     * @param length        the max records of the result
     * @return list of T
     */
    @Override
    public List<User> find(CriteriaQuery<User> criteriaQuery, Integer start, Integer length) {
        return userDao.find(criteriaQuery, start, length);
    }

    /**
     * Call generic dao to retrieve CriteriaBuilder from EntityManage, which will be
     * used to formulate CriteriaQuery and Criteria including where, orderBy, etc...
     *
     * @return CriteriaBuilder to be used by the controller for domain T
     */
    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return userDao.getCriteriaBuilder();
    }

    /**
     * Count by jpa criteria query.
     *
     * @param criteriaQuery the criteria contains all query
     * @return the number of the query
     */
    @Override
    public Long count(CriteriaQuery<Long> criteriaQuery) {
        return userDao.count(criteriaQuery);
    }

    /**
     * Count by jqa query language.
     *
     * @param jpql query string, like SELECT u.username FROM User AS u.
     * @return number of result
     */
    @Override
    public Long count(String jpql) {
        return userDao.count(jpql);
    }

    /**
     * Count by jqa query language.
     *
     * @param jpql query string, like SELECT u.username FROM User AS u.
     * @return list of result
     */
    @Override
    public List<User> find(String jpql) {
        return userDao.find(jpql);
    }
}
