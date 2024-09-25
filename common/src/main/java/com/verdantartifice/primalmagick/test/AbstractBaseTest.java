package com.verdantartifice.primalmagick.test;

import net.minecraft.gametest.framework.GameTestHelper;

public abstract class AbstractBaseTest {
    @SuppressWarnings("unchecked")
    protected static <T> T assertInstanceOf(GameTestHelper helper, Object obj, Class<T> clazz, String failureMessage) {
        helper.assertTrue(clazz.isInstance(obj), failureMessage);
        return (T)obj;
    }
}
