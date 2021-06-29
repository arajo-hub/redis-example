package com.example.redis.cluster;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;

import java.util.Set;


@Configuration
public class JedisConfig {

    @Bean
    public JedisCluster jedisCluster() {

        try{
            Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();

            jedisClusterNodes.add(new HostAndPort("192.168.30.136", 7000));
            jedisClusterNodes.add(new HostAndPort("192.168.30.136", 7001));
            jedisClusterNodes.add(new HostAndPort("192.168.30.136", 7002));

            //필요하다면 pool 정의
            //JedisPoolConfig jedisPoolConfig = new JedisPoolConfig()

            return new JedisCluster(jedisClusterNodes);
        }catch (Exception e){
            return null;
        }

    }
}