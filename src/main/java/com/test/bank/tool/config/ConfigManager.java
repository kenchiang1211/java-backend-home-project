package com.test.bank.tool.config;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.typesafe.config.Config;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

class ConfigManager {
    private Config config;

    public ConfigManager() {
    }

    public ConfigManager(Config config) {
        this.config = config;
    }

    public boolean hasPath(String key) {
        return config.hasPath(key);
    }

    public int getConfigAsInt(String key) {
        return config.getInt(key);
    }

    public long getConfigAsLong(String key) {
        return config.getLong(key);
    }

    public boolean getConfigAsBoolean(String key) {
        return config.getBoolean(key);
    }

    public Set<String> getConfigAsSet(String key) {
        return new HashSet<>(config.getStringList(key));
    }

    public Set<String> getConfigAsCSV(String key) {
        return ImmutableSet.copyOf(Splitter.on(",").split(config.getString(key)));
    }

    public String getConfigAsString(String key) {
        return config.getString(key);
    }

    public String getEnv() {
        return config.getString("env");
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }

    public String addPrefix(String prefix, String key) {
        return prefix + key;
    }

    public String getConfigAsString(String key, String defaultValue) {
        String configAsString = getConfigAsString(key);
        if (configAsString != null && StringUtils.isNotBlank(configAsString)) {
            return configAsString;
        }
        return defaultValue;
    }

    public int getConfigAsInt(String key, int defaultValue) {
        String configAsString = getConfigAsString(key);
        if (configAsString != null) {
            return Integer.parseInt(configAsString);
        }
        return defaultValue;
    }

    public long getConfigAsLong(String key, long defaultValue) {
        String configAsString = getConfigAsString(key);
        if (configAsString != null) {
            return Long.parseLong(configAsString);
        }
        return defaultValue;
    }

    public double getConfigAsDouble(String key, double defaultValue) {
        String configAsString = getConfigAsString(key);
        if (configAsString != null) {
            return Double.parseDouble(configAsString);
        }
        return defaultValue;
    }

    public boolean getConfigAsBoolean(String key, boolean defaultValue) {
        String configAsString = getConfigAsString(key);
        if (key != null && StringUtils.isNotBlank(configAsString)) {
            return Boolean.parseBoolean(configAsString);
        }
        return defaultValue;
    }

}
