package com.mogu.hui.service;

import com.mogu.hui.dao.UserDao;
import com.mogu.hui.domain.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by yihui on 15/12/19.
 */
@Service("userService")
public class UserService {
    @Resource
    private UserDao userDao;

    public String getUserName(int id) {
        User user = userDao.getUserById(id);
        return user.getUsername();
    }
}
