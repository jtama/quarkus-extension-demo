package org.acme.configurationProvider.deployment.devui;

import io.quarkus.deployment.IsDevelopment;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.devui.spi.JsonRPCProvidersBuildItem;
import io.quarkus.devui.spi.page.CardPageBuildItem;
import io.quarkus.devui.spi.page.MenuPageBuildItem;
import io.quarkus.devui.spi.page.Page;
import io.quarkus.devui.spi.page.WebComponentPageBuilder;
import org.acme.configurationProvider.deployment.AcmeConfigurationBuildTimeConfiguration;
import org.acme.configurationProvider.deployment.AcmeEnvironmentBuildItem;
import org.acme.configurationProvider.devui.ConfigurationProviderJsonRPCService;

import java.util.List;
import java.util.Optional;

public class ConfigurationProviderDevUIProcessor {

    @BuildStep(onlyIf = IsDevelopment.class)
    void createCard(BuildProducer<MenuPageBuildItem> menuProducer,
                    Optional<AcmeEnvironmentBuildItem> acmeEnvironmentBuildItem,
                    AcmeConfigurationBuildTimeConfiguration buildTimeConfiguration) {
        System.out.println("greeeeeee");
        WebComponentPageBuilder environnementProviderPage = Page.webComponentPageBuilder().title("Environnement provider extension");
        // Menu
        MenuPageBuildItem menuPageBuildItem = new MenuPageBuildItem();
        menuPageBuildItem.addBuildTimeData("enabled", buildTimeConfiguration.devservices().enabled() && acmeEnvironmentBuildItem.isPresent());
        menuPageBuildItem.addBuildTimeData("strictRest", buildTimeConfiguration.strict().isRestStrict());
        menuPageBuildItem.addBuildTimeData("strictUtils", buildTimeConfiguration.strict().isUtilsStrict());
        menuPageBuildItem.addPage(environnementProviderPage
                .icon("font-awesome-solid:")
                .title("Acme Environment")
                .componentLink("qwc-configuration-provider-card.js"));
        acmeEnvironmentBuildItem.ifPresentOrElse(
                item -> {
                    environnementProviderPage
                            .icon("font-awesome-solid:hands-asl-interpreting");
                    menuPageBuildItem.addBuildTimeData("expectedEnv", item.getExpectedEnvProps());
                },
                () -> {
                    environnementProviderPage
                        .icon("font-awesome-solid:biohazard");
                    menuPageBuildItem.addBuildTimeData("expectedEnv", List.of());
        });
        menuProducer.produce(menuPageBuildItem);
    }

    @BuildStep(onlyIf = IsDevelopment.class)
    JsonRPCProvidersBuildItem createJsonRPCServiceForCache() {
        return new JsonRPCProvidersBuildItem(ConfigurationProviderJsonRPCService.class);
    }

}
