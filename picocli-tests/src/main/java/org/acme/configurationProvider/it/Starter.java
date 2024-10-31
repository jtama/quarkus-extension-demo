package org.acme.configurationProvider.it;

import jakarta.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import picocli.CommandLine;

@CommandLine.Command
public class Starter implements Runnable{

    @Inject
    Logger log;

    @ConfigProperty(name = "env.devfest.title")
    String devfestConfTitle;
    @ConfigProperty(name = "env.devfest.author")
    String devfestConfAuthor;

    @Override
    public void run() {
        System.out.println();
        System.out.println(pinkify("********   WELCOME !     ********"));
        System.out.println(purplefy("Welcome %s, that will present: \"%s\"".formatted(devfestConfAuthor, devfestConfTitle)));
        System.out.println(pinkify("*********************************"));
        System.out.println();
    }

    private String pinkify(String message){
        return ConsoleColors.PINK + message + ConsoleColors.RESET;
    }

    private String purplefy(String message){
        return ConsoleColors.PURPLE + message + ConsoleColors.RESET;
    }
}
