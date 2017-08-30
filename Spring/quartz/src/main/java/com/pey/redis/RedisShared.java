package com.pey.redis;

import redis.clients.jedis.Jedis;

import java.util.zip.CRC32;

/**
 * Created by Peysen on 2017/8/29.
 * 使用分片提高访问性能
 */
public class RedisShared {


    public String shard_key(String base,String key,int totalCounts,int shardSize){
        Integer shard_id=Integer.parseInt(key);
        if (!(shard_id instanceof Integer)){
            Integer shard=2*totalCounts/shardSize;
            CRC32 crc32 = new CRC32();
            crc32.update("key".getBytes());
            shard_id=(int)crc32.getValue()%shard;
        }
        return shard_id.toString();
    }

    public void hset(Jedis jedis,String base,String key,String value,int totalCounts,int shardSize){
        String shardId = this.shard_key(base, key, totalCounts, shardSize);
        jedis.hset(shardId,key,value);
    }

    public void hget(Jedis jedis,String base,String key,String value,int totalCounts,int shardSize){
        String shardId = this.shard_key(base, key, totalCounts, shardSize);
        jedis.hget(shardId,key);
    }

    public void shard_sadd(Jedis jedis,String base,Object member,int totalCounts,int shardSize){
        String shardId = this.shard_key(base, "s"+member.toString(), totalCounts, shardSize);
        jedis.sadd(shardId,member.toString());
    }

    //负责记录每天唯一访客人数的函数
    public void count_visit(Jedis jedis,String sessionId){

    }
}
