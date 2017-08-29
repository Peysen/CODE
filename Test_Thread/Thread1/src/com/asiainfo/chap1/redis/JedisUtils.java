package com.asiainfo.chap1.redis;
/**
 * 作者：裴萌萌
 * 时间：2017-04-01 19:53
 * 功能：获取redis集群
 */
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import java.util.Map;

public class JedisUtils {
    public static JedisCluster statisticsJedisCluster;

    public static void initJedisCluster() {

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnReturn(true);
        jedisPoolConfig.setTestOnCreate(true);
        jedisPoolConfig.setTestWhileIdle(true);
        jedisPoolConfig.setMaxTotal(RedisConfig.maxTotal);
        jedisPoolConfig.setMinIdle(1);
        //一定要设置不然会一直等待获取连接导致线程阻塞
        jedisPoolConfig.setMaxWaitMillis(RedisConfig.maxWaitMillis);
        statisticsJedisCluster = new JedisCluster(RedisConfig.statisticsNodes, 60000, 3000, 1000, "Tyhj@1118", jedisPoolConfig);
//      statisticsJedisCluster = new JedisCluster(RedisConfig.statisticsNodes, 60000, jedisPoolConfig);

        for (Map.Entry<String, JedisPool> node : statisticsJedisCluster.getClusterNodes().entrySet()) {
           System.out.println("REDIS集群节点：" + node.getKey() + "\n" + node.getValue().getResource().clusterNodes());
        }
       System.out.println("########REDIS CLUSTER连接池,启动成功########");
    }
    
    /**
     * 根据配置文件获取用户轨迹redis集群客户端
     */
    public static JedisCluster getJedisClusterForStatistics(){
    	if(JedisUtils.statisticsJedisCluster != null){
    		return statisticsJedisCluster;
    	}else{
    		initJedisCluster();
    		return statisticsJedisCluster;
    	}
    }
}
