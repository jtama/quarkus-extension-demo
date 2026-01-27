package org.acme.configurationProvider.deployment.devservice;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

import org.acme.configurationProvider.deployment.AcmeConfigurationBuildTimeConfiguration;
import org.acme.configurationProvider.deployment.AcmeConfigurationProviderProcessor;
import org.acme.configurationProvider.deployment.AcmeEnvironmentBuildItem;
import org.jboss.logging.Logger;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.utility.DockerImageName;

import io.quarkus.deployment.IsNormal;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.ContainerRuntimeStatusBuildItem;
import io.quarkus.deployment.builditem.DevServicesResultBuildItem;
import io.quarkus.deployment.builditem.DockerStatusBuildItem;
import io.quarkus.deployment.builditem.LaunchModeBuildItem;
import io.quarkus.deployment.builditem.Startable;
import io.quarkus.deployment.dev.devservices.DevServicesConfig;
import io.quarkus.devservices.common.ConfigureUtil;
import io.quarkus.devservices.common.ContainerAddress;
import io.quarkus.devservices.common.ContainerLocator;
import io.quarkus.runtime.configuration.ConfigUtils;

public class DevServicesProcessor {

    /**
     * Label to add to shared Dev Service for acme-env running in containers.
     * This allows other applications to discover the running service and use it instead of starting a new instance.
     */
    static final String DEV_SERVICE_LABEL = "acme-env-dev-service";
    static final int ACME_PORT = 8080;
    private static final Logger LOGGER = Logger.getLogger(DevServicesProcessor.class);
    private static final String ACME_ENV_URL_KEY = "acme.environment.url";
    private static final ContainerLocator acmeEnvContainerLocator = new ContainerLocator(DEV_SERVICE_LABEL, ACME_PORT);
    static volatile DevServicesResultBuildItem.RunningDevService devService;

    static volatile boolean first = true;

    @BuildStep(onlyIfNot = IsNormal.class, onlyIf = DevServicesConfig.Enabled.class)
    public DevServicesResultBuildItem startEnvProviderDevService(
            DockerStatusBuildItem dockerStatusBuildItem,
            LaunchModeBuildItem launchMode,
            AcmeConfigurationBuildTimeConfiguration buildTimeConfiguration,
            AcmeEnvironmentBuildItem acmeEnvironmentBuildItem,
            DevServicesConfig devServicesConfig) {
        if (isDevserviceDisabled(buildTimeConfiguration.devservices(), acmeEnvironmentBuildItem, dockerStatusBuildItem)) {
            return null;
        }

        final Optional<ContainerAddress> maybeContainerAddress = acmeEnvContainerLocator.locateContainer(DEV_SERVICE_LABEL,
                true,
                launchMode.getLaunchMode());

        return maybeContainerAddress
                .map(containerAddress -> DevServicesResultBuildItem.discovered()
                        .name(AcmeConfigurationProviderProcessor.FEATURE)
                        .containerId(containerAddress.getId())
                        .config(Map.of(ACME_ENV_URL_KEY,
                                "http://%s:%d".formatted(containerAddress.getHost(), containerAddress.getPort())))
                        .build())
                .orElseGet(() -> DevServicesResultBuildItem.owned()
                        .feature(AcmeConfigurationProviderProcessor.FEATURE)
                        .startable(() -> startAcmeEnv(devServicesConfig.timeout(), buildTimeConfiguration.devservices()))
                        .configProvider(Map.of(ACME_ENV_URL_KEY, AcmeEnvContainer::getConnectionInfo))
                        .postStartHook(this::logStarted)
                        .build());
    }

    private void logStarted(AcmeEnvContainer container) {
        LOGGER.infof("Dev Services for Acme Env started on %s", container.getConnectionInfo());
        LOGGER.infof("Other Quarkus applications in dev mode will find the "
                + "instance automatically. For Quarkus applications in production mode, you can connect to"
                + " this by starting your application with -D%s=%s",
                ACME_ENV_URL_KEY, container.getConnectionInfo());
    }

    private Object getAcmeEnvUrl() {
        return devService.getConfig().get(ACME_ENV_URL_KEY);
    }

    private Boolean isDevserviceDisabled(AcmeDevServicesConfig devServicesConfig,
            AcmeEnvironmentBuildItem acmeEnvironmentBuildItem,
            ContainerRuntimeStatusBuildItem dockerStatusBuildItem) {
        if (!devServicesConfig.enabled()) {
            // explicitly disabled
            LOGGER.debug("Not starting dev services for AcmeEnv, as it has been disabled in the config.");
            return true;
        }

        // Check if quarkus.environment.url is set
        if (acmeEnvironmentBuildItem == null) {
            LOGGER.debug("We said you should remove the extension");
            return true;
        }

        // Check if acme.environment.url is set
        if (ConfigUtils.isPropertyPresent(ACME_ENV_URL_KEY)) {
            LOGGER.debugf("Not starting dev services for AcmeEnv, the % is configured.", ACME_ENV_URL_KEY);
            return true;
        }

        if (!dockerStatusBuildItem.isContainerRuntimeAvailable()) {
            LOGGER.warnf("Docker isn't working, please configure the AcmeEnv Url property (%s).", ACME_ENV_URL_KEY);
            return true;
        }
        return false;
    }

    private AcmeEnvContainer startAcmeEnv(
            Optional<Duration> timeout,
            AcmeDevServicesConfig devServicesConfig) {

        AcmeEnvContainer container = new AcmeEnvContainer(DockerImageName.parse(devServicesConfig.imageName()));

        ConfigureUtil.configureSharedNetwork(container, "acmeEnv");
        container.withLabel(DEV_SERVICE_LABEL, DEV_SERVICE_LABEL);
        container.addEnv("CONFERENCE_maryCoreTech_TITLE", "A mandonné, ça fatigue !");
        container.addEnv("CONFERENCE_maryCoreTech_AUTHOR", "Myra Cool");
        container.addEnv("CONFERENCE_daminouTech_TITLE", "Tchou la bise !");
        container.addEnv("CONFERENCE_daminouTech_AUTHOR", "Super Daminou");
        container.addEnv("CONFERENCE_rivieraDev_TITLE", "Quarkus: Greener, Better, Faster, Stronger");
        container.addEnv("CONFERENCE_rivieraDev_AUTHOR", "j.tama");
        container.addEnv("CONFERENCE_devoxxFR_AUTHOR", "Hubert Sablonnière ❤");
        container.addEnv("CONFERENCE_devoxxFR_TITLE", "#RetourAuxSources : Le cache HTTP");
        timeout.ifPresent(container::withStartupTimeout);

        return container;
    }

    /**
     * Container configuring and starting the Minio server.
     */
    private static final class AcmeEnvContainer extends GenericContainer<AcmeEnvContainer> implements Startable {

        private AcmeEnvContainer(DockerImageName dockerImageName) {
            super(dockerImageName);
            withNetwork(Network.SHARED);
            withExposedPorts(ACME_PORT);
        }

        public int getPort() {
            return getMappedPort(ACME_PORT);
        }

        @Override
        public String getConnectionInfo() {
            return "http://%s:%s".formatted(getHost(), getPort());
        }

        @Override
        public void close() {
            super.close();
        }
    }
}
