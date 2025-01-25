package com.verdantartifice.primalmagick.test.research;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.test.TestUtilsNeoforge;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(Constants.MOD_ID)
public class ResearchRequirementsTestNeoforge extends AbstractResearchRequirementsTest {
    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void research_requirement(GameTestHelper helper) {
        super.research_requirement(helper);
    }

    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void knowledge_requirement(GameTestHelper helper) {
        super.knowledge_requirement(helper);
    }

    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void item_stack_requirement(GameTestHelper helper) {
        super.item_stack_requirement(helper);
    }

    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void item_tag_requirement(GameTestHelper helper) {
        super.item_tag_requirement(helper);
    }

    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void stat_requirement(GameTestHelper helper) {
        super.stat_requirement(helper);
    }

    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void expertise_requirement(GameTestHelper helper) {
        super.expertise_requirement(helper);
    }

    @PrefixGameTestTemplate(false)
    @GameTest(template = "test/floor5x5x5")
    @Override
    public void vanilla_item_used_stat_requirement(GameTestHelper helper) {
        super.vanilla_item_used_stat_requirement(helper);
    }

    @PrefixGameTestTemplate(false)
    @GameTest(template = "test/floor5x5x5")
    @Override
    public void vanilla_custom_stat_requirement(GameTestHelper helper) {
        super.vanilla_custom_stat_requirement(helper);
    }

    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void and_requirement(GameTestHelper helper) {
        super.and_requirement(helper);
    }

    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void or_requirement(GameTestHelper helper) {
        super.or_requirement(helper);
    }

    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void quorum_requirement(GameTestHelper helper) {
        super.quorum_requirement(helper);
    }
}
