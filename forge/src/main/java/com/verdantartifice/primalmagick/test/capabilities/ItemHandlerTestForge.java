package com.verdantartifice.primalmagick.test.capabilities;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.test.TestUtilsForge;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.gametest.GameTestHolder;

import java.util.Collection;

@GameTestHolder(Constants.MOD_ID + ".forge.item_handler")
public class ItemHandlerTestForge extends AbstractItemHandlerTest {
    @Override
    protected boolean touchItemHandler(BlockEntity blockEntity, Direction direction) {
        var handlerOpt = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, direction);
        return true;
    }

    @GameTestGenerator
    public Collection<TestFunction> block_entity_can_retrieve_item_handler_with_null_direction() {
        return super.block_entity_can_retrieve_item_handler_with_null_direction(TestUtilsForge.DEFAULT_TEMPLATE);
    }
}
