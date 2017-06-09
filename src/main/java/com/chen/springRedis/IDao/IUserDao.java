package com.chen.springRedis.IDao;

import com.chen.springRedis.Entity.User;

/**
 * Created by Chen on 2017/6/8.
 */
public interface IUserDao {
    void saveUser(final User user);
    User getUser(final long id);
}
