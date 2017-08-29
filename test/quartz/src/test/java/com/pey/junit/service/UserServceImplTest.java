package com.pey.junit.service;

import com.pey.junit.dao.impl.UserDao;
import com.pey.model.User;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by Peysen on 2017/7/31.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-context.xml"
})
public class UserServceImplTest extends TestCase {
    //@Resource
    private UserDao userDao;

    @Resource
    public void setUserDao(UserDao ud){
        System.out.println("resource....");
        userDao=ud;
    }

    @Test
    public void saveUser() throws Exception {
        User user=new User();
        user.setPhone_num("158");
        user.setUname("PEIMM");
        System.out.println("userdAO"+userDao.toString());
        userDao.save(user);
    }

    @Test
    public void findUserByName(){
        User peimm = userDao.findByStatus("PEIMM");
        System.out.println("user="+peimm.getUid());
    }
}