package com.test.bank.service;

import com.test.bank.db.tables.records.AdminRecord;
import com.test.bank.db.tables.records.AdminroleRecord;
import com.test.bank.initializer.DataSourceInitializer;
import com.test.bank.model.admin.AdminUserVo;
import com.test.bank.tool.PasswordUtils;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.stream.Collectors;

import static com.test.bank.db.tables.Admin.ADMIN;
import static com.test.bank.db.tables.Adminrole.ADMINROLE;

@Singleton
public class AdminService {

    private final static Logger logger = LoggerFactory.getLogger(AdminService.class);

    DefaultConfiguration jooqConfiguration;

    @Inject
    public AdminService(DataSourceInitializer dataSourceInitializer) {
        this.jooqConfiguration = dataSourceInitializer.getJooqConfiguration();
    }

    public AdminUserVo login(String account, String password) {
        // TODO implement login with PasswordUtils
        AdminRecord adminRecord = DSL.using(jooqConfiguration)
                .selectFrom(ADMIN)
                .where(ADMIN.ACCOUNT.eq(account))
                .fetchAny();

        if (adminRecord != null) {
            String salt = adminRecord.getSalt();
            String securePassword = adminRecord.getPassword();
            boolean valid = PasswordUtils.verifyUserPassword(password, securePassword, salt);

            if (valid) {
                AdminUserVo adminUserVo = new AdminUserVo();
                adminUserVo.setId(adminRecord.getId().intValue());
                adminUserVo.setAccount(adminRecord.getAccount());
                adminUserVo.setPassword(adminRecord.getPassword());
                adminUserVo.setSalt(adminRecord.getSalt());

                Result<AdminroleRecord> roleRecords = DSL.using(jooqConfiguration)
                         .selectFrom(ADMINROLE)
                         .where(ADMINROLE.ADMINID.eq(adminRecord.getId()))
                         .fetch();

                if (roleRecords != null) {
                    adminUserVo.setRoleSet(roleRecords.stream().map(record -> record.get(ADMINROLE.ROLEID).toString()).collect(Collectors.toSet()));
                }
                return adminUserVo;
            } else {
                logger.info("Failed to verify user password.");
            }
        }

        return null;
    }

}
