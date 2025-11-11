package com.verdantartifice.primalmagick.test.capabilities;

import com.verdantartifice.primalmagick.platform.Services;
import com.verdantartifice.primalmagick.test.AbstractBaseTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ItemHandlerTests extends AbstractBaseTest {
    public static void block_entity_can_retrieve_item_handler_with_null_direction(GameTestHelper helper, Block block) {
        // Place a copy of the block into the test world
        var pos = BlockPos.ZERO;
        helper.setBlock(pos, block);
        var tile = helper.getBlockEntity(pos, BlockEntity.class);

        // Confirm that the item handler for the block entity can be fetched with a null direction without crashing
        assertTrue(helper, Services.ITEM_HANDLERS.touch(tile, null), "Failed to get item handler");
        helper.succeed();
    }
}
