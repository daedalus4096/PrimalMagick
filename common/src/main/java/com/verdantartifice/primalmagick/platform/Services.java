package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.platform.services.IPlatformService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ServiceLoader;

/**
 * Definition point for cross-platform services. These are used to allow common code to call
 * into platform-specific (e.g. Forge) code using Java services.
 * @author Daedalus4096
 */
public class Services {
    private static final Logger LOGGER = LogManager.getLogger();

    public static final IPlatformService PLATFORM = load(IPlatformService.class);

    // This code is used to load a service for the current environment. Your implementation of the service must be defined
    // manually by including a text file in META-INF/services named with the fully qualified class name of the service.
    // Inside the file you should write the fully qualified class name of the implementation to load for the platform.
    private static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}