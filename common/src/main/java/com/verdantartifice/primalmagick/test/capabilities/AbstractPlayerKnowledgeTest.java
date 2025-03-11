package com.verdantartifice.primalmagick.test.capabilities;

import com.verdantartifice.primalmagick.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagick.common.capabilities.PlayerKnowledge;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.KnowledgeType;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.keys.EntityScanKey;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.topics.EntryResearchTopic;
import com.verdantartifice.primalmagick.common.research.topics.MainIndexResearchTopic;
import com.verdantartifice.primalmagick.common.theorycrafting.ProjectTemplates;
import com.verdantartifice.primalmagick.test.AbstractBaseTest;
import net.minecraft.gametest.framework.GameTestHelper;

import java.util.List;
import java.util.Set;

public class AbstractPlayerKnowledgeTest extends AbstractBaseTest {
    private static final ResearchEntryKey DEFAULT_RESEARCH_KEY = new ResearchEntryKey(ResearchEntries.FIRST_STEPS);
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
        var knowledge = new PlayerKnowledge();
        helper.assertValueEqual(knowledge.getResearchFlags(DEFAULT_RESEARCH_KEY), Set.of(), "Initial research flags without research");
        helper.assertTrue(knowledge.addResearch(DEFAULT_RESEARCH_KEY), "Failed to add research");
        helper.assertValueEqual(knowledge.getResearchFlags(DEFAULT_RESEARCH_KEY), Set.of(), "Initial research flags with research");
        helper.assertTrue(knowledge.addResearchFlag(DEFAULT_RESEARCH_KEY, IPlayerKnowledge.ResearchFlag.UPDATED), "Failed to add research flag 1");
        helper.assertValueEqual(knowledge.getResearchFlags(DEFAULT_RESEARCH_KEY), Set.of(IPlayerKnowledge.ResearchFlag.UPDATED), "Post-add 1 flags");
        helper.assertTrue(knowledge.addResearchFlag(DEFAULT_RESEARCH_KEY, IPlayerKnowledge.ResearchFlag.POPUP), "Failed to add research flag 2");
        helper.assertValueEqual(knowledge.getResearchFlags(DEFAULT_RESEARCH_KEY), Set.of(IPlayerKnowledge.ResearchFlag.UPDATED, IPlayerKnowledge.ResearchFlag.POPUP), "Post-add 2 flags");
        helper.assertTrue(knowledge.removeResearchFlag(DEFAULT_RESEARCH_KEY, IPlayerKnowledge.ResearchFlag.UPDATED), "Failed to remove research flag");
        helper.assertValueEqual(knowledge.getResearchFlags(DEFAULT_RESEARCH_KEY), Set.of(IPlayerKnowledge.ResearchFlag.POPUP), "Post-remove flags");
        helper.succeed();
    }

    public void player_knowledge_get_research_status(GameTestHelper helper) {
        var knowledge = new PlayerKnowledge();
        var ra = helper.getLevel().registryAccess();
        helper.assertValueEqual(knowledge.getResearchStatus(ra, DEFAULT_RESEARCH_KEY), IPlayerKnowledge.ResearchStatus.UNKNOWN, "Pre-add research status");
        helper.assertTrue(knowledge.addResearch(DEFAULT_RESEARCH_KEY), "Failed to add research");
        helper.assertValueEqual(knowledge.getResearchStatus(ra, DEFAULT_RESEARCH_KEY), IPlayerKnowledge.ResearchStatus.IN_PROGRESS, "Post-add research status");
        helper.assertTrue(knowledge.setResearchStage(DEFAULT_RESEARCH_KEY, 1), "Failed to set research stage 1");
        helper.assertValueEqual(knowledge.getResearchStatus(ra, DEFAULT_RESEARCH_KEY), IPlayerKnowledge.ResearchStatus.IN_PROGRESS, "Post-stage 1 research status");
        helper.assertTrue(knowledge.setResearchStage(DEFAULT_RESEARCH_KEY, DEFAULT_MAX_STAGES), "Failed to set research stage 2");
        helper.assertValueEqual(knowledge.getResearchStatus(ra, DEFAULT_RESEARCH_KEY), IPlayerKnowledge.ResearchStatus.COMPLETE, "Post-stage 2 research status");
        helper.assertTrue(knowledge.setResearchStage(DEFAULT_RESEARCH_KEY, 1000), "Failed to set research stage 3");
        helper.assertValueEqual(knowledge.getResearchStatus(ra, DEFAULT_RESEARCH_KEY), IPlayerKnowledge.ResearchStatus.COMPLETE, "Post-stage 3 research status");
        helper.succeed();
    }

    public void player_knowledge_is_research_complete(GameTestHelper helper) {
        var knowledge = new PlayerKnowledge();
        var ra = helper.getLevel().registryAccess();
        helper.assertFalse(knowledge.isResearchComplete(ra, DEFAULT_RESEARCH_KEY), "Pre-add research status");
        helper.assertTrue(knowledge.addResearch(DEFAULT_RESEARCH_KEY), "Failed to add research");
        helper.assertFalse(knowledge.isResearchComplete(ra, DEFAULT_RESEARCH_KEY), "Post-add research status");
        helper.assertTrue(knowledge.setResearchStage(DEFAULT_RESEARCH_KEY, 1), "Failed to set research stage 1");
        helper.assertFalse(knowledge.isResearchComplete(ra, DEFAULT_RESEARCH_KEY), "Post-stage 1 research status");
        helper.assertTrue(knowledge.setResearchStage(DEFAULT_RESEARCH_KEY, DEFAULT_MAX_STAGES), "Failed to set research stage 2");
        helper.assertTrue(knowledge.isResearchComplete(ra, DEFAULT_RESEARCH_KEY), "Post-stage 2 research status");
        helper.assertTrue(knowledge.setResearchStage(DEFAULT_RESEARCH_KEY, 1000), "Failed to set research stage 3");
        helper.assertTrue(knowledge.isResearchComplete(ra, DEFAULT_RESEARCH_KEY), "Post-stage 3 research status");
        helper.succeed();
    }

    public void player_knowledge_get_set_knowledge_raw(GameTestHelper helper) {
        var knowledge = new PlayerKnowledge();
        helper.assertValueEqual(knowledge.getKnowledgeRaw(KnowledgeType.THEORY), 0, "Pre-set knowledge raw");
        helper.assertTrue(knowledge.addKnowledge(KnowledgeType.THEORY, KnowledgeType.THEORY.getProgression()), "Failed to add knowledge");
        helper.assertValueEqual(knowledge.getKnowledgeRaw(KnowledgeType.THEORY), KnowledgeType.THEORY.getProgression(), "Post-set knowledge raw");
        helper.succeed();
    }

    public void player_knowledge_get_knowledge_levels(GameTestHelper helper) {
        var knowledge = new PlayerKnowledge();
        helper.assertValueEqual(knowledge.getKnowledge(KnowledgeType.THEORY), 0, "Pre-set knowledge");
        helper.assertTrue(knowledge.addKnowledge(KnowledgeType.THEORY, KnowledgeType.THEORY.getProgression()), "Failed to add knowledge");
        helper.assertValueEqual(knowledge.getKnowledge(KnowledgeType.THEORY), 1, "Post-set knowledge");
        helper.succeed();
    }

    public void player_knowledge_get_set_active_research_project(GameTestHelper helper) {
        // Create a player to test with
        var player = this.makeMockServerPlayer(helper);

        // Create a research project to test with
        var projectTemplateKey = ProjectTemplates.EXPEDITION;
        var projectTemplate = helper.getLevel().registryAccess().registryOrThrow(RegistryKeysPM.PROJECT_TEMPLATES).getOrThrow(projectTemplateKey);
        var project = projectTemplate.initialize(player, Set.of());
        helper.assertTrue(project != null, "Failed to initialize project");

        var knowledge = new PlayerKnowledge();
        helper.assertTrue(knowledge.getActiveResearchProject() == null, "Pre-set active research project");
        knowledge.setActiveResearchProject(project);
        helper.assertValueEqual(knowledge.getActiveResearchProject(), project, "Post-set active research project");

        helper.succeed();
    }

    public void player_knowledge_get_set_last_research_topic(GameTestHelper helper) {
        var knowledge = new PlayerKnowledge();
        var topic = new EntryResearchTopic(DEFAULT_RESEARCH_KEY, 0);
        helper.assertValueEqual(knowledge.getLastResearchTopic(), MainIndexResearchTopic.INSTANCE, "Pre-set last research topic");
        knowledge.setLastResearchTopic(topic);
        helper.assertValueEqual(knowledge.getLastResearchTopic(), topic, "Post-set last research topic");
        helper.succeed();
    }

    public void player_knowledge_get_set_research_topic_history(GameTestHelper helper) {
        var knowledge = new PlayerKnowledge();
        var topic = new EntryResearchTopic(DEFAULT_RESEARCH_KEY, 0);
        helper.assertValueEqual(knowledge.getResearchTopicHistory(), List.of(), "Pre-set research topic history");
        knowledge.setResearchTopicHistory(List.of(MainIndexResearchTopic.INSTANCE));
        helper.assertValueEqual(knowledge.getResearchTopicHistory(), List.of(MainIndexResearchTopic.INSTANCE), "Post-set 1 research topic history");
        knowledge.setResearchTopicHistory(List.of(topic));
        helper.assertValueEqual(knowledge.getResearchTopicHistory(), List.of(topic), "Post-set 2 research topic history");
        helper.succeed();
    }

    public void player_knowledge_serialization(GameTestHelper helper) {
        // Create a player to test with
        var player = this.makeMockServerPlayer(helper);

        // Create a research project to test with
        var projectTemplateKey = ProjectTemplates.EXPEDITION;
        var projectTemplate = helper.getLevel().registryAccess().registryOrThrow(RegistryKeysPM.PROJECT_TEMPLATES).getOrThrow(projectTemplateKey);
        var project = projectTemplate.initialize(player, Set.of());
        helper.assertTrue(project != null, "Failed to initialize project");

        var topic = new EntryResearchTopic(DEFAULT_RESEARCH_KEY, 0);

        // Populate knowledge capability
        var before = new PlayerKnowledge();
        before.addResearch(DEFAULT_RESEARCH_KEY);
        before.setResearchStage(DEFAULT_RESEARCH_KEY, 1);
        before.addResearchFlag(DEFAULT_RESEARCH_KEY, IPlayerKnowledge.ResearchFlag.UPDATED);
        before.addKnowledge(KnowledgeType.THEORY, 1);
        before.setActiveResearchProject(project);
        before.setLastResearchTopic(topic);
        before.setResearchTopicHistory(List.of(topic));

        // Serialize the capability to a tag
        var tag = before.serializeNBT(helper.getLevel().registryAccess());

        // Deserialize a new capability and ensure it's the same
        var after = new PlayerKnowledge();
        after.deserializeNBT(helper.getLevel().registryAccess(), tag);
        helper.assertValueEqual(after, before, "Knowledge capabilities");

        helper.succeed();
    }
}
