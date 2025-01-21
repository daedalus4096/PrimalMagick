package com.verdantartifice.primalmagick.test;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;

public abstract class AbstractCanaryTest extends AbstractBaseTest {
    @GameTest(template = TestUtils.DEFAULT_TEMPLATE)
    public void canary(GameTestHelper helper) {
        helper.assertTrue(true, "Something's wrong with the universe!");
        helper.succeed();
    }
}
