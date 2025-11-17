package com.verdantartifice.primalmagick.test.tiles;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.menus.WandChargerMenu;
import com.verdantartifice.primalmagick.common.tiles.mana.WandChargerTileEntity;
import com.verdantartifice.primalmagick.platform.Services;
import com.verdantartifice.primalmagick.test.AbstractBaseTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class WandChargerTests extends AbstractBaseTest {
    private static ItemStack getChargeableTestStack() {
        // TODO Add support for other test items, like modular wands, modular staves, and warded armor
        return ItemsPM.MUNDANE_WAND.get().getDefaultInstance();
    }

    private static ItemStack getUnchargeableTestStack() {
        // TODO Add support for other test items, such as earth shards and unwarded armor
        return Items.STICK.getDefaultInstance();
    }

    private static IItemHandlerPM getItemHandlerForNewWandCharger(GameTestHelper helper, Direction direction) {
        // Place a wand charger block and get its block entity
        var pos = BlockPos.ZERO;
        helper.setBlock(pos, BlocksPM.WAND_CHARGER.get());
        var tile = helper.getBlockEntity(pos, WandChargerTileEntity.class);

        // Get the item handler for the block entity for the given face
        var handler = tile.getRawItemHandler(direction);
        assertFalse(helper, handler == null, "No item handler found");

        return handler;
    }

    public static void wand_charger_can_have_its_menu_opened(GameTestHelper helper) {
        // Create a test player
        var player = makeMockServerPlayer(helper);

        // Place a wand charger block and get its block entity
        var pos = BlockPos.ZERO;
        helper.setBlock(pos, BlocksPM.WAND_CHARGER.get());
        var tile = helper.getBlockEntity(pos, WandChargerTileEntity.class);

        // Open the block entity menu
        Services.PLAYER.openMenu(player, tile, pos);
        assertInstanceOf(helper, player.containerMenu, WandChargerMenu.class, "Menu not of expected type");

        helper.succeed();
    }

    public static void wand_charger_output_allows_chargeable_items(GameTestHelper helper) {
        var stack = getChargeableTestStack();

        // Place a wand charger block and get its output handler
        var handler = getItemHandlerForNewWandCharger(helper, Direction.NORTH);

        // Confirm that the output item handler will accept the test item
        assertTrue(helper, handler.isItemValid(0, stack), "Test stack unexpectedly invalid for item handler");

        helper.succeed();
    }

    public static void wand_charger_output_does_not_allow_unchargeable_items(GameTestHelper helper) {
        var stack = getUnchargeableTestStack();

        // Place a wand charger block and get its output handler
        var handler = getItemHandlerForNewWandCharger(helper, Direction.NORTH);

        // Confirm that the output item handler will accept the test item
        assertFalse(helper, handler.isItemValid(0, stack), "Test stack unexpectedly valid for item handler");

        helper.succeed();
    }

    public static void wand_charger_input_allows_essence(GameTestHelper helper) {
        var stack = ItemsPM.ESSENCE_SHARD_EARTH.get().getDefaultInstance(); // TODO Allow other essence types

        // Place a wand charger block and get its input handler
        var handler = getItemHandlerForNewWandCharger(helper, Direction.UP);

        // Confirm that the output item handler will accept the test item
        assertTrue(helper, handler.isItemValid(0, stack), "Test stack unexpectedly invalid for item handler");

        helper.succeed();
    }

    public static void wand_charger_input_does_not_allow_non_essence(GameTestHelper helper) {
        var stack = getUnchargeableTestStack();

        // Place a wand charger block and get its output handler
        var handler = getItemHandlerForNewWandCharger(helper, Direction.UP);

        // Confirm that the output item handler will accept the test item
        assertFalse(helper, handler.isItemValid(0, stack), "Test stack unexpectedly valid for item handler");

        helper.succeed();
    }

    public static void wand_charger_can_charge_with_right_items(GameTestHelper helper) {
        var stack = getChargeableTestStack();

        // Place a wand charger block and get its block entity
        var pos = BlockPos.ZERO;
        helper.setBlock(pos, BlocksPM.WAND_CHARGER.get());
        var tile = helper.getBlockEntity(pos, WandChargerTileEntity.class);

        // Fill the block entity with essence and a chargeable item
        tile.setItem(WandChargerTileEntity.INPUT_INV_INDEX, 0, ItemsPM.ESSENCE_DUST_EARTH.get().getDefaultInstance());
        tile.setItem(WandChargerTileEntity.CHARGE_INV_INDEX, 0, stack);

        // Confirm that the charger can charge with the inputs provided
        assertTrue(helper, tile.canCharge(), "Unable to charge");

        helper.succeed();
    }

    public static void wand_charger_do_charge_with_right_items(GameTestHelper helper) {
        var stack = getChargeableTestStack();

        // Place a wand charger block and get its block entity
        var pos = BlockPos.ZERO;
        helper.setBlock(pos, BlocksPM.WAND_CHARGER.get());
        var tile = helper.getBlockEntity(pos, WandChargerTileEntity.class);

        // Fill the block entity with essence and a chargeable item
        var essenceItem = ItemsPM.ESSENCE_DUST_EARTH.get();
        tile.setItem(WandChargerTileEntity.INPUT_INV_INDEX, 0, essenceItem.getDefaultInstance());
        tile.setItem(WandChargerTileEntity.CHARGE_INV_INDEX, 0, stack);

        // Confirm that the test stack has mana storage and note its initial load of the relevant mana source
        assertTrue(helper, stack.has(DataComponentsPM.CAPABILITY_MANA_STORAGE.get()), "Stack has no starting mana storage");
        var before = stack.getOrDefault(DataComponentsPM.CAPABILITY_MANA_STORAGE.get(), ManaStorage.EMPTY).getManaStored(essenceItem.getSource());

        // Attempt the charge
        tile.doCharge();

        // Confirm that the output items are correct
        assertTrue(helper, tile.getItem(WandChargerTileEntity.INPUT_INV_INDEX, 0).isEmpty(), "Input stack not empty");
        assertFalse(helper, tile.getItem(WandChargerTileEntity.CHARGE_INV_INDEX, 0).isEmpty(), "Charge stack empty");

        // Confirm that the test stack's mana load for the relevant source has increased by the correct amount
        assertTrue(helper, stack.has(DataComponentsPM.CAPABILITY_MANA_STORAGE.get()), "Stack has no ending mana storage");
        assertValueEqual(helper, stack.getOrDefault(
                        DataComponentsPM.CAPABILITY_MANA_STORAGE.get(), ManaStorage.EMPTY).getManaStored(essenceItem.getSource()),
                before + essenceItem.getEssenceType().getManaEquivalent(),
                "Final mana load not as expected");

        helper.succeed();
    }
}
