package com.test.bank.initializer;

import com.test.bank.tool.config.EnvConfigManager;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RedissonInitializer {

    RedissonClient mainRedissonClient;
    EnvConfigManager envConfigManager;

    @Inject
    public RedissonInitializer(EnvConfigManager envConfigManager) {
        this.envConfigManager = envConfigManager;
    }

    public void initialize() {
        this.mainRedissonClient = createRedissonClient("main");
    }

    private RedissonClient createRedissonClient(String name) {
        String password = envConfigManager.getConfigAsString("redis." + name + ".password");
        Config redissonConfig = new Config();
        redissonConfig.useSingleServer()
                .setAddress(envConfigManager.getConfigAsString("redis." + name + ".host"))
                .setConnectionPoolSize(envConfigManager.getConfigAsInt("redis." + name + ".pool_size"))
                .setConnectTimeout(envConfigManager.getConfigAsInt("redis." + name + ".client_timeout_millis"));

        if (!password.equals("")) {
            redissonConfig.useSingleServer().setPassword(password);
        }
        return Redisson.create(redissonConfig);
    }

    public RedissonClient getMainRedissonClient() {
        return mainRedissonClient;
    }
}
