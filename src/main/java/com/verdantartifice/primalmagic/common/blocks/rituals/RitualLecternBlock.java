package com.verdantartifice.primalmagic.common.blocks.rituals;

import java.awt.Color;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.Maps;
import com.verdantartifice.primalmagic.client.fx.FxDispatcher;
import com.verdantartifice.primalmagic.common.rituals.IRitualProp;
import com.verdantartifice.primalmagic.common.util.VoxelShapeUtils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Block definition for a ritual lectern.  Ritual lecterns serve as props in magical rituals; placing
 * an enchanted book on one at the right time can allow a ritual to progress to the next stage.
 * 
 * @author Daedalus4096
 */
public class RitualLecternBlock extends Block implements IRitualProp {
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty HAS_BOOK = BlockStateProperties.HAS_BOOK;

    protected static final VoxelShape BASE_AND_STAND_SHAPE = VoxelShapes.or(Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), Block.makeCuboidShape(4.0D, 2.0D, 4.0D, 12.0D, 14.0D, 12.0D));
    protected static final VoxelShape COLLISION_SHAPE = VoxelShapes.or(BASE_AND_STAND_SHAPE, Block.makeCuboidShape(0.0D, 15.0D, 0.0D, 16.0D, 15.0D, 16.0D));
    protected static final VoxelShape SHAPE = VoxelShapes.or(Block.makeCuboidShape(0.0D, 10.0D, 1.0D, 16.0D, 14.0D, 5.333333D), Block.makeCuboidShape(0.0D, 12.0D, 5.333333D, 16.0D, 16.0D, 9.666667D), Block.makeCuboidShape(0.0D, 14.0D, 9.666667D, 16.0D, 18.0D, 14.0D), BASE_AND_STAND_SHAPE);
    protected static final Map<Direction, VoxelShape> SHAPES = Util.make(Maps.newEnumMap(Direction.class), (map) -> {
        map.put(Direction.NORTH, SHAPE);
        map.put(Direction.SOUTH, VoxelShapeUtils.rotate(SHAPE, Direction.Axis.Y, Rotation.CLOCKWISE_180));
        map.put(Direction.WEST, VoxelShapeUtils.rotate(SHAPE, Direction.Axis.Y, Rotation.CLOCKWISE_90));
        map.put(Direction.EAST, VoxelShapeUtils.rotate(SHAPE, Direction.Axis.Y, Rotation.COUNTERCLOCKWISE_90));
    });
    
    public RitualLecternBlock() {
        super(Block.Properties.create(Material.WOOD).hardnessAndResistance(2.5F).sound(SoundType.WOOD));
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH).with(HAS_BOOK, Boolean.FALSE));
    }
    
    @Override
    public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return BASE_AND_STAND_SHAPE;
    }
    
    @Override
    public boolean isTransparent(BlockState state) {
        return true;
    }
    
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return COLLISION_SHAPE;
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPES.getOrDefault(state.get(FACING), BASE_AND_STAND_SHAPE);
    }
    
    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction) {
        return state.with(FACING, direction.rotate(state.get(FACING)));
    }
    
    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }
    
    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder) {
        builder.add(FACING, HAS_BOOK);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock() && state.get(HAS_BOOK)) {
            // TODO Drop book
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }
    
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (player != null && player.getHeldItem(handIn).getItem() instanceof EnchantedBookItem && !state.get(HAS_BOOK)) {
            // TODO If using an enchanted book on an empty lectern, place it
            return ActionResultType.SUCCESS;
        } else if (player != null && player.getHeldItem(handIn).isEmpty() && state.get(HAS_BOOK)) {
            // TODO If using an empty hand on a full lectern, retrieve the book
            return ActionResultType.SUCCESS;
        } else {
            return ActionResultType.PASS;
        }
    }
    
    @Override
    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        // Show spell sparkles if receiving salt power
        if (this.isBlockSaltPowered(worldIn, pos)) {
            FxDispatcher.INSTANCE.spellTrail(pos.getX() + rand.nextDouble(), pos.getY() + rand.nextDouble(), pos.getZ() + rand.nextDouble(), Color.WHITE.getRGB());
        }
    }

    @Override
    public float getStabilityBonus(World world, BlockPos pos) {
        return 0.02F;
    }

    @Override
    public float getSymmetryPenalty(World world, BlockPos pos) {
        return 0.02F;
    }

    @Override
    public boolean isPropActivated(BlockState state, World world, BlockPos pos) {
        if (state != null && state.getBlock() instanceof RitualLecternBlock) {
            return state.get(HAS_BOOK);
        } else {
            return false;
        }
    }

    @Override
    public String getPropTranslationKey() {
        return "primalmagic.ritual.prop.ritual_lectern";
    }

    @Override
    public float getUsageStabilityBonus() {
        return 10.0F;
    }
}
