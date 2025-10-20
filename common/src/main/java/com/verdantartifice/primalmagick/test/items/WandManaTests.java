package com.verdantartifice.primalmagick.test.items;

import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.wands.IHasWandComponents;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.wands.IWand;
import com.verdantartifice.primalmagick.common.wands.WandCap;
import com.verdantartifice.primalmagick.common.wands.WandCore;
import com.verdantartifice.primalmagick.common.wands.WandGem;
import com.verdantartifice.primalmagick.test.AbstractBaseTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class WandManaTests extends AbstractBaseTest {
    // TODO Expand scope to test other mana sources
    // TODO Expand scope to test other wand types
    protected static final Source source = Sources.EARTH;

    protected static Item getTestWandItem() {
        return ItemsPM.MODULAR_WAND.get();
    }

    protected static ItemStack getTestWand() {
        return IHasWandComponents.setWandComponents(getTestWandItem().getDefaultInstance(), WandCore.HEARTWOOD, WandCap.IRON, WandGem.APPRENTICE);
    }

    public static void wand_can_get_and_add_mana(GameTestHelper helper) {
        var wandStack = getTestWand();

        // Confirm that the wand was created successfully
        IWand wand = assertInstanceOf(helper, wandStack.getItem(), IWand.class, "Wand stack is not a wand as expected");

        // Confirm that the wand is empty at first
        assertValueEqual(helper, wand.getMana(wandStack, source), 0, "Wand is not empty as expected");

        // Add a point of real mana to the wand
        assertValueEqual(helper, wand.addMana(wandStack, source, 1), 0, "Failed to add centimana to wand");

        // Confirm that the wand has mana in it
        assertValueEqual(helper, wand.getMana(wandStack, source), 1, "Wand mana total is not as expected");

        helper.succeed();
    }

    public static void wand_can_get_and_add_real_mana(GameTestHelper helper) {
        var wandStack = getTestWand();

        // Confirm that the wand was created successfully
        IWand wand = assertInstanceOf(helper, wandStack.getItem(), IWand.class, "Wand stack is not a wand as expected");

        // Confirm that the wand is empty at first
        assertTrue(helper, wand.getMana(wandStack, source) == 0, "Wand is not empty as expected");

        // Add a point of real mana to the wand
        assertTrue(helper, wand.addMana(wandStack, source, 100) == 0, "Failed to add real mana to wand");

        // Confirm that the wand has mana in it
        assertTrue(helper, wand.getMana(wandStack, source) == 100, "Wand mana total is not as expected");

        helper.succeed();
    }

    public static void wand_cannot_add_too_much_mana(GameTestHelper helper) {
        var wandStack = getTestWand();

        // Confirm that the wand was created successfully
        IWand wand = assertInstanceOf(helper, wandStack.getItem(), IWand.class, "Wand stack is not a wand as expected");

        int maxCentimana = wand.getMaxMana(wandStack, source);
        int attemptedRealMana = 100000;
        int attemptedCentimana = 100 * attemptedRealMana;
        int expectedCentimana = attemptedCentimana - maxCentimana;
        int actualMana = wand.addMana(wandStack, source, attemptedCentimana);

        // Confirm that the overfill for the wand is as expected
        assertValueEqual(helper, wand.getMana(wandStack, source), maxCentimana, "Wand mana total");
        assertValueEqual(helper, actualMana, expectedCentimana, "Wand overfill");

        helper.succeed();
    }

    public static void wand_can_get_all_mana(GameTestHelper helper) {
        var wandStack = getTestWand();

        // Confirm that the wand was created successfully
        IWand wand = assertInstanceOf(helper, wandStack.getItem(), IWand.class, "Wand stack is not a wand as expected");

        // Add a point of real mana to the wand for each source *except* the test source
        Sources.stream().filter(s -> !s.equals(source)).forEach(s -> wand.addMana(wandStack, s, 100));

        // Create a source list of centimana to be expected; all sources *except* the test source
        var sourceListBuilder = SourceList.builder();
        Sources.stream().filter(s -> !s.equals(source)).forEach(s -> sourceListBuilder.with(s, 100));
        var sourceList = sourceListBuilder.build();

        // Confirm that the wand has the expected amount of mana in it
        assertTrue(helper, wand.getAllMana(wandStack).equals(sourceList), "Wand mana total is not as expected");

        helper.succeed();
    }

    public static void wand_can_consume_mana(GameTestHelper helper) {
        var player = makeMockServerPlayer(helper);
        var wandStack = getTestWand();

        // Confirm that the wand was created successfully
        IWand wand = assertInstanceOf(helper, wandStack.getItem(), IWand.class, "Wand stack is not a wand as expected");

        final int startingRealMana = 10;
        final int startingCentimana = 100 * startingRealMana;
        final int consumedCentimana = 100;
        final int finalCost = wand.getModifiedCost(wandStack, player, source, consumedCentimana, helper.getLevel().registryAccess());
        final int expectedCentimana = startingCentimana - finalCost;

        // Add a point of real mana to the wand
        assertTrue(helper, wand.addMana(wandStack, source, startingCentimana) == 0, "Failed to add real mana to wand");

        // Confirm that a few points of centimana can be consumed
        assertTrue(helper, wand.consumeMana(wandStack, player, source, consumedCentimana, helper.getLevel().registryAccess()), "Failed to consume mana from wand");

        // Confirm that the mana was deducted correctly
        var actual = wand.getMana(wandStack, source);
        assertValueEqual(helper, actual, expectedCentimana, "Mana total for " + source.getId());

        helper.succeed();
    }

    public static void wand_cannot_consume_more_mana_than_it_has(GameTestHelper helper) {
        var player = makeMockServerPlayer(helper);
        var wandStack = getTestWand();

        // Confirm that the wand was created successfully
        IWand wand = assertInstanceOf(helper, wandStack.getItem(), IWand.class, "Wand stack is not a wand as expected");

        // Add a point of real mana to the wand
        assertTrue(helper, wand.addMana(wandStack, source, 100) == 0, "Failed to add real mana to wand");

        // Confirm that attempting to consume more mana than the wand has fails
        assertFalse(helper, wand.consumeMana(wandStack, player, source, 200, helper.getLevel().registryAccess()), "Consumption of maan succeeded when it shouldn't have");

        // Confirm that the wand still has the mana it started with
        assertTrue(helper, wand.getMana(wandStack, source) == 100, "Wand mana total is not as expected");

        helper.succeed();
    }

    public static void wand_can_consume_multiple_types_of_mana(GameTestHelper helper) {
        var player = makeMockServerPlayer(helper);
        var wandStack = getTestWand();

        // Confirm that the wand was created successfully
        IWand wand = assertInstanceOf(helper, wandStack.getItem(), IWand.class, "Wand stack is not a wand as expected");

        final int startingRealMana = 10;
        final int startingCentimana = 100 * startingRealMana;
        final int consumedCentimana = 100;
        final int finalCost = wand.getModifiedCost(wandStack, player, source, consumedCentimana, helper.getLevel().registryAccess());
        final int expectedCentimana = startingCentimana - finalCost;

        // Add a point of real mana to the wand for each source
        Sources.getAll().forEach(s -> {
            assertTrue(helper, wand.addMana(wandStack, s, startingCentimana) == 0, "Failed to add real mana to wand for " + s.getId());
        });

        // Create a source list of centimana to be deducted; all sources *except* the test source
        var sourceListBuilder = SourceList.builder();
        Sources.stream().filter(s -> !s.equals(source)).forEach(s -> sourceListBuilder.with(s, consumedCentimana));
        var sourceList = sourceListBuilder.build();

        // Confirm that the centimana can be consumed
        assertTrue(helper, wand.consumeMana(wandStack, player, sourceList, helper.getLevel().registryAccess()), "Failed to consume mana from wand");

        // Confirm that the mana was deducted correctly for each source
        Sources.getAll().forEach(s -> {
            var expected = s.equals(source) ? startingCentimana : expectedCentimana;
            var actual = wand.getMana(wandStack, s);
            assertValueEqual(helper, actual, expected, "Mana total for " + s.getId());
        });

        helper.succeed();
    }

    public static void wand_cannot_consume_more_mana_than_it_has_with_multiple_types(GameTestHelper helper) {
        var player = makeMockServerPlayer(helper);
        var wandStack = getTestWand();

        // Confirm that the wand was created successfully
        IWand wand = assertInstanceOf(helper, wandStack.getItem(), IWand.class, "Wand stack is not a wand as expected");

        // Add a point of real mana to the wand for every source
        Sources.getAll().forEach(s -> wand.addMana(wandStack, s, 100));

        // Create a source list of centimana to be deducted; all sources *except* for the test source
        var sourceListBuilder = SourceList.builder();
        Sources.stream().filter(s -> !s.equals(source)).forEach(s -> sourceListBuilder.with(s, 500));
        var sourceList = sourceListBuilder.build();

        // Confirm that attempting to deduct more mana than the wand has fails
        assertFalse(helper, wand.consumeMana(wandStack, player, sourceList, helper.getLevel().registryAccess()), "Mana consumption succeeded when it shouldn't have");

        // Confirm that the wand's mana is still in its original state
        Sources.getAll().forEach(s -> assertTrue(helper, wand.getMana(wandStack, source) == 100, "Mana total is not as expected"));

        helper.succeed();
    }

    public static void wand_can_remove_mana_raw(GameTestHelper helper) {
        var wandStack = getTestWand();

        // Confirm that the wand was created successfully
        IWand wand = assertInstanceOf(helper, wandStack.getItem(), IWand.class, "Wand stack is not a wand as expected");

        final int startingRealMana = 1;
        final int startingCentimana = 100 * startingRealMana;
        final int removedCentimana = 10;
        final int expectedCentimana = startingCentimana - removedCentimana;

        // Add some real mana to the wand for the test source
        assertTrue(helper, wand.addMana(wandStack, source, startingCentimana) == 0, "Failed to add mana to wand");

        // Confirm that a few points of centimana can be consumed
        assertTrue(helper, wand.removeManaRaw(wandStack, source, removedCentimana), "Failed to remove mana from wand");

        // Confirm that the mana was deducted correctly
        assertTrue(helper, wand.getMana(wandStack, source) == expectedCentimana, "Wand mana total is not as expected");

        helper.succeed();
    }

    public static void wand_cannot_remove_more_raw_mana_than_it_has(GameTestHelper helper) {
        var wandStack = getTestWand();

        // Confirm that the wand was created successfully
        IWand wand = assertInstanceOf(helper, wandStack.getItem(), IWand.class, "Wand stack is not a wand as expected");

        // Add some real mana to the wand for the test source
        assertTrue(helper, wand.addMana(wandStack, source, 100) == 0, "Failed to add mana to wand");

        // Confirm that attempting to remove more than that fails
        assertFalse(helper, wand.removeManaRaw(wandStack, source, 200), "Mana removal succeeded when it shouldn't have");

        // Confirm that the wand's mana is still in its starting state
        assertTrue(helper, wand.getMana(wandStack, source) == 100, "Mana total is not as expected");

        helper.succeed();
    }

    public static void wand_contains_mana(GameTestHelper helper) {
        var player = makeMockServerPlayer(helper);
        var wandStack = getTestWand();

        // Confirm that the wand was created successfully
        IWand wand = assertInstanceOf(helper, wandStack.getItem(), IWand.class, "Wand stack is not a wand as expected");

        final int startingRealMana = 10;
        final int startingCentimana = 100 * startingRealMana;
        final double costModifier = 1 + (wand.getTotalCostModifier(wandStack, player, source, helper.getLevel().registryAccess()) / 100D);
        final int exactCentimana = (int)(startingCentimana * costModifier);
        final int lessCentimana = exactCentimana - 10;
        final int greaterCentimana = exactCentimana + 10;

        // Add some real mana to the wand for the test source
        final int overflow = wand.addMana(wandStack, source, startingCentimana);
        assertTrue(helper, overflow == 0, "Failed to add real mana to wand, overflow is " + overflow);

        // Confirm that the wand recognizes it contains centimana up to the threshold of what it was given
        assertValueEqual(helper, wand.getMana(wandStack, source), startingCentimana, "Mana total for source " + source.getId());
        assertTrue(helper, wand.containsMana(wandStack, player, source, lessCentimana, helper.getLevel().registryAccess()), "Contains returned false for less than held");
        assertTrue(helper, wand.containsMana(wandStack, player, source, exactCentimana, helper.getLevel().registryAccess()), "Contains returned false for exact held");
        assertFalse(helper, wand.containsMana(wandStack, player, source, greaterCentimana, helper.getLevel().registryAccess()), "Contains returned true for greater than held");

        helper.succeed();
    }

    public static void wand_contains_mana_list(GameTestHelper helper) {
        var player = makeMockServerPlayer(helper);
        var wandStack = getTestWand();

        // Confirm that the wand was created successfully
        IWand wand = assertInstanceOf(helper, wandStack.getItem(), IWand.class, "Wand stack is not a wand as expected");

        final int startingRealMana = 10;
        final int startingCentimana = startingRealMana * 100;
        final double costModifier = 1 + (wand.getTotalCostModifier(wandStack, player, source, helper.getLevel().registryAccess()) / 100D);
        final int modifiedCentimana = (int)(startingCentimana * costModifier);

        // Add some real mana to the wand for all sources except the test source
        Sources.stream().filter(s -> !s.equals(source)).forEach(s -> wand.addMana(wandStack, s, startingCentimana));

        // Confirm that the wand contains centimana for a list containing all source except the test source
        var greenBuilder = SourceList.builder();
        Sources.stream().filter(s -> !s.equals(source)).forEach(s -> greenBuilder.with(s, modifiedCentimana));
        var greenList = greenBuilder.build();
        assertTrue(helper, wand.containsMana(wandStack, player, greenList, helper.getLevel().registryAccess()), "Contains returned false for green list");

        // Confirm that the wand does not contain centimana for all sources
        var redBuilder = SourceList.builder();
        Sources.getAll().forEach(s -> redBuilder.with(s, modifiedCentimana));
        var redList = redBuilder.build();
        assertFalse(helper, wand.containsMana(wandStack, player, redList, helper.getLevel().registryAccess()), "Contains returned true for red list");

        helper.succeed();
    }

    public static void wand_contains_mana_raw(GameTestHelper helper) {
        var wandStack = getTestWand();

        // Confirm that the wand was created successfully
        IWand wand = assertInstanceOf(helper, wandStack.getItem(), IWand.class, "Wand stack is not a wand as expected");

        final int startingRealMana = 10;
        final int exactCentimana = 100 * startingRealMana;
        final int lessCentimana = exactCentimana - 1;
        final int greaterCentimana = exactCentimana + 1;

        // Add some real mana to the wand for the test source
        final int overflow = wand.addMana(wandStack, source, exactCentimana);
        assertTrue(helper, overflow == 0, "Failed to add mana to wand, overflow is " + overflow);

        // Confirm that the wand recognizes it contains centimana up to the threshold of what it was given
        assertTrue(helper, wand.containsManaRaw(wandStack, source, lessCentimana), "Contains returned false for less than held");
        assertTrue(helper, wand.containsManaRaw(wandStack, source, exactCentimana), "Contains returned false for exact held");
        assertFalse(helper, wand.containsManaRaw(wandStack, source, greaterCentimana), "Contains returned true for greater than held");

        helper.succeed();
    }
}
