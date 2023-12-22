package org.acme.configurationProvider.it;

import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import picocli.CommandLine;

@CommandLine.Command
public class Starter implements Runnable{

    @Inject
    Logger log;

    @ConfigProperty(name = "env.snowcamp.title")
    String snowcampConfTitle;
    @ConfigProperty(name = "env.snowcamp.author")
    String snowcampConfAuthor;

    @Override
    public void run() {
        log.info("********   WELCOME !     ********");
        log.infof("Welcome %s, that will present: \"%s\"%n",snowcampConfAuthor, snowcampConfTitle);
        log.info("*********************************");
    }
}
