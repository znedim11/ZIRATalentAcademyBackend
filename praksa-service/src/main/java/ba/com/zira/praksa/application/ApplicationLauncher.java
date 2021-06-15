package ba.com.zira.praksa.application;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import ba.com.zira.praksa.configuration.ApplicationConfiguration;

public class ApplicationLauncher implements Daemon {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationLauncher.class);

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init(final DaemonContext context) throws DaemonInitException {
        try {
            LOGGER.info("Initializing application");
            applicationContext = SpringApplication.run(ApplicationConfiguration.class);
        } catch (Exception e) {
            if (context != null) {
                context.getController().shutdown();
            }
        }
    }

    @Override
    public void start() throws Exception {
        LOGGER.info("Application started successfully");
    }

    @Override
    public void stop() throws Exception {
        applicationContext.stop();
    }

    @Override
    public void destroy() {
        LOGGER.info("Destroying application");
    }

    public static void main(final String[] args) {
        SpringApplication.run(ApplicationConfiguration.class);
    }

}
