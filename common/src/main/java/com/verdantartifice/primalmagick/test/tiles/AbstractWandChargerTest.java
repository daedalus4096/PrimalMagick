package com.verdantartifice.primalmagick.test.tiles;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.essence.EssenceItem;
import com.verdantartifice.primalmagick.common.items.wands.IHasWandComponents;
import com.verdantartifice.primalmagick.common.menus.WandChargerMenu;
import com.verdantartifice.primalmagick.common.tiles.mana.WandChargerTileEntity;
import com.verdantartifice.primalmagick.common.wands.WandCap;
import com.verdantartifice.primalmagick.common.wands.WandCore;
import com.verdantartifice.primalmagick.common.wands.WandGem;
import com.verdantartifice.primalmagick.platform.Services;
import com.verdantartifice.primalmagick.test.AbstractBaseTest;
import com.verdantartifice.primalmagick.test.TestUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class AbstractWandChargerTest extends AbstractBaseTest {
    public void wand_charger_can_have_its_menu_opened(GameTestHelper helper) {
        // Create a test player
        var player = this.makeMockServerPlayer(helper);

        // Place a wand charger block and get its block entity
        var pos = BlockPos.ZERO;
        helper.setBlock(pos, BlocksPM.WAND_CHARGER.get());
        var tile = helper.<WandChargerTileEntity>getBlockEntity(pos);

        // Open the block entity menu
        Services.PLAYER.openMenu(player, tile, pos);
        assertInstanceOf(helper, player.containerMenu, WandChargerMenu.class, "Menu not of expected type");

        helper.succeed();
    }

    public Collection<TestFunction> wand_charger_output_allows_chargeable_items(String templateName) {
        Map<String, ItemStack> testParams = ImmutableMap.<String, ItemStack>builder()
                .put("mundane_wand", ItemsPM.MUNDANE_WAND.get().getDefaultInstance())
                .put("modular_wand", IHasWandComponents.setWandComponents(ItemsPM.MODULAR_WAND.get().getDefaultInstance(), WandCore.HEARTWOOD, WandCap.IRON, WandGem.APPRENTICE))
                .put("modular_staff", IHasWandComponents.setWandComponents(ItemsPM.MODULAR_STAFF.get().getDefaultInstance(), WandCore.HEARTWOOD, WandCap.IRON, WandGem.APPRENTICE))
                .put("warded_armor", ItemsPM.BASIC_WARDING_MODULE.get().applyWard(ItemsPM.PRIMALITE_CHEST.get().getDefaultInstance()))
                .build();
        return TestUtils.createParameterizedTestFunctions("wand_charger_output_allows_chargeable_items", templateName, testParams, (helper, stack) -> {
            // Place a wand charger block and get its output handler
            var handler = this.getItemHandlerForNewWandCharger(helper, Direction.NORTH);

            // Confirm that the output item handler will accept the test item
            helper.assertTrue(handler.isItemValid(0, stack), "Test stack unexpectedly invalid for item handler");

            helper.succeed();
        });
    }

    public Collection<TestFunction> wand_charger_output_does_not_allow_unchargeable_items(String templateName) {
        Map<String, ItemStack> testParams = ImmutableMap.<String, ItemStack>builder()
                .put("stick", Items.STICK.getDefaultInstance())
                .put("earth_shard", ItemsPM.ESSENCE_DUST_EARTH.get().getDefaultInstance())
                .put("unwarded_armor", ItemsPM.PRIMALITE_CHEST.get().getDefaultInstance())
                .build();
        return TestUtils.createParameterizedTestFunctions("wand_charger_output_does_not_allow_unchargeable_items", templateName, testParams, (helper, stack) -> {
            // Place a wand charger block and get its output handler
            var handler = this.getItemHandlerForNewWandCharger(helper, Direction.NORTH);

            // Confirm that the output item handler will accept the test item
            helper.assertFalse(handler.isItemValid(0, stack), "Test stack unexpectedly valid for item handler");

            helper.succeed();
        });
    }

    public Collection<TestFunction> wand_charger_input_allows_essence(String templateName) {
        Map<String, ItemStack> testParams = EssenceItem.getAllEssences().stream().collect(Collectors.toMap(
                i -> Services.ITEMS_REGISTRY.getKey(i).getPath(),
                Item::getDefaultInstance
        ));
        return TestUtils.createParameterizedTestFunctions("wand_charger_input_allows_essence", templateName, testParams, (helper, stack) -> {
            // Place a wand charger block and get its input handler
            var handler = this.getItemHandlerForNewWandCharger(helper, Direction.UP);

            // Confirm that the output item handler will accept the test item
            helper.assertTrue(handler.isItemValid(0, stack), "Test stack unexpectedly invalid for item handler");

            helper.succeed();
        });
    }

    public Collection<TestFunction> wand_charger_input_does_not_allow_non_essence(String templateName) {
        Map<String, ItemStack> testParams = ImmutableMap.<String, ItemStack>builder()
                .put("mundane_wand", ItemsPM.MUNDANE_WAND.get().getDefaultInstance())
                .put("stick", Items.STICK.getDefaultInstance())
                .put("unwarded_armor", ItemsPM.PRIMALITE_CHEST.get().getDefaultInstance())
                .build();
        return TestUtils.createParameterizedTestFunctions("wand_charger_input_does_not_allow_non_essence", templateName, testParams, (helper, stack) -> {
            // Place a wand charger block and get its output handler
            var handler = this.getItemHandlerForNewWandCharger(helper, Direction.UP);

            // Confirm that the output item handler will accept the test item
            helper.assertFalse(handler.isItemValid(0, stack), "Test stack unexpectedly valid for item handler");

            helper.succeed();
        });
    }

    private IItemHandlerPM getItemHandlerForNewWandCharger(GameTestHelper helper, Direction direction) {
        // Place a wand charger block and get its block entity
        var pos = BlockPos.ZERO;
        helper.setBlock(pos, BlocksPM.WAND_CHARGER.get());
        var tile = helper.<WandChargerTileEntity>getBlockEntity(pos);

        // Get the item handler for the block entity for the given face
        var handler = tile.getRawItemHandler(direction);
        helper.assertFalse(handler == null, "No item handler found");

        return handler;
    }

    public Collection<TestFunction> wand_charger_can_charge_with_right_items(String templateName) {
        Map<String, ItemStack> testParams = ImmutableMap.<String, ItemStack>builder()
                .put("mundane_wand", ItemsPM.MUNDANE_WAND.get().getDefaultInstance())
                .put("modular_wand", IHasWandComponents.setWandComponents(ItemsPM.MODULAR_WAND.get().getDefaultInstance(), WandCore.HEARTWOOD, WandCap.IRON, WandGem.APPRENTICE))
                .put("modular_staff", IHasWandComponents.setWandComponents(ItemsPM.MODULAR_STAFF.get().getDefaultInstance(), WandCore.HEARTWOOD, WandCap.IRON, WandGem.APPRENTICE))
                .put("warded_armor", ItemsPM.BASIC_WARDING_MODULE.get().applyWard(ItemsPM.PRIMALITE_CHEST.get().getDefaultInstance()))
                .build();
        return TestUtils.createParameterizedTestFunctions("wand_charger_can_charge_with_right_items", templateName, testParams, (helper, stack) -> {
            // Place a wand charger block and get its block entity
            var pos = BlockPos.ZERO;
            helper.setBlock(pos, BlocksPM.WAND_CHARGER.get());
            var tile = helper.<WandChargerTileEntity>getBlockEntity(pos);

            // Fill the block entity with essence and a chargeable item
            tile.setItem(WandChargerTileEntity.INPUT_INV_INDEX, 0, ItemsPM.ESSENCE_DUST_EARTH.get().getDefaultInstance());
            tile.setItem(WandChargerTileEntity.CHARGE_INV_INDEX, 0, stack);

            // Confirm that the charger can charge with the inputs provided
            helper.assertTrue(tile.canCharge(), "Unable to charge");

            helper.succeed();
        });
    }

    public Collection<TestFunction> wand_charger_do_charge_with_right_items(String templateName) {
        Map<String, ItemStack> testParams = ImmutableMap.<String, ItemStack>builder()
                .put("mundane_wand", ItemsPM.MUNDANE_WAND.get().getDefaultInstance())
                .put("modular_wand", IHasWandComponents.setWandComponents(ItemsPM.MODULAR_WAND.get().getDefaultInstance(), WandCore.HEARTWOOD, WandCap.IRON, WandGem.APPRENTICE))
                .put("modular_staff", IHasWandComponents.setWandComponents(ItemsPM.MODULAR_STAFF.get().getDefaultInstance(), WandCore.HEARTWOOD, WandCap.IRON, WandGem.APPRENTICE))
                .put("warded_armor", ItemsPM.BASIC_WARDING_MODULE.get().applyWard(ItemsPM.PRIMALITE_CHEST.get().getDefaultInstance()))
                .build();
        return TestUtils.createParameterizedTestFunctions("wand_charger_do_charge_with_right_items", templateName, testParams, (helper, stack) -> {
            // Place a wand charger block and get its block entity
            var pos = BlockPos.ZERO;
            helper.setBlock(pos, BlocksPM.WAND_CHARGER.get());
            var tile = helper.<WandChargerTileEntity>getBlockEntity(pos);

            // Fill the block entity with essence and a chargeable item
            var essenceItem = ItemsPM.ESSENCE_DUST_EARTH.get();
            tile.setItem(WandChargerTileEntity.INPUT_INV_INDEX, 0, essenceItem.getDefaultInstance());
            tile.setItem(WandChargerTileEntity.CHARGE_INV_INDEX, 0, stack);

            // Confirm that the test stack has mana storage and note its initial load of the relevant mana source
            helper.assertTrue(stack.has(DataComponentsPM.CAPABILITY_MANA_STORAGE.get()), "Stack has no starting mana storage");
            var before = stack.getOrDefault(DataComponentsPM.CAPABILITY_MANA_STORAGE.get(), ManaStorage.EMPTY).getManaStored(essenceItem.getSource());

            // Attempt the charge
            tile.doCharge();

            // Confirm that the output items are correct
            helper.assertTrue(tile.getItem(WandChargerTileEntity.INPUT_INV_INDEX, 0).isEmpty(), "Input stack not empty");
            helper.assertFalse(tile.getItem(WandChargerTileEntity.CHARGE_INV_INDEX, 0).isEmpty(), "Charge stack empty");

            // Confirm that the test stack's mana load for the relevant source has increased by the correct amount
            helper.assertTrue(stack.has(DataComponentsPM.CAPABILITY_MANA_STORAGE.get()), "Stack has no ending mana storage");
            helper.assertValueEqual(stack.getOrDefault(
                    DataComponentsPM.CAPABILITY_MANA_STORAGE.get(), ManaStorage.EMPTY).getManaStored(essenceItem.getSource()),
                    before + essenceItem.getEssenceType().getManaEquivalent(),
                    "Final mana load not as expected");

            helper.succeed();
        });
    }
}
