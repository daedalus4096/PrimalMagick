package com.verdantartifice.primalmagick.common.blocks.misc;

import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.sources.Sources;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

/**
 * Block definition for the glow field.  An invisible, intangible block that provides light which
 * may fade over time.
 * 
 * @author Daedalus4096
 */
public class GlowFieldBlock extends Block implements SimpleWaterloggedBlock {
    public static final IntegerProperty LIGHT = IntegerProperty.create("light", 1, 15);
    public static final BooleanProperty FADING = BooleanProperty.create("fading");
    public static final BooleanProperty SPARKLING = BooleanProperty.create("sparkling");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    
    public GlowFieldBlock() {
        super(Block.Properties.of().air().replaceable().strength(-1, 3600000).lightLevel(state -> state.getValue(LIGHT)).noLootTable().noOcclusion().randomTicks());
        this.registerDefaultState(this.stateDefinition.any().setValue(LIGHT, 15).setValue(FADING, Boolean.FALSE).setValue(SPARKLING, Boolean.TRUE).setValue(WATERLOGGED, Boolean.FALSE));
    }
    
    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(LIGHT, FADING, SPARKLING, WATERLOGGED);
    }

    @Override
    public void animateTick(@NotNull BlockState stateIn, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull RandomSource rand) {
        // Show glittering particles
        super.animateTick(stateIn, worldIn, pos, rand);
        if (stateIn.getValue(SPARKLING)) {
            FxDispatcher.INSTANCE.spellTrail(pos.getX() + rand.nextDouble(), pos.getY() + rand.nextDouble(), pos.getZ() + rand.nextDouble(), Sources.SUN.getColor());
        }
    }
    
    @Override
    @NotNull
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        // Don't show a selection highlight when mousing over the field
        return Shapes.empty();
    }
    
    @Override
    @NotNull
    public RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.INVISIBLE;
    }
    
    @Override
    @NotNull
    public ItemStack getCloneItemStack(@NotNull LevelReader pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, boolean includeData) {
        // Don't work with the creative pick-block feature, as this block has no corresponding item block
        return ItemStack.EMPTY;
    }

    @Override
    public boolean propagatesSkylightDown(@NotNull BlockState state) {
        return true;
    }

    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource rng) {
        if (state.getValue(FADING)) {
            if (state.getValue(LIGHT) <= 1) {
                level.removeBlock(pos, false);
            } else {
                level.setBlock(pos, state.setValue(LIGHT, state.getValue(LIGHT) - 1), Block.UPDATE_CLIENTS);
            }
        }
    }

    @Override
    @NotNull
    public BlockState updateShape(@NotNull BlockState state, @NotNull LevelReader level, @NotNull ScheduledTickAccess scheduledTickAccess,
                                  @NotNull BlockPos pos, @NotNull Direction direction, @NotNull BlockPos neighborPos, @NotNull BlockState neighborState,
                                  @NotNull RandomSource random) {
        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            scheduledTickAccess.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, level, scheduledTickAccess, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    @NotNull
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }
}
