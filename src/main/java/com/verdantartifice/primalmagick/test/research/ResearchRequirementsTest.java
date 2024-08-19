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
import com.verdantartifice.primalmagick.common.research.requirements.VanillaItemUsedStatRequirement;
import com.verdantartifice.primalmagick.common.stats.ExpertiseManager;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;
import com.verdantartifice.primalmagick.test.TestUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
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

    @GameTest(template = "primalmagick:test/floor5x5x5")
    public static void vanilla_item_used_stat_requirement(GameTestHelper helper) {
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
}
