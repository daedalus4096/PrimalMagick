package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.platform.services.ITestService;
import net.minecraftforge.gametest.GameTestHolder;

public class TestServiceForge implements ITestService {
    @Override
    public String getTestNamespace(Class<?> testClazz) {
        GameTestHolder holder = testClazz.getAnnotation(GameTestHolder.class);
        if (holder != null && !holder.value().isEmpty()) {
            return holder.value();
        } else {
            return null;
        }
    }
}
