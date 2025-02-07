package com.verdantartifice.primalmagick.test.research;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.test.TestUtilsForge;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;

@GameTestHolder(Constants.MOD_ID + ".forge.research_keys")
public class ResearchKeysTestForge extends AbstractResearchKeysTest {
    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void entity_scan(GameTestHelper helper) {
        super.entity_scan(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void item_scan(GameTestHelper helper) {
        super.item_scan(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void research_discipline(GameTestHelper helper) {
        super.research_discipline(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void research_entry(GameTestHelper helper) {
        super.research_entry(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void research_stage(GameTestHelper helper) {
        super.research_stage(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void rune_enchantment(GameTestHelper helper) {
        super.rune_enchantment(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void rune_enchantment_partial(GameTestHelper helper) {
        super.rune_enchantment_partial(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void stack_crafted(GameTestHelper helper) {
        super.stack_crafted(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void tag_crafted(GameTestHelper helper) {
        super.tag_crafted(helper);
    }
}
