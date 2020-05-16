package com.verdantartifice.primalmagic.common.blocks.rituals;

import java.awt.Color;
import java.util.Random;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.fx.FxDispatcher;
import com.verdantartifice.primalmagic.common.rituals.ISaltPowered;
import com.verdantartifice.primalmagic.common.tiles.rituals.OfferingPedestalTileEntity;
import com.verdantartifice.primalmagic.common.util.VoxelShapeUtils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

/**
 * Block definition for an offering pedestal.  An offering pedestal holds an item stack for use as
 * an ingredient in a magical ritual.
 * 
 * @author Daedalus4096
 */
public class OfferingPedestalBlock extends Block implements ISaltPowered {
    protected static final VoxelShape SHAPE = VoxelShapeUtils.fromModel(new ResourceLocation(PrimalMagic.MODID, "block/offering_pedestal"));
    
    public OfferingPedestalBlock() {
        super(Block.Properties.create(Material.ROCK, MaterialColor.QUARTZ).hardnessAndResistance(1.5F, 6.0F).sound(SoundType.STONE));
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }
    
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
        return new OfferingPedestalTileEntity();
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
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote && handIn == Hand.MAIN_HAND) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof OfferingPedestalTileEntity) {
                OfferingPedestalTileEntity pedestalTile = (OfferingPedestalTileEntity)tile;
                if (pedestalTile.getStackInSlot(0).isEmpty() && !player.getHeldItem(handIn).isEmpty()) {
                    // When activating an empty pedestal with an item in hand, place it on the pedestal
                    ItemStack stack = player.getHeldItem(handIn).copy();
                    stack.setCount(1);
                    pedestalTile.setInventorySlotContents(0, stack);
                    player.getHeldItem(handIn).shrink(1);
                    if (player.getHeldItem(handIn).getCount() <= 0) {
                        player.setHeldItem(handIn, ItemStack.EMPTY);
                    }
                    player.inventory.markDirty();
                    worldIn.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.4F, 1.0F);
                    return ActionResultType.SUCCESS;
                } else if (!pedestalTile.getStackInSlot(0).isEmpty() && player.getHeldItem(handIn).isEmpty()) {
                    // When activating a full pedestal with an empty hand, pick up the item
                    ItemStack stack = pedestalTile.getStackInSlot(0).copy();
                    pedestalTile.setInventorySlotContents(0, ItemStack.EMPTY);
                    player.setHeldItem(handIn, stack);
                    player.inventory.markDirty();
                    worldIn.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.4F, 1.0F);
                    return ActionResultType.SUCCESS;
                }
            }
        }
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }
}
