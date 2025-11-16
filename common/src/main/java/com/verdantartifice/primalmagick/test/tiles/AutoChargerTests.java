package com.verdantartifice.primalmagick.test.tiles;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.tiles.mana.AbstractManaFontTileEntity;
import com.verdantartifice.primalmagick.common.tiles.mana.AutoChargerTileEntity;
import com.verdantartifice.primalmagick.test.AbstractBaseTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.BlockHitResult;

public class AutoChargerTests extends AbstractBaseTest {
    private static ItemStack getChargeableTestStack() {
        // TODO Add support for other test items, like modular wands, modular staves, and warded armor
        return ItemsPM.MUNDANE_WAND.get().getDefaultInstance();
    }

    private static ItemStack getUnchargableTestStack() {
        // TODO Add support for other test items, such as earth shards and unwarded armor
        return Items.STICK.getDefaultInstance();
    }

    private static IItemHandlerPM getItemHandlerForNewAutoCharger(GameTestHelper helper, BlockPos pos, Direction face) {
        // Place an auto charger block and get its block entity
        helper.setBlock(pos, BlocksPM.AUTO_CHARGER.get());
        var tile = helper.getBlockEntity(pos, AutoChargerTileEntity.class);

        // Get the item handler for the block entity for the given face
        var handler = tile.getRawItemHandler(face);
        assertFalse(helper, handler == null, "No item handler found");

        return handler;
    }

    public static void auto_charger_output_allows_chargeable_items(GameTestHelper helper) {
        var stack = getChargeableTestStack();

        // Place an auto charger block and get its output handler
        var handler = getItemHandlerForNewAutoCharger(helper, BlockPos.ZERO, Direction.NORTH);

        // Confirm that the output item handler will accept the test item
        assertTrue(helper, handler.isItemValid(0, stack), "Test stack unexpectedly invalid for item handler");

        helper.succeed();
    }

    public static void auto_charger_output_does_not_allow_unchargeable_items(GameTestHelper helper) {
        var stack = getUnchargableTestStack();

        // Place an auto charger block and get its output handler
        var handler = getItemHandlerForNewAutoCharger(helper, BlockPos.ZERO, Direction.NORTH);

        // Confirm that the output item handler will accept the test item
        assertFalse(helper, handler.isItemValid(0, stack), "Test stack unexpectedly valid for item handler");

        helper.succeed();
    }

    public static void auto_charger_can_have_chargeable_items_inserted(GameTestHelper helper) {
        var stack = getChargeableTestStack();

        // Track a copy of the test stack for later
        var before = stack.copy();

        // Create a test player with a chargeable item in hand
        var player = makeMockServerPlayer(helper);
        player.setItemInHand(InteractionHand.MAIN_HAND, stack);

        // Place an auto charger and get its item handler
        var chargerPos = BlockPos.ZERO;
        var handler = getItemHandlerForNewAutoCharger(helper, chargerPos, Direction.UP);
        assertTrue(helper, handler.getStackInSlot(0).isEmpty(), "Charger has an item before use");

        // Use the player's main hand item on the charger
        var chargerState = helper.getBlockState(chargerPos);
        var hitResult = new BlockHitResult(helper.absolutePos(chargerPos).getCenter(), Direction.UP, helper.absolutePos(chargerPos), true);
        var useResult = chargerState.useItemOn(stack, helper.getLevel(), player, InteractionHand.MAIN_HAND, hitResult);

        // Confirm success
        assertTrue(helper, useResult.consumesAction(), "Use action failed");
        assertFalse(helper, handler.getStackInSlot(0).isEmpty(), "Charger has no item after use");
        assertTrue(helper, handler.getStackInSlot(0).is(before.getItem()), "Charge item does not match initial stack");

        helper.succeed();
    }

    public static void auto_charger_cannot_have_unchargeable_items_inserted(GameTestHelper helper) {
        var stack = getUnchargableTestStack();

        // Create a test player with a chargeable item in hand
        var player = makeMockServerPlayer(helper);
        player.setItemInHand(InteractionHand.MAIN_HAND, stack);

        // Place an auto charger and get its item handler
        var chargerPos = BlockPos.ZERO.north();
        var handler = getItemHandlerForNewAutoCharger(helper, chargerPos, Direction.UP);
        assertTrue(helper, handler.getStackInSlot(0).isEmpty(), "Charger has an item before use");

        // Use the player's main hand item on the charger
        var chargerState = helper.getBlockState(chargerPos);
        var hitResult = new BlockHitResult(chargerPos.getCenter(), Direction.UP, chargerPos, true);
        var useResult = chargerState.useItemOn(stack, helper.getLevel(), player, InteractionHand.MAIN_HAND, hitResult);

        // Confirm success
        assertFalse(helper, useResult.consumesAction(), "Use action unexpectedly succeeded");
        assertTrue(helper, handler.getStackInSlot(0).isEmpty(), "Charger has item after use");

        helper.succeed();
    }

