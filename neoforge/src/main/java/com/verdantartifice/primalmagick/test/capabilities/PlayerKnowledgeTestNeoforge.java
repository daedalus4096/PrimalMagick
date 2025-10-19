package com.verdantartifice.primalmagick.test.capabilities;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.test.TestUtilsNeoforge;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(Constants.MOD_ID)
public class PlayerKnowledgeTestNeoforge extends AbstractPlayerKnowledgeTest {
    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_get_set_active_research_project(GameTestHelper helper) {
        super.player_knowledge_get_set_active_research_project(helper);
    }

    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_get_set_last_research_topic(GameTestHelper helper) {
        super.player_knowledge_get_set_last_research_topic(helper);
    }

    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_get_set_research_topic_history(GameTestHelper helper) {
        super.player_knowledge_get_set_research_topic_history(helper);
    }

    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_serialization(GameTestHelper helper) {
        super.player_knowledge_serialization(helper);
    }

    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_deserialize_from_legacy_format(GameTestHelper helper) {
        super.player_knowledge_deserialize_from_legacy_format(helper);
    }

    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_add_and_check_research_post_serialization(GameTestHelper helper) {
        super.player_knowledge_add_and_check_research_post_serialization(helper);
    }

    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_schema_version(GameTestHelper helper) {
        super.player_knowledge_schema_version(helper);
    }

    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_marks_default_entries_as_read_on_upversion(GameTestHelper helper) {
        super.player_knowledge_marks_default_entries_as_read_on_upversion(helper);
    }

    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_does_not_mark_non_default_entries_as_read_on_upversion(GameTestHelper helper) {
        super.player_knowledge_does_not_mark_non_default_entries_as_read_on_upversion(helper);
    }
}
