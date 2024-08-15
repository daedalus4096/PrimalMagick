package com.verdantartifice.primalmagick.test;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;

@GameTestHolder(PrimalMagick.MODID)
public class CanaryTest {
    @GameTest(template = "primalmagick:test/empty3x3x3")
    public static void canary(GameTestHelper helper) {
        helper.assertTrue(true, "Something's wrong with the universe!");
        helper.succeed();
    }
}
