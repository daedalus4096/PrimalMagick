package com.verdantartifice.primalmagick.test.research;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.test.TestUtilsForge;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;

@GameTestHolder(Constants.MOD_ID + ".forge.research_requirements")
public class ResearchRequirementsTestForge extends AbstractResearchRequirementsTest {
    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void and_requirement(GameTestHelper helper) {
        super.and_requirement(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void expertise_requirement(GameTestHelper helper) {
        super.expertise_requirement(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void item_stack_requirement(GameTestHelper helper) {
        super.item_stack_requirement(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void item_tag_requirement(GameTestHelper helper) {
        super.item_tag_requirement(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void knowledge_requirement(GameTestHelper helper) {
        super.knowledge_requirement(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void or_requirement(GameTestHelper helper) {
        super.or_requirement(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void quorum_requirement(GameTestHelper helper) {
        super.quorum_requirement(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void research_requirement(GameTestHelper helper) {
        super.research_requirement(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void stat_requirement(GameTestHelper helper) {
        super.stat_requirement(helper);
    }

    @GameTest(template = "primalmagick:test/floor5x5x5")
    @Override
    public void vanilla_custom_stat_requirement(GameTestHelper helper) {
        super.vanilla_custom_stat_requirement(helper);
    }

    @GameTest(template = "primalmagick:test/floor5x5x5")
    @Override
    public void vanilla_item_used_stat_requirement(GameTestHelper helper) {
        super.vanilla_item_used_stat_requirement(helper);
    }
}
