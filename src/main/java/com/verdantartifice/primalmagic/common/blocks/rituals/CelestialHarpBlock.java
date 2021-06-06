package com.verdantartifice.primalmagic.common.blocks.rituals;

import java.awt.Color;
import java.util.Random;

import com.verdantartifice.primalmagic.client.fx.FxDispatcher;
import com.verdantartifice.primalmagic.common.misc.HarvestLevel;
import com.verdantartifice.primalmagic.common.rituals.IRitualPropBlock;
import com.verdantartifice.primalmagic.common.sounds.SoundsPM;
import com.verdantartifice.primalmagic.common.tiles.rituals.CelestialHarpTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
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
import net.minecraftforge.common.ToolType;

/**
 * Block definition for a celestial harp.  Celestial harps serve as props in magical rituals;
 * playing them at the right time can allow a ritual to progress to the next stage.
 * 
 * @author Daedalus4096
 */
public class CelestialHarpBlock extends Block implements IRitualPropBlock {
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    
    protected static final VoxelShape SHAPE = VoxelShapes.or(Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), Block.makeCuboidShape(7.0D, 0.0D, 0.0D, 9.0D, 16.0D, 16.0D));
    
    public CelestialHarpBlock() {
        super(Block.Properties.create(Material.WOOD, MaterialColor.GOLD).hardnessAndResistance(2.5F).sound(SoundType.WOOD).harvestTool(ToolType.AXE).harvestLevel(HarvestLevel.NONE.getLevel()));
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction) {
        return state.with(FACING, direction.rotate(state.get(FACING)));
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }
    
    @Override
    public float getStabilityBonus(World world, BlockPos pos) {
        return 0.04F;
    }

    @Override
    public float getSymmetryPenalty(World world, BlockPos pos) {
        return 0.04F;
    }

    @Override
    public boolean isPropActivated(BlockState state, World world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        return (tile instanceof CelestialHarpTileEntity && ((CelestialHarpTileEntity)tile).isPlaying());
    }

    @Override
    public String getPropTranslationKey() {
        return "primalmagic.ritual.prop.celestial_harp";
    }

    @Override
    public float getUsageStabilityBonus() {
        return 20.0F;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (player != null && player.getHeldItem(handIn).isEmpty() && !this.isPropActivated(state, worldIn, pos)) {
            TileEntity tile = worldIn.getTileEntity(pos);
            worldIn.playSound(player, pos, SoundsPM.HARP.get(), SoundCategory.BLOCKS, 1.0F, 1.0F);
            // TODO Launch note particles
            if (!worldIn.isRemote && tile instanceof CelestialHarpTileEntity) {
                // Start the harp tile entity playing
                ((CelestialHarpTileEntity)tile).startPlaying();
                
                // If this block is awaiting activation for an altar, notify it
                if (this.isPropOpen(state, worldIn, pos)) {
                    this.onPropActivated(state, worldIn, pos);
                }
            }
            return ActionResultType.SUCCESS;
        } else {
            return ActionResultType.PASS;
        }
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        // Close out any pending ritual activity if replaced
        if (!worldIn.isRemote && state.getBlock() != newState.getBlock()) {
            this.closeProp(state, worldIn, pos);
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
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
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CelestialHarpTileEntity();
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int id, int param) {
        // Pass any received events on to the tile entity and let it decide what to do with it
        super.eventReceived(state, worldIn, pos, id, param);
        TileEntity tile = worldIn.getTileEntity(pos);
        return (tile == null) ? false : tile.receiveClientEvent(id, param);
    }
}
