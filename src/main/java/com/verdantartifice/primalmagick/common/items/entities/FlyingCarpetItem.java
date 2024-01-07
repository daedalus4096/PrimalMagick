package com.verdantartifice.primalmagick.common.items.entities;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.entities.misc.FlyingCarpetEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Item definition for a flying carpet.  Spawns a flying carpet entity when used for the player to
 * ride around on.
 * 
 * @author Daedalus4096
 */
public class FlyingCarpetItem extends Item {
    public static final ResourceLocation COLOR_PROPERTY = PrimalMagick.resource("color");
    
    public static final CauldronInteraction DYED_CARPET = (BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) -> {
        Item item = stack.getItem();
        if (!(item instanceof FlyingCarpetItem)) {
            return InteractionResult.PASS;
        } else {
            FlyingCarpetItem carpet = (FlyingCarpetItem)item;
            if (carpet.getDyeColor(stack) == null) {
                return InteractionResult.PASS;
            } else {
                if (!level.isClientSide) {
                    carpet.removeDyeColor(stack);
                    LayeredCauldronBlock.lowerFillLevel(state, level, pos);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
    };

    public FlyingCarpetItem(Item.Properties properties) {
        super(properties);
    }
    
    public static ItemStack dyeCarpet(ItemStack carpetStack, DyeItem dye) {
        if (carpetStack.getItem() instanceof FlyingCarpetItem carpet) {
            ItemStack retVal = carpetStack.copy();
            carpet.setDyeColor(retVal, dye.getDyeColor());
            return retVal;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        if (!world.isClientSide) {
            if (context.getClickedFace() != Direction.UP) {
                return InteractionResult.PASS;
            }
            ItemStack stack = context.getItemInHand();
            double posX = context.getClickLocation().x;
            double posY = context.getClickLocation().y;
            double posZ = context.getClickLocation().z;
            FlyingCarpetEntity entityCarpet = new FlyingCarpetEntity(world, posX, posY, posZ);
            if (stack.hasTag()) {
                entityCarpet.setDyeColor(this.getDyeColor(stack));
            }
            entityCarpet.setYRot(context.getPlayer().getYRot());
            world.addFreshEntity(entityCarpet);
            world.playSound(null, posX, posY, posZ, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
            context.getPlayer().setItemInHand(context.getHand(), ItemStack.EMPTY);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }
    
    public DyeColor getDyeColor(ItemStack stack) {
        if (stack.hasTag()) {
            CompoundTag nbt = stack.getTag();
            if (nbt != null && nbt.contains("display", Tag.TAG_COMPOUND)) {
                CompoundTag displayNbt = nbt.getCompound("display");
                if (displayNbt != null && displayNbt.contains("color", Tag.TAG_INT)) {
                    return DyeColor.byId(displayNbt.getInt("color"));
                }
            }
        }
        return null;
    }
    
    public void setDyeColor(ItemStack stack, DyeColor color) {
        if (color == null) {
            return;
        }
        if (!stack.hasTag()) {
            stack.setTag(new CompoundTag());
        }
        CompoundTag nbt = stack.getTag();
        if (!nbt.contains("display", Tag.TAG_COMPOUND)) {
            nbt.put("display", new CompoundTag());
        }
        nbt.getCompound("display").putInt("color", color.getId());
    }
    
    public void removeDyeColor(ItemStack stack) {
        if (stack.hasTag()) {
            CompoundTag nbt = stack.getTag();
            if (nbt != null && nbt.contains("display", Tag.TAG_COMPOUND)) {
                CompoundTag displayNbt = nbt.getCompound("display");
                if (displayNbt != null && displayNbt.contains("color", Tag.TAG_INT)) {
                    displayNbt.remove("color");
                }
            }
        }
    }
}
