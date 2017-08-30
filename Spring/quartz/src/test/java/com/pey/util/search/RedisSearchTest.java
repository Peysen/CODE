package com.pey.util.search;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Peysen on 2017/8/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-context.xml",
        "classpath:spring/schedule-cache.xml"
})
public class RedisSearchTest {
    @Resource
    private JedisConnectionFactory connectionFactory;

    private JedisConnection jedisConnection;

    private Jedis jedis;

    private String[] cwords="wo wants was we were what when where which while who".split(" ");

//    @Before
    @Test
    public void getJedis(){
        if (jedis == null) {
            jedisConnection = connectionFactory.getConnection();
            jedis = jedisConnection.getNativeConnection();
        }
    }

    public Set<String>  tokenSize() throws Exception {
        String content="wo de name is jiao pei meng meng a 'xiaoming'";
        String[] words = content.toLowerCase().split(" ");
        System.out.println("words=" + words.length);
        Pattern pattern = Pattern.compile("[a-z']{2,}");

        Set<String> addWords = new HashSet<String>();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            Matcher matcher = pattern.matcher(word);
            if (matcher.matches()) {
                String regexp = "\'";
                //去除单词前后的单引号
                word = word.replaceAll(regexp, "");
                if (word.length() >= 2) {
                    addWords.add(word);
                }
            }
        }
        System.out.println("addWords="+addWords.size());
        addWords.removeAll(Arrays.asList(cwords));
        System.out.println("addWords="+addWords.size());
        return addWords;
    }

    @Test
    public void index() throws Exception {
        String docName="chapA";
        Set<String> words = this.tokenSize();
        System.out.println("set=="+words.toString());
        Pipeline pipelined = jedis.pipelined();
        pipelined.multi();
        for(String word:words){
            pipelined.sadd("idx:"+word,docName);
        }
        pipelined.exec();
        pipelined.sync();

        System.out.println("meng:members:"+ jedis.smembers("idx:meng"));
    }

    @Test
    public void set_common() throws Exception {
        //创建临时id，将结果集放入其中
        String id="idx:result";
        Set<String> names=new HashSet<String>();
        names.add("name");
        names.add("meng");
        int ttl=3000000;

        Pipeline pipelined = jedis.pipelined();
        pipelined.multi();
        Set<String> newNames=new HashSet<String>();
        for(String name:names){
            newNames.add("idx:"+name);
        }
        Object[] fields=newNames.toArray();
        for (Object obj:fields){
            System.out.println("filed=="+obj.toString());
            pipelined.sadd(obj.toString(),"chapB");
        }
//        pipelined.sinterstore(id,fields.toString());
        pipelined.sunionstore(id,"idx:name","idx:meng");
//        pipelined.sdiffstore(id,fields);
        pipelined.expire(id,ttl);
        pipelined.exec();
        pipelined.sync();

        System.out.println("name:members:"+ jedis.smembers("idx:name"));
        System.out.println("meng:members:"+ jedis.smembers("idx:meng"));
        System.out.println("result:"+ jedis.smembers("idx:result"));
    }

    //

}