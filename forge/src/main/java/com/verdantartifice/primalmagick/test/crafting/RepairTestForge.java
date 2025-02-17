package com.verdantartifice.primalmagick.test.crafting;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.test.TestUtilsForge;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;

@GameTestHolder(Constants.MOD_ID + ".forge.repair")
public class RepairTestForge extends AbstractRepairTest {
    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void earthshatter_hammer_cannot_be_repaired(GameTestHelper helper) {
        super.earthshatter_hammer_cannot_be_repaired(helper);
    }
}
