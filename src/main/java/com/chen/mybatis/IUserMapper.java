package com.chen.mybatis;

import java.util.List;
import java.util.Map;

/**
 * Created by Chen on 2017/6/4.
 */
public interface IUserMapper {
    userInfo selectUser(String id);
    void getUserCount(Map getUserCountMap);
    List<userInfo> selectAll();
}
