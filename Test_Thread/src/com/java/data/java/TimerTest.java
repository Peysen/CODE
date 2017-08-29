package com.java.data.java;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Peysen on 2017/7/26.
 */
public class TimerTest {
    private static class MyTask extends TimerTask {
        public void run() {
            System.out.println("我在测试我自己的myTask。。。。");
        }
    }

    public static void main(String[] args) {
        Timer timer = new Timer();
        //在1秒后执行此任务,每次间隔2秒执行一次,如果传递一个Data参数
        // 就可以在某个固定的时间执行这个任务.
        timer.schedule(new MyTask(), 1000, 2000);

        //这个是用来停止此任务的,否则就一直循环执行此任务
        while (true) {
            try {
                int in = System.in.read();
                if (in == 's') {
                    timer.cancel();//使用这个方法退出任务
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
