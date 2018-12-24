package com.test.bank.initializer;

import com.test.bank.dagger.BootstrapComponent;
import io.dropwizard.setup.Environment;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class JerseyInitializer {

    BootstrapComponent bootstrapComponent;

    public JerseyInitializer(BootstrapComponent bootstrapComponent) {
        this.bootstrapComponent = bootstrapComponent;
    }

    public void initialize(Environment environment) {
        Arrays.stream(bootstrapComponent.getClass().getMethods()).forEach(method -> {
            if (method.getName().toLowerCase().contains("resource")) {
                try {
                    environment.jersey().register(method.invoke(bootstrapComponent));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
