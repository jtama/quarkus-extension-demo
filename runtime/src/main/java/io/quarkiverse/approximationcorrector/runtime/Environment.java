package io.quarkiverse.approximationcorrector.runtime;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.inject.Qualifier;

/**
 * Allow environment injection as a <code>Map<String,String</code> value for fields and parameters.
 *
 */
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
@Documented
@Qualifier
public @interface Environment {

    String value() default "acme";

    class EnvironmentPrefixLiteral extends AnnotationLiteral<Environment> implements Environment {

        private String prefix;

        public EnvironmentPrefixLiteral(String prefix) {
            this.prefix = prefix;
        }

        public String value() {
            return prefix;
        }
    }

}
