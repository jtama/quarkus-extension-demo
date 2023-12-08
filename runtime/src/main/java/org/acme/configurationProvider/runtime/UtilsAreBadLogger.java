package org.acme.configurationProvider.runtime;

import io.quarkus.runtime.annotations.Recorder;
import org.jboss.logging.Logger;

import java.util.List;

@Recorder
public class UtilsAreBadLogger {

    private static final Logger logger = Logger.getLogger(UtilsAreBadLogger.class);

    public void youAreNotDoingREST(List<String> warnings) {
        logger.error(String.join(System.lineSeparator(), warnings));

        logger.error("If I had been stricter I would have changed your code...");
    }

}
