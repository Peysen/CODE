package com.pey.junit.service;

import com.pey.junit.dao.impl.UserDao;
import com.pey.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Peysen on 2017/7/31.
 */
@Service("usersService")
//@Transactional(value="masterTransactionManager")
public class UserServceImpl{
    @Autowired
    private UserDao usersDao;

    public int findUserAgeById(String uname){
        User users = usersDao.findByStatus(uname);
        return users.getUid();
    }

    public User saveUser(User user){
        User user1 = usersDao.save(user);
        return user1;
    }
}
