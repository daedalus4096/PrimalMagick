package com.verdantartifice.primalmagick.test.items;

import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.test.AbstractBaseTest;
import com.verdantartifice.primalmagick.test.TestUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AbstractBeeswaxTest extends AbstractBaseTest {
    private record WaxableBlock(Block waxOff, Block waxOn) {}

    private static final Map<String, WaxableBlock> TEST_PARAMS = HoneycombItem.WAXABLES.get().entrySet().stream().collect(Collectors.toMap(
            e -> BuiltInRegistries.BLOCK.getKey(e.getKey()).getPath(),
            e -> new WaxableBlock(e.getKey(), e.getValue())));

    public Collection<TestFunction> apply_beeswax_directly(String templateName) {
        return TestUtils.createParameterizedTestFunctions("apply_beeswax_directly", templateName, TEST_PARAMS, (helper, waxable) -> {
            // Create a test player with beeswax in hand
            var player = this.makeMockServerPlayer(helper);
            player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ItemsPM.BEESWAX.get()));

            // Place an unwaxed block at the origin of the test structure
            helper.setBlock(BlockPos.ZERO, waxable.waxOff());

            // Use the beeswax on the block
            var context = new UseOnContext(player, InteractionHand.MAIN_HAND, new BlockHitResult(BlockPos.ZERO.getCenter(), Direction.DOWN, helper.absolutePos(BlockPos.ZERO), true));
            helper.assertTrue(player.getItemInHand(InteractionHand.MAIN_HAND).getItem().useOn(context).indicateItemUse(), "Beeswax use did not succeed");

            // Confirm that the waxed version of the block is now at the origin of the test structure
            helper.succeedIf(() -> {
                helper.assertBlockState(BlockPos.ZERO, state -> state.is(waxable.waxOn()), () -> "Waxed block not found as expected");
            });
        });
    }

    public Collection<TestFunction> apply_beeswax_via_crafting(String templateName) {
        return TestUtils.createParameterizedTestFunctions("apply_beeswax_via_crafting", templateName, TEST_PARAMS, (helper, waxable) -> {
            // Create a crafting input with the beeswax and the expected input item
            var craftingInput = CraftingInput.of(2, 1, List.of(new ItemStack(ItemsPM.BEESWAX.get()), new ItemStack(waxable.waxOff())));

            // Run the crafting input through the recipe manager
            var outputOpt = helper.getLevel().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, craftingInput, helper.getLevel());

            helper.succeedIf(() -> {
                helper.assertTrue(outputOpt.isPresent(), "No recipe found");
                helper.assertTrue(outputOpt.get().value().getResultItem(helper.getLevel().registryAccess()).is(waxable.waxOn().asItem()), "Recipe output not as expected");
            });
        });
    }
}
