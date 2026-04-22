package com.verdantartifice.primalmagick.test.tiles;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.blocks.mana.ManaBatteryBlock;
import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.menus.ManaBatteryMenu;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.tiles.mana.AbstractManaFontTileEntity;
import com.verdantartifice.primalmagick.common.tiles.mana.ManaBatteryTileEntity;
import com.verdantartifice.primalmagick.platform.Services;
import com.verdantartifice.primalmagick.test.AbstractBaseTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ManaBatteryTests extends AbstractBaseTest {
    private static ItemStack getChargeableTestStack() {
        // TODO Add support for other test items, like modular wands, modular staves, and warded armor
        return ItemsPM.MUNDANE_WAND.get().getDefaultInstance();
    }

    private static ItemStack getUnchargableTestStack() {
        // TODO Add support for other test items, such as earth shards and unwarded armor
        return Items.STICK.getDefaultInstance();
    }

    private static ManaBatteryBlock getTestBattery() {
        // TODO Add support for other mana battery instances
        return BlocksPM.MANA_NEXUS.get();
    }

    private static IItemHandlerPM getItemHandlerForNewManaBattery(GameTestHelper helper, ManaBatteryBlock block, Direction direction) {
        // Place a mana battery block and get its block entity
        var pos = BlockPos.ZERO;
        helper.setBlock(pos, block);
        var tile = helper.getBlockEntity(pos, ManaBatteryTileEntity.class);

        // Get the item handler for the block entity for the given face
        var handler = tile.getRawItemHandler(direction);
        assertFalse(helper, handler == null, "No item handler found");

        return handler;
    }

    public static void mana_battery_can_have_its_menu_opened(GameTestHelper helper) {
        var block = getTestBattery();

        // Create a test player
        var player = makeMockServerPlayer(helper);

        // Place a mana battery block and get its block entity
        var pos = BlockPos.ZERO;
        helper.setBlock(pos, block);
        var tile = helper.getBlockEntity(pos, ManaBatteryTileEntity.class);

        // Open the block entity menu
        Services.PLAYER.openMenu(player, tile, pos);
        assertInstanceOf(helper, player.containerMenu, ManaBatteryMenu.class, "Menu not of expected type");

        helper.succeed();
    }

    public static void mana_battery_output_allows_chargeable_items(GameTestHelper helper) {
        var block = getTestBattery();
        var stack = getChargeableTestStack();

        // Place a mana battery block and get its output handler
        var handler = getItemHandlerForNewManaBattery(helper, block, Direction.NORTH);

        // Confirm that the output item handler will accept the test item
        assertTrue(helper, handler.isItemValid(0, stack), "Test stack unexpectedly invalid for item handler");

        helper.succeed();
    }

    public static void mana_battery_output_does_not_allow_unchargeable_items(GameTestHelper helper) {
        var block = getTestBattery();
        var stack = getUnchargableTestStack();

        // Place a mana battery block and get its output handler
        var handler = getItemHandlerForNewManaBattery(helper, block, Direction.NORTH);

        // Confirm that the output item handler will accept the test item
        assertFalse(helper, handler.isItemValid(0, stack), "Test stack unexpectedly valid for item handler");

        helper.succeed();
    }

    public static void mana_battery_input_allows_essence(GameTestHelper helper) {
        var block = getTestBattery();
        var item = ItemsPM.ESSENCE_SHARD_EARTH.get();   // TODO Test other kinds of essences as well

        // Place a mana battery block and get its input handler
        var handler = getItemHandlerForNewManaBattery(helper, block, Direction.UP);

        // Confirm that the input item handler will accept the test item
        assertTrue(helper, handler.isItemValid(0, item.getDefaultInstance()), "Test stack unexpectedly invalid for item handler");

        helper.succeed();
    }

    public static void mana_battery_input_allows_wands(GameTestHelper helper) {
        var block = getTestBattery();
        var stack = ItemsPM.MUNDANE_WAND.get().getDefaultInstance();    // TODO Test other wand types as well

        // Place a mana battery block and get its input handler
        var handler = getItemHandlerForNewManaBattery(helper, block, Direction.UP);

        // Confirm that the input item handler will accept the test item
        assertTrue(helper, handler.isItemValid(0, stack), "Test stack unexpectedly invalid for item handler");

        helper.succeed();
    }

    public static void mana_battery_siphons_from_nearby_fonts(GameTestHelper helper) {
        var block = getTestBattery();
        var font = BlocksPM.ANCIENT_FONT_EARTH.get();   // TODO Test other font types as well

        // Place a mana battery block
        var batteryPos = BlockPos.ZERO.north();
        helper.setBlock(batteryPos, block);
        var batteryTile = helper.getBlockEntity(batteryPos, ManaBatteryTileEntity.class);

        // Place a mana font block
        var fontPos = BlockPos.ZERO.east();
        helper.setBlock(fontPos, font);
        var fontTile = helper.getBlockEntity(fontPos, AbstractManaFontTileEntity.class);
        final int startFontMana = 1000;
        fontTile.setMana(startFontMana);

        // Confirm initial state
        assertValueEqual(helper, fontTile.getMana(), startFontMana, "Before font mana");
        assertTrue(helper, batteryTile.getAllMana().isEmpty(), "Before battery mana not empty");

        // Confirm that mana was siphoned from the font to the battery
        final int transferCap = batteryTile.getBatteryTransferCap();
        final int expectedTransfer = Math.min(startFontMana, transferCap);
        final int expectedFontMana = startFontMana - expectedTransfer + fontTile.getManaRechargedPerTick();
        final SourceList expectedBatteryMana = SourceList.builder().with(font.getSource(), expectedTransfer).build();
        helper.succeedOnTickWhen(1, () -> {
            assertValueEqual(helper, fontTile.getMana(), expectedFontMana, "After font mana");
            assertValueEqual(helper, batteryTile.getAllMana(), expectedBatteryMana, "After battery mana");
        });
    }
}
