package com.example.redis.lettuce;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LettuceTest {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Test
    public void redisBasicFunction_guide() throws JSONException {
        // Given
        String key = "IamKey";
        String value = "IamValue";

        // redis에 set
        redisTemplate.opsForValue().set(key, value);

        // redis에서 get
        String valueByKey = (String) redisTemplate.opsForValue().get(key);

        System.out.println("valueByKey = " + valueByKey);

        // redis에서 data delete
        this.redisTemplate.delete(key);

        // redis에 해당 key를 가지고 있는지 확인
        if (!this.redisTemplate.hasKey("999")) {
            System.out.println("key가 존재하지 않습니다.");
        }

    }

}