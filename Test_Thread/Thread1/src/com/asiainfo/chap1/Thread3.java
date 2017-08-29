package com.asiainfo.chap1;

import com.asiainfo.chap1.model.SynchronizedObject;

/**
 * Created by prayyic on 2017/3/29.
 * stop()
 * 测试强制关闭线程会导致数据不一致的问题！
 */

class Mythread3 extends Thread{
    private SynchronizedObject object;

    public Mythread3(SynchronizedObject object){
        this.object=object;
    }

    @Override
    public void run(){
        object.printString("b","bb");
    }
}

public class Thread3 {
    public static void main(String[] args) {
       try {
            SynchronizedObject object=new SynchronizedObject();
            Mythread3 thread=new Mythread3(object);
            thread.start();
            Thread.sleep(500);
            thread.stop();//会造成数据不一致
            System.out.println("object.name="+object.getUserName()+",object.password="+object.getPassword());
       }catch (InterruptedException e){
           e.printStackTrace();
       }
    }
}
