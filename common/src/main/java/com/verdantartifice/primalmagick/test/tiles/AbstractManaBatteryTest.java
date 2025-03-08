package com.verdantartifice.primalmagick.test.tiles;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.blocks.mana.ManaBatteryBlock;
import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.wands.IHasWandComponents;
import com.verdantartifice.primalmagick.common.menus.ManaBatteryMenu;
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

            // Place a wand charger block and get its block entity
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
            // Place a wand charger block and get its output handler
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
            // Place a wand charger block and get its output handler
            var handler = this.getItemHandlerForNewManaBattery(helper, block, Direction.NORTH);

            // Confirm that the output item handler will accept the test item
            helper.assertFalse(handler.isItemValid(0, stack), "Test stack unexpectedly valid for item handler");

            helper.succeed();
        });
    }

    private IItemHandlerPM getItemHandlerForNewManaBattery(GameTestHelper helper, ManaBatteryBlock block, Direction direction) {
        // Place a wand charger block and get its block entity
        var pos = BlockPos.ZERO;
        helper.setBlock(pos, block);
        var tile = helper.<ManaBatteryTileEntity>getBlockEntity(pos);

        // Get the item handler for the block entity for the given face
        var handler = tile.getRawItemHandler(direction);
        helper.assertFalse(handler == null, "No item handler found");

        return handler;
    }
}
