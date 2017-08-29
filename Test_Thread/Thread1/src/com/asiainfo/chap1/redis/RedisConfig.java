package com.asiainfo.chap1.redis;

import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.HostAndPort;

import java.util.HashSet;
import java.util.Set;

/**
 * 作者：裴萌萌
 * 时间：2017-04-01 19:53
 * 功能：配置redis集群连接池的信息
 */
class RedisConfig {

    static Set<HostAndPort> statisticsNodes = new HashSet<HostAndPort>();

    static String password="Tyhj@1118";
    
    static Integer maxTotal = 50;
    static Integer maxWaitMillis = 3000;
    
    static {
    	try{
            String statisticsNodesArr[] = {"10.11.60.204:2181","10.11.60.205:2181","10.11.60.206:2181"};
//    		String statisticsNodesArr[] = {"10.11.94.91:6379","10.11.94.99:6379","10.11.94.106:6379","10.11.94.113:6379","10.11.94.121:6379","10.11.94.128:6379", "10.11.94.135:6379","10.11.94.143:6379","10.11.94.151:6379","10.11.94.158:6379","10.11.94.165:6379","10.11.94.173:6379"};
            //HostAndPort
            for(String ip : statisticsNodesArr){
            	HostAndPort hostAndPort = new HostAndPort(ip.split(":")[0], Integer.parseInt(ip.split(":")[1]));
            	statisticsNodes.add(hostAndPort);
            }
            
            //maxTotal
            String maxTotalValue = "50";
            if (StringUtils.isNotEmpty(maxTotalValue)) {
            	maxTotal = Integer.parseInt(maxTotalValue);
            }
            
            //maxWaitMillis
            String maxWaitMillisValue = "3000";
            if(StringUtils.isNotEmpty(maxWaitMillisValue)){
            	maxWaitMillis = Integer.parseInt(maxWaitMillisValue);
            }

            //password
            if(StringUtils.isNotEmpty(password)){
                password = "Tyhj@1118";
            }


    	}catch(Exception e){
            System.out.println("获取Redis配置错误，无法获取redis的集群配置信息，系统退出");
            System.exit(-1);
    	}
    }

}
