package org.acme.configurationProvider.devui;

import io.smallrye.common.annotation.NonBlocking;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import java.util.List;
import java.util.Map;

public class ConfigurationProviderJsonRPCService {

    Config config = ConfigProvider.getConfig();

    public String getConfigurationProviderURL() {
        return config.getValue("acme.environment.url", String.class);
    }

    public List<JsonObject> getProvidedConfigurations(List<String> expectedValues) {
        return expectedValues.stream().map(key -> new JsonObject(Map.of("key", key, "value", config.getConfigValue(key).getValue()))).toList();
    }
}
