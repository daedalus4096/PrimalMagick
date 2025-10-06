package com.verdantartifice.primalmagick.test;

import net.minecraft.gametest.framework.GameTestHelper;

public abstract class CanaryTest extends AbstractBaseTest {
    public static void canary(GameTestHelper helper) {
        assertTrue(helper, true, "Something's wrong with the universe!");
        helper.succeed();
    }
}