    public static void auto_charger_can_have_chargeable_items_removed(GameTestHelper helper) {
        var stack = getChargeableTestStack();

        // Track a copy of the test stack for later
        var before = stack.copy();

        // Create a test player
        var player = makeMockServerPlayer(helper);
        player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);

        // Place an auto charger, get its item handler, and insert the test stack
        var chargerPos = BlockPos.ZERO;
        var handler = getItemHandlerForNewAutoCharger(helper, chargerPos, Direction.UP);
        handler.setStackInSlot(0, stack);
        assertFalse(helper, handler.getStackInSlot(0).isEmpty(), "Failed to set item in charger");

        // Use the player's empty main hand on the charger
        var chargerState = helper.getBlockState(chargerPos);
        var hitResult = new BlockHitResult(helper.absolutePos(chargerPos).getCenter(), Direction.UP, helper.absolutePos(chargerPos), true);
        var useResult = chargerState.useItemOn(ItemStack.EMPTY, helper.getLevel(), player, InteractionHand.MAIN_HAND, hitResult);

        // Confirm success
        assertTrue(helper, useResult.consumesAction(), "Use action failed");
        assertTrue(helper, handler.getStackInSlot(0).isEmpty(), "Charger has item after use");
        assertTrue(helper, player.getItemInHand(InteractionHand.MAIN_HAND).is(before.getItem()), "Hand item does not match initial stack");

        helper.succeed();
    }

    public static void auto_charger_siphons_into_chargeable_items(GameTestHelper helper) {
        var baseStack = getChargeableTestStack();

        // Get a clean stack free of state from previous tests
        var stack = baseStack.copy();

        // Place an auto charger block
        var chargerPos = BlockPos.ZERO.north();
        helper.setBlock(chargerPos, BlocksPM.AUTO_CHARGER.get());
        var chargerTile = helper.getBlockEntity(chargerPos, AutoChargerTileEntity.class);

        // Place an earth font block
        var fontPos = BlockPos.ZERO.east();
        helper.setBlock(fontPos, BlocksPM.ARTIFICIAL_FONT_EARTH.get());
        var fontTile = helper.getBlockEntity(fontPos, AbstractManaFontTileEntity.class);
        final int startFontMana = 1000;
        fontTile.setMana(startFontMana);

        // Place the chargeable item stack into the auto charger
        var handler = chargerTile.getRawItemHandler(Direction.NORTH);
        assertFalse(helper, handler == null, "No item handler found");
        handler.setStackInSlot(0, stack);

        // Confirm initial state
        var beforeStack = handler.getStackInSlot(0);
        assertFalse(helper, beforeStack.isEmpty(), "Stack not successfully inserted into charger");
        assertTrue(helper, beforeStack.has(DataComponentsPM.CAPABILITY_MANA_STORAGE.get()), "Before stack has no mana storage");
        assertValueEqual(helper, beforeStack.getOrDefault(DataComponentsPM.CAPABILITY_MANA_STORAGE.get(), ManaStorage.EMPTY).getManaStored(Sources.EARTH), 0, "Before stack not initially empty");
        assertValueEqual(helper, fontTile.getMana(), startFontMana, "Before font mana not as expected");

        // Confirm that mana was successfully siphoned
        final int expectedSiphonAmount = 100;
        helper.succeedOnTickWhen(1, () -> {
            var afterStack = handler.getStackInSlot(0);
            assertFalse(helper, afterStack.isEmpty(), "After stack empty");
            assertTrue(helper, afterStack.has(DataComponentsPM.CAPABILITY_MANA_STORAGE.get()), "After stack has no mana storage");
            int afterStackMana = afterStack.getOrDefault(DataComponentsPM.CAPABILITY_MANA_STORAGE.get(), ManaStorage.EMPTY).getManaStored(Sources.EARTH);
            int fontMana = fontTile.getMana();
            assertValueEqual(helper, afterStackMana, expectedSiphonAmount, "After stack mana total not as expected");
            assertValueEqual(helper, fontMana, startFontMana - expectedSiphonAmount + fontTile.getManaRechargedPerTick(), "After font mana not as expected");
        });
    }
}
