package com.verdantartifice.primalmagick.test.capabilities;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.test.TestUtilsNeoforge;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.gametest.GameTestHolder;

import java.util.Collection;

@GameTestHolder(Constants.MOD_ID)
public class ItemHandlerTestNeoforge extends AbstractItemHandlerTest {
    @Override
    protected boolean touchItemHandler(BlockEntity blockEntity, Direction direction) {
        var handler = blockEntity.getLevel().getCapability(Capabilities.ItemHandler.BLOCK, blockEntity.getBlockPos(), direction);
        return true;
    }

    @GameTestGenerator
    public Collection<TestFunction> block_entity_can_retrieve_item_handler_with_null_direction() {
        return super.block_entity_can_retrieve_item_handler_with_null_direction(TestUtilsNeoforge.DEFAULT_GENERATOR_TEMPLATE);
    }
}
