package com.verdantartifice.primalmagick.common.blocks.devices;

import com.verdantartifice.primalmagick.common.tiles.BlockEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.devices.WindGeneratorTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

/**
 * Base class for a wind generator block that moves entities in range.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractWindGeneratorBlock extends BaseEntityBlock {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    
    public AbstractWindGeneratorBlock(Block.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP).setValue(POWERED, false));
    }

    @Override
    @NotNull
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        // TODO Assemble more detailed shape for device
        return Shapes.block();
    }
    
    @Override
    @NotNull
    public RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // Use the clicked face to determine orientation of the block
        return this.defaultBlockState().setValue(FACING, context.getClickedFace()).setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }
    
    @Override
    @NotNull
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }
    
    @Override
    @NotNull
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }
    
    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED);
    }
    
    @Override
    public void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Block blockIn, @Nullable Orientation orientation, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, blockIn, orientation, movedByPiston);
        if (!level.isClientSide()) {
            boolean powered = state.getValue(POWERED);
            if (powered != level.hasNeighborSignal(pos)) {
                level.setBlock(pos, state.cycle(POWERED), UPDATE_CLIENTS);
            }
        }
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new WindGeneratorTileEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return createTickerHelper(type, BlockEntityTypesPM.WIND_GENERATOR.get(), WindGeneratorTileEntity::tick);
    }

    public abstract Direction getWindDirection(BlockState state);
    
    public abstract ParticleOptions getParticleType();

    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (state.getValue(POWERED)) {
            double lifetime = 20D;
            int power = level.getBestNeighborSignal(pos);
            Direction windDir = this.getWindDirection(state);
            Vec3i velocity = windDir.getUnitVec3i().multiply(power);
            Vec3 start = this.getParticleStartPoint(state, level, pos, random);
            level.addParticle(this.getParticleType(), start.x, start.y, start.z, velocity.getX() / lifetime, velocity.getY() / lifetime, velocity.getZ() / lifetime);
        }
    }
    
    protected Vec3 getParticleStartPoint(BlockState state, Level level, BlockPos pos, RandomSource random) {
        Direction dir = state.getValue(FACING);
        Vec3 dirUnit = new Vec3(dir.getStepX(), dir.getStepY(), dir.getStepZ());
        Vec3 center = Vec3.atCenterOf(pos);
        Vec3 retVal = center.add(dirUnit.scale(0.5D));
        for (Axis axis : Axis.values()) {
            Direction deltaDir = Direction.fromAxisAndDirection(axis, AxisDirection.POSITIVE);
            Vec3 normal = new Vec3(deltaDir.step()).scale(random.nextDouble() - 0.5D);
            retVal = retVal.add(normal);
        }
        return retVal;
    }

    public abstract int getCoreColor();
}
