package com.verdantartifice.primalmagick.test.crafting;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.test.TestUtilsForge;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;

@GameTestHolder(Constants.MOD_ID + ".forge.arcane_workbench")
public class ArcaneWorkbenchTestForge extends AbstractArcaneWorkbenchTest {
    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void arcane_workbench_craft_works(GameTestHelper helper) {
        super.arcane_workbench_craft_works(helper);
    }
}
