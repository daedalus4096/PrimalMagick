package com.verdantartifice.primalmagic.common.blocks.devices;

import java.util.Random;

import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.items.misc.SanguineCoreItem;
import com.verdantartifice.primalmagic.common.misc.HarvestLevel;
import com.verdantartifice.primalmagic.common.tiles.devices.SanguineCrucibleTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.util.Constants;

/**
 * Block definition for a sanguine crucible.  Uses blood cores and captured souls to summon creatures.
 * 
 * @author Daedalus4096
 */
public class SanguineCrucibleBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    protected static final VoxelShape INSIDE = makeCuboidShape(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    protected static final VoxelShape SHAPE = VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), VoxelShapes.or(makeCuboidShape(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), makeCuboidShape(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), INSIDE), IBooleanFunction.ONLY_FIRST);

    public SanguineCrucibleBlock() {
        super(Block.Properties.create(Material.IRON).hardnessAndResistance(7.0F, 6.0F).setLightLevel((state) -> { 
            return state.get(BlockStateProperties.LIT) ? 13 : 0; 
        }).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(HarvestLevel.IRON.getLevel()));
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH).with(LIT, Boolean.valueOf(false)));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getRaytraceShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return INSIDE;
    }

    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACING, LIT);
    }
    
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        // Make the block face the player when placed
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }
    
    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
    
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new SanguineCrucibleTileEntity();
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
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (!worldIn.isRemote) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof SanguineCrucibleTileEntity) {
                SanguineCrucibleTileEntity crucibleTile = (SanguineCrucibleTileEntity)tile;
                if (entityIn instanceof ItemEntity) {
                    ItemEntity itemEntity = (ItemEntity)entityIn;
                    if (itemEntity.getItem().getItem() == ItemsPM.SOUL_GEM.get()) {
                        crucibleTile.addSouls(itemEntity.getItem().getCount());
                        entityIn.remove();
                        worldIn.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    }
                }
            }
        }
        super.onEntityCollision(state, worldIn, pos, entityIn);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack stack = player.getHeldItem(handIn);
        TileEntity tile = worldIn.getTileEntity(pos);
        if (!worldIn.isRemote && tile instanceof SanguineCrucibleTileEntity) {
            SanguineCrucibleTileEntity crucibleTile = (SanguineCrucibleTileEntity)tile;
            if (stack.getItem() instanceof SanguineCoreItem && !crucibleTile.hasCore()) {
                crucibleTile.setInventorySlotContents(0, stack.copy());
                stack.shrink(1);
                worldIn.playSound(null, pos, SoundEvents.BLOCK_STONE_PRESSURE_PLATE_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.6F);
                worldIn.setBlockState(pos, state.with(LIT, true), Constants.BlockFlags.DEFAULT_AND_RERENDER);
                return ActionResultType.SUCCESS;
            } else if (player.isSecondaryUseActive() && stack.isEmpty() && crucibleTile.hasCore()) {
                spawnAsEntity(worldIn, pos.offset(hit.getFace()), crucibleTile.removeStackFromSlot(0));
                worldIn.playSound(null, pos, SoundEvents.BLOCK_STONE_PRESSURE_PLATE_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.5F);
                worldIn.setBlockState(pos, state.with(LIT, false), Constants.BlockFlags.DEFAULT_AND_RERENDER);
                return ActionResultType.SUCCESS;
            }
        }

        return ActionResultType.PASS;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        // Drop the tile entity's inventory into the world when the block is replaced
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof SanguineCrucibleTileEntity) {
                InventoryHelper.dropInventoryItems(worldIn, pos, (SanguineCrucibleTileEntity)tile);
                worldIn.updateComparatorOutputLevel(pos, this);
            }
            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (rand.nextDouble() < 0.1D) {
            worldIn.playSound(null, pos, SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS, 0.1F + (rand.nextFloat() * 0.1F), 0.8F + (rand.nextFloat() * 0.4F));
        }
    }
}
