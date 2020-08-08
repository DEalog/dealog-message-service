package de.dealog.msg;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import org.jboss.logging.Logger;

@ApplicationScoped
public class AppLifecycleBean {

    private static final Logger LOGGER = Logger.getLogger("ListenerBean");

    void onStart(@Observes final StartupEvent ev) {
        LOGGER.info("DEalog message service is starting...");
    }

    void onStop(@Observes final ShutdownEvent ev) {
        LOGGER.info("DEalog message service is stopping...");
    }
}