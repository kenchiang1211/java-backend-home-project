package com.test.bank.dagger;

import com.test.bank.MicroServiceApplication;
import dagger.Component;

import javax.inject.Singleton;

@Component(modules = {BootstrapModule.class})
@Singleton
public interface BootstrapComponent {
    void inject(MicroServiceApplication microServiceApplication);

}
