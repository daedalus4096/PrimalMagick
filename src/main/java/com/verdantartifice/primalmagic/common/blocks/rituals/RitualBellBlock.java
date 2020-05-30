package com.verdantartifice.primalmagic.common.blocks.rituals;

import java.util.Collections;
import java.util.Map;

import com.google.common.collect.Maps;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.rituals.IRitualPropBlock;
import com.verdantartifice.primalmagic.common.util.VoxelShapeUtils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.HorizontalFaceBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BellAttachment;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

/**
 * Block definition for a ritual bell.  Ritual bells serve as props in magical rituals; ringing them
 * at the right time can allow a ritual to progress to the next stage.
 * 
 * @author Daedalus4096
 */
public class RitualBellBlock extends Block implements IRitualPropBlock {
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final EnumProperty<BellAttachment> ATTACHMENT = BlockStateProperties.BELL_ATTACHMENT;
    
    protected static final VoxelShape BELL_CORE_SHAPE = VoxelShapes.or(Block.makeCuboidShape(5.0D, 6.0D, 5.0D, 11.0D, 13.0D, 11.0D), Block.makeCuboidShape(4.0D, 4.0D, 4.0D, 12.0D, 6.0D, 12.0D));
    protected static final VoxelShape FLOOR_SHAPE = VoxelShapes.or(BELL_CORE_SHAPE, VoxelShapeUtils.fromModel(new ResourceLocation(PrimalMagic.MODID, "block/ritual_bell_floor")));
    protected static final VoxelShape CEILING_SHAPE = VoxelShapes.or(BELL_CORE_SHAPE, VoxelShapeUtils.fromModel(new ResourceLocation(PrimalMagic.MODID, "block/ritual_bell_ceiling")));
    protected static final VoxelShape ONE_WALL_SHAPE = VoxelShapes.or(BELL_CORE_SHAPE, VoxelShapeUtils.fromModel(new ResourceLocation(PrimalMagic.MODID, "block/ritual_bell_wall")));
    protected static final VoxelShape TWO_WALLS_SHAPE = VoxelShapes.or(BELL_CORE_SHAPE, VoxelShapeUtils.fromModel(new ResourceLocation(PrimalMagic.MODID, "block/ritual_bell_between_walls")));
    protected static final Map<BellAttachment, Map<Direction, VoxelShape>> SHAPES = Util.make(Maps.newEnumMap(BellAttachment.class), map -> {
        map.put(BellAttachment.FLOOR, Util.make(Maps.newEnumMap(Direction.class), innerMap -> {
            innerMap.put(Direction.NORTH, FLOOR_SHAPE);
            innerMap.put(Direction.SOUTH, VoxelShapeUtils.rotate(FLOOR_SHAPE, Direction.Axis.Y, Rotation.CLOCKWISE_180));
            innerMap.put(Direction.WEST, VoxelShapeUtils.rotate(FLOOR_SHAPE, Direction.Axis.Y, Rotation.CLOCKWISE_90));
            innerMap.put(Direction.EAST, VoxelShapeUtils.rotate(FLOOR_SHAPE, Direction.Axis.Y, Rotation.COUNTERCLOCKWISE_90));
        }));
        map.put(BellAttachment.CEILING, Util.make(Maps.newEnumMap(Direction.class), innerMap -> {
            innerMap.put(Direction.NORTH, CEILING_SHAPE);
            innerMap.put(Direction.SOUTH, VoxelShapeUtils.rotate(CEILING_SHAPE, Direction.Axis.Y, Rotation.CLOCKWISE_180));
            innerMap.put(Direction.WEST, VoxelShapeUtils.rotate(CEILING_SHAPE, Direction.Axis.Y, Rotation.CLOCKWISE_90));
            innerMap.put(Direction.EAST, VoxelShapeUtils.rotate(CEILING_SHAPE, Direction.Axis.Y, Rotation.COUNTERCLOCKWISE_90));
        }));
        map.put(BellAttachment.SINGLE_WALL, Util.make(Maps.newEnumMap(Direction.class), innerMap -> {
            innerMap.put(Direction.NORTH, VoxelShapeUtils.rotate(ONE_WALL_SHAPE, Direction.Axis.Y, Rotation.CLOCKWISE_90));
            innerMap.put(Direction.SOUTH, VoxelShapeUtils.rotate(ONE_WALL_SHAPE, Direction.Axis.Y, Rotation.COUNTERCLOCKWISE_90));
            innerMap.put(Direction.WEST, VoxelShapeUtils.rotate(ONE_WALL_SHAPE, Direction.Axis.Y, Rotation.CLOCKWISE_180));
            innerMap.put(Direction.EAST, ONE_WALL_SHAPE);
        }));
        map.put(BellAttachment.DOUBLE_WALL, Util.make(Maps.newEnumMap(Direction.class), innerMap -> {
            innerMap.put(Direction.NORTH, VoxelShapeUtils.rotate(TWO_WALLS_SHAPE, Direction.Axis.Y, Rotation.CLOCKWISE_90));
            innerMap.put(Direction.SOUTH, VoxelShapeUtils.rotate(TWO_WALLS_SHAPE, Direction.Axis.Y, Rotation.COUNTERCLOCKWISE_90));
            innerMap.put(Direction.WEST, VoxelShapeUtils.rotate(TWO_WALLS_SHAPE, Direction.Axis.Y, Rotation.CLOCKWISE_180));
            innerMap.put(Direction.EAST, TWO_WALLS_SHAPE);
        }));
    });
    
