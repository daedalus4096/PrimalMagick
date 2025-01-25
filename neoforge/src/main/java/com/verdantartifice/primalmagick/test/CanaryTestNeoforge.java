package com.verdantartifice.primalmagick.test;

import com.verdantartifice.primalmagick.Constants;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(Constants.MOD_ID)
public class CanaryTestNeoforge extends AbstractCanaryTest{
    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void canary(GameTestHelper helper) {
        super.canary(helper);
    }
}
