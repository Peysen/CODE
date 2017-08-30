package com.pey.redis;

import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * Created by Peysen on 2017/8/23.
 */
public class RedisCity {

    //根据ip地址转换成整数分值
    public int ip_to_score(String ip_address){
        int score=0;
        String[] split = ip_address.split("\\.");
        for(String str:split){
            score=score*256+Integer.parseInt(str);
        }
        return score;
    }

    //通过ip地址映射到城市id
    public void import_redis(Jedis jedis){
        String start_ip="";
        int score=0;
        if(start_ip.contains(".")){
            score=this.ip_to_score(start_ip);
        }else{
            score=Integer.parseInt(start_ip);
        }
        String city_id="cityId_num";

        jedis.zadd("ip2cityId:",Double.parseDouble(score+""),city_id);
    }

    //将城市信息存储到redis中
    public void import_cities_to_redis(Jedis jedis){
        String city_id="";
        JSONObject obj=new JSONObject();
        String city="city";
        String country="country";
        String region="country";
        obj.put("city",city);
        obj.put("country",country);
        obj.put("region",region);
        jedis.hset("cityId2City:",city_id,obj.toJSONString());
    }

    //通过ip查找城市信息
    public JSONObject find_city_by_ip(Jedis jedis,String ip_address){
        //先将ip地址转换成分值
        int score=0;
        if(ip_address!=null && ip_address!=""){
            score=this.ip_to_score(ip_address);
        }
        //通过分值在ip2cityId有序集合中查找城市Id
        Set<String> citys = jedis.zrevrangeByScore("ip2cityId:", score, 0, 0, 1);
        String cityId="";
        if(citys!=null){
            cityId=citys.iterator().next().split("\\_")[0];
        }


        //通过cityId在cityId2City集合中查找城市信息
        String city = jedis.hget("cityId2City:", cityId);
        return JSONObject.parseObject(city);
    }
}
