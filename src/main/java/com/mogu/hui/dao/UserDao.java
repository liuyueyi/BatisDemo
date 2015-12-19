package com.mogu.hui.dao;

import com.mogu.hui.domain.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yihui on 15/12/18.
 */
@Repository(value = "userDao")
public interface UserDao {

    public User getUserById(int id);


    public List<User> getUser();

}
