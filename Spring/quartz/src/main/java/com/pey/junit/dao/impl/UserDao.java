package com.pey.junit.dao.impl;

import com.pey.junit.dao.RepositorySupport;
import com.pey.model.User;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

/**
 * Created by Peysen on 2017/7/31.
 */
public class UserDao extends RepositorySupport<User> {

    public User saveQuotaId(User user){
        return  this.save(user);
    }

    public User findByStatus(String uname){
        DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
        criteria.add(Restrictions.eq("uname", uname));
        return  findOne(criteria);
    }
}