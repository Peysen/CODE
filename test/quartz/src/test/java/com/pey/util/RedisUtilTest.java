package com.pey.util;

import com.alibaba.fastjson.JSONObject;
import com.pey.model.Articel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Created by Peysen on 2017/8/10.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-context.xml",
        "classpath:spring/schedule-cache.xml"
})
public class RedisUtilTest {
    @Resource
    private JedisConnectionFactory connectionFactory;

    private JedisConnection jedisConnection;

    private Jedis jedis;

    @Before
    public void getJedis(){
        if (jedis == null) {
            jedisConnection = connectionFactory.getConnection();

            jedis = jedisConnection.getNativeConnection();
        }
    }

    //创建文章并将其创建时间和分数放入set中。
    @Test
    public void createArticle() throws Exception {
        Map<String, String> map = null;

        Articel article=new Articel("go to redis",
                        "http://redis.com","user:00003",
                        new Date().getTime()+"",0);
        JSONObject json = new JSONObject();
        json.put("title",article.getTitle());
        json.put("link",article.getLink());
        json.put("poster",article.getPoster());
        json.put("time",article.getTime());
        json.put("votes",article.getVotes());

        Long id=1l;

        if(!jedis.exists("article")){
            System.out.println("article不存在。。。");
        }else{
            System.out.println("article存在。。。"+id);
            id = jedis.hlen("article")+1;
        }
        String article_id="article:"+id;
        System.out.println("article_id="+article_id);

        //将article_id作为key，article的json作为value放入名为article的hash中。
        jedis.hset("article",article_id,json.toJSONString());

        String voted="voted:"+id;
        //将voted：article_id作为set的key，将投票者放入其中。
        jedis.sadd(voted,article.getPoster());

        //设置有效时间
        jedis.expire(voted,7*86400);
        //将article_id作为key，分数作为value，放入名为score的有序集合中。
        jedis.zadd("score:",Double.parseDouble(article.getTime()+article.getVotes()),article_id);
        //将article_id作为key，时间作为value，放入名为time的有序集合中。
        jedis.zadd("time:", Double.parseDouble(article.getTime()),article_id);

    }

    //投票功能
    @Test
    public void acticle_vote(){
        int vote_score=432;
        long one_week_in_seconds=7*86400;

        String article_id="article:1";
        String id=article_id.split("\\:")[1];
        String voted="voted:"+id;
        String user="user:pmm2";

        long cutOff = new Date().getTime() - one_week_in_seconds;
        //获取文章的发布时间
        Double time = jedis.zscore("time:", article_id);
        System.out.println("文章的发布时间："+time.toString());
        if(time<cutOff){//已过期,不能进行投票
            System.out.println("已过期。。。");
            return;
        }else{
            System.out.println("未过期。。。");
            //判断此用户是否已投票
            if(!jedis.sismember(voted,user)){//此用户没投票过
                System.out.println("此用户没投票过....");
                //将此用户加入投票set中
                jedis.sadd(voted,user);

                //将此文章的评分+432
                jedis.zincrby("score:",Double.parseDouble(vote_score+""),article_id);

                //将此文章的投票数+1
                String article = jedis.hget("article", article_id);
                System.out.println("before："+article);
                JSONObject json=JSONObject.parseObject(article);
                json.put("votes",Integer.parseInt(json.get("votes")+"")+1);
                jedis.hset("article",article_id,json.toJSONString());
                String article1 = jedis.hget("article", article_id);
                System.out.println("after："+article1);
            }
        }
    }

    //获取评分最高的文章
    @Test
    public void getScoreMax(){
        Set<String> sroceSet = jedis.zrevrange("score:", 0, -1);
        for(String sroce:sroceSet){
            System.out.println("sroce=="+sroce.toString());
        }
    }
    //最获取新发布的文章
    public void getNewTime(){
        Set<String> timeSet = jedis.zrevrange("time:", 0, -1);
    }

    @Test
    public void showAll(){
        String voted1="voted:1";
        String voted2="voted:2";
        String voted3="voted:3";

        Map<String,String> map=null;
        map = jedis.hgetAll("article");
        for(String key:map.keySet()){
            System.out.println("key="+key+",value="+map.get(key));
        }
        System.out.println("voted:"+jedis.smembers(voted1));
        System.out.println("voted:"+jedis.smembers(voted2));
        System.out.println("voted:"+jedis.smembers(voted3));

        System.out.println("score:"+jedis.zrange("score:", 0, -1));
        System.out.println("time:" +jedis.zrange("time:",0,-1));
        System.out.println("time="+jedis.zscore("score:", "article:1"));

    }

    @Test
    public void clear(){
        jedis.del("article");
        jedis.del("score:");
        jedis.del("time:");
        jedis.del("voted:1");
        jedis.del("voted:2");
        jedis.del("voted:3");
    }
}