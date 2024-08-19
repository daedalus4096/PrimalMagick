package com.verdantartifice.primalmagick.test.research;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;
import com.verdantartifice.primalmagick.test.TestUtils;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.GameType;
import net.minecraftforge.gametest.GameTestHolder;

@GameTestHolder(PrimalMagick.MODID + ".research_requirements")
public class ResearchRequirementsTest {
    @GameTest(template = TestUtils.DEFAULT_TEMPLATE)
    public static void research_requirement(GameTestHelper helper) {
        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        var req = new ResearchRequirement(new ResearchEntryKey(ResearchEntries.FIRST_STEPS));
        helper.assertFalse(req.isMetBy(player), "Baseline expectation failed");
        ResearchManager.forceGrantWithAllParents(player, ResearchEntries.FIRST_STEPS);
        helper.assertTrue(req.isMetBy(player), "Requirement not met");
        helper.succeed();
    }
}
