package com.mogu.hui;

import com.mogu.hui.dao.UserDao;
import com.mogu.hui.domain.User;
import com.mogu.hui.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Created by yihui on 15/12/18.
 */
public class DaoTest {
    private static ApplicationContext ctx;
    private UserDao userDao;
    private UserService userService;


    @Before
    public void init() throws IOException {
        ctx = new ClassPathXmlApplicationContext("classpath:spring.xml");
        userDao = ctx.getBean("userDao", UserDao.class);

        userService = ctx.getBean("userService", UserService.class);
    }

    @Test
    public void testDao() {
        try {
            User user =  userDao.getUserById(1);
            System.out.println(user.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
        }
    }

    @Test
    public void testGetNamte() {
        int id = 1;
        String name = userService.getUserName(id);
        System.out.println(name);
    }
}
