package com.swang.smartart.core.persistence.dao.impl;

import com.swang.smartart.core.persistence.dao.UserDao;
import com.swang.smartart.core.persistence.entity.User;
import org.springframework.stereotype.Repository;

/**
 * Created by swang on 3/9/2015.
 */
@Repository("userDao")
public class UserDaoImpl extends GenericDaoImpl<User, Long> implements UserDao {

}
