package com.verdantartifice.primalmagick.test.capabilities;

import com.verdantartifice.primalmagick.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagick.common.capabilities.PlayerKnowledge;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.keys.AbstractResearchKey;
import com.verdantartifice.primalmagick.common.research.keys.EntityScanKey;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.test.AbstractBaseTest;
import net.minecraft.gametest.framework.GameTestHelper;

import java.util.Set;

public class AbstractPlayerKnowledgeTest extends AbstractBaseTest {
    private static final AbstractResearchKey<?> DEFAULT_RESEARCH_KEY = new ResearchEntryKey(ResearchEntries.FIRST_STEPS);
    private static final int DEFAULT_MAX_STAGES = 4;

    public void player_knowledge_add_and_check_research(GameTestHelper helper) {
        var knowledge = new PlayerKnowledge();
        helper.assertFalse(knowledge.isResearchKnown(DEFAULT_RESEARCH_KEY), "Research key known upon creation");
        helper.assertTrue(knowledge.addResearch(DEFAULT_RESEARCH_KEY), "Failed to add research");
        helper.assertTrue(knowledge.isResearchKnown(DEFAULT_RESEARCH_KEY), "Research key not known after adding");
        helper.succeed();
    }

    public void player_knowledge_cannot_add_duplicate_research(GameTestHelper helper) {
        var knowledge = new PlayerKnowledge();
        helper.assertTrue(knowledge.addResearch(DEFAULT_RESEARCH_KEY), "Failed to add research");
        helper.assertTrue(knowledge.isResearchKnown(DEFAULT_RESEARCH_KEY), "Research key not known after adding");
        helper.assertFalse(knowledge.addResearch(DEFAULT_RESEARCH_KEY), "Erroneously added research again");
        helper.assertTrue(knowledge.isResearchKnown(DEFAULT_RESEARCH_KEY), "Research key not known after duplicate adding");
        helper.succeed();
    }

    public void player_knowledge_remove_research(GameTestHelper helper) {
        var knowledge = new PlayerKnowledge();
        helper.assertFalse(knowledge.removeResearch(DEFAULT_RESEARCH_KEY), "Managed to remove research before adding");
        helper.assertFalse(knowledge.isResearchKnown(DEFAULT_RESEARCH_KEY), "Research key known after dud removal");
        helper.assertTrue(knowledge.addResearch(DEFAULT_RESEARCH_KEY), "Failed to add research");
        helper.assertTrue(knowledge.isResearchKnown(DEFAULT_RESEARCH_KEY), "Research key not known after adding");
        helper.assertTrue(knowledge.removeResearch(DEFAULT_RESEARCH_KEY), "Failed to remove research");
        helper.assertFalse(knowledge.isResearchKnown(DEFAULT_RESEARCH_KEY), "Research key known after removal");
        helper.succeed();
    }

    public void player_knowledge_get_research_set(GameTestHelper helper) {
        var knowledge = new PlayerKnowledge();
        var otherKey = new EntityScanKey(EntityTypesPM.TREEFOLK.get());
        helper.assertValueEqual(knowledge.getResearchSet(), Set.of(), "Initial research set");
        helper.assertTrue(knowledge.addResearch(DEFAULT_RESEARCH_KEY), "Failed to add research 1");
        helper.assertValueEqual(knowledge.getResearchSet(), Set.of(DEFAULT_RESEARCH_KEY), "Post-add 1 research set");
        helper.assertTrue(knowledge.addResearch(otherKey), "Failed to add research 2");
        helper.assertValueEqual(knowledge.getResearchSet(), Set.of(DEFAULT_RESEARCH_KEY, otherKey), "Post-add 2 research set");
        helper.assertTrue(knowledge.removeResearch(DEFAULT_RESEARCH_KEY), "Failed to remove research");
        helper.assertValueEqual(knowledge.getResearchSet(), Set.of(otherKey), "Post-remove research set");
        helper.succeed();
    }

