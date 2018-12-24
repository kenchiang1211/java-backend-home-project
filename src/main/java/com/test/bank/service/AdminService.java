package com.test.bank.service;

import com.test.bank.initializer.DataSourceInitializer;
import com.test.bank.model.admin.AdminUserVo;
import org.jooq.impl.DefaultConfiguration;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AdminService {

    DefaultConfiguration jooqConfiguration;

    @Inject
    public AdminService(DataSourceInitializer dataSourceInitializer) {
        this.jooqConfiguration = dataSourceInitializer.getJooqConfiguration();
    }

    public AdminUserVo login(String account, String password) {
        // TODO implement login with PasswordUtils
        return null;
    }

}
