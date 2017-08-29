package com.asiainfo.chap1.model;

/**
 * Created by prayyic on 2017/3/29.
 */
public class SynchronizedObject {
    private String userName="a";
    private String password="aa";

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setValue(String userName,String password){
        this.userName=userName;
        if(Thread.currentThread().getName().equals("a")){
            System.out.println("暂停a线程");
            Thread.currentThread().suspend();
        }
        this.password=password;
    }

    public void printUsername(){
        System.out.println("userName="+userName+",password="+password);
    }

    synchronized  public void printString(String userName,String password){
        try {
            this.userName=userName;
            Thread.sleep(10000);
            this.password=password;
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    synchronized  public void OutString(){
        System.out.println("begin ");
        if(Thread.currentThread().getName().equals("a")){
            System.out.println("a线程将此方式suspend（）了，将导致其他线程无法访问");
            Thread.currentThread().suspend();
        }
    }
}
