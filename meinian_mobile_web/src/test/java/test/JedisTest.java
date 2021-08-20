package test;

import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * @author 15144
 * @version 1.0
 * @description: TODO
 * @date 2021/8/17 15:21
 */
public class JedisTest {
    public static void main(String[] args) {
        JedisPool jedisPool = new JedisPool("192.168.6.100",6379);
        String s = jedisPool.getResource().get("18982629786002");
        System.out.println( s);
    }
}
