package com.verdantartifice.primalmagic.common.blocks.rituals;

import java.util.Random;

import com.verdantartifice.primalmagic.common.rituals.IRitualPropBlock;
import com.verdantartifice.primalmagic.common.tiles.rituals.EntropySinkTileEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;

/**
 * Block definition for an entropy sink.  Entropy sinks are optional props used in rituals to
 * reduce instability at the cost of essence.
 * 
 * @author Daedalus4096
 */
public class EntropySinkBlock extends BaseEntityBlock implements IRitualPropBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    
    public EntropySinkBlock() {
        super(Block.Properties.of(Material.STONE).strength(3.5F).lightLevel((state) -> { 
            return state.getValue(BlockStateProperties.LIT) ? 15 : 0; 
        }).sound(SoundType.STONE).noOcclusion());
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.valueOf(false)));
    }
    
    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, LIT);
    }
    
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // Make the block face the player when placed
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }
    
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EntropySinkTileEntity(pos, state);
    }

    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand) {
        // TODO Auto-generated method stub
        super.animateTick(stateIn, worldIn, pos, rand);
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
        BlockEntity tile = world.getBlockEntity(pos);
        return (tile instanceof EntropySinkTileEntity && ((EntropySinkTileEntity)tile).isGlowing());
    }

    @Override
    public String getPropTranslationKey() {
        return "primalmagic.ritual.prop.entropy_sink";
    }

    @Override
    public float getUsageStabilityBonus() {
        // TODO Determine amount based on type of essence used
        return 5.0F;
    }
}
