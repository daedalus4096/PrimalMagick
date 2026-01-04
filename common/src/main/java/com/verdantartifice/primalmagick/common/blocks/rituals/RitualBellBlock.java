package com.verdantartifice.primalmagick.common.blocks.rituals;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.rituals.IRitualPropBlock;
import com.verdantartifice.primalmagick.common.tiles.BlockEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.rituals.RitualBellTileEntity;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.common.util.VoxelShapeUtils;
import net.minecraft.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BellAttachType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.awt.Color;
import java.util.Collections;
import java.util.Map;

/**
 * Block definition for a ritual bell.  Ritual bells serve as props in magickal rituals; ringing them
 * at the right time can allow a ritual to progress to the next stage.
 * 
 * @author Daedalus4096
 */
public class RitualBellBlock extends BaseEntityBlock implements IRitualPropBlock {
    public static final MapCodec<RitualBellBlock> CODEC = simpleCodec(RitualBellBlock::new);
    
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<BellAttachType> ATTACHMENT = BlockStateProperties.BELL_ATTACHMENT;
    
    protected static final VoxelShape BELL_CORE_SHAPE = Shapes.or(Block.box(5.0D, 6.0D, 5.0D, 11.0D, 13.0D, 11.0D), Block.box(4.0D, 4.0D, 4.0D, 12.0D, 6.0D, 12.0D));
    protected static final VoxelShape FLOOR_SHAPE = Shapes.or(BELL_CORE_SHAPE, VoxelShapeUtils.fromModel(ResourceUtils.loc("block/ritual_bell_floor")));
    protected static final VoxelShape CEILING_SHAPE = Shapes.or(BELL_CORE_SHAPE, VoxelShapeUtils.fromModel(ResourceUtils.loc("block/ritual_bell_ceiling")));
    protected static final VoxelShape ONE_WALL_SHAPE = Shapes.or(BELL_CORE_SHAPE, VoxelShapeUtils.fromModel(ResourceUtils.loc("block/ritual_bell_wall")));
    protected static final VoxelShape TWO_WALLS_SHAPE = Shapes.or(BELL_CORE_SHAPE, VoxelShapeUtils.fromModel(ResourceUtils.loc("block/ritual_bell_between_walls")));
    protected static final Map<BellAttachType, Map<Direction, VoxelShape>> SHAPES = Util.make(Maps.newEnumMap(BellAttachType.class), map -> {
        map.put(BellAttachType.FLOOR, Util.make(Maps.newEnumMap(Direction.class), innerMap -> {
            innerMap.put(Direction.NORTH, FLOOR_SHAPE);
            innerMap.put(Direction.SOUTH, VoxelShapeUtils.rotate(FLOOR_SHAPE, Direction.Axis.Y, Rotation.CLOCKWISE_180));
            innerMap.put(Direction.WEST, VoxelShapeUtils.rotate(FLOOR_SHAPE, Direction.Axis.Y, Rotation.CLOCKWISE_90));
            innerMap.put(Direction.EAST, VoxelShapeUtils.rotate(FLOOR_SHAPE, Direction.Axis.Y, Rotation.COUNTERCLOCKWISE_90));
        }));
        map.put(BellAttachType.CEILING, Util.make(Maps.newEnumMap(Direction.class), innerMap -> {
            innerMap.put(Direction.NORTH, CEILING_SHAPE);
            innerMap.put(Direction.SOUTH, VoxelShapeUtils.rotate(CEILING_SHAPE, Direction.Axis.Y, Rotation.CLOCKWISE_180));
            innerMap.put(Direction.WEST, VoxelShapeUtils.rotate(CEILING_SHAPE, Direction.Axis.Y, Rotation.CLOCKWISE_90));
            innerMap.put(Direction.EAST, VoxelShapeUtils.rotate(CEILING_SHAPE, Direction.Axis.Y, Rotation.COUNTERCLOCKWISE_90));
        }));
        map.put(BellAttachType.SINGLE_WALL, Util.make(Maps.newEnumMap(Direction.class), innerMap -> {
            innerMap.put(Direction.NORTH, VoxelShapeUtils.rotate(ONE_WALL_SHAPE, Direction.Axis.Y, Rotation.CLOCKWISE_90));
            innerMap.put(Direction.SOUTH, VoxelShapeUtils.rotate(ONE_WALL_SHAPE, Direction.Axis.Y, Rotation.COUNTERCLOCKWISE_90));
            innerMap.put(Direction.WEST, VoxelShapeUtils.rotate(ONE_WALL_SHAPE, Direction.Axis.Y, Rotation.CLOCKWISE_180));
            innerMap.put(Direction.EAST, ONE_WALL_SHAPE);
        }));
        map.put(BellAttachType.DOUBLE_WALL, Util.make(Maps.newEnumMap(Direction.class), innerMap -> {
            innerMap.put(Direction.NORTH, VoxelShapeUtils.rotate(TWO_WALLS_SHAPE, Direction.Axis.Y, Rotation.CLOCKWISE_90));
            innerMap.put(Direction.SOUTH, VoxelShapeUtils.rotate(TWO_WALLS_SHAPE, Direction.Axis.Y, Rotation.COUNTERCLOCKWISE_90));
            innerMap.put(Direction.WEST, VoxelShapeUtils.rotate(TWO_WALLS_SHAPE, Direction.Axis.Y, Rotation.CLOCKWISE_180));
            innerMap.put(Direction.EAST, TWO_WALLS_SHAPE);
        }));
    });
    
