package io.quarkiverse.approximationcorrector.runtime;

import java.util.function.Supplier;

import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class EnvironmentRecorder {

    public Supplier<EnvironmentValues> environmentSupplier(String prefix,
            @SuppressWarnings("unused") EnvironmentRuntimeConfiguration environmentConfiguration) {
        return () -> {
            var values = Environments.get(prefix);
            return new EnvironmentValues(
                    values.get("%s.CONFERENCE".formatted(prefix)),
                    values.get("%s.TITLE".formatted(prefix)));
        };
    }

}
