package org.acme.configurationProvider.it;

import jakarta.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import picocli.CommandLine;

@CommandLine.Command
public class Starter implements Runnable{

    @Inject
    Logger log;

    @ConfigProperty(name = "env.smetayer.title")
    String devfestConfTitle;
    @ConfigProperty(name = "env.smetayer.author")
    String devfestConfAuthor;

    @Override
    public void run() {
        log.info("");
        log.info("");
        log.info("********   WELCOME !     ********");
        log.infof("Welcome %s, that will present: %s%n",devfestConfAuthor, devfestConfTitle);
        log.info("*********************************");
        log.info("");
        log.info("");
    }
}
