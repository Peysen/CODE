package com.pey.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Peysen on 2017/7/31.
 */
@Entity
@Table(name="user")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer uid;

    @Column(name="uname")
    private String uname;

    @Column(name="sid")
    private Integer sid;

    @Column(name="sname")
    private String sname;

    @Column(name="pid")
    private Integer pid;

    @Column(name="pname")
    private String pname;

    @Column(name="utype")
    private Integer type;

    @Column(name="phone_num")
    private String phone_num;

    @Column(name="pw")
    private String password;

    public User() {

    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", uname='" + uname + '\'' +
                ", sid=" + sid +
                ", sname='" + sname + '\'' +
                ", pid=" + pid +
                ", pname='" + pname + '\'' +
                ", type=" + type +
                ", phone_num='" + phone_num + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
