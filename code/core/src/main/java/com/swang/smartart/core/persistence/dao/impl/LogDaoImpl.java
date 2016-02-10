package com.swang.smartart.core.persistence.dao.impl;

import com.swang.smartart.core.persistence.dao.LogDao;
import com.swang.smartart.core.persistence.entity.Log;
import org.springframework.stereotype.Repository;

/**
 * Created by swang on 4/30/2015.
 */
@Repository("logDao")
public class LogDaoImpl extends GenericDaoImpl<Log, Long> implements LogDao {

}
