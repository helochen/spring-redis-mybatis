package com.chen.springRedis.IDao.DaoImpl;

import com.chen.springRedis.Entity.User;
import com.chen.springRedis.IDao.IUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by Chen on 2017/6/8.
 */
@Component
public class UserDaoImpl implements IUserDao {
    @Autowired(required = true)
    private RedisTemplate<Serializable,Serializable> redisTemplate;

    @Override
    public void saveUser(final User user) {
        redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException{
                connection.set(redisTemplate.getStringSerializer().serialize("user.uid." + user.getId()),
                        redisTemplate.getStringSerializer().serialize(user.getName()));
                return null;
            }
        });
    }

    @Override
    public User getUser(final long id) {
        return redisTemplate.execute(new RedisCallback<User>() {
            @Override
            public User doInRedis(RedisConnection redisConnection) throws DataAccessException {
                byte[] key = redisTemplate.getStringSerializer().serialize("user.uid." + id);
                if (redisConnection.exists(key)) {
                    byte[] value = redisConnection.get(key);
                    String name = redisTemplate.getStringSerializer().deserialize(value);
                    User user = new User();
                    user.setName(name);
                    user.setId(id);
                    return user;
                }
                return null;
            }
        });
    }
}
