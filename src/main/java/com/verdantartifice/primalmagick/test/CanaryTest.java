package com.verdantartifice.primalmagick.test;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;

@GameTestHolder(PrimalMagick.MODID)
public class CanaryTest {
    @GameTest(template = TestUtils.DEFAULT_TEMPLATE)
    public static void canary(GameTestHelper helper) {
        helper.assertTrue(true, "Something's wrong with the universe!");
        helper.succeed();
    }
}
