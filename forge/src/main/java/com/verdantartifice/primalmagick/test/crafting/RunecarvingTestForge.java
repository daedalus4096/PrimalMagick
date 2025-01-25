package com.verdantartifice.primalmagick.test.crafting;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.test.TestUtilsForge;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;

@GameTestHolder(Constants.MOD_ID + ".forge.runecarving")
public class RunecarvingTestForge extends AbstractRunecarvingTest{
    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void craft_works(GameTestHelper helper) {
        super.craft_works(helper);
    }
}
