package com.mogu.hui.dao;

import com.mogu.hui.domain.User;

import java.util.List;

/**
 * Created by yihui on 15/12/18.
 */
public interface UserDao {

    public User getUserById(int id);


    public List<User> getUser();

}
