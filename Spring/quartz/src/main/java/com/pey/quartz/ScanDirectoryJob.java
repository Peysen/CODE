package com.pey.quartz;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*;

import java.io.File;
import java.io.FileFilter;
import java.util.Date;

/**
 * Created by Peysen on 2017/7/26.
 */
public class ScanDirectoryJob  implements Job {
    static Log logger = LogFactory.getLog(ScanDirectoryJob.class);//日志记录器

    public void execute(JobExecutionContext context) throws JobExecutionException
    {
        //Every job has its own job detail
        JobDetail jobDetail = context.getJobDetail();
        // The name is defined in the job definition
        String jobName = jobDetail.getName();//任务名称
        // Log the time the job started
        System.out.println(jobName + " fired at " + new Date());//记录任务开始执行的时间
        // The directory to scan is stored in the job map
        JobDataMap dataMap = jobDetail.getJobDataMap();//任务所配置的数据映射表
        String dirName = dataMap.getString("SCAN_DIR");//获取要扫描的目录
        System.out.println("scan_dir="+dirName);
        // Validate the required input
        if (dirName == null)
        {//所需要的扫描目录没有提供
            throw new JobExecutionException( "Directory not configured" );
        }
        // Make sure the directory exists
        File dir = new File(dirName);
        if (!dir.exists())
        {//提供的是错误目录
            throw new JobExecutionException( "Invalid Dir "+ dirName);
        }
        // Use FileFilter to get only XML files
        FileFilter filter = new FileExtensionFileFilter(".xml");
        //只统计xml文件
        File[] files = dir.listFiles(filter);
        if (files == null || files.length <= 0)
        {//目录下没有xml文件
            logger.info("No XML files found in " + dir);
            // Return since there were no files
            return;
        }
        // The number of XML files
        int size = files.length;
        // Iterate through the files found
        for (int i = 0; i < size; i++)
        {
            File file = files[i];
            // Log something interesting about each file.
            File aFile = file.getAbsoluteFile();
            long fileSize = file.length();
            String msg = aFile + " - Size: " + fileSize;
            logger.info(msg);//记录下文件的路径和大小
        }
    }
}
