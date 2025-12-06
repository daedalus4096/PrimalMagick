package com.verdantartifice.primalmagick.common.items.concoctions;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

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
    @NotNull
    public InteractionResult use(@NotNull Level worldIn, @NotNull Player playerIn, @NotNull InteractionHand handIn) {
        ItemStack itemStack = playerIn.getItemInHand(handIn);
        BlockHitResult rayTraceResult = getPlayerPOVHitResult(worldIn, playerIn, ClipContext.Fluid.SOURCE_ONLY);
        if (rayTraceResult.getType() == HitResult.Type.MISS) {
            return InteractionResult.PASS;
        } else {
            if (rayTraceResult.getType() == HitResult.Type.BLOCK) {
                BlockPos blockPos = rayTraceResult.getBlockPos();
                if (!worldIn.mayInteract(playerIn, blockPos)) {
                    return InteractionResult.PASS;
                }
                if (worldIn.getFluidState(blockPos).is(FluidTags.WATER)) {
                    worldIn.playSound(playerIn, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
                    return InteractionResult.SUCCESS_SERVER.heldItemTransformedTo(this.turnFlaskIntoItem(itemStack, playerIn, this.getConcoctionContainerItem()));
                }
            }
            return InteractionResult.PASS;
        }
    }

    protected ItemStack turnFlaskIntoItem(ItemStack flaskStack, Player player, ItemStack stack) {
        return ItemUtils.createFilledResult(flaskStack, player, stack);
    }
}
