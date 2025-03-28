package com.verdantartifice.primalmagick.test.capabilities;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.test.TestUtilsForge;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;

@GameTestHolder(Constants.MOD_ID + ".forge.player_knowledge")
public class PlayerKnowledgeTestForge extends AbstractPlayerKnowledgeTest {
    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_add_and_check_research(GameTestHelper helper) {
        super.player_knowledge_add_and_check_research(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_cannot_add_duplicate_research(GameTestHelper helper) {
        super.player_knowledge_cannot_add_duplicate_research(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_remove_research(GameTestHelper helper) {
        super.player_knowledge_remove_research(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_get_research_set(GameTestHelper helper) {
        super.player_knowledge_get_research_set(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_get_set_research_stage(GameTestHelper helper) {
        super.player_knowledge_get_set_research_stage(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_get_set_research_flag(GameTestHelper helper) {
        super.player_knowledge_get_set_research_flag(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_remove_research_flag(GameTestHelper helper) {
        super.player_knowledge_remove_research_flag(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_get_research_flags(GameTestHelper helper) {
        super.player_knowledge_get_research_flags(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_get_research_status(GameTestHelper helper) {
        super.player_knowledge_get_research_status(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_is_research_complete(GameTestHelper helper) {
        super.player_knowledge_is_research_complete(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_get_set_knowledge_raw(GameTestHelper helper) {
        super.player_knowledge_get_set_knowledge_raw(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_get_knowledge_levels(GameTestHelper helper) {
        super.player_knowledge_get_knowledge_levels(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_get_set_active_research_project(GameTestHelper helper) {
        super.player_knowledge_get_set_active_research_project(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_get_set_last_research_topic(GameTestHelper helper) {
        super.player_knowledge_get_set_last_research_topic(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_get_set_research_topic_history(GameTestHelper helper) {
        super.player_knowledge_get_set_research_topic_history(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_serialization(GameTestHelper helper) {
        super.player_knowledge_serialization(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_deserialize_from_legacy_format(GameTestHelper helper) {
        super.player_knowledge_deserialize_from_legacy_format(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_add_and_check_research_post_serialization(GameTestHelper helper) {
        super.player_knowledge_add_and_check_research_post_serialization(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_schema_version(GameTestHelper helper) {
        super.player_knowledge_schema_version(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_marks_default_entries_as_read_on_upversion(GameTestHelper helper) {
        super.player_knowledge_marks_default_entries_as_read_on_upversion(helper);
    }

    @GameTest(template = TestUtilsForge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_does_not_mark_non_default_entries_as_read_on_upversion(GameTestHelper helper) {
        super.player_knowledge_does_not_mark_non_default_entries_as_read_on_upversion(helper);
    }
}
