package com.verdantartifice.primalmagick.test.tiles;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.wands.IHasWandComponents;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.tiles.mana.AbstractManaFontTileEntity;
import com.verdantartifice.primalmagick.common.tiles.mana.AutoChargerTileEntity;
import com.verdantartifice.primalmagick.common.wands.WandCap;
import com.verdantartifice.primalmagick.common.wands.WandCore;
import com.verdantartifice.primalmagick.common.wands.WandGem;
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

public class AbstractAutoChargerTest extends AbstractBaseTest {
    private Map<String, ItemStack> getChargeableTestParams() {
        return ImmutableMap.<String, ItemStack>builder()
                .put("mundane_wand", ItemsPM.MUNDANE_WAND.get().getDefaultInstance())
                .put("modular_wand", IHasWandComponents.setWandComponents(ItemsPM.MODULAR_WAND.get().getDefaultInstance(), WandCore.HEARTWOOD, WandCap.IRON, WandGem.APPRENTICE))
                .put("modular_staff", IHasWandComponents.setWandComponents(ItemsPM.MODULAR_STAFF.get().getDefaultInstance(), WandCore.HEARTWOOD, WandCap.IRON, WandGem.APPRENTICE))
                .put("warded_armor", ItemsPM.BASIC_WARDING_MODULE.get().applyWard(ItemsPM.PRIMALITE_CHEST.get().getDefaultInstance()))
                .build();
    }

    private Map<String, ItemStack> getUnchargableTestParams() {
        return ImmutableMap.<String, ItemStack>builder()
                .put("stick", Items.STICK.getDefaultInstance())
                .put("earth_shard", ItemsPM.ESSENCE_DUST_EARTH.get().getDefaultInstance())
                .put("unwarded_armor", ItemsPM.PRIMALITE_CHEST.get().getDefaultInstance())
                .build();
    }

    public Collection<TestFunction> auto_charger_output_allows_chargeable_items(String templateName) {
        var testParams = this.getChargeableTestParams();
        return TestUtils.createParameterizedTestFunctions("auto_charger_output_allows_chargeable_items", templateName, testParams, (helper, stack) -> {
            // Place an auto charger block and get its output handler
            var handler = this.getItemHandlerForNewAutoCharger(helper, BlockPos.ZERO, Direction.NORTH);

            // Confirm that the output item handler will accept the test item
            helper.assertTrue(handler.isItemValid(0, stack), "Test stack unexpectedly invalid for item handler");

            helper.succeed();
        });
    }

    public Collection<TestFunction> auto_charger_output_does_not_allow_unchargeable_items(String templateName) {
        var testParams = this.getUnchargableTestParams();
        return TestUtils.createParameterizedTestFunctions("auto_charger_output_does_not_allow_unchargeable_items", templateName, testParams, (helper, stack) -> {
            // Place an auto charger block and get its output handler
            var handler = this.getItemHandlerForNewAutoCharger(helper, BlockPos.ZERO, Direction.NORTH);

            // Confirm that the output item handler will accept the test item
            helper.assertFalse(handler.isItemValid(0, stack), "Test stack unexpectedly valid for item handler");

            helper.succeed();
        });
    }

    private IItemHandlerPM getItemHandlerForNewAutoCharger(GameTestHelper helper, BlockPos pos, Direction face) {
        // Place an auto charger block and get its block entity
        helper.setBlock(pos, BlocksPM.AUTO_CHARGER.get());
        var tile = helper.<AutoChargerTileEntity>getBlockEntity(pos);

        // Get the item handler for the block entity for the given face
        var handler = tile.getRawItemHandler(face);
        helper.assertFalse(handler == null, "No item handler found");

        return handler;
    }

    public Collection<TestFunction> auto_charger_siphons_into_chargeable_items(String templateName) {
        var stackParams = this.getChargeableTestParams();
        return TestUtils.createParameterizedTestFunctions("auto_charger_siphons_into_chargeable_items", templateName, stackParams, (helper, baseStack) -> {
            // Get a clean stack free of state from previous tests
            var stack = baseStack.copy();

            // Place an auto charger block
            var chargerPos = BlockPos.ZERO.north();
            helper.setBlock(chargerPos, BlocksPM.AUTO_CHARGER.get());
            var chargerTile = helper.<AutoChargerTileEntity>getBlockEntity(chargerPos);
            var chargerState = helper.getBlockState(chargerPos);

            // Place an earth font block
            var fontPos = BlockPos.ZERO.east();
            helper.setBlock(fontPos, BlocksPM.ARTIFICIAL_FONT_EARTH.get());
            var fontTile = helper.<AbstractManaFontTileEntity>getBlockEntity(fontPos);
            final int startFontMana = 1000;
            fontTile.setMana(startFontMana);

            // Place the chargeable item stack into the auto charger
            var handler = chargerTile.getRawItemHandler(Direction.NORTH);
            helper.assertFalse(handler == null, "No item handler found");
            handler.setStackInSlot(0, stack);

            // Confirm initial state
            var beforeStack = handler.getStackInSlot(0);
            helper.assertFalse(beforeStack.isEmpty(), "Stack not successfully inserted into charger");
            helper.assertTrue(beforeStack.has(DataComponentsPM.CAPABILITY_MANA_STORAGE.get()), "Before stack has no mana storage");
            helper.assertValueEqual(beforeStack.getOrDefault(DataComponentsPM.CAPABILITY_MANA_STORAGE.get(), ManaStorage.EMPTY).getManaStored(Sources.EARTH), 0, "Before stack not initially empty");
            helper.assertValueEqual(fontTile.getMana(), startFontMana, "Before font mana not as expected");

            // Run a tick of the auto charger
            AutoChargerTileEntity.tick(helper.getLevel(), chargerPos, chargerState, chargerTile);

            // Confirm that mana was successfully siphoned
            final int expectedSiphonAmount = 100;
            var afterStack = handler.getStackInSlot(0);
            helper.assertFalse(afterStack.isEmpty(), "After stack empty");
            helper.assertTrue(afterStack.has(DataComponentsPM.CAPABILITY_MANA_STORAGE.get()), "After stack has no mana storage");
            helper.assertValueEqual(afterStack.getOrDefault(DataComponentsPM.CAPABILITY_MANA_STORAGE.get(), ManaStorage.EMPTY).getManaStored(Sources.EARTH), expectedSiphonAmount, "After stack mana total not as expected");
            helper.assertValueEqual(fontTile.getMana(), startFontMana - expectedSiphonAmount, "After font mana not as expected");

            helper.succeed();
        });
    }
}
