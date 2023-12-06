package org.acme.configurationProvider.runtime;

import org.eclipse.microprofile.config.spi.ConfigSource;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;

public class AcmeConfigSource implements ConfigSource {

    private final EnvironmentProviderClient environmentProviderClient;

    private final Pattern patternMatcher;

    private final Predicate<String> isAcme = key -> key.startsWith("acme.");

    private final Predicate<String> isProviderConfiguration = key -> key.equals("acme.environment.url");


    public AcmeConfigSource(EnvironmentProviderClient environmentProviderClient) {
        String pattern = "acme\\.(?<env>.*)\\.(?<key>.*)";

        // Create a Pattern object
        patternMatcher = Pattern.compile(pattern);
        this.environmentProviderClient = environmentProviderClient;
    }

    /**
     * always return an empty map to protect from accidental properties logging
     *
     * @return empty map
     */
    @Override
    public Map<String, String> getProperties() {
        return emptyMap();
    }

    @Override
    public Set<String> getPropertyNames() {
        return emptySet();
    }

    @Override
    public int getOrdinal() {
        return ConfigSource.super.getOrdinal();
    }

    @Override
    public String getValue(String propertyName) {
        if (Predicate.not(isAcme)
                .or(isProviderConfiguration)
                .test(propertyName))
            return null;

        // Now create matcher object.
        Matcher m = patternMatcher.matcher(propertyName);

        if (m.find()) {
            Map<String, String> env = environmentProviderClient.getEnvironment(m.group("env"));
            if (env != null) {
                return env.get(m.group("key"));
            }
        }
        return null;
    }

    @Override
    public String getName() {
        return "AcmeConfigSource";
    }
}
