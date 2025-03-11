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
    public void player_knowledge_add_and_check_research(GameTestHelper helper) {
        super.player_knowledge_add_and_check_research(helper);
    }

    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_cannot_add_duplicate_research(GameTestHelper helper) {
        super.player_knowledge_cannot_add_duplicate_research(helper);
    }

    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_remove_research(GameTestHelper helper) {
        super.player_knowledge_remove_research(helper);
    }

    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_get_research_set(GameTestHelper helper) {
        super.player_knowledge_get_research_set(helper);
    }

    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_get_set_research_stage(GameTestHelper helper) {
        super.player_knowledge_get_set_research_stage(helper);
    }

    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_get_set_research_flag(GameTestHelper helper) {
        super.player_knowledge_get_set_research_flag(helper);
    }

    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_remove_research_flag(GameTestHelper helper) {
        super.player_knowledge_remove_research_flag(helper);
    }

    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_get_research_flags(GameTestHelper helper) {
        super.player_knowledge_get_research_flags(helper);
    }

    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_get_research_status(GameTestHelper helper) {
        super.player_knowledge_get_research_status(helper);
    }

    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_is_research_complete(GameTestHelper helper) {
        super.player_knowledge_is_research_complete(helper);
    }

    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_get_set_knowledge_raw(GameTestHelper helper) {
        super.player_knowledge_get_set_knowledge_raw(helper);
    }

    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_get_knowledge_levels(GameTestHelper helper) {
        super.player_knowledge_get_knowledge_levels(helper);
    }

    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_get_set_active_research_project(GameTestHelper helper) {
        super.player_knowledge_get_set_active_research_project(helper);
    }

    @PrefixGameTestTemplate(false)
    @GameTest(template = TestUtilsNeoforge.DEFAULT_TEMPLATE)
    @Override
    public void player_knowledge_get_set_last_research_project(GameTestHelper helper) {
        super.player_knowledge_get_set_last_research_project(helper);
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
}
