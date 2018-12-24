package com.test.bank.dagger;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.bank.tool.config.EnvConfigManager;
import com.typesafe.config.ConfigFactory;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class BootstrapModule {
    private MetricRegistry metrics;

    public BootstrapModule(MetricRegistry metrics) {
        this.metrics = metrics;
    }

    @Provides
    @Singleton
    public MetricRegistry metricRegistry() {
        return metrics;
    }

    @Provides
    @Singleton
    public EnvConfigManager envConfigManager() {
        return new EnvConfigManager(ConfigFactory.load());
    }

    @Provides
    @Singleton
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
