package com.verdantartifice.primalmagick.test;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

public class TestUtils {
    public static final Identifier DEFAULT_TEMPLATE = ResourceUtils.loc("test/empty3x3x3");

    public static void placeBed(GameTestHelper helper, BlockPos bedPos) {
        helper.setBlock(bedPos, Blocks.BLUE_BED);
        BlockState footState = helper.getBlockState(bedPos);
        BlockPos headPos = bedPos.relative(footState.getValue(BedBlock.FACING));
        helper.setBlock(headPos, footState.setValue(BedBlock.PART, BedPart.HEAD));
    }
}
