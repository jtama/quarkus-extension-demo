package io.quarkiverse.approximationcorrector.runtime;

import java.util.function.Supplier;

import org.eclipse.microprofile.config.ConfigProvider;

import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class EnvironmentRecorder {

    public Supplier<String> environmentSupplier() {
        return () -> ConfigProvider.getConfig().getValue("acme.env", String.class);
    }

}
