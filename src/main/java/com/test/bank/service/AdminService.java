package com.test.bank.service;

import com.test.bank.db.tables.Admin;
import com.test.bank.db.tables.records.AdminRecord;
import com.test.bank.initializer.DataSourceInitializer;
import com.test.bank.model.admin.AdminUserVo;
import com.test.bank.tool.PasswordUtils;
import org.jooq.DSLContext;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.SelectQuery;
import org.jooq.impl.DSL;
import javax.ws.rs.NotAuthorizedException;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.test.bank.db.tables.Admin.ADMIN;

@Singleton
public class AdminService {

    DefaultConfiguration jooqConfiguration;
    AdminUserVo adminUserVo;

    @Inject
    public AdminService(DataSourceInitializer dataSourceInitializer) {
        this.jooqConfiguration = dataSourceInitializer.getJooqConfiguration();
    }

    public AdminUserVo login(String account, String password) {
        // TODO implement login with PasswordUtils

        //shall verify user string first, 轉大小寫 驗是否存在 之類的？

        //get from DB
        DSLContext dslContext = DSL.using(this.jooqConfiguration);
        AdminRecord adminRecord = dslContext.fetchOne(ADMIN, ADMIN.ACCOUNT.eq(account));

        //verify password
        PasswordUtils pwdUtils = new PasswordUtils();
        boolean isValid = pwdUtils.verifyUserPassword(password, adminRecord.getPassword(), adminRecord.getSalt());

        if(!isValid){
            throw new NotAuthorizedException("Login is not valid");
        }

        this.adminUserVo = new AdminUserVo();
        this.adminUserVo.setId(adminRecord.getId().intValue()); // this is weird, uint->int
        this.adminUserVo.setAccount(account);
        this.adminUserVo.setPassword(password);

        return this.adminUserVo;
        //return null;
    }

}
