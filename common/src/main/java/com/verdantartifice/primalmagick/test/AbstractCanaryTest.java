package com.verdantartifice.primalmagick.test;

import net.minecraft.gametest.framework.GameTestHelper;

public abstract class AbstractCanaryTest extends AbstractBaseTest {
    public void canary(GameTestHelper helper) {
        helper.assertTrue(false, "Something's wrong with the universe!");
        helper.succeed();
    }
}
