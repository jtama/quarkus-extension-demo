package org.acme.configurationProvider.runtime;

import io.quarkus.runtime.annotations.Recorder;
import org.jboss.logging.Logger;

import java.util.List;

@Recorder
public class ThisIsNotRestLogger {

    private static final Logger logger = Logger.getLogger(ThisIsNotRestLogger.class);

    public void youAreNotDoingREST(List<String> warnings) {
        logger.errorf("%s******** You should Listen *********%s%s%s%s",
                System.lineSeparator(),
                System.lineSeparator(),
                String.join(System.lineSeparator(), warnings),
                System.lineSeparator(),
                "If I had been stricter I would have changed your code...");
    }

}
