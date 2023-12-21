package org.acme.configurationProvider.it;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import picocli.CommandLine;

@CommandLine.Command
public class Starter implements Runnable{


    @ConfigProperty(name = "env.snowcamp.title")
    String snowcampConfTitle;
    @ConfigProperty(name = "env.snowcamp.author")
    String snowcampConfAuthor;

    @Override
    public void run() {
        System.out.println("********   WELCOME !     ********");
        System.out.println("Welcome %s, that will present: \"%s\"%n".formatted(snowcampConfAuthor, snowcampConfTitle));
        System.out.println("*********************************");
    }
}
