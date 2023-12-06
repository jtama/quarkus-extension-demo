import org.eclipse.microprofile.config.inject.ConfigProperty;

public class FooBar {
    @ConfigProperty(name = "acme.property.key")
    String value;
}
