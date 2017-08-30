package com.pey.redis;

import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by Peysen on 2017/8/17.
 * 使用redis实现时间序列计数器
 */
public class RedisCounter {

    @Resource
    private JedisConnectionFactory connectionFactory;

    private JedisConnection jedisConnection;

    private Jedis jedis;

    private long[] PRECISION={1,5,60,300,3600,18000,86400};

    private Jedis getJedis() {
        if (jedis == null) {
            System.out.println("factory="+connectionFactory.toString());
            jedisConnection = connectionFactory.getConnection();
            jedis = jedisConnection.getNativeConnection();
            return jedis;
        }
        return jedis;
    }

    public void updateCounter(Jedis jedis,String name){
        Pipeline pipelined = jedis.pipelined();
        long now=new Date().getTime();
        for(long prec : PRECISION){
            long pnow=(now/prec)*prec;
            String key=prec+name;
            pipelined.zadd("known:",Double.parseDouble("0"),key);
            pipelined.hincrBy("count:"+key,pnow+"",1);
            pipelined.exec();
        }
    }
    //获取计数器集合中的数据
    public List<String> getCounter(Jedis jedis,String name,String prec){
        String hash=prec+name;
        Map<String, String> result = jedis.hgetAll("count:" + hash);
        List<String> values=new ArrayList<String>();
        for(String key:result.keySet()){
            values.add(result.get(key));
        }
        Collections.sort(values);
        return values;
    }

    //清理旧计数器
    public void cleanCounter(Jedis jedis){
        Pipeline pipelined = jedis.pipelined();
        int passes=0; 
        
        while(true){
            long start = new Date().getTime();
            int index=0;
            while(index<jedis.zcard("known:")){
                //一个个的获取“known”中的计数器
                Set<String> hash = jedis.zrange("known:", index, index);
                index++;
                if(hash!=null){
                    String value=hash.iterator().next();
                    int prec =Integer.parseInt(value.split("\\:")[0]);
                    int bprec=(prec / 60);
                    if(passes % bprec ==0){
                        continue;
                    }else{
                        String hkey="count:"+value;
//                        new  Date().getTime()-10*10;

                    }
                }
            }
        }
    }
}
