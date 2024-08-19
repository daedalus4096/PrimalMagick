package com.verdantartifice.primalmagick.test.research;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.research.KnowledgeType;
import com.verdantartifice.primalmagick.common.research.ResearchDisciplines;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.research.ResearchTier;
import com.verdantartifice.primalmagick.common.research.keys.ResearchDisciplineKey;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.ExpertiseRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ItemStackRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ItemTagRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.KnowledgeRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.StatRequirement;
import com.verdantartifice.primalmagick.common.stats.ExpertiseManager;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;
import com.verdantartifice.primalmagick.test.TestUtils;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraftforge.common.Tags;
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

    @GameTest(template = TestUtils.DEFAULT_TEMPLATE)
    public static void knowledge_requirement(GameTestHelper helper) {
        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        var req = new KnowledgeRequirement(KnowledgeType.OBSERVATION, 5);
        helper.assertFalse(req.isMetBy(player), "Baseline expectation failed");
        ResearchManager.addKnowledge(player, KnowledgeType.OBSERVATION, 5 * KnowledgeType.OBSERVATION.getProgression());
        helper.assertTrue(req.isMetBy(player), "Requirement not met");
        helper.succeed();
    }

    @GameTest(template = TestUtils.DEFAULT_TEMPLATE)
    public static void item_stack_requirement(GameTestHelper helper) {
        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        var req = new ItemStackRequirement(new ItemStack(Items.IRON_INGOT));
        helper.assertFalse(req.isMetBy(player), "Baseline expectation failed");
        player.getInventory().add(new ItemStack(Items.IRON_INGOT));
        helper.assertTrue(req.isMetBy(player), "Requirement not met");
        helper.succeed();
    }

    @GameTest(template = TestUtils.DEFAULT_TEMPLATE)
    public static void item_tag_requirement(GameTestHelper helper) {
        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        var req = new ItemTagRequirement(Tags.Items.EGGS, 1);
        helper.assertFalse(req.isMetBy(player), "Baseline expectation failed");
        player.getInventory().add(new ItemStack(Items.EGG));
        helper.assertTrue(req.isMetBy(player), "Requirement not met");
        helper.succeed();
    }

    @GameTest(template = TestUtils.DEFAULT_TEMPLATE)
    public static void stat_requirement(GameTestHelper helper) {
        var player = helper.makeMockServerPlayer(); // Stats are only recorded on the server side
        var req = new StatRequirement(StatsPM.MANA_SIPHONED, 2);
        helper.assertFalse(req.isMetBy(player), "Baseline expectation failed");
        StatsManager.setValue(player, StatsPM.MANA_SIPHONED, 2);
        helper.assertTrue(req.isMetBy(player), "Requirement not met");
        helper.succeed();
    }

    @GameTest(template = TestUtils.DEFAULT_TEMPLATE)
    public static void expertise_requirement(GameTestHelper helper) {
        var player = helper.makeMockServerPlayer(); // Stats are only recorded on the server side
        var req = new ExpertiseRequirement(ResearchDisciplines.MANAWEAVING, ResearchTier.EXPERT, 12);
        helper.assertFalse(req.isMetBy(player), "Baseline expectation failed");
        ResearchManager.forceGrantWithAllParents(player, ResearchEntries.UNLOCK_MANAWEAVING);
        ExpertiseManager.setValue(player, new ResearchDisciplineKey(ResearchDisciplines.MANAWEAVING), 12);
        helper.assertTrue(req.isMetBy(player), "Requirement not met");
        helper.succeed();
    }
}
