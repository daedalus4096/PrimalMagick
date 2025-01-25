package com.verdantartifice.primalmagick.test.research;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.test.TestUtilsForge;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;

@GameTestHolder(Constants.MOD_ID + ".forge.research")
public class ResearchTestForge extends AbstractResearchTest {
    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void research_grant_works(GameTestHelper helper) {
        super.research_grant_works(helper);
    }
}
