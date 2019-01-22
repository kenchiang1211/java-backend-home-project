package com.test.bank.service;

import com.sun.prism.impl.Disposer;
import com.test.bank.db.tables.records.AdminRecord;
import com.test.bank.db.tables.records.TransactionRecord;
import com.test.bank.db.tables.records.UserRecord;
import com.test.bank.initializer.DataSourceInitializer;
import com.test.bank.model.transaction.TransactionVo;
import com.test.bank.db.tables.Transaction;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.DSLContext;
import org.jooq.Record6;
import org.jooq.impl.DSL;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.jooq.types.UInteger;
import java.util.List;

import java.sql.Timestamp;

import static com.test.bank.db.tables.Transaction.TRANSACTION;
import static com.test.bank.db.tables.User.USER;
import static com.test.bank.db.tables.Admin.ADMIN;


@Singleton
public class TransactionService {

    DefaultConfiguration jooqConfiguration;
    DSLContext dslContext;
    TransactionVo transactionVo;

    @Inject
    public TransactionService(DataSourceInitializer dataSourceInitializer) {
        this.jooqConfiguration = dataSourceInitializer.getJooqConfiguration();
        this.dslContext = DSL.using(this.jooqConfiguration);;
    }

    public void transfer(int fromUserId, int toUserId, int amount, int adminId){
        // TODO implement transfer
        UserRecord fromUserRecord = this.dslContext.fetchOne(USER, USER.ID.eq(UInteger.valueOf(fromUserId)));
        if (fromUserRecord.getId() == null) {
            //throw new Exception("From User Not Exist");
            System.out.println("From User not Exist");
            return;
        }
        if (fromUserRecord.getWallet() < amount){
            //throw new Exception("amount is not enough");
            System.out.println("Amount is not enough");
            return;
        }
        UserRecord toUserRecord = this.dslContext.fetchOne(USER, USER.ID.eq(UInteger.valueOf(toUserId)));
        if(toUserRecord.getId() == null){
            //throw new Exception("To user not exist");
            System.out.println("To User ID not Exist");
            return;
        }

        if(!this.isAdminExist(adminId)){
            //throw new Exception("Admin ID not Exist");
            System.out.println("Admin ID not Exist");
            return;
        }

        //shall start transaction here, and rollback when commit fail
        TransactionRecord transactionRecord = this.dslContext.newRecord(TRANSACTION);
        transactionRecord.setAction((byte)0); //not sure the value
        transactionRecord.setFromuserid(UInteger.valueOf(fromUserId));
        transactionRecord.setTouserid(UInteger.valueOf(toUserId));
        transactionRecord.setAdminid(UInteger.valueOf(adminId));
        //transactionRecord.setCreatedat(date.getTime());
        transactionRecord.store();

        fromUserRecord.setWallet(fromUserRecord.getWallet()-amount);
        toUserRecord.setWallet(toUserRecord.getWallet()+amount);
        fromUserRecord.store();
        toUserRecord.store();

    }

    public TransactionVo getTransactionLog(int userId) {
        // TODO implement getTransactionLog
        // id, fromuserid, touserid, action, adminid, createdate
        /*List<Record6<UInteger, UInteger, UInteger, Byte, UInteger, Timestamp>> r =
                dslContext.select()
                .from(TRANSACTION)
                .where(TRANSACTION.FROMUSERID.eq(UInteger.valueOf(userId)).or(TRANSACTION.TOUSERID.eq(UInteger.valueOf(userId))))
                .fetch();*/
        return null;
    }

    public void creditAndDebit(int userId, int amount, int adminId) {
        // TODO implement creditAndDebit
        UserRecord userRecord = this.dslContext.fetchOne(USER, USER.ID.eq(UInteger.valueOf(userId)));
        if (userRecord.getId() == null) {
            System.out.println("From User not Exist");
            return;
        }
        if(amount < 0 && (userRecord.getWallet() + amount)<0){
            System.out.println("Wallet not enough");
            return;
        }

        if(!this.isAdminExist(adminId)){
            System.out.println("Admin ID not Exist");
            return;
        }
        userRecord.setWallet( userRecord.getWallet() + amount );
        userRecord.store();

        TransactionRecord transactionRecord = this.dslContext.newRecord(TRANSACTION);
        int action = (amount>0)?1:2;
        transactionRecord.setAction((byte)action);
        transactionRecord.setTouserid(UInteger.valueOf(userId));
        transactionRecord.setAdminid(UInteger.valueOf(adminId));
        //transactionRecord.setCreatedat(date.getTime());
        transactionRecord.store();

    }


    private boolean isAdminExist(int adminId){
        AdminRecord adminRecord = this.dslContext.fetchOne(ADMIN, ADMIN.ID.eq(UInteger.valueOf(adminId)));
        if( adminRecord.getId() == null ){
            return false;
        }
        return true;
    }
}