    public void player_knowledge_get_set_research_stage(GameTestHelper helper) {
        var knowledge = new PlayerKnowledge();
        helper.assertValueEqual(knowledge.getResearchStage(DEFAULT_RESEARCH_KEY), -1, "Initial research stage");
        helper.assertTrue(knowledge.addResearch(DEFAULT_RESEARCH_KEY), "Failed to add research");
        helper.assertValueEqual(knowledge.getResearchStage(DEFAULT_RESEARCH_KEY), 0, "Post-add research stage");
        helper.assertTrue(knowledge.setResearchStage(DEFAULT_RESEARCH_KEY, 1), "Failed to set research stage");
        helper.assertValueEqual(knowledge.getResearchStage(DEFAULT_RESEARCH_KEY), 1, "Post-set research stage");
        helper.succeed();
    }

    public void player_knowledge_get_set_research_flag(GameTestHelper helper) {
        var knowledge = new PlayerKnowledge();
        helper.assertFalse(knowledge.hasResearchFlag(DEFAULT_RESEARCH_KEY, IPlayerKnowledge.ResearchFlag.UPDATED), "Flag present before adding research");
        helper.assertTrue(knowledge.addResearch(DEFAULT_RESEARCH_KEY), "Failed to add research");
        helper.assertFalse(knowledge.hasResearchFlag(DEFAULT_RESEARCH_KEY, IPlayerKnowledge.ResearchFlag.UPDATED), "Flag present after adding research but before setting flag");
        helper.assertTrue(knowledge.addResearchFlag(DEFAULT_RESEARCH_KEY, IPlayerKnowledge.ResearchFlag.UPDATED), "Failed to add research flag");
        helper.assertTrue(knowledge.hasResearchFlag(DEFAULT_RESEARCH_KEY, IPlayerKnowledge.ResearchFlag.UPDATED), "Flag not present after setting flag");
        helper.succeed();
    }

    public void player_knowledge_remove_research_flag(GameTestHelper helper) {
        var knowledge = new PlayerKnowledge();
        helper.assertFalse(knowledge.removeResearchFlag(DEFAULT_RESEARCH_KEY, IPlayerKnowledge.ResearchFlag.UPDATED), "Remove succeeded before adding research");
        helper.assertTrue(knowledge.addResearch(DEFAULT_RESEARCH_KEY), "Failed to add research");
        helper.assertFalse(knowledge.removeResearchFlag(DEFAULT_RESEARCH_KEY, IPlayerKnowledge.ResearchFlag.UPDATED), "Remove succeeded before adding research flag");
        helper.assertTrue(knowledge.addResearchFlag(DEFAULT_RESEARCH_KEY, IPlayerKnowledge.ResearchFlag.UPDATED), "Failed to add research flag");
        helper.assertTrue(knowledge.hasResearchFlag(DEFAULT_RESEARCH_KEY, IPlayerKnowledge.ResearchFlag.UPDATED), "Flag not present after setting flag");
        helper.assertTrue(knowledge.removeResearchFlag(DEFAULT_RESEARCH_KEY, IPlayerKnowledge.ResearchFlag.UPDATED), "Remove failed");
        helper.assertFalse(knowledge.hasResearchFlag(DEFAULT_RESEARCH_KEY, IPlayerKnowledge.ResearchFlag.UPDATED), "Flag present after removing research");
        helper.succeed();
    }

    public void player_knowledge_get_research_flags(GameTestHelper helper) {
        // TODO Stub
        helper.succeed();
    }

    public void player_knowledge_get_research_status(GameTestHelper helper) {
        // TODO Stub
        helper.succeed();
    }

    public void player_knowledge_is_research_complete(GameTestHelper helper) {
        // TODO Stub
        helper.succeed();
    }

    public void player_knowledge_get_set_knowledge_raw(GameTestHelper helper) {
        // TODO Stub
        helper.succeed();
    }

    public void player_knowledge_get_knowledge_levels(GameTestHelper helper) {
        // TODO Stub
        helper.succeed();
    }

    public void player_knowledge_get_set_active_research_project(GameTestHelper helper) {
        // TODO Stub
        helper.succeed();
    }

    public void player_knowledge_get_set_last_research_project(GameTestHelper helper) {
        // TODO Stub
        helper.succeed();
    }

    public void player_knowledge_get_set_research_topic_history(GameTestHelper helper) {
        // TODO Stub
        helper.succeed();
    }

    public void player_knowledge_serialization(GameTestHelper helper) {
        // TODO Stub
        helper.succeed();
    }
}
