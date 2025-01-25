package com.verdantartifice.primalmagick.test.research;

import com.verdantartifice.primalmagick.common.research.KnowledgeType;
import com.verdantartifice.primalmagick.common.research.ResearchDisciplines;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.research.ResearchTier;
import com.verdantartifice.primalmagick.common.research.keys.ResearchDisciplineKey;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.AndRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ExpertiseRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ItemStackRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ItemTagRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.KnowledgeRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.OrRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.QuorumRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.StatRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.VanillaCustomStatRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.VanillaItemUsedStatRequirement;
import com.verdantartifice.primalmagick.common.stats.ExpertiseManager;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;
import com.verdantartifice.primalmagick.common.tags.CommonTags;
import com.verdantartifice.primalmagick.test.AbstractBaseTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractResearchRequirementsTest extends AbstractBaseTest {
    public void research_requirement(GameTestHelper helper) {
        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        var req = new ResearchRequirement(new ResearchEntryKey(ResearchEntries.FIRST_STEPS));
        helper.assertFalse(req.isMetBy(player), "Baseline expectation failed");
        ResearchManager.forceGrantWithAllParents(player, ResearchEntries.FIRST_STEPS);
        helper.assertTrue(req.isMetBy(player), "Requirement not met");
        helper.succeed();
    }

    public void knowledge_requirement(GameTestHelper helper) {
        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        var req = new KnowledgeRequirement(KnowledgeType.OBSERVATION, 5);
        helper.assertFalse(req.isMetBy(player), "Baseline expectation failed");
        ResearchManager.addKnowledge(player, KnowledgeType.OBSERVATION, 5 * KnowledgeType.OBSERVATION.getProgression());
        helper.assertTrue(req.isMetBy(player), "Requirement not met");
        helper.succeed();
    }

    public void item_stack_requirement(GameTestHelper helper) {
        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        var req = new ItemStackRequirement(new ItemStack(Items.IRON_INGOT));
        helper.assertFalse(req.isMetBy(player), "Baseline expectation failed");
        player.getInventory().add(new ItemStack(Items.IRON_INGOT));
        helper.assertTrue(req.isMetBy(player), "Requirement not met");
        helper.succeed();
    }

    public void item_tag_requirement(GameTestHelper helper) {
        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        var req = new ItemTagRequirement(CommonTags.Items.EGGS, 1);
        helper.assertFalse(req.isMetBy(player), "Baseline expectation failed");
        player.getInventory().add(new ItemStack(Items.EGG));
        helper.assertTrue(req.isMetBy(player), "Requirement not met");
        helper.succeed();
    }

    public void stat_requirement(GameTestHelper helper) {
        var player = this.makeMockServerPlayer(helper); // Stats are only recorded on the server side
        var req = new StatRequirement(StatsPM.MANA_SIPHONED, 2);
        helper.assertFalse(req.isMetBy(player), "Baseline expectation failed");
        StatsManager.setValue(player, StatsPM.MANA_SIPHONED, 2);
        helper.assertTrue(req.isMetBy(player), "Requirement not met");
        helper.succeed();
    }

    public void expertise_requirement(GameTestHelper helper) {
        var player = this.makeMockServerPlayer(helper); // Stats are only recorded on the server side
        var req = new ExpertiseRequirement(ResearchDisciplines.MANAWEAVING, ResearchTier.EXPERT, 12);
        helper.assertFalse(req.isMetBy(player), "Baseline expectation failed");
        ResearchManager.forceGrantWithAllParents(player, ResearchEntries.UNLOCK_MANAWEAVING);
        ExpertiseManager.setValue(player, new ResearchDisciplineKey(ResearchDisciplines.MANAWEAVING), 12);
        helper.assertTrue(req.isMetBy(player), "Requirement not met");
        helper.succeed();
    }

    public void vanilla_item_used_stat_requirement(GameTestHelper helper) {
        @SuppressWarnings("removal")
        var player = helper.makeMockServerPlayerInLevel(); // Vanilla stats require an explicit client or server player
        var req = new VanillaItemUsedStatRequirement(Items.SNOWBALL, 1);
        helper.assertFalse(req.isMetBy(player), "Baseline expectation failed");
        var pos = new BlockPos(2, 2, 2);
        player.setPos(Vec3.atBottomCenterOf(helper.absolutePos(pos)));
        var stack = new ItemStack(Items.SNOWBALL);
        stack.use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
        helper.assertTrue(req.isMetBy(player), "Requirement not met");
        helper.succeed();
    }

    public void vanilla_custom_stat_requirement(GameTestHelper helper) {
        var player = this.makeMockServerPlayer(helper); // Vanilla stats require an explicit client or server player
        var req = new VanillaCustomStatRequirement(Stats.JUMP, 1, null);
        helper.assertFalse(req.isMetBy(player), "Baseline expectation failed");
        player.jumpFromGround();
        helper.assertTrue(req.isMetBy(player), "Requirement not met");
        helper.succeed();
    }

    public void and_requirement(GameTestHelper helper) {
        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        var req = new AndRequirement(
                new ResearchRequirement(new ResearchEntryKey(ResearchEntries.FIRST_STEPS)),
                new KnowledgeRequirement(KnowledgeType.OBSERVATION, 5),
                new ItemStackRequirement(new ItemStack(Items.IRON_INGOT))
            );
        helper.assertFalse(req.isMetBy(player), "Baseline expectation failed");
        
        // Grant the first sub-requirement and re-test
        ResearchManager.forceGrantWithAllParents(player, ResearchEntries.FIRST_STEPS);
        helper.assertFalse(req.isMetBy(player), "Partial expectation 1 failed");
        
        // Grant the second sub-requirement and re-test
        player.getInventory().add(new ItemStack(Items.IRON_INGOT));
        helper.assertFalse(req.isMetBy(player), "Partial expectation 2 failed");
        
        // Grant the third sub-requirement and re-test
        ResearchManager.addKnowledge(player, KnowledgeType.OBSERVATION, 5 * KnowledgeType.OBSERVATION.getProgression());
        helper.assertTrue(req.isMetBy(player), "Requirement not met");
        helper.succeed();
    }

    public void or_requirement(GameTestHelper helper) {
        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        var req = new OrRequirement(
                new ResearchRequirement(new ResearchEntryKey(ResearchEntries.FIRST_STEPS)),
                new KnowledgeRequirement(KnowledgeType.OBSERVATION, 5),
                new ItemStackRequirement(new ItemStack(Items.IRON_INGOT))
            );
        helper.assertFalse(req.isMetBy(player), "Baseline expectation failed");
        
        // Grant the first sub-requirement and re-test
        ResearchManager.forceGrantWithAllParents(player, ResearchEntries.FIRST_STEPS);
        helper.assertTrue(req.isMetBy(player), "Partial expectation 1 failed");
        
        // Reset, grant the second sub-requirement, and re-test
        ResearchManager.forceRevokeWithAllChildren(player, ResearchEntries.FIRST_STEPS);
        player.getInventory().add(new ItemStack(Items.IRON_INGOT));
        helper.assertTrue(req.isMetBy(player), "Partial expectation 2 failed");
        
        // Reset, grant the third sub-requirement, and re-test
        player.getInventory().clearContent();
        ResearchManager.addKnowledge(player, KnowledgeType.OBSERVATION, 5 * KnowledgeType.OBSERVATION.getProgression());
        helper.assertTrue(req.isMetBy(player), "Partial expectation 3 failed");
        helper.succeed();
    }

    public void quorum_requirement(GameTestHelper helper) {
        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        var req = new QuorumRequirement(2,
                new ResearchRequirement(new ResearchEntryKey(ResearchEntries.FIRST_STEPS)),
                new KnowledgeRequirement(KnowledgeType.OBSERVATION, 5),
                new ItemStackRequirement(new ItemStack(Items.IRON_INGOT))
            );
        helper.assertFalse(req.isMetBy(player), "Baseline expectation failed");
        
        // Grant the first sub-requirement and re-test
        ResearchManager.forceGrantWithAllParents(player, ResearchEntries.FIRST_STEPS);
        helper.assertFalse(req.isMetBy(player), "Partial expectation 1 failed");
        
        // Grant the second sub-requirement and re-test
        player.getInventory().add(new ItemStack(Items.IRON_INGOT));
        helper.assertTrue(req.isMetBy(player), "Requirement not met");
        helper.succeed();
    }
}
