package com.chen.springRedis;

import com.chen.springRedis.Entity.User;
import com.chen.springRedis.IDao.DaoImpl.UserDaoImpl;
import com.chen.springRedis.IDao.IUserDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Created by Chen on 2017/6/8.
 */
public class SpringRedis {
    private ApplicationContext applicationContext = null;
    @Before
    public void createApplicationContext(){
        try {
            applicationContext = new ClassPathXmlApplicationContext("spring-config.xml");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @After
    public void destoryApplicationContext(){
        System.out.println("end the process");
    }

    @Test
    public void getTemplate(){
        if(applicationContext != null){
            RedisTemplate redisTemplate = (RedisTemplate) applicationContext.getBean("redisTemplate");
        }
    }
    @Test
    public void saveEntity(){
        if(applicationContext!=null){
            User u = new User();
            u.setId(1);
            u.setName("chen");
            User u1 = new User();
            u1.setId(2);
            u1.setName("root");

            IUserDao iUserDao = (IUserDao) applicationContext.getBean(UserDaoImpl.class);
            iUserDao.saveUser(u);
            iUserDao.saveUser(u1);
        }
    }

    @Test
    public void getEntity(){
        if(applicationContext != null){
            IUserDao iUserDao = (IUserDao) applicationContext.getBean(UserDaoImpl.class);
            User u = iUserDao.getUser(1);
            System.out.println(u.getId() + ":" + u.getName());
        }
    }
}
