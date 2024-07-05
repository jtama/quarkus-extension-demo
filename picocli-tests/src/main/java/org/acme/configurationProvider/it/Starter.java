package org.acme.configurationProvider.it;

import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import picocli.CommandLine;

@CommandLine.Command
public class Starter implements Runnable{

    @Inject
    Logger log;

    @ConfigProperty(name = "env.rivieradev.title")
    String rivieraDevConfTitle;
    @ConfigProperty(name = "env.rivieradev.author")
    String rivieraDevConfAuthor;

    @Override
    public void run() {
        log.info("********   WELCOME !     ********");
        log.infof("Welcome %s, that will present: \"%s\"%n",rivieraDevConfAuthor, rivieraDevConfTitle);
        log.info("*********************************");
    }
}
