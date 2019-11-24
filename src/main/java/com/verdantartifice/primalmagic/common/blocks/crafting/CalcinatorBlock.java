package com.verdantartifice.primalmagic.common.blocks.crafting;

import java.util.Random;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.tiles.crafting.CalcinatorTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CalcinatorBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    
    protected static final VoxelShape PART_LEG1 = Block.makeCuboidShape(2, 0, 2, 5, 2, 5);
    protected static final VoxelShape PART_LEG2 = Block.makeCuboidShape(11, 0, 2, 14, 2, 5);
    protected static final VoxelShape PART_LEG3 = Block.makeCuboidShape(11, 0, 11, 14, 2, 14);
    protected static final VoxelShape PART_LEG4 = Block.makeCuboidShape(2, 0, 11, 5, 2, 14);
    protected static final VoxelShape PART_BODY = Block.makeCuboidShape(2, 2, 2, 14, 11, 14);
    protected static final VoxelShape PART_TOP1 = Block.makeCuboidShape(3, 11, 3, 13, 12, 13);
    protected static final VoxelShape PART_TOP2 = Block.makeCuboidShape(4, 12, 4, 12, 13, 12);
    protected static final VoxelShape PART_STACK = Block.makeCuboidShape(6, 13, 6, 10, 16, 10);
    protected static final VoxelShape SHAPE = VoxelShapes.or(PART_LEG1, PART_LEG2, PART_LEG3, PART_LEG4, PART_BODY, PART_TOP1, PART_TOP2, PART_STACK);
    
    public CalcinatorBlock() {
        super(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.5F).lightValue(13).sound(SoundType.STONE));
        this.setRegistryName(PrimalMagic.MODID, "calcinator");
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH).with(LIT, Boolean.valueOf(false)));
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }
    
    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACING, LIT);
    }
    
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }
    
    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }
    
    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public int getLightValue(BlockState state) {
        return state.get(LIT) ? super.getLightValue(state) : 0;
    }
    
    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
    
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CalcinatorTileEntity();
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int id, int param) {
        super.eventReceived(state, worldIn, pos, id, param);
        TileEntity tile = worldIn.getTileEntity(pos);
        return (tile == null) ? false : tile.receiveClientEvent(id, param);
    }
    
    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof CalcinatorTileEntity) {
                player.openContainer((CalcinatorTileEntity)tile);
            }
        }
        return true;
    }
    
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        if (!worldIn.isRemote && placer instanceof PlayerEntity) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof CalcinatorTileEntity) {
                ((CalcinatorTileEntity)tile).setTileOwner((PlayerEntity)placer);
            }
        }
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof CalcinatorTileEntity) {
                InventoryHelper.dropInventoryItems(worldIn, pos, (CalcinatorTileEntity)tile);
                worldIn.updateComparatorOutputLevel(pos, this);
            }
            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (stateIn.get(LIT)) {
            double d0 = (double)pos.getX() + 0.5D;
            double d1 = (double)pos.getY();
            double d2 = (double)pos.getZ() + 0.5D;
            if (rand.nextDouble() < 0.1D) {
                worldIn.playSound(d0, d1, d2, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            // Add smoke and flame at calcinator front
            Direction direction = stateIn.get(FACING);
            Direction.Axis axis = direction.getAxis();
            double d3 = 0.5D;
            double d4 = rand.nextDouble() * 0.6D - 0.3D;
            double d5 = axis == Direction.Axis.X ? (double)direction.getXOffset() * d3 : d4;
            double d6 = ((rand.nextDouble() * 3.0D) + 3.0D) / 16.0D;
            double d7 = axis == Direction.Axis.Z ? (double)direction.getZOffset() * d3 : d4;
            worldIn.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
            worldIn.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
            
            // Add smoke at calcinator smokestack
            double d8 = rand.nextDouble() * 0.2D - 0.1D;
            double d9 = rand.nextDouble() * 0.2D - 0.1D;
            worldIn.addParticle(ParticleTypes.SMOKE, d0 + d8, d1 + 1.0D, d2 + d9, 0.0D, 0.0D, 0.0D);
        }
    }
}
