package com.verdantartifice.primalmagick.common.blocks.rituals;

import java.awt.Color;
import java.util.Collections;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.rituals.IRitualPropBlock;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.rituals.RitualBellTileEntity;
import com.verdantartifice.primalmagick.common.util.VoxelShapeUtils;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BellAttachType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Block definition for a ritual bell.  Ritual bells serve as props in magickal rituals; ringing them
 * at the right time can allow a ritual to progress to the next stage.
 * 
 * @author Daedalus4096
 */
public class RitualBellBlock extends BaseEntityBlock implements IRitualPropBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<BellAttachType> ATTACHMENT = BlockStateProperties.BELL_ATTACHMENT;
    
    protected static final VoxelShape BELL_CORE_SHAPE = Shapes.or(Block.box(5.0D, 6.0D, 5.0D, 11.0D, 13.0D, 11.0D), Block.box(4.0D, 4.0D, 4.0D, 12.0D, 6.0D, 12.0D));
    protected static final VoxelShape FLOOR_SHAPE = Shapes.or(BELL_CORE_SHAPE, VoxelShapeUtils.fromModel(PrimalMagick.resource("block/ritual_bell_floor")));
    protected static final VoxelShape CEILING_SHAPE = Shapes.or(BELL_CORE_SHAPE, VoxelShapeUtils.fromModel(PrimalMagick.resource("block/ritual_bell_ceiling")));
    protected static final VoxelShape ONE_WALL_SHAPE = Shapes.or(BELL_CORE_SHAPE, VoxelShapeUtils.fromModel(PrimalMagick.resource("block/ritual_bell_wall")));
    protected static final VoxelShape TWO_WALLS_SHAPE = Shapes.or(BELL_CORE_SHAPE, VoxelShapeUtils.fromModel(PrimalMagick.resource("block/ritual_bell_between_walls")));
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
    
    public RitualBellBlock() {
        super(Block.Properties.of().mapColor(MapColor.COLOR_CYAN).pushReaction(PushReaction.DESTROY).strength(3.0F, 6.0F).sound(SoundType.ANVIL));
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(ATTACHMENT, BellAttachType.FLOOR));
    }
    
    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(FACING, ATTACHMENT);
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        BellAttachType attachment = state.getValue(ATTACHMENT);
        Direction facing = state.getValue(FACING);
        return SHAPES.getOrDefault(attachment, Collections.emptyMap()).getOrDefault(facing, BELL_CORE_SHAPE);
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    protected static Direction getAttachmentDirection(BlockState state) {
        switch (state.getValue(ATTACHMENT)) {
        case FLOOR:
            return Direction.DOWN;
        case CEILING:
            return Direction.UP;
        default:
            return state.getValue(FACING);
        }
    }
    
    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
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
            BlockState blockstate = this.defaultBlockState().setValue(ATTACHMENT, direction == Direction.DOWN ? BellAttachType.CEILING : BellAttachType.FLOOR).setValue(FACING, context.getHorizontalDirection());
            if (blockstate.canSurvive(context.getLevel(), blockpos)) {
                return blockstate;
            }
        } else {
            boolean flag = axis == Direction.Axis.X && world.getBlockState(blockpos.west()).isFaceSturdy(world, blockpos.west(), Direction.EAST) && world.getBlockState(blockpos.east()).isFaceSturdy(world, blockpos.east(), Direction.WEST) || axis == Direction.Axis.Z && world.getBlockState(blockpos.north()).isFaceSturdy(world, blockpos.north(), Direction.SOUTH) && world.getBlockState(blockpos.south()).isFaceSturdy(world, blockpos.south(), Direction.NORTH);
            BlockState blockstate1 = this.defaultBlockState().setValue(FACING, direction.getOpposite()).setValue(ATTACHMENT, flag ? BellAttachType.DOUBLE_WALL : BellAttachType.SINGLE_WALL);
            if (blockstate1.canSurvive(context.getLevel(), context.getClickedPos())) {
                return blockstate1;
            }

            boolean flag1 = world.getBlockState(blockpos.below()).isFaceSturdy(world, blockpos.below(), Direction.UP);
            blockstate1 = blockstate1.setValue(ATTACHMENT, flag1 ? BellAttachType.FLOOR : BellAttachType.CEILING);
            if (blockstate1.canSurvive(context.getLevel(), context.getClickedPos())) {
                return blockstate1;
            }
        }
        
        return null;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        BellAttachType attachment = stateIn.getValue(ATTACHMENT);
        Direction direction = getAttachmentDirection(stateIn);
        if (direction == facing && !stateIn.canSurvive(worldIn, currentPos) && attachment != BellAttachType.DOUBLE_WALL) {
            return Blocks.AIR.defaultBlockState();
        } else {
            if (facing.getAxis() == stateIn.getValue(FACING).getAxis()) {
                if (attachment == BellAttachType.DOUBLE_WALL && !facingState.isFaceSturdy(worldIn, facingPos, facing)) {
                    return stateIn.setValue(ATTACHMENT, BellAttachType.SINGLE_WALL).setValue(FACING, facing.getOpposite());
                }
                if (attachment == BellAttachType.SINGLE_WALL && direction.getOpposite() == facing && facingState.isFaceSturdy(worldIn, facingPos, stateIn.getValue(FACING))) {
                    return stateIn.setValue(ATTACHMENT, BellAttachType.DOUBLE_WALL);
                }
            }
            return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
        }
    }
    
    @Override
    public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
        return false;
    }
    
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RitualBellTileEntity(pos, state);
    }
    
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, TileEntityTypesPM.RITUAL_BELL.get(), RitualBellTileEntity::tick);
    }

    @Override
    public void onProjectileHit(Level worldIn, BlockState state, BlockHitResult hit, Projectile projectile) {
        Entity entity = projectile.getOwner();
        Player playerentity = entity instanceof Player ? (Player)entity : null;
        this.tryRing(worldIn, state, hit, playerentity);
    }
    
    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        return this.tryRing(worldIn, state, hit, player) ? InteractionResult.SUCCESS : InteractionResult.PASS;
    }
    
    protected boolean tryRing(Level world, BlockState state, BlockHitResult hit, @Nullable Player player) {
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
        if (!world.isClientSide && tile instanceof RitualBellTileEntity) {
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
            switch (attachment) {
            case FLOOR:
                return facing.getAxis() == dir.getAxis();
            case SINGLE_WALL:   // Fall through
            case DOUBLE_WALL:
                return facing.getAxis() != dir.getAxis();
            case CEILING:
                return true;
            default:
                return false;
            }
        } else {
            return false;
        }
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        // Close out any pending ritual activity if replaced
        if (!worldIn.isClientSide && state.getBlock() != newState.getBlock()) {
            this.closeProp(state, worldIn, pos);
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }
    
    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
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
}
