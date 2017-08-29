package com.asiainfo.chap1;

import com.asiainfo.chap1.model.SynchronizedObject;

/**
 * Created by prayyic on 2017/3/29.
 * suspend()    resume()
 * 测试两方法使用不当，造成公共的同步对象被独占，使得其他线程无法访问公共同步对象
 */
class MyThread4 extends  Thread{
    private SynchronizedObject object;
    public MyThread4(SynchronizedObject object){
        this.object=object;
    }
    @Override
    public void run(){
        object.OutString();
    }
}

public class Thread4 {
    public static void main(String[] args) {

        try {
            SynchronizedObject object=new SynchronizedObject();
            MyThread4 thread1=new MyThread4(object);
            thread1.setName("a");
            thread1.start();
            thread1.sleep(1000);
            MyThread4 thread2=new MyThread4(object);
            thread2.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
