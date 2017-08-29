package com.pey.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

/**
 * Created by Peysen on 2017/7/27.
 */
public class SpringQtz {
    private static int counter = 0;
    protected void execute() throws JobExecutionException {
        System.out.println("peimm---quates"+new Date().toString());
    }
}
