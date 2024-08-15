package com.verdantartifice.primalmagick.test;

import java.util.Locale;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;

import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.StructureUtils;
import net.minecraft.gametest.framework.TestFunction;

public class TestUtils {
    public static String getCurrentMethodName() {
        return StackWalker.getInstance().walk(s -> s.skip(1).findFirst()).map(f -> f.getMethodName()).orElse(null);
    }
    
    public static String getCallerMethodName() {
        return StackWalker.getInstance().walk(s -> s.skip(2).findFirst()).map(f -> f.getMethodName()).orElse(null);
    }
    
    public static TestFunction createNestedTestFunction(String nameSuffix, TestOptions options, Consumer<GameTestHelper> consumer) {
//        String callerName = getCallerMethodName();
        String callerName = "generated";
        String finalName = (callerName == null ? nameSuffix : String.join(".", callerName, nameSuffix)).toLowerCase(Locale.ENGLISH);
        LogManager.getLogger().debug("Creating nested test function with name: {}", finalName);
        return new TestFunction(
            options.batch(),
            finalName,
            options.template(),
            StructureUtils.getRotationForRotationSteps(options.rotationSteps()),
            options.timeoutTicks(),
            options.setupTicks(),
            options.required(),
            options.manualOnly(),
            options.requiredSuccesses(),
            options.attempts(),
            options.skyAccess(),
            consumer
        );
    }
}
