package com.verdantartifice.primalmagick.test.capabilities;

import com.google.common.collect.ImmutableList;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.platform.Services;
import com.verdantartifice.primalmagick.test.AbstractBaseTest;
import com.verdantartifice.primalmagick.test.TestUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractItemHandlerTest extends AbstractBaseTest {
    protected abstract boolean touchItemHandler(BlockEntity blockEntity, Direction direction);

    public Collection<TestFunction> block_entity_can_retrieve_item_handler_with_null_direction(String templateName) {
        Map<String, Block> testParams = ImmutableList.<Block>builder()
                .add(BlocksPM.CALCINATOR_BASIC.get(), BlocksPM.CONCOCTER.get(), BlocksPM.RUNECARVING_TABLE.get(),
                        BlocksPM.DISSOLUTION_CHAMBER.get(), BlocksPM.ESSENCE_CASK_ENCHANTED.get(), BlocksPM.ESSENCE_TRANSMUTER.get(),
                        BlocksPM.HONEY_EXTRACTOR.get(), BlocksPM.INFERNAL_FURNACE.get(), BlocksPM.RESEARCH_TABLE.get(),
                        BlocksPM.SANGUINE_CRUCIBLE.get(), BlocksPM.SCRIBE_TABLE.get(), BlocksPM.AUTO_CHARGER.get(),
                        BlocksPM.MANA_NEXUS.get(), BlocksPM.WAND_CHARGER.get(), BlocksPM.MARBLE_BOOKSHELF.get(),
                        BlocksPM.OFFERING_PEDESTAL.get(), BlocksPM.RITUAL_ALTAR.get(), BlocksPM.RITUAL_LECTERN.get())
                .build().stream().collect(Collectors.toMap(i -> Services.BLOCKS_REGISTRY.getKey(i).getPath(), i -> i));
        return TestUtils.createParameterizedTestFunctions("block_entity_can_retrieve_item_handler_with_null_direction", templateName, testParams, (helper, block) -> {
            // Place a copy of the block into the test world
            var pos = BlockPos.ZERO;
            helper.setBlock(pos, block);
            var tile = helper.getBlockEntity(pos);

            // Confirm that the item handler for the block entity can be fetched with a null direction without crashing
            helper.assertTrue(this.touchItemHandler(tile, null), "Failed to get item handler");
            helper.succeed();
        });
    }
}
