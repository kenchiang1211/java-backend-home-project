package com.test.bank.initializer;

import com.fuan.admin.filter.JwtAuthFilter;
import com.test.bank.constant.Role;
import com.test.bank.model.admin.AdminUserVo;
import com.test.bank.tool.config.EnvConfigManager;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.Authorizer;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.consumer.JwtContext;
import org.jose4j.keys.HmacKey;
import org.redisson.api.RedissonClient;

import java.security.Principal;
import java.util.Optional;

public class JwtInitializer {

    EnvConfigManager envConfigManager;
    RedissonClient redissonClient;

    public JwtInitializer(EnvConfigManager envConfigManager,
                          RedissonInitializer redissonInitializer) {
        this.redissonClient = redissonInitializer.getMainRedissonClient();
        this.envConfigManager = envConfigManager;
    }

    public void initialize(Environment environment) {
        final byte[] key = envConfigManager.getConfigAsString("jwtTokenSecret").getBytes();

        final JwtConsumer consumer = new JwtConsumerBuilder()
                .setAllowedClockSkewInSeconds(30)
                .setRequireExpirationTime()
                .setRequireSubject()
                .setVerificationKey(new HmacKey(key))
                .setRelaxVerificationKeyValidation()
                .build();

        environment.jersey().register(new AuthDynamicFeature(
                new JwtAuthFilter.Builder<AdminUserVo>()
                        .setJwtConsumer(consumer)
                        .setRealm("realm")
                        .setPrefix("Bearer")
                        .setAuthenticator(new AdminAuthenticator(this.redissonClient))
                        .setAuthorizer(new AdminAuthorizer())
                        .buildAuthFilter()));

        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(Principal.class));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
    }

    private static class AdminAuthenticator implements Authenticator<JwtContext, AdminUserVo> {

        RedissonClient redissonClient;

        public AdminAuthenticator(RedissonClient redissonClient) {
            this.redissonClient = redissonClient;
        }

        @Override
        public Optional<AdminUserVo> authenticate(JwtContext context) {
            // TODO add token authentication from redis
            return null;
        }
    }

    private static class AdminAuthorizer implements Authorizer<AdminUserVo> {

        @Override
        public boolean authorize(AdminUserVo user, String role)
        {
            // TODO add other role authorize, thinking how to retrieve the role information

            if(user.getRoleSet().contains(Role.SUPER_ADMIN)) {
                return true;
            }

            return false;
        }
    }
}