    public RitualBellBlock() {
        super(Block.Properties.create(Material.IRON, MaterialColor.CYAN).hardnessAndResistance(5.0F).sound(SoundType.ANVIL).harvestTool(ToolType.PICKAXE));
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH).with(ATTACHMENT, BellAttachment.FLOOR));
    }
    
    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder) {
        builder.add(FACING, ATTACHMENT);
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        BellAttachment attachment = state.get(ATTACHMENT);
        Direction facing = state.get(FACING);
        return SHAPES.getOrDefault(attachment, Collections.emptyMap()).getOrDefault(facing, BELL_CORE_SHAPE);
    }
    
    protected static Direction getAttachmentDirection(BlockState state) {
        switch (state.get(ATTACHMENT)) {
        case FLOOR:
            return Direction.DOWN;
        case CEILING:
            return Direction.UP;
        default:
            return state.get(FACING);
        }
    }
    
    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return HorizontalFaceBlock.func_220185_b(worldIn, pos, getAttachmentDirection(state));
    }
    
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        Direction direction = context.getFace();
        BlockPos blockpos = context.getPos();
        World world = context.getWorld();
        Direction.Axis axis = direction.getAxis();
        
        if (axis == Direction.Axis.Y) {
            BlockState blockstate = this.getDefaultState().with(ATTACHMENT, direction == Direction.DOWN ? BellAttachment.CEILING : BellAttachment.FLOOR).with(FACING, context.getPlacementHorizontalFacing());
            if (blockstate.isValidPosition(context.getWorld(), blockpos)) {
                return blockstate;
            }
        } else {
            boolean flag = axis == Direction.Axis.X && world.getBlockState(blockpos.west()).isSolidSide(world, blockpos.west(), Direction.EAST) && world.getBlockState(blockpos.east()).isSolidSide(world, blockpos.east(), Direction.WEST) || axis == Direction.Axis.Z && world.getBlockState(blockpos.north()).isSolidSide(world, blockpos.north(), Direction.SOUTH) && world.getBlockState(blockpos.south()).isSolidSide(world, blockpos.south(), Direction.NORTH);
            BlockState blockstate1 = this.getDefaultState().with(FACING, direction.getOpposite()).with(ATTACHMENT, flag ? BellAttachment.DOUBLE_WALL : BellAttachment.SINGLE_WALL);
            if (blockstate1.isValidPosition(context.getWorld(), context.getPos())) {
                return blockstate1;
            }

            boolean flag1 = world.getBlockState(blockpos.down()).isSolidSide(world, blockpos.down(), Direction.UP);
            blockstate1 = blockstate1.with(ATTACHMENT, flag1 ? BellAttachment.FLOOR : BellAttachment.CEILING);
            if (blockstate1.isValidPosition(context.getWorld(), context.getPos())) {
                return blockstate1;
            }
        }
        
        return null;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        BellAttachment attachment = stateIn.get(ATTACHMENT);
        Direction direction = getAttachmentDirection(stateIn);
        if (direction == facing && !stateIn.isValidPosition(worldIn, currentPos) && attachment != BellAttachment.DOUBLE_WALL) {
            return Blocks.AIR.getDefaultState();
        } else {
            if (facing.getAxis() == stateIn.get(FACING).getAxis()) {
                if (attachment == BellAttachment.DOUBLE_WALL && !facingState.isSolidSide(worldIn, facingPos, facing)) {
                    return stateIn.with(ATTACHMENT, BellAttachment.SINGLE_WALL).with(FACING, facing.getOpposite());
                }
                if (attachment == BellAttachment.SINGLE_WALL && direction.getOpposite() == facing && facingState.isSolidSide(worldIn, facingPos, stateIn.get(FACING))) {
                    return stateIn.with(ATTACHMENT, BellAttachment.DOUBLE_WALL);
                }
            }
            return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
        }
    }
    
    @Override
    public PushReaction getPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }
    
    @Override
    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }
    
    @Override
    public float getStabilityBonus(World world, BlockPos pos) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float getSymmetryPenalty(World world, BlockPos pos) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isPropActivated(BlockState state, World world, BlockPos pos) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getPropTranslationKey() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public float getUsageStabilityBonus() {
        // TODO Auto-generated method stub
        return 0;
    }
}
