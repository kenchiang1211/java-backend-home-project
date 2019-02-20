package com.test.bank.dagger;

import com.test.bank.tool.config.EnvConfigManager;
import com.typesafe.config.ConfigFactory;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class BootstrapModule {

    public BootstrapModule() {
    }

    @Provides
    @Singleton
    public EnvConfigManager envConfigManager() {
        return new EnvConfigManager(ConfigFactory.load());
    }

}
