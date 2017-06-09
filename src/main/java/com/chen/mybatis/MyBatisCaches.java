package com.chen.mybatis;

import com.chen.mybatis.userInfo;
import com.chen.redis.RedisClient;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chen on 2017/6/4.
 */
public class MyBatisCaches {

    public static void main(String[] args) throws Exception {
        String config = "mybatis-config.xml";
        InputStream is = Resources.getResourceAsStream(config);
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(is);
        SqlSession session = factory.openSession();

        userInfo u = session.selectOne("com.chen.mybatis.IUserMapper.selectUser", 1);
        // 同一个session的相同sql查询,将会使用一级缓存
        session.selectOne("com.chen.mybatis.IUserMapper.selectUser", 1);
        // 参数改变,需要重新查询
        session.selectOne("com.chen.mybatis.IUserMapper.selectUser", 2);
        // 清空缓存后需要重新查询
        session.clearCache();
        session.selectOne("com.chen.mybatis.IUserMapper.selectUser", 1);
        // session close以后,仍然使用同一个db connection
        session.close();
        session = factory.openSession();
        session.selectOne("com.chen.mybatis.IUserMapper.selectUser", 1);
    }

    @Test
    public void testCache2() throws IOException {
        String statement = "com.chen.mybatis.IUserMapper.selectUser";

        String config = "mybatis-config.xml";
        InputStream is = Resources.getResourceAsStream(config);
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(is);
        SqlSession session1 = factory.openSession();
        SqlSession session2 = factory.openSession();

        userInfo u = session1.selectOne(statement ,1);
        session1.commit();
        System.out.println("user="+u);

        //由于使用的是两个不同的SqlSession对象，所以即使查询条件相同，一级缓存也不会开启使用
        u = session2.selectOne(statement, 1);
        //session2.commit();
        System.out.println("user2="+u);
    }

    private SqlSessionFactory factory = null;
    @Before
    public  void  getFactory() throws IOException {
        String config = "mybatis-config.xml";
        InputStream is = Resources.getResourceAsStream(config);
        factory = new SqlSessionFactoryBuilder().build(is);
    }

    @After
    public void release(){
        System.out.println("end and clear the redis cache");
        Jedis jedis = new RedisClient().getJedis();
        jedis.flushDB();
    }

    @Test
    public void getOneUser(){
        if(factory!=null){
            SqlSession sqlSession = factory.openSession();
            String statement = "com.chen.mybatis.IUserMapper.selectUser";
            IUserMapper iUserMapper = sqlSession.getMapper(IUserMapper.class);
            userInfo u = iUserMapper.selectUser("1");
            sqlSession.commit();
            u = iUserMapper.selectUser("1");
            System.out.println(u.getId());
            sqlSession.close();
        }
    }

    @Test
    public void getAllUserInfo(){
        if(factory!=null){
            SqlSession sqlSession = factory.openSession();
            List<userInfo> lst_u = null;
            String statement = "com.chen.mybatis.IUserMapper.selectAll";
            lst_u = sqlSession.selectList(statement);
            for(userInfo u : lst_u){
                System.out.println(u.getId());
            }
            sqlSession.close();
            sqlSession = factory.openSession();
            IUserMapper iUserMapper = sqlSession.getMapper(IUserMapper.class);
            userInfo u = iUserMapper.selectUser("1");
            System.out.println(u.getId());
            lst_u = sqlSession.selectList(statement);
            for(userInfo u1 : lst_u){
                System.out.println(u1.getId());
            }
        }
    }

    @Test
    public void testCall(){
        if(factory!=null){
            SqlSession sqlSession = factory.openSession();
            String statement = "com.chen.mybatis.IUserMapper.getUserCount";
            Map<String, Integer> parameterMap = new HashMap<String, Integer>();
            parameterMap.put("id",2);
            parameterMap.put("user_count" , -1);
            sqlSession.selectOne(statement , parameterMap);
            Integer result = parameterMap.get("user_count");
            System.out.println(result);
            sqlSession.close();
        }
    }
}
