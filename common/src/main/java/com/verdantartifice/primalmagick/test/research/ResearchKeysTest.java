package com.verdantartifice.primalmagick.test.research;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.items.ItemRegistration;
import com.verdantartifice.primalmagick.common.research.ResearchDisciplines;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.research.keys.EntityScanKey;
import com.verdantartifice.primalmagick.common.research.keys.ItemScanKey;
import com.verdantartifice.primalmagick.common.research.keys.ResearchDisciplineKey;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.keys.ResearchStageKey;
import com.verdantartifice.primalmagick.common.research.keys.RuneEnchantmentKey;
import com.verdantartifice.primalmagick.common.research.keys.RuneEnchantmentPartialKey;
import com.verdantartifice.primalmagick.common.research.keys.StackCraftedKey;
import com.verdantartifice.primalmagick.common.research.keys.TagCraftedKey;
import com.verdantartifice.primalmagick.common.runes.RuneType;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
import com.verdantartifice.primalmagick.test.TestUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameType;
import net.minecraftforge.gametest.GameTestHolder;

@GameTestHolder(Constants.MOD_ID + ".research_keys")
public class ResearchKeysTest {
    @GameTest(template = TestUtils.DEFAULT_TEMPLATE)
    public static void research_discipline(GameTestHelper helper) {
        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        var key = new ResearchDisciplineKey(ResearchDisciplines.MANAWEAVING);
        helper.assertFalse(key.isKnownBy(player), "Baseline expectation failed");
        ResearchManager.forceGrantWithAllParents(player, ResearchEntries.UNLOCK_MANAWEAVING);
        helper.assertTrue(key.isKnownBy(player), "Key not known");
        helper.succeed();
    }
    
    @GameTest(template = TestUtils.DEFAULT_TEMPLATE)
    public static void research_entry(GameTestHelper helper) {
        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        var key = new ResearchEntryKey(ResearchEntries.FIRST_STEPS);
        helper.assertFalse(key.isKnownBy(player), "Baseline expectation failed");
        ResearchManager.forceGrantWithAllParents(player, ResearchEntries.FIRST_STEPS);
        helper.assertTrue(key.isKnownBy(player), "Key not known");
        helper.succeed();
    }
    
    @GameTest(template = TestUtils.DEFAULT_TEMPLATE)
    public static void research_stage(GameTestHelper helper) {
        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        var key = new ResearchStageKey(ResearchEntries.FIRST_STEPS, 2);
        helper.assertFalse(key.isKnownBy(player), "Baseline expectation failed");
        ResearchManager.progressResearch(player, ResearchEntries.FIRST_STEPS);
        helper.assertFalse(key.isKnownBy(player), "Partial expectation failed");
        ResearchManager.progressResearch(player, ResearchEntries.FIRST_STEPS);
        helper.assertTrue(key.isKnownBy(player), "Key not known");
        helper.succeed();
    }
    
    @GameTest(template = TestUtils.DEFAULT_TEMPLATE)
    public static void item_scan(GameTestHelper helper) {
        var player = helper.makeMockServerPlayer();
        var key = new ItemScanKey(Items.IRON_INGOT);
        helper.assertFalse(key.isKnownBy(player), "Baseline expectation failed");
        ResearchManager.setScanned(new ItemStack(Items.IRON_INGOT), player);
        helper.assertTrue(key.isKnownBy(player), "Key not known");
        helper.succeed();
    }
    
    @GameTest(template = TestUtils.DEFAULT_TEMPLATE)
    public static void entity_scan(GameTestHelper helper) {
        var player = helper.makeMockServerPlayer();
        var key = new EntityScanKey(EntityType.BAT);
        helper.assertFalse(key.isKnownBy(player), "Baseline expectation failed");
        ResearchManager.setScanned(EntityType.BAT, player);
        helper.assertTrue(key.isKnownBy(player), "Key not known");
        helper.succeed();
    }
    
    @GameTest(template = TestUtils.DEFAULT_TEMPLATE)
    public static void stack_crafted(GameTestHelper helper) {
        var player = helper.makeMockServerPlayer();
        var item = ItemsPM.PRIMALITE_INGOT.get();
        var key = new StackCraftedKey(item);
        helper.assertFalse(key.isKnownBy(player), "Baseline expectation failed");
        ResearchManager.completeResearch(player, key);
        helper.assertTrue(key.isKnownBy(player), "Key not known");
        helper.succeed();
    }
    
    @GameTest(template = TestUtils.DEFAULT_TEMPLATE)
    public static void tag_crafted(GameTestHelper helper) {
        var player = helper.makeMockServerPlayer();
        var tag = ItemTagsPM.INGOTS_PRIMALITE;
        var key = new TagCraftedKey(tag);
        helper.assertFalse(key.isKnownBy(player), "Baseline expectation failed");
        ResearchManager.completeResearch(player, key);
        helper.assertTrue(key.isKnownBy(player), "Key not known");
        helper.succeed();
    }
    
    @GameTest(template = TestUtils.DEFAULT_TEMPLATE)
    public static void rune_enchantment(GameTestHelper helper) {
        var player = helper.makeMockServerPlayer();
        var enchKey = Enchantments.AQUA_AFFINITY;
        var ench = helper.getLevel().registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolder(enchKey).get();
        var key = new RuneEnchantmentKey(ench);
        helper.assertFalse(key.isKnownBy(player), "Baseline expectation failed");
        ResearchManager.completeResearch(player, key);
        helper.assertTrue(key.isKnownBy(player), "Key not known");
        helper.succeed();
    }
    
    @GameTest(template = TestUtils.DEFAULT_TEMPLATE)
    public static void rune_enchantment_partial(GameTestHelper helper) {
        var player = helper.makeMockServerPlayer();
        var enchKey = Enchantments.AQUA_AFFINITY;
        var ench = helper.getLevel().registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolder(enchKey).get();
        var key = new RuneEnchantmentPartialKey(ench, RuneType.SOURCE);
        helper.assertFalse(key.isKnownBy(player), "Baseline expectation failed");
        ResearchManager.completeResearch(player, key);
        helper.assertTrue(key.isKnownBy(player), "Key not known");
        helper.succeed();
    }
}
