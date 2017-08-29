package com.asiainfo.chap1;

/**
 * Created by prayyic on 2017/3/29.
 * 测试Thread先interrupt()再sleep()时抛出异常！
 */


class Mythread2 extends  Thread{
    @Override
    public void run(){
        super.run();
        try {
            for (int i=0;i<10000;i++){
                System.out.println("i="+(i+1));
            }
            System.out.println("run begin");
            Thread.sleep(200000);
            System.out.println("run end");

        }catch (InterruptedException e){
            System.out.println("先停止，再遇到了sleep，进入catch");
            e.printStackTrace();
        }
    }
}
public class Thread2 {
    public static void main(String[] args) {

        try {
            Mythread2 thread2=new Mythread2();
            thread2.start();
            Thread.sleep(200);//先让子线程运行200ms
            thread2.interrupt();//200ms后让子线程中断，此时主线程运行。
            System.out.println("end!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



    }
}
