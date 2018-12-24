package com.test.bank.initializer;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.test.bank.tool.config.EnvConfigManager;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.StringUtils;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Singleton
public class DataSourceInitializer {

    Logger log = LoggerFactory.getLogger(DataSourceInitializer.class);

    DefaultConfiguration jooqConfiguration;

    EnvConfigManager envConfigManager;

    @Inject
    public DataSourceInitializer(EnvConfigManager envConfigManager) {
        this.envConfigManager = envConfigManager;
    }

    public void initialize() {
        ExecutorService executor = Executors.newWorkStealingPool();
        List<Callable<Void>> callableList = Arrays.asList(
                () -> {
                    this.jooqConfiguration = jooqConfiguration("main");
                    return null;
                }
        );

        try {
            executor.invokeAll(callableList);
        } catch (InterruptedException e) {
            log.error("data source initialize failed", e);
            System.exit(9);
        }
    }

    private DefaultConfiguration jooqConfiguration(String source) {
        String jdbcUrl = envConfigManager.getConfigAsString("db." + source + ".url");
        String dbUser = envConfigManager.getConfigAsString("db." + source + ".username");
        String dbPw = envConfigManager.getConfigAsString("db." + source + ".password");

        if (StringUtils.isNotBlank(jdbcUrl) && jdbcUrl.contains(",")) {
            List<String> urls = Lists.newArrayList(Splitter.on(CharMatcher.anyOf(","))
                    .trimResults(CharMatcher.is(','))
                    .omitEmptyStrings()
                    .splitToList(jdbcUrl.toLowerCase()));
            Collections.shuffle(urls);
            jdbcUrl = Iterables.getLast(urls);
        }

        // http://stackoverflow.com/questions/11133759/0000-00-00-000000-can-not-be-represented-as-java-sql-timestamp-error
//        jdbcUrl += "?zeroDateTimeBehavior=convertToNull&queryInterceptors=brave.mysql8.TracingQueryInterceptor&exceptionInterceptors=brave.mysql8.TracingExceptionInterceptor";
        jdbcUrl += "?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf8";

        int maxConnections = envConfigManager.getConfigAsInt("db." + source + ".max_connections");
        int connectionTimeout = envConfigManager.getConfigAsInt("db." + source + ".connection_timeout");

        HikariDataSource dataSource = setupConnectionPool(source + "Pool", jdbcUrl, dbUser, dbPw, maxConnections, connectionTimeout);

        DefaultConfiguration jooqConfiguration = new DefaultConfiguration();
        DataSourceConnectionProvider connProvider = new DataSourceConnectionProvider(dataSource);
        jooqConfiguration.set(connProvider);
        jooqConfiguration.set(SQLDialect.MYSQL);

        return jooqConfiguration;
    }

    private HikariDataSource setupConnectionPool(String poolName, String jdbcUrl, String dbUser, String dbPw, int maxConnections, int connectionTimeout) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(dbUser);
        config.setPassword(dbPw);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setMaximumPoolSize(maxConnections);
        config.setPoolName(poolName);
        config.setConnectionTimeout(connectionTimeout);
        return new HikariDataSource(config);
    }

    public DefaultConfiguration getJooqConfiguration() {
        return jooqConfiguration;
    }
}

