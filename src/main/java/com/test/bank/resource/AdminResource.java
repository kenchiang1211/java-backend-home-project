package com.test.bank.resource;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.test.bank.initializer.RedissonInitializer;
import com.test.bank.model.admin.AdminUserVo;
import com.test.bank.model.response.SessionResponse;
import com.test.bank.service.AdminService;
import com.test.bank.tool.config.EnvConfigManager;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.concurrent.TimeUnit;

import static org.jose4j.mac.MacUtil.HMAC_SHA256;

@Path("")
@Produces(MediaType.APPLICATION_JSON)
public class AdminResource extends BaseResource {

    private final static Logger logger = LoggerFactory.getLogger(AdminResource.class);

    private final byte[] jwtTokenSecret;
    private AdminService adminService;
    private RedissonClient redissonClient;
    private static final int MINUTE_TO_LIVE = 720;
    private final static String SESSION_PREFIX = "admin:sess:";

    @Inject
    public AdminResource(EnvConfigManager envConfigManager,
                         AdminService adminService,
                         RedissonInitializer redissonInitializer) {
        super(envConfigManager);
        this.jwtTokenSecret = envConfigManager.getConfigAsString("jwtTokenSecret").getBytes();
        this.adminService = adminService;
        this.redissonClient = redissonInitializer.getMainRedissonClient();
    }


    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public SessionResponse doLogin(@Valid AdminUserVo adminUserVo) throws JoseException, JsonProcessingException {
        AdminUserVo adminUser = adminService.login(adminUserVo.getAccount(), adminUserVo.getPassword());

        if (adminUser == null) {
            throw new BadRequestException();
        }

        logger.info("user = {}", new ObjectMapper().writeValueAsString(adminUser));

        final JwtClaims claims = new JwtClaims();
        claims.setSubject(String.valueOf(adminUser.getId()));
        claims.setExpirationTimeMinutesInTheFuture(MINUTE_TO_LIVE);
        claims.setClaim("ROLES", Lists.newArrayList(adminUser.getRoleSet()));

        final JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setAlgorithmHeaderValue("HS256");
        jws.setKey(new HmacKey(jwtTokenSecret));

        String token = jws.getCompactSerialization();

        // TODO add redis save token
        RBucket<String> bucket = redissonClient.getBucket(SESSION_PREFIX + adminUser.getId().toString());
        bucket.set(token, MINUTE_TO_LIVE, TimeUnit.MINUTES);

        SessionResponse response = new SessionResponse();
        response.setToken(token);
        return response;
    }
}
