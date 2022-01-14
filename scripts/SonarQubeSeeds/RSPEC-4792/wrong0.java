
// === Log4J 2 ===
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.config.*;

// Sensitive: creating a new custom configuration
abstract class CustomConfigFactory extends ConfigurationFactory {
    // ...
}

class A {
    void foo(Configuration config, LoggerContext context, java.util.Map<String, Level> levelMap,
            Appender appender, java.io.InputStream stream, java.net.URI uri,
            java.io.File file, java.net.URL url, String source, ClassLoader loader, Level level, Filter filter)
            throws java.io.IOException {
        // Creating a new custom configuration
        ConfigurationBuilderFactory.newConfigurationBuilder();  // Sensitive

        // Setting loggers level can result in writing sensitive information in production
        Configurator.setAllLevels("com.example", Level.DEBUG);  // Sensitive
        Configurator.setLevel("com.example", Level.DEBUG);  // Sensitive
        Configurator.setLevel(levelMap);  // Sensitive
        Configurator.setRootLevel(Level.DEBUG);  // Sensitive

        config.addAppender(appender); // Sensitive: this modifies the configuration

        LoggerConfig loggerConfig = config.getRootLogger();
        loggerConfig.addAppender(appender, level, filter); // Sensitive
        loggerConfig.setLevel(level); // Sensitive

        context.setConfigLocation(uri); // Sensitive

        // Load the configuration from a stream or file
        new ConfigurationSource(stream);  // Sensitive
        new ConfigurationSource(stream, file);  // Sensitive
        new ConfigurationSource(stream, url);  // Sensitive
        ConfigurationSource.fromResource(source, loader);  // Sensitive
        ConfigurationSource.fromUri(uri);  // Sensitive
    }
}
