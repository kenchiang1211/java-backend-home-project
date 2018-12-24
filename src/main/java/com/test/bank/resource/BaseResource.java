package com.test.bank.resource;


import com.test.bank.tool.config.EnvConfigManager;

public class BaseResource {
    EnvConfigManager envConfigManager;

    public BaseResource(EnvConfigManager envConfigManager) {
        this.envConfigManager = envConfigManager;
    }

    public EnvConfigManager getEnvConfigManager() {
        return envConfigManager;
    }

    public void setEnvConfigManager(EnvConfigManager envConfigManager) {
        this.envConfigManager = envConfigManager;
    }
}
