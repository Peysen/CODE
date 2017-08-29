package com.pey.redis;

import com.pey.model.User;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Set;

/**
 * Created by Peysen on 2017/8/11.
 * 使用redis对cookie进行缓存。
 */
public class RedisWeb {
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

    //需求：①使用一个hash来存储登录cookie令牌token和已登录用户user之间的映射。
    public void setCookie(String token,User user){
        jedis.hset("login:",token,user.toString());
    }

    //通过token检查登录cookie的方法
    public void checkToken(String token){
        String user = jedis.hget("login:", token);
    }

    //更新cookie
    public void updateToken(String token,User user,String item){
        String time = new Date().getTime()+"";
        this.setCookie(token,user);
        jedis.zadd("recent:",Double.parseDouble(time),token);

        if(item.equals("1")){//表示用户浏览的是商品页面
            //将此用户在该时间戳time中查看的页面加入viewed有序集合中。
            jedis.zadd("viewed:"+token,Double.parseDouble(item),time);
            //移除旧的元素，只保留用户最近浏览的25个商品。
            jedis.zremrangeByRank("viewed:"+token,0,-26);
        }
    }

    //检查最近登录的集合，if（size>num），则移除100个旧令牌
    //通过移除的100旧令牌的token，移除最近登录集合和用户查看页面记录的集合
    //清理旧的会话记录
    public void cleanSession(){
        long LIMIT=3000;
        while(true){
            //检查最近登录用户的个数
            Long size = jedis.zcard("recent:");
            if(size<=LIMIT){
                System.out.println("从新来过");
            }else{
                long end_index = Math.min(size - LIMIT, 100);
                Set<String> tokens = jedis.zrange("login:", 0, end_index);
                for(String token:tokens){
                    jedis.del("viewed:"+token);
                    jedis.hdel("login:",token);
                    jedis.zrem("recent:",token);
                }

            }
        }
    }

}
