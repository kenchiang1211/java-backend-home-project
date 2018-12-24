package com.test.bank.dagger;

import com.test.bank.MicroServiceApplication;
import com.test.bank.resource.AdminResource;
import com.test.bank.resource.TransactionResource;
import dagger.Component;

import javax.inject.Singleton;

@Component(modules = {BootstrapModule.class})
@Singleton
public interface BootstrapComponent {
    void inject(MicroServiceApplication microServiceApplication);

    AdminResource adminResource();

    TransactionResource transactionResource();

}
