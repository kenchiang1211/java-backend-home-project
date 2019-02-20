package com.test.bank.initializer;

import com.fuan.admin.filter.JwtAuthFilter;
import com.google.common.collect.Sets;
import com.test.bank.constant.Role;
import com.test.bank.model.admin.AdminUserVo;
import com.test.bank.tool.config.EnvConfigManager;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.Authorizer;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.consumer.JwtContext;
import org.jose4j.keys.HmacKey;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public class JwtInitializer {
    private final static Logger logger = LoggerFactory.getLogger(JwtInitializer.class);

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
        private final static String SESSION_PREFIX = "admin:sess:";

        public AdminAuthenticator(RedissonClient redissonClient) {
            this.redissonClient = redissonClient;
        }

        @Override
        public Optional<AdminUserVo> authenticate(JwtContext context) {
            // TODO add token authentication from redis
            try {
                JwtClaims claims = context.getJwtClaims();
                String adminIdStr = claims.getSubject();

                RBucket<String> bucket = redissonClient.getBucket(SESSION_PREFIX + adminIdStr);
                String savedToken = bucket.get();

                if (context.getJwt().equals(savedToken)) {
                    List<String> roles = claims.getClaimValue("ROLES", List.class);

                    AdminUserVo adminUser = new AdminUserVo();
                    adminUser.setId(Integer.valueOf(adminIdStr));
                    adminUser.setRoleSet(Sets.newHashSet(roles));

                    return Optional.of(adminUser);
                }
            } catch (MalformedClaimException e) {
                logger.warn("Failed to get claim for {}", context.getJwt());
                logger.warn(e.getMessage(), e);
            }

            return Optional.empty();
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

            return user.getRoleSet().contains(role);
        }
    }
}
