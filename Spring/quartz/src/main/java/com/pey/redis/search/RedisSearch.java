package com.pey.redis.search;

import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import javax.annotation.Resource;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Peysen on 2017/8/22.
 */
public class RedisSearch {
    @Resource
    private JedisConnectionFactory connectionFactory;

    private JedisConnection jedisConnection;

    private Jedis jedis;

    private String[] arr="wo wants was we were what when where which while who".split(" ");

    private Set cwords=new HashSet<String>(Arrays.asList(arr));

    private long[] PRECISION={1,5,60,300,3600,18000,86400};

    private Jedis getJedis() {
        if (jedis == null) {
            jedisConnection = connectionFactory.getConnection();
            jedis = jedisConnection.getNativeConnection();
            return jedis;
        }
        return jedis;
    }


    //将文档生成索引，并移除非用词
    public Set<String> tokenSize(String content){
        String[] words=content.toLowerCase().split(" ");
        System.out.println("words="+words.length);
        Pattern pattern = Pattern.compile("[a-z']{2,}");

        Set<String> addWords=new HashSet<String>();
        for(int i=0;i<words.length;i++){
            String word=words[i];
            Matcher matcher = pattern.matcher(word);
            if(matcher.matches()){
                String regexp = "\'";
                //去除单词前后的单引号
                word = word.replaceAll(regexp, "");
                if(word.length()>=2){
                    addWords.add(word);
                }
            }
        }
        System.out.println("addWords="+addWords.size());
        addWords.removeAll(cwords);
        System.out.println("addWords="+addWords.size());
        return addWords;
    }

    //参数：文档名称，文档内容
    public void index(Jedis jedis,String docName,String content){
        Set<String> words = this.tokenSize(content);

        Pipeline pipelined = jedis.pipelined();
        for(String word:words){
            pipelined.sadd("idx:"+word,docName);
        }
        pipelined.exec();
    }

    //参数：查询单词的集合，结果集的过期时间
    public String set_common(Jedis jedis,Set<String> names,int ttl){
        //创建临时id，将结果集放入其中
        String id="idx:result";

        Pipeline pipelined = jedis.pipelined();
        Set<String> newNames=new HashSet<String>();
        for(String name:names){
            newNames.add("idx:"+name);
        }
        String[] fields=(String[])newNames.toArray();
        pipelined.sinterstore(id,fields);
//        pipelined.sunionstore(id,fields);
//        pipelined.sdiffstore(id,fields);
        pipelined.expire(id,ttl);
        return id;
    }

    //使用+-符号对单词进行过滤
    public Map<String,Object> parse(String content){
//        String content="wo de name is jiao pei meng meng a 'xiaoming'";
        Set<String> unwanted=new HashSet<String>();
        Set<Set> wanted  =new HashSet<Set>();
        Set<String> current =new HashSet<String>();

        String[] words=content.toLowerCase().split(" ");
        System.out.println("words="+words.length);
        Pattern pattern = Pattern.compile("[+-]?[a-z']{2,}");

        for(int i=0;i<words.length;i++){
            String word=words[i];
            Matcher matcher = pattern.matcher(word);
            if(matcher.matches()){
                //检查单词是否带有前缀
                char prefix=' ';
                if(word.charAt(0)=='-' || word.charAt(0)=='+'){
                    word=word.substring(1,word.length()-1);
                    prefix=word.charAt(0);
                }else{
                    prefix=' ';
                }
                String regexp = "\'";
                //去除单词前后的单引号
                word = word.replaceAll(regexp, "");
                if(word.length()<2 || cwords.contains(word)){
                    continue;
                }

                if(word.charAt(0)=='-'){
                    unwanted.add(word);
                    continue;
                }
                if(current.size()<=0 && prefix==' '){
                    wanted.add(current);
                    current=new HashSet<String>();
                    current.add(word);
                }
            }
            if(current.size()>0){
                wanted.add(current);
            }
        }
        Map<String,Object> result=new HashMap<String,Object>();
        result.put("allwant",wanted);
        result.put("unwant" ,unwanted);
        return result;
    }

    public String intersect(Jedis jedis,Set<String> names,int ttl){
        return set_common(jedis,names,ttl);
    }
    public String unionsect(Jedis jedis,Set<String> names,int ttl){
        return set_common(jedis,names,ttl);
    }
    public String diffsect(Jedis jedis,Set<String> names,int ttl){
        return set_common(jedis,names,ttl);
    }

    public String parseAndSearch(Jedis jedis,String content,int ttl){
        Map<String, Object> resutlMap = parse(content);
        Set<Set<String>> all=(Set<Set<String>>)resutlMap.get("allwant");
        Set<String> unwant=(Set<String>)resutlMap.get("unwant");

        String intersect_result="";
        if(all==null){
            return null;
        }

        Set<String> to_insersect=new HashSet<String>();
        for(Set<String> set:all){
            if(set.size()>1){
                to_insersect.add(unionsect(jedis,set,ttl));
            }else{
                to_insersect.add(set.iterator().next());
            }
        }
        if(to_insersect.size()>1){
            intersect_result= intersect(jedis, to_insersect, ttl);
        }else{
            intersect_result=to_insersect.iterator().next();
        }

        if(unwant!=null){
            intersect_result=diffsect(jedis,unwant,ttl);
        }
        return intersect_result;
    }

    public static void main(String[] args) {
        RedisSearch redis= new RedisSearch();
        String content="-wo +de +name +is -jiao +pei +meng +meng -a +'xiaoming'";
//        Set<String> strings =redis.tokenSize(content);
//        for(String s:strings){
//            System.out.println("aaa==="+s);
//        }
        redis.parse(content);




    }
}
