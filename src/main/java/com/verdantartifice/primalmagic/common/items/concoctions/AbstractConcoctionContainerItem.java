package com.verdantartifice.primalmagic.common.items.concoctions;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;

/**
 * Base definition of a concoction container.  Interacts with water sources to fill the container
 * in prepration for concocting.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractConcoctionContainerItem extends Item {
    public AbstractConcoctionContainerItem(Item.Properties properties) {
        super(properties);
    }
    
    protected abstract ItemStack getConcoctionContainerItem();

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemStack = playerIn.getItemInHand(handIn);
        HitResult rayTraceResult = getPlayerPOVHitResult(worldIn, playerIn, ClipContext.Fluid.SOURCE_ONLY);
        if (rayTraceResult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemStack);
        } else {
            if (rayTraceResult.getType() == HitResult.Type.BLOCK) {
                BlockPos blockPos = ((BlockHitResult)rayTraceResult).getBlockPos();
                if (!worldIn.mayInteract(playerIn, blockPos)) {
                    return InteractionResultHolder.pass(itemStack);
                }
                if (worldIn.getFluidState(blockPos).is(FluidTags.WATER)) {
                    worldIn.playSound(playerIn, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
                    return InteractionResultHolder.sidedSuccess(this.turnFlaskIntoItem(itemStack, playerIn, this.getConcoctionContainerItem()), worldIn.isClientSide());
                }
            }
            return InteractionResultHolder.pass(itemStack);
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState blockState = world.getBlockState(pos);
        if (blockState.getBlock() == Blocks.CAULDRON) {
            int waterLevel = blockState.getValue(CauldronBlock.LEVEL);
            if (waterLevel > 0 && !world.isClientSide) {
                Player player = context.getPlayer();
                if (!player.abilities.instabuild) {
                    ItemStack flaskStack = context.getItemInHand();
                    ItemStack waterStack = this.getConcoctionContainerItem();
                    flaskStack.shrink(1);
                    if (flaskStack.isEmpty()) {
                        player.setItemInHand(context.getHand(), waterStack);
                    } else if (!player.inventory.add(waterStack)) {
                        player.drop(waterStack, false);
                    } else if (player instanceof ServerPlayer) {
                        ((ServerPlayer)player).refreshContainer(player.inventoryMenu);
                    }
                }
                world.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                ((CauldronBlock)Blocks.CAULDRON).setWaterLevel(world, pos, blockState, waterLevel - 1);                
            }
            return InteractionResult.sidedSuccess(world.isClientSide);
        }
        return super.useOn(context);
    }
    
    protected ItemStack turnFlaskIntoItem(ItemStack flaskStack, Player player, ItemStack stack) {
        return ItemUtils.createFilledResult(flaskStack, player, stack);
    }
}
