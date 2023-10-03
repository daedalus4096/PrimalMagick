package com.verdantartifice.primalmagick.common.blocks.devices;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.blocks.misc.GlowFieldBlock;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.devices.SunlampTileEntity;
import com.verdantartifice.primalmagick.common.util.VoxelShapeUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Block definition for a sunlamp.  Sunlamps are like normal lanterns, but they spawn glow fields
 * nearby in sufficiently dark spaces.  It can also be attached to any side of a block instead of
 * just the top or bottom.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.common.blocks.misc.GlowFieldBlock}
 */
public class SunlampBlock extends BaseEntityBlock {
    protected static final Logger LOGGER = LogManager.getLogger();
    public static final DirectionProperty ATTACHMENT = DirectionProperty.create("attachment", Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN);
    
    protected static final VoxelShape GROUND_SHAPE = VoxelShapeUtils.fromModel(PrimalMagick.resource("block/sunlamp_ground_base"));
    protected static final VoxelShape HANGING_SHAPE = VoxelShapeUtils.fromModel(PrimalMagick.resource("block/sunlamp_hanging_base"));
    
    protected final Supplier<GlowFieldBlock> glowSupplier;
    
    public SunlampBlock(Supplier<GlowFieldBlock> glowSupplier) {
        super(Block.Properties.of().mapColor(MapColor.METAL).pushReaction(PushReaction.DESTROY).strength(3.5F).sound(SoundType.LANTERN).lightLevel((state) -> { return 15; }).noOcclusion());
        this.registerDefaultState(this.defaultBlockState().setValue(ATTACHMENT, Direction.DOWN));
        this.glowSupplier = glowSupplier;
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return state.getValue(ATTACHMENT) == Direction.DOWN ? GROUND_SHAPE : HANGING_SHAPE;
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(ATTACHMENT);
    }
    
    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = this.defaultBlockState().setValue(ATTACHMENT, context.getClickedFace().getOpposite());
        return state.canSurvive(context.getLevel(), context.getClickedPos()) ? state : null;
    }
    
    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        Direction dir = state.getValue(ATTACHMENT);
        return Block.canSupportCenter(worldIn, pos.relative(dir), dir.getOpposite());
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        return stateIn.getValue(ATTACHMENT) == facing && !stateIn.canSurvive(worldIn, currentPos) ? 
                Blocks.AIR.defaultBlockState() : 
                super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }
    
    @Override
    public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
        return false;
    }
    
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SunlampTileEntity(pos, state);
    }
    
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, TileEntityTypesPM.SUNLAMP.get(), SunlampTileEntity::tick);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        for (int x = -15; x <= 15; x++) {
            for (int y = -15; y <= 15; y++) {
                for (int z = -15; z <= 15; z++) {
                    BlockPos bp = pos.offset(x, y, z);
                    if (worldIn.getBlockState(bp) == BlocksPM.GLOW_FIELD.get().defaultBlockState()) {
                        worldIn.removeBlock(bp, false);
                    }
                }
            }
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }
    
    public GlowFieldBlock getGlowField() {
        return this.glowSupplier.get();
    }
}