    public RitualBellBlock(Block.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(ATTACHMENT, BellAttachType.FLOOR));
    }
    
    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(FACING, ATTACHMENT);
    }
    
    @Override
    @NotNull
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        BellAttachType attachment = state.getValue(ATTACHMENT);
        Direction facing = state.getValue(FACING);
        return SHAPES.getOrDefault(attachment, Collections.emptyMap()).getOrDefault(facing, BELL_CORE_SHAPE);
    }
    
    @Override
    @NotNull
    public RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    protected static Direction getAttachmentDirection(BlockState state) {
        return switch (state.getValue(ATTACHMENT)) {
            case FLOOR -> Direction.DOWN;
            case CEILING -> Direction.UP;
            default -> state.getValue(FACING);
        };
    }
    
    @Override
    public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader worldIn, @NotNull BlockPos pos) {
        Direction direction = getAttachmentDirection(state);
        return direction == Direction.UP ? Block.canSupportCenter(worldIn, pos.above(), Direction.DOWN) : FaceAttachedHorizontalDirectionalBlock.canAttach(worldIn, pos, direction);
    }
    
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getClickedFace();
        BlockPos blockpos = context.getClickedPos();
        Level world = context.getLevel();
        Direction.Axis axis = direction.getAxis();
        
        if (axis == Direction.Axis.Y) {
            BlockState blockState = this.defaultBlockState().setValue(ATTACHMENT, direction == Direction.DOWN ? BellAttachType.CEILING : BellAttachType.FLOOR).setValue(FACING, context.getHorizontalDirection());
            if (blockState.canSurvive(context.getLevel(), blockpos)) {
                return blockState;
            }
        } else {
            boolean flag = axis == Direction.Axis.X && world.getBlockState(blockpos.west()).isFaceSturdy(world, blockpos.west(), Direction.EAST) && world.getBlockState(blockpos.east()).isFaceSturdy(world, blockpos.east(), Direction.WEST) || axis == Direction.Axis.Z && world.getBlockState(blockpos.north()).isFaceSturdy(world, blockpos.north(), Direction.SOUTH) && world.getBlockState(blockpos.south()).isFaceSturdy(world, blockpos.south(), Direction.NORTH);
            BlockState blockState1 = this.defaultBlockState().setValue(FACING, direction.getOpposite()).setValue(ATTACHMENT, flag ? BellAttachType.DOUBLE_WALL : BellAttachType.SINGLE_WALL);
            if (blockState1.canSurvive(context.getLevel(), context.getClickedPos())) {
                return blockState1;
            }

            boolean flag1 = world.getBlockState(blockpos.below()).isFaceSturdy(world, blockpos.below(), Direction.UP);
            blockState1 = blockState1.setValue(ATTACHMENT, flag1 ? BellAttachType.FLOOR : BellAttachType.CEILING);
            if (blockState1.canSurvive(context.getLevel(), context.getClickedPos())) {
                return blockState1;
            }
        }
        
        return null;
    }

    @Override
    @NotNull
    public BlockState updateShape(@NotNull BlockState state, @NotNull LevelReader level, @NotNull ScheduledTickAccess scheduledTickAccess,
                                  @NotNull BlockPos pos, @NotNull Direction direction, @NotNull BlockPos neighborPos, @NotNull BlockState neighborState,
                                  @NotNull RandomSource random) {
        BellAttachType attachment = state.getValue(ATTACHMENT);
        Direction facing = getAttachmentDirection(state);
        if (direction == facing && !state.canSurvive(level, pos) && attachment != BellAttachType.DOUBLE_WALL) {
            return Blocks.AIR.defaultBlockState();
        } else {
            if (direction.getAxis() == state.getValue(FACING).getAxis()) {
                if (attachment == BellAttachType.DOUBLE_WALL && !neighborState.isFaceSturdy(level, neighborPos, direction)) {
                    return state.setValue(ATTACHMENT, BellAttachType.SINGLE_WALL).setValue(FACING, direction.getOpposite());
                }
                if (attachment == BellAttachType.SINGLE_WALL && direction.getOpposite() == direction && neighborState.isFaceSturdy(level, neighborPos, state.getValue(FACING))) {
                    return state.setValue(ATTACHMENT, BellAttachType.DOUBLE_WALL);
                }
            }
            return super.updateShape(state, level, scheduledTickAccess, pos, direction, neighborPos, neighborState, random);
        }
    }
    
    @Override
    public boolean isPathfindable(@NotNull BlockState state, @NotNull PathComputationType type) {
        return false;
    }
    
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new RitualBellTileEntity(pos, state);
    }
    
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return createTickerHelper(type, BlockEntityTypesPM.RITUAL_BELL.get(), RitualBellTileEntity::tick);
    }

    @Override
    public void onProjectileHit(@NotNull Level worldIn, @NotNull BlockState state, @NotNull BlockHitResult hit, @NotNull Projectile projectile) {
        this.tryRing(worldIn, state, hit);
    }
    
    @Override
    @NotNull
    protected InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hit) {
        return this.tryRing(worldIn, state, hit) ? InteractionResult.SUCCESS : InteractionResult.PASS;
    }
    
    protected boolean tryRing(Level world, BlockState state, BlockHitResult hit) {
        Direction dir = hit.getDirection();
        BlockPos pos = hit.getBlockPos();
        if (this.canRingFrom(state, dir, hit.getLocation().y - (double)pos.getY())) {
            this.doRing(state, world, pos, dir);
            return true;
        } else {
            return false;
        }
    }

    protected void doRing(BlockState state, Level world, BlockPos pos, @Nullable Direction dir) {
        BlockEntity tile = world.getBlockEntity(pos);
        if (!world.isClientSide() && tile instanceof RitualBellTileEntity) {
            if (dir == null) {
                dir = world.getBlockState(pos).getValue(FACING);
            }
            ((RitualBellTileEntity)tile).ring(dir);
            world.playSound(null, pos, SoundEvents.BELL_BLOCK, SoundSource.BLOCKS, 2.0F, 1.0F);
            
            // If this block is awaiting activation for an altar, notify it
            if (this.isPropOpen(state, world, pos)) {
                this.onPropActivated(state, world, pos, this.getUsageStabilityBonus());
            }
        }
    }

    protected boolean canRingFrom(BlockState state, Direction dir, double yPos) {
        if (dir.getAxis() != Direction.Axis.Y && yPos <= 0.8124D) {
            Direction facing = state.getValue(FACING);
            BellAttachType attachment = state.getValue(ATTACHMENT);
            return switch (attachment) {
                case FLOOR -> facing.getAxis() == dir.getAxis();
                case SINGLE_WALL, DOUBLE_WALL -> facing.getAxis() != dir.getAxis();
                case CEILING -> true;
            };
        } else {
            return false;
        }
    }
    
    @Override
    public void animateTick(@NotNull BlockState stateIn, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull RandomSource rand) {
        // Show spell sparkles if receiving salt power
        if (this.isBlockSaltPowered(worldIn, pos)) {
            FxDispatcher.INSTANCE.spellTrail(pos.getX() + rand.nextDouble(), pos.getY() + rand.nextDouble(), pos.getZ() + rand.nextDouble(), Color.WHITE.getRGB());
        }
    }

    @Override
    public float getStabilityBonus(Level world, BlockPos pos) {
        return 0.02F;
    }

    @Override
    public float getSymmetryPenalty(Level world, BlockPos pos) {
        return 0.02F;
    }

    @Override
    public boolean isPropActivated(BlockState state, Level world, BlockPos pos) {
        return world.getBlockEntity(pos) instanceof RitualBellTileEntity bell && bell.isRinging();
    }

    @Override
    public String getPropTranslationKey() {
        return "ritual.primalmagick.prop.ritual_bell";
    }

    public float getUsageStabilityBonus() {
        return 10.0F;
    }

    @Override
    @NotNull
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
