package com.aaa.zhang.util;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 风行烈
 */
@Configuration
public class RedisConfig {

    @Value("${redis.password}")
    private String password;

    @Value("${redis.database}")
    private Integer database;

    @Value("${redis.address}")
    private String address;

    @Value("${redis.port}")
    private String port;

    @Bean
    public RedissonClient redissonClient() {
        //配置当前redis要连接的信息  address格式redis://127.0.0.1:6379
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + address + ":" + port).setPassword(password).setDatabase(database);
        return Redisson.create(config);
    }


}
