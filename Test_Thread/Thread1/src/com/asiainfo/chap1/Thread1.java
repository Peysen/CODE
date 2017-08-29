package com.asiainfo.chap1;

/**
 * Created by prayyic on
 * 2017/3/29.
 * 测试Thread先sleep（）再interrupt()时抛出异常！
 */

class MyThread extends Thread{
    @Override
    public void run(){
        super.run();
        try {
            System.out.println("run begin");
            System.out.println("MyThread中当前线程："+Thread.currentThread().getName());
          Thread.currentThread().sleep(20000);
//            Thread.sleep(20);
            System.out.println("run end");
        }catch (InterruptedException e){
            System.out.println("在沉睡中被停止！进入 catch！"+this.isInterrupted());
        }
    }
}

public class Thread1 {
    public static void main(String[] args) {
        try {
            MyThread thread=new MyThread();
            thread.start();
            Thread.currentThread().sleep(200);//sleep（）方法是让当前正在运行的线程睡眠
//          Thread.sleep(20000);
            System.out.println("当前线程："+Thread.currentThread().getName());
            thread.interrupt();
        } catch (InterruptedException e) {
            System.out.println("main catch");
            e.printStackTrace();
        }

    }

}
