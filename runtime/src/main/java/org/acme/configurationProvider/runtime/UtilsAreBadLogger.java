package org.acme.configurationProvider.runtime;

import io.quarkus.runtime.annotations.Recorder;
import org.jboss.logging.Logger;

import java.util.List;

@Recorder
public class UtilsAreBadLogger {

    private static final Logger logger = Logger.getLogger(UtilsAreBadLogger.class);

    public void nope(List<String> warnings) {
        if (warnings.isEmpty()) {
            return;
        }
        logger.error("💀 I told you not do this!");
        logger.errorf("💀 Would have I been stricter your code would have panic at runtime. Here : %s%s", System.lineSeparator(), String.join(System.lineSeparator(), warnings));
    }

}
