package com.test.bank.service;

import com.test.bank.initializer.DataSourceInitializer;
import com.test.bank.model.transaction.TransactionVo;
import org.jooq.impl.DefaultConfiguration;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TransactionService {

    DefaultConfiguration jooqConfiguration;

    @Inject
    public TransactionService(DataSourceInitializer dataSourceInitializer) {
        this.jooqConfiguration = dataSourceInitializer.getJooqConfiguration();
    }

    public void transfer(int fromUserId, int toUserId, int amount, int adminId) {
        // TODO implement transfer
    }

    public TransactionVo getTransactionLog(int userId) {
        // TODO implement getTransactionLog
        return null;
    }

    public void creditAndDebit(int userId, int amount, int adminId) {
        // TODO implement creditAndDebit
    }
}
