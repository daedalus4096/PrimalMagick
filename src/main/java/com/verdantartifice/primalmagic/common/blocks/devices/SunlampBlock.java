package com.verdantartifice.primalmagic.common.blocks.devices;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.blocks.misc.GlowFieldBlock;
import com.verdantartifice.primalmagic.common.tiles.devices.SunlampTileEntity;
import com.verdantartifice.primalmagic.common.util.VoxelShapeUtils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

/**
 * Block definition for a sunlamp.  Sunlamps are like normal lanterns, but they spawn glow fields
 * nearby in sufficiently dark spaces.  It can also be attached to any side of a block instead of
 * just the top or bottom.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagic.common.blocks.misc.GlowFieldBlock}
 */
public class SunlampBlock extends Block {
    public static final DirectionProperty ATTACHMENT = DirectionProperty.create("attachment", Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN);
    
    protected static final VoxelShape GROUND_SHAPE = VoxelShapeUtils.fromModel(new ResourceLocation(PrimalMagic.MODID, "block/sunlamp_ground_base"));
    protected static final VoxelShape HANGING_SHAPE = VoxelShapeUtils.fromModel(new ResourceLocation(PrimalMagic.MODID, "block/sunlamp_hanging_base"));
    
    protected final Supplier<GlowFieldBlock> glowSupplier;
    
    public SunlampBlock(Supplier<GlowFieldBlock> glowSupplier) {
        super(Block.Properties.create(Material.IRON).hardnessAndResistance(3.5F).sound(SoundType.LANTERN).setLightLevel((state) -> { return 15; }).notSolid());
        this.setDefaultState(this.getDefaultState().with(ATTACHMENT, Direction.DOWN));
        this.glowSupplier = glowSupplier;
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return state.get(ATTACHMENT) == Direction.DOWN ? GROUND_SHAPE : HANGING_SHAPE;
    }
    
    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder) {
        builder.add(ATTACHMENT);
    }
    
    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState state = this.getDefaultState().with(ATTACHMENT, context.getFace().getOpposite());
        return state.isValidPosition(context.getWorld(), context.getPos()) ? state : null;
    }
    
    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        Direction dir = state.get(ATTACHMENT);
        return Block.hasEnoughSolidSide(worldIn, pos.offset(dir), dir.getOpposite());
    }
    
    @Override
    public PushReaction getPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return stateIn.get(ATTACHMENT) == facing ? 
                Blocks.AIR.getDefaultState() : 
                super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }
    
    @Override
    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }
    
    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
    
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new SunlampTileEntity();
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int id, int param) {
        // Pass any received events on to the tile entity and let it decide what to do with it
        super.eventReceived(state, worldIn, pos, id, param);
        TileEntity tile = worldIn.getTileEntity(pos);
        return (tile == null) ? false : tile.receiveClientEvent(id, param);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        for (int x = -15; x <= 15; x++) {
            for (int y = -15; y <= 15; y++) {
                for (int z = -15; z <= 15; z++) {
                    BlockPos bp = pos.add(x, y, z);
                    if (worldIn.getBlockState(bp) == BlocksPM.GLOW_FIELD.get().getDefaultState()) {
                        worldIn.removeBlock(bp, false);
                    }
                }
            }
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }
    
    public GlowFieldBlock getGlowField() {
        return this.glowSupplier.get();
    }
}
