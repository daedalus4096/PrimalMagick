package com.verdantartifice.primalmagic.common.blocks.rituals;

import java.util.Map;

import com.google.common.collect.Maps;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.misc.HarvestLevel;
import com.verdantartifice.primalmagic.common.rituals.IRitualPropBlock;
import com.verdantartifice.primalmagic.common.util.VoxelShapeUtils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

/**
 * Block definition for a soul anvil.  Soul anvils serve as props in magical rituals; breaking a soul
 * gem on one at the right time can allow a ritual to progress to the next stage.
 * 
 * @author Daedalus4096
 */
public class SoulAnvilBlock extends FallingBlock implements IRitualPropBlock {
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty DIRTY = BooleanProperty.create("dirty");
    
    protected static final VoxelShape BASE_SHAPE = VoxelShapeUtils.fromModel(new ResourceLocation(PrimalMagic.MODID, "block/soul_anvil"));
    protected static final Map<Direction, VoxelShape> SHAPES = Util.make(Maps.newEnumMap(Direction.class), (map) -> {
        map.put(Direction.NORTH, BASE_SHAPE);
        map.put(Direction.SOUTH, VoxelShapeUtils.rotate(BASE_SHAPE, Direction.Axis.Y, Rotation.CLOCKWISE_180));
        map.put(Direction.WEST, VoxelShapeUtils.rotate(BASE_SHAPE, Direction.Axis.Y, Rotation.CLOCKWISE_90));
        map.put(Direction.EAST, VoxelShapeUtils.rotate(BASE_SHAPE, Direction.Axis.Y, Rotation.COUNTERCLOCKWISE_90));
    });
    
    public SoulAnvilBlock() {
        super(Block.Properties.create(Material.ANVIL, MaterialColor.RED).hardnessAndResistance(5.0F, 1200.0F).sound(SoundType.ANVIL).harvestTool(ToolType.PICKAXE).harvestLevel(HarvestLevel.WOOD.getLevel()));
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH).with(DIRTY, Boolean.FALSE));
    }
    
    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder) {
        builder.add(FACING, DIRTY);
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPES.getOrDefault(state.get(FACING), BASE_SHAPE);
    }
    
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().rotateY());
    }
    
    @Override
    protected void onStartFalling(FallingBlockEntity fallingEntity) {
        fallingEntity.setHurtEntities(true);
    }
    
    @Override
    public void onEndFalling(World worldIn, BlockPos pos, BlockState fallingState, BlockState hitState) {
        worldIn.playEvent(1031, pos, 0);
    }
    
    @Override
    public void onBroken(World worldIn, BlockPos pos) {
        worldIn.playEvent(1029, pos, 0);
    }
    
    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
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
