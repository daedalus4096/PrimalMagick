package com.verdantartifice.primalmagick.common.blocks.rituals;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.rituals.IRitualStabilizer;
import com.verdantartifice.primalmagick.common.rituals.ISaltPowered;
import com.verdantartifice.primalmagick.common.tiles.rituals.OfferingPedestalTileEntity;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.common.util.VoxelShapeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.awt.Color;

/**
 * Block definition for an offering pedestal.  An offering pedestal holds an item stack for use as
 * an ingredient in a magickal ritual.
 * 
 * @author Daedalus4096
 */
public class OfferingPedestalBlock extends BaseEntityBlock implements ISaltPowered, IRitualStabilizer {
    public static final MapCodec<OfferingPedestalBlock> CODEC = simpleCodec(OfferingPedestalBlock::new);
    
    protected static final VoxelShape SHAPE = VoxelShapeUtils.fromModel(ResourceUtils.loc("block/offering_pedestal"));
    
    public OfferingPedestalBlock(Block.Properties properties) {
        super(properties);
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
        // Show spell sparkles if receiving salt power
        if (this.isBlockSaltPowered(worldIn, pos)) {
            FxDispatcher.INSTANCE.spellTrail(pos.getX() + rand.nextDouble(), pos.getY() + rand.nextDouble(), pos.getZ() + rand.nextDouble(), Color.WHITE.getRGB());
        }
    }
    
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new OfferingPedestalTileEntity(pos, state);
    }
    
    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (handIn == InteractionHand.MAIN_HAND) {
            if (worldIn.getBlockEntity(pos) instanceof OfferingPedestalTileEntity pedestalTile) {
                if (pedestalTile.getItem().isEmpty() && !stack.isEmpty()) {
                    // When activating an empty pedestal with an item in hand, place it on the pedestal
                    ItemStack placementStack = stack.copyWithCount(1);
                    pedestalTile.setItem(placementStack);
                    player.getItemInHand(handIn).shrink(1);
                    if (player.getItemInHand(handIn).getCount() <= 0) {
                        player.setItemInHand(handIn, ItemStack.EMPTY);
                    }
                    player.getInventory().setChanged();
                    worldIn.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.4F, 1.0F);
                    return ItemInteractionResult.SUCCESS;
                }
            }
        }
        return super.useItemOn(stack, state, worldIn, pos, player, handIn, hit);
    }
    
    @Override
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        if (pLevel.getBlockEntity(pPos) instanceof OfferingPedestalTileEntity pedestalTile) {
            if (!pedestalTile.getItem().isEmpty()) {
                // When activating a full pedestal, pick up the item
                ItemStack stack = pedestalTile.getItem().copy();
                pedestalTile.setItem(ItemStack.EMPTY);
                if (!stack.isEmpty() && !pPlayer.getInventory().add(stack)) {
                    pPlayer.drop(stack, false);
                }
                pPlayer.getInventory().setChanged();
                pLevel.playSound(null, pPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.4F, 1.0F);
                return InteractionResult.SUCCESS;
            }
        }
        return super.useWithoutItem(pState, pLevel, pPos, pPlayer, pHitResult);
    }

    @Override
    public boolean hasSymmetryPenalty(Level world, BlockPos pos, BlockPos otherPos) {
        BlockEntity tile = world.getBlockEntity(pos);
        BlockEntity otherTile = world.getBlockEntity(otherPos);
        
        // If there's a pedestal in one spot but not the other, invoke a symmetry penalty
        if ( ((tile instanceof OfferingPedestalTileEntity) && !(otherTile instanceof OfferingPedestalTileEntity)) ||
                (!(tile instanceof OfferingPedestalTileEntity) && (otherTile instanceof OfferingPedestalTileEntity)) ) {
            return true;
        }

        // If one pedestal is full and the other is empty, invoke a symmetry penalty
        if (world.isClientSide) {
            return ( (tile instanceof OfferingPedestalTileEntity pedestal) &&
                     (otherTile instanceof OfferingPedestalTileEntity otherPedestal) &&
                     pedestal.getSyncedStack().isEmpty() != otherPedestal.getSyncedStack().isEmpty() );
        } else {
            return ( (tile instanceof OfferingPedestalTileEntity pedestal) &&
                     (otherTile instanceof OfferingPedestalTileEntity otherPedestal) &&
                     pedestal.getItem().isEmpty() != otherPedestal.getItem().isEmpty() );
        }
    }
    
    @Override
    public float getStabilityBonus(Level world, BlockPos pos) {
        return 0.0F;
    }
    
    @Override
    public float getSymmetryPenalty(Level world, BlockPos pos) {
        return 0.01F;
    }
    
    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        // Drop the tile entity's inventory into the world when the block is replaced
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity tile = worldIn.getBlockEntity(pos);
            if (tile instanceof OfferingPedestalTileEntity pedestalTile) {
                pedestalTile.dropContents(worldIn, pos);
                worldIn.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
