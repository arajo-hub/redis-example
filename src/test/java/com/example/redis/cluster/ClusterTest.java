package com.example.redis.cluster;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.JedisCluster;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClusterTest {

    @Autowired
    private JedisCluster jedisCluster;

    @Test
    public void connectionTest() {

        String key = "result";
        String value = "success";

        jedisCluster.set(key, value);

        String valueFromCluster = jedisCluster.get(key);

        assertThat(valueFromCluster).isEqualTo(value);

    }

}