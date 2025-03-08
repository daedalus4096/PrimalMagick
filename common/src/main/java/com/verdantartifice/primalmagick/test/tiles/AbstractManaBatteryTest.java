package com.verdantartifice.primalmagick.test.tiles;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.blocks.mana.AbstractManaFontBlock;
import com.verdantartifice.primalmagick.common.blocks.mana.ManaBatteryBlock;
import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.essence.EssenceItem;
import com.verdantartifice.primalmagick.common.items.wands.IHasWandComponents;
import com.verdantartifice.primalmagick.common.menus.ManaBatteryMenu;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.tiles.mana.AbstractManaFontTileEntity;
import com.verdantartifice.primalmagick.common.tiles.mana.ManaBatteryTileEntity;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class AbstractManaBatteryTest extends AbstractBaseTest {
    private Map<String, ItemStack> getChargeableTestParams() {
        return ImmutableMap.<String, ItemStack>builder()
                .put("mundane_wand", ItemsPM.MUNDANE_WAND.get().getDefaultInstance())
                .put("modular_wand", IHasWandComponents.setWandComponents(ItemsPM.MODULAR_WAND.get().getDefaultInstance(), WandCore.HEARTWOOD, WandCap.IRON, WandGem.APPRENTICE))
                .put("modular_staff", IHasWandComponents.setWandComponents(ItemsPM.MODULAR_STAFF.get().getDefaultInstance(), WandCore.HEARTWOOD, WandCap.IRON, WandGem.APPRENTICE))
                .put("warded_armor", ItemsPM.BASIC_WARDING_MODULE.get().applyWard(ItemsPM.PRIMALITE_CHEST.get().getDefaultInstance()))
                .build();
    }

    private Map<String, ItemStack> getUnchargeableTestParams() {
        return ImmutableMap.<String, ItemStack>builder()
                .put("stick", Items.STICK.getDefaultInstance())
                .put("earth_shard", ItemsPM.ESSENCE_DUST_EARTH.get().getDefaultInstance())
                .put("unwarded_armor", ItemsPM.PRIMALITE_CHEST.get().getDefaultInstance())
                .build();
    }

    private Map<String, ManaBatteryBlock> getBatteryTestParams() {
        return ImmutableMap.<String, ManaBatteryBlock>builder()
                .put("mana_nexus", BlocksPM.MANA_NEXUS.get())
                .put("mana_singularity", BlocksPM.MANA_SINGULARITY.get())
                .put("creative_mana_singularity", BlocksPM.MANA_SINGULARITY_CREATIVE.get())
                .build();
    }

    public Collection<TestFunction> mana_battery_can_have_its_menu_opened(String templateName) {
        var testParams = this.getBatteryTestParams();
        return TestUtils.createParameterizedTestFunctions("", templateName, testParams, (helper, block) -> {
            // Create a test player
            var player = this.makeMockServerPlayer(helper);

            // Place a mana battery block and get its block entity
            var pos = BlockPos.ZERO;
            helper.setBlock(pos, block);
            var tile = helper.<ManaBatteryTileEntity>getBlockEntity(pos);

            // Open the block entity menu
            Services.PLAYER.openMenu(player, tile, pos);
            assertInstanceOf(helper, player.containerMenu, ManaBatteryMenu.class, "Menu not of expected type");

            helper.succeed();
        });
    }

    public Collection<TestFunction> mana_battery_output_allows_chargeable_items(String templateName) {
        var blockParams = this.getBatteryTestParams();
        var testParams = this.getChargeableTestParams();
        return TestUtils.createDualParameterizedTestFunctions("mana_battery_output_allows_chargeable_items", templateName, blockParams, testParams, (helper, block, stack) -> {
            // Place a mana battery block and get its output handler
            var handler = this.getItemHandlerForNewManaBattery(helper, block, Direction.NORTH);

            // Confirm that the output item handler will accept the test item
            helper.assertTrue(handler.isItemValid(0, stack), "Test stack unexpectedly invalid for item handler");

            helper.succeed();
        });
    }

    public Collection<TestFunction> mana_battery_output_does_not_allow_unchargeable_items(String templateName) {
        var blockParams = this.getBatteryTestParams();
        var testParams = this.getUnchargeableTestParams();
        return TestUtils.createDualParameterizedTestFunctions("mana_battery_output_does_not_allow_unchargeable_items", templateName, blockParams, testParams, (helper, block, stack) -> {
            // Place a mana battery block and get its output handler
            var handler = this.getItemHandlerForNewManaBattery(helper, block, Direction.NORTH);

            // Confirm that the output item handler will accept the test item
            helper.assertFalse(handler.isItemValid(0, stack), "Test stack unexpectedly valid for item handler");

            helper.succeed();
        });
    }

    private IItemHandlerPM getItemHandlerForNewManaBattery(GameTestHelper helper, ManaBatteryBlock block, Direction direction) {
        // Place a mana battery block and get its block entity
        var pos = BlockPos.ZERO;
        helper.setBlock(pos, block);
        var tile = helper.<ManaBatteryTileEntity>getBlockEntity(pos);

        // Get the item handler for the block entity for the given face
        var handler = tile.getRawItemHandler(direction);
        helper.assertFalse(handler == null, "No item handler found");

        return handler;
    }

    public Collection<TestFunction> mana_battery_input_allows_essence(String templateName) {
        var blockParams = this.getBatteryTestParams();
        var essenceParams = EssenceItem.getAllEssences().stream().collect(Collectors.toMap(e -> Services.ITEMS_REGISTRY.getKey(e).getPath(), e -> e));
        return TestUtils.createDualParameterizedTestFunctions("mana_battery_input_allows_essence", templateName, blockParams, essenceParams, (helper, block, item) -> {
            // Place a mana battery block and get its input handler
            var handler = this.getItemHandlerForNewManaBattery(helper, block, Direction.UP);

            // Confirm that the input item handler will accept the test item
            helper.assertTrue(handler.isItemValid(0, item.getDefaultInstance()), "Test stack unexpectedly invalid for item handler");

            helper.succeed();
        });
    }

    public Collection<TestFunction> mana_battery_input_allows_wands(String templateName) {
        var blockParams = this.getBatteryTestParams();
        var wandParams = ImmutableMap.<String, ItemStack>builder()
                .put("mundane_wand", ItemsPM.MUNDANE_WAND.get().getDefaultInstance())
                .put("modular_wand", IHasWandComponents.setWandComponents(ItemsPM.MODULAR_WAND.get().getDefaultInstance(), WandCore.HEARTWOOD, WandCap.IRON, WandGem.APPRENTICE))
                .put("modular_staff", IHasWandComponents.setWandComponents(ItemsPM.MODULAR_STAFF.get().getDefaultInstance(), WandCore.HEARTWOOD, WandCap.IRON, WandGem.APPRENTICE))
                .build();
        return TestUtils.createDualParameterizedTestFunctions("mana_battery_input_allows_wands", templateName, blockParams, wandParams, (helper, block, stack) -> {
            // Place a mana battery block and get its input handler
            var handler = this.getItemHandlerForNewManaBattery(helper, block, Direction.UP);

            // Confirm that the input item handler will accept the test item
            helper.assertTrue(handler.isItemValid(0, stack), "Test stack unexpectedly invalid for item handler");

            helper.succeed();
        });
    }

    public Collection<TestFunction> mana_battery_siphons_from_nearby_fonts(String templateName) {
        // Exclude the Creative Mana Singularity because it's always full by definition
        var blockParams = ImmutableMap.<String, ManaBatteryBlock>builder()
                .put("mana_nexus", BlocksPM.MANA_NEXUS.get())
                .put("mana_singularity", BlocksPM.MANA_SINGULARITY.get())
                .build();
        var fontParams = AbstractManaFontBlock.getAll().stream().collect(Collectors.toMap(b -> Services.BLOCKS_REGISTRY.getKey(b).getPath(), b -> b));
        return TestUtils.createDualParameterizedTestFunctions("mana_battery_siphons_from_nearby_fonts", templateName, blockParams, fontParams, (helper, block, font) -> {
            // Place a mana battery block
            var batteryPos = BlockPos.ZERO.north();
            helper.setBlock(batteryPos, block);
            var batteryTile = helper.<ManaBatteryTileEntity>getBlockEntity(batteryPos);
            var batteryState = helper.getBlockState(batteryPos);

            // Place a mana font block
            var fontPos = BlockPos.ZERO.east();
            helper.setBlock(fontPos, font);
            var fontTile = helper.<AbstractManaFontTileEntity>getBlockEntity(fontPos);
            final int startFontMana = 1000;
            fontTile.setMana(startFontMana);

            // Confirm initial state
            helper.assertValueEqual(fontTile.getMana(), startFontMana, "Before font mana");
            helper.assertTrue(batteryTile.getAllMana().isEmpty(), "Before battery mana not empty");

            // Run a tick of the mana battery
            ManaBatteryTileEntity.tick(helper.getLevel(), helper.absolutePos(batteryPos), batteryState, batteryTile);

            // Confirm that mana was siphoned from the font to the battery
            final int transferCap = batteryTile.getBatteryTransferCap();
            final int expectedTransfer = Math.min(startFontMana, transferCap);
            final int expectedFontMana = startFontMana - expectedTransfer;
            final SourceList expectedBatteryMana = SourceList.builder().with(font.getSource(), expectedTransfer).build();
            helper.assertValueEqual(fontTile.getMana(), expectedFontMana, "After font mana");
            helper.assertValueEqual(batteryTile.getAllMana(), expectedBatteryMana, "After battery mana");

            helper.succeed();
        });
    }
}
