package com.example.redis.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class JedisTest {

    public static void main(String[] args) {

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

        JedisPool pool = new JedisPool(jedisPoolConfig, "localhost", 6379, 1000, null);
        Jedis jedis = pool.getResource();
        // 이 test처럼 jedis 객체를 계속 사용하는 것은 위험한 코드.
        // 보통 하나의 동작을 하고 close() 해주고
        // 필요하면 다시 JedisPool에서 getResource()로 받아서 사용한다. (***)

        // 데이터 입력
        jedis.set("1", "hello");

        // 데이터 출력
        System.out.println(jedis.get("1")); // hello

        // 데이터 삭제
        jedis.del("1");
        System.out.println(jedis.get("1")); // null. 삭제한 데이터이므로 출력 안 되어야 맞음.

        try {
            jedis.set("key", "value");

            // 데이터 만료시간 지정
            jedis.expire("key", 5); // 5초
            Thread.sleep(4000); // 쓰레드 4초간 재우기
            System.out.println(jedis.get("key")); // value
            Thread.sleep(2000);
            System.out.println(jedis.get("key")); // null. 만료된 데이터이므로 출력 안 되어야 맞음.
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* Lists 형태 입출력 */
        jedis.lpush("/home/jdk", "firstTask");
        jedis.lpush("/home/jdk", "secondTask");
        System.out.println(jedis.rpop("/home/jdk")); // firstTask
        System.out.println(jedis.rpop("home/jdk")); // null

        /* Sets 형태 입출력 */
        jedis.sadd("nicknames", "judy");
        jedis.sadd("nicknames", "jdk");
        jedis.sadd("nicknames", "arua");
        Set<String> nickname = jedis.smembers("nicknames"); // key가 nicknames인 members들 전부 Set 형태로.
        Iterator iter = nickname.iterator();

        while (iter.hasNext()) {
            System.out.println(iter.next()); // 넣은 순서 관계없이 출력
        }

        /* Hashes 형태 입출력 */
        jedis.hset("user", "name", "joara");
        jedis.hset("user", "job", "software engineer");
        jedis.hset("user", "hobby", "coding");

        System.out.println(jedis.hget("user", "name")); // joara
        Map<String, String> fields = jedis.hgetAll("user"); // user 데이터 전부 Map 형태로.
        System.out.println(fields.get("name")); // joara
        System.out.println(fields.get("job")); // software enginner
        System.out.println(fields.get("hobby")); // coding

        /* Sorted Sets 형태 입출력 */
        jedis.zadd("scores", 6379.0, "PlayerOne");
        jedis.zadd("scores", 8000.0, "PlayerTwo");
        jedis.zadd("scores", 1200.5, "PlayerThree");

        System.out.println(jedis.zrangeByScore("scores", 0, 10000)); // [PlayerThree, PlayerOne, PlayerTwo]
        System.out.println(jedis.zrangeByScore("scores", 0, 7000)); // [PlayerThree, PlayerOne]

        if (jedis != null) {
            jedis.close();
        }
        pool.close();

    }

}
