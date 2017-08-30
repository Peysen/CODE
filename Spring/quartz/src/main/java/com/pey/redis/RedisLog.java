package com.pey.redis;

import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by Peysen on 2017/8/17.
 * 使用redis保存日志记录
 */
public class RedisLog {

    @Resource
    private JedisConnectionFactory connectionFactory;

    private JedisConnection jedisConnection;

    private Jedis jedis;

    private Jedis getJedis() {
        if (jedis == null) {
            System.out.println("factory="+connectionFactory.toString());
            jedisConnection = connectionFactory.getConnection();
            jedis = jedisConnection.getNativeConnection();
            return jedis;
        }
        return jedis;
    }

    //记录最新的日志
    //参数：jedis--key--value--日志等级：info，debug，error
    public void log_recent(Jedis jedis,String name,String message,String severity){
        String key="recent:"+name+":"+severity;
        String value=new Date().getTime()+" "+message;
        Pipeline pipelined = jedis.pipelined();
        pipelined.lpush(key,value);
        pipelined.ltrim(key,0,99);
        pipelined.exec();
    }

    //记录常见日志
    //参数：jedis--key--value--日志等级：info，debug，error--超时时间
    public void log_common(Jedis jedis,String name,String message,String severity,Long timeout){
        String key="recent:"+name+":"+severity;
        String start_key=key+":start";
        String value=new Date().getTime()+" "+message;

        Pipeline pipelined = jedis.pipelined();
        Long now=new Date().getTime();
        Long end=now+timeout;
        while(now<end){
            try{
                pipelined.watch(start_key);
                now =new Date().getTime();


            }catch (Exception e){

            }
        }

    }
}
