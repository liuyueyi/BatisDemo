package com.mogu.hui;

import com.mogu.hui.domain.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by yihui on 15/12/18.
 */
public class DaoTest {
    private static SqlSessionFactory sqlSessionFactory;
    private static Reader reader;

    @Before
    public void init() throws IOException {
        reader = Resources.getResourceAsReader("mybatis.xml");
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

    }

    @Test
    public void testDao() {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            User user = session.selectOne("com.mogu.hui.dao.UserDao.getUserById", 1);
            System.out.println(user.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            session.close();
        }
    }
}
