package com.verdantartifice.primalmagick.test.crafting;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.test.TestUtilsNeoforge;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(Constants.MOD_ID)
public class RepairTestNeoforge extends AbstractRepairTest {
    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void earthshatter_hammer_cannot_be_repaired(GameTestHelper helper) {
        super.earthshatter_hammer_cannot_be_repaired(helper);
    }
}
