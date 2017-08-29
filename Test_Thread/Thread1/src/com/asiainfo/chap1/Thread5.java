package com.asiainfo.chap1;

import com.asiainfo.chap1.model.SynchronizedObject;

/**
 * Created by prayyic on 2017/3/29.
 * suspend()    resume()
 * 测试两方法也会导致数据不同步的情况
 */
public class Thread5 {
    public static void main(String[] args) {
        try {
            final SynchronizedObject object=new SynchronizedObject();

            Thread thread1=new Thread(){
                public void run(){
                    object.setValue("b","bb");
                }
            };
            thread1.setName("a");
            thread1.start();
            Thread.sleep(500);

            Thread thread2=new Thread(){
                public void run(){
                    object.printUsername();
                }
            };
            thread2.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
