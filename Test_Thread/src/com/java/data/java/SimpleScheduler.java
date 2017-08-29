package com.java.data.java;


import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.helpers.TriggerUtils;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

/**
 * Created by Peysen on 2017/7/26.
 */
public class SimpleScheduler {

    //创建调度器
    public Scheduler createScheduler() throws SchedulerException {
        return StdSchedulerFactory.getDefaultScheduler();
    }

    //Create and Schedule a ScanDirectoryJob with the Scheduler
    private void scheduleJob(Scheduler scheduler) throws SchedulerException {
        System.out.println("scheduleJob。。。");
        // Create a JobDetail for the Job
        JobDetail jobDetail = new JobDetail("ScanDirectory",Scheduler.DEFAULT_GROUP,ScanDirectoryJob.class);
        // Configure the directory to scan
        //set the JobDataMap that is associated with the Job.
        jobDetail.getJobDataMap().put("SCAN_DIR","D:\\conf");
        //每10秒触发一次
        Trigger trigger = TriggerUtils.makeSecondlyTrigger(10);
        trigger.setName("scanTrigger");
        trigger.setGroup("peimm");
        //设置第一次触发时间
        trigger.setStartTime(new Date());
        scheduler.scheduleJob(jobDetail, trigger);
    }

    public static void main(String[] args) {
        SimpleScheduler simple = new SimpleScheduler();
        try{
            // Create a Scheduler and schedule the Job
            Scheduler scheduler = simple.createScheduler();
            System.out.println("scheduler:"+scheduler.toString());
            simple.scheduleJob(scheduler);
            // Start the Scheduler running
            scheduler.start();
            System.out.println( "Scheduler started at " + new Date());
        } catch (SchedulerException ex) {
            System.out.println(ex);
        }
    }
}
