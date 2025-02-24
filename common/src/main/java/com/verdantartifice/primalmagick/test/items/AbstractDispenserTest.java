package com.verdantartifice.primalmagick.test.items;

import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.entities.projectiles.ManaArrowEntity;
import com.verdantartifice.primalmagick.common.items.entities.ManaArrowItem;
import com.verdantartifice.primalmagick.test.AbstractBaseTest;
import com.verdantartifice.primalmagick.test.TestUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class AbstractDispenserTest extends AbstractBaseTest {
    public Collection<TestFunction> mana_arrows_fired_from_dispenser(String templateName) {
        Map<String, ManaArrowItem> testParams = ManaArrowItem.getManaArrows().stream().collect(Collectors.toMap(a -> a.getSource().getId().getPath(), a -> a));
        return TestUtils.createParameterizedTestFunctions("mana_arrows_fired_from_dispenser", templateName, testParams, (helper, arrow) -> {
            // Place a north-facing dispenser at the origin of the structure template
            var dispenserPos = BlockPos.ZERO;
            helper.setBlock(dispenserPos, Blocks.DISPENSER.defaultBlockState().setValue(DispenserBlock.FACING, Direction.NORTH));

            // Attach a button to the south side of the dispenser
            var buttonPos = dispenserPos.south();
            helper.setBlock(buttonPos, Blocks.OAK_BUTTON.defaultBlockState().setValue(ButtonBlock.FACING, Direction.NORTH));

            // Insert an appropriate mana-tinged arrow into the dispenser
            var dispenserEntity = helper.<DispenserBlockEntity>getBlockEntity(dispenserPos);
            dispenserEntity.insertItem(new ItemStack(arrow));

            // Press the button to fire the arrow
            helper.assertBlockState(dispenserPos, state -> !state.getValue(DispenserBlock.TRIGGERED), () -> "Dispenser triggered before expected");
            helper.pressButton(buttonPos);
            helper.assertBlockState(dispenserPos, state -> state.getValue(DispenserBlock.TRIGGERED), () -> "Dispenser was not triggered as expected");

            // Confirm that the dispenser fired the correct arrow instead of dispensing an item stack
            var expectedArrowPos = dispenserPos.north();
            helper.succeedWhen(() -> {
                helper.assertItemEntityNotPresent(arrow, expectedArrowPos, 1D);
                helper.assertEntitiesPresent(EntityTypesPM.MANA_ARROW.get(), expectedArrowPos, 1, 1D);
                helper.getEntities(EntityTypesPM.MANA_ARROW.get(), expectedArrowPos, 1D).forEach(e -> {
                    helper.assertTrue(e.getSource().equals(arrow.getSource()), "Arrow entity source is not as expected: " + e.getSource());
                });
            });
        });
    }
}
