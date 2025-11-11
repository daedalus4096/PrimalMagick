package com.verdantartifice.primalmagick.test.research;

import com.verdantartifice.primalmagick.common.items.ItemsPM;
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
import com.verdantartifice.primalmagick.test.AbstractBaseTest;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameType;

public class ResearchKeysTests extends AbstractBaseTest {
    public static void research_discipline(GameTestHelper helper) {
        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        var key = new ResearchDisciplineKey(ResearchDisciplines.MANAWEAVING);
        assertFalse(helper, key.isKnownBy(player), "Baseline expectation failed");
        ResearchManager.forceGrantWithAllParents(player, ResearchEntries.UNLOCK_MANAWEAVING);
        assertTrue(helper, key.isKnownBy(player), "Key not known");
        helper.succeed();
    }
    
    public static void research_entry(GameTestHelper helper) {
        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        var key = new ResearchEntryKey(ResearchEntries.FIRST_STEPS);
        assertFalse(helper, key.isKnownBy(player), "Baseline expectation failed");
        ResearchManager.forceGrantWithAllParents(player, ResearchEntries.FIRST_STEPS);
        assertTrue(helper, key.isKnownBy(player), "Key not known");
        helper.succeed();
    }
    
    public static void research_stage(GameTestHelper helper) {
        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        var key = new ResearchStageKey(ResearchEntries.FIRST_STEPS, 2);
        assertFalse(helper, key.isKnownBy(player), "Baseline expectation failed");
        ResearchManager.progressResearch(player, ResearchEntries.FIRST_STEPS);
        assertFalse(helper, key.isKnownBy(player), "Partial expectation failed");
        ResearchManager.progressResearch(player, ResearchEntries.FIRST_STEPS);
        assertTrue(helper, key.isKnownBy(player), "Key not known");
        helper.succeed();
    }
    
    public static void item_scan(GameTestHelper helper) {
        var player = makeMockServerPlayer(helper);
        var key = new ItemScanKey(Items.IRON_INGOT);
        assertFalse(helper, key.isKnownBy(player), "Baseline expectation failed");
        ResearchManager.setScanned(new ItemStack(Items.IRON_INGOT), player);
        assertTrue(helper, key.isKnownBy(player), "Key not known");
        helper.succeed();
    }
    
    public static void entity_scan(GameTestHelper helper) {
        var player = makeMockServerPlayer(helper);
        var key = new EntityScanKey(EntityType.BAT);
        assertFalse(helper, key.isKnownBy(player), "Baseline expectation failed");
        ResearchManager.setScanned(EntityType.BAT, player);
        assertTrue(helper, key.isKnownBy(player), "Key not known");
        helper.succeed();
    }
    
    public static void stack_crafted(GameTestHelper helper) {
        var player = makeMockServerPlayer(helper);
        var item = ItemsPM.PRIMALITE_INGOT.get();
        var key = new StackCraftedKey(item);
        assertFalse(helper, key.isKnownBy(player), "Baseline expectation failed");
        ResearchManager.completeResearch(player, key);
        assertTrue(helper, key.isKnownBy(player), "Key not known");
        helper.succeed();
    }
    
    public static void tag_crafted(GameTestHelper helper) {
        var player = makeMockServerPlayer(helper);
        var tag = ItemTagsPM.INGOTS_PRIMALITE;
        var key = new TagCraftedKey(tag);
        assertFalse(helper, key.isKnownBy(player), "Baseline expectation failed");
        ResearchManager.completeResearch(player, key);
        assertTrue(helper, key.isKnownBy(player), "Key not known");
        helper.succeed();
    }
    
    public static void rune_enchantment(GameTestHelper helper) {
        var player = makeMockServerPlayer(helper);
        var enchKey = Enchantments.AQUA_AFFINITY;
        var ench = helper.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchKey);
        var key = new RuneEnchantmentKey(ench);
        assertFalse(helper, key.isKnownBy(player), "Baseline expectation failed");
        ResearchManager.completeResearch(player, key);
        assertTrue(helper, key.isKnownBy(player), "Key not known");
        helper.succeed();
    }
    
    public static void rune_enchantment_partial(GameTestHelper helper) {
        var player = makeMockServerPlayer(helper);
        var enchKey = Enchantments.AQUA_AFFINITY;
        var ench = helper.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchKey);
        var key = new RuneEnchantmentPartialKey(ench, RuneType.SOURCE);
        assertFalse(helper, key.isKnownBy(player), "Baseline expectation failed");
        ResearchManager.completeResearch(player, key);
        assertTrue(helper, key.isKnownBy(player), "Key not known");
        helper.succeed();
    }
}
