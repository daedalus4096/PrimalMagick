package com.verdantartifice.primalmagic.common.items.entities;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.entities.misc.FlyingCarpetEntity;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.DyeItem;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

/**
 * Item definition for a flying carpet.  Spawns a flying carpet entity when used for the player to
 * ride around on.
 * 
 * @author Daedalus4096
 */
public class FlyingCarpetItem extends Item {
    public static final ResourceLocation COLOR_PROPERTY = new ResourceLocation(PrimalMagic.MODID, "color");

    public FlyingCarpetItem(Item.Properties properties) {
        super(properties);
    }
    
    public static IItemPropertyGetter getColorProperty() {
        return new IItemPropertyGetter() {
            @OnlyIn(Dist.CLIENT)
            @Override
            public float call(ItemStack stack, ClientWorld world, LivingEntity entity) {
                DyeColor color = null;
                if (stack != null && stack.getItem() instanceof FlyingCarpetItem) {
                    color = ((FlyingCarpetItem)stack.getItem()).getDyeColor(stack);
                }
                if (color == null) {
                    // Default to white if no dye color is applied
                    color = DyeColor.WHITE;
                }
                return ((float)color.getId() / 16.0F);
            }
        };
    }
    
    public static ItemStack dyeCarpet(ItemStack carpetStack, DyeItem dye) {
        if (carpetStack.getItem() instanceof FlyingCarpetItem) {
            ItemStack retVal = carpetStack.copy();
            ((FlyingCarpetItem)retVal.getItem()).setDyeColor(retVal, dye.getDyeColor());
            return retVal;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        BlockState state = world.getBlockState(pos);
        if (!world.isRemote && state.getBlock() == Blocks.CAULDRON) {
            int level = state.get(CauldronBlock.LEVEL).intValue();
            if (level > 0) {
                this.removeDyeColor(stack);
                ((CauldronBlock)Blocks.CAULDRON).setWaterLevel(world, pos, state, level - 1);
                return ActionResultType.SUCCESS;
            } else {
                return ActionResultType.PASS;
            }
        } else if (!world.isRemote) {
            if (context.getFace() != Direction.UP) {
                return ActionResultType.PASS;
            }
            double posX = /* (double)pos.getX() + */ context.getHitVec().x;
            double posY = /* (double)pos.getY() + */ context.getHitVec().y;
            double posZ = /* (double)pos.getZ() + */ context.getHitVec().z;
            FlyingCarpetEntity entityCarpet = new FlyingCarpetEntity(world, posX, posY, posZ);
            if (stack.hasTag()) {
                entityCarpet.setDyeColor(this.getDyeColor(stack));
            }
            entityCarpet.rotationYaw = context.getPlayer().rotationYaw;
            world.addEntity(entityCarpet);
            world.playSound(null, posX, posY, posZ, SoundEvents.BLOCK_WOOL_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            stack.shrink(1);
            return ActionResultType.SUCCESS;
        } else {
            return ActionResultType.PASS;
        }
    }
    
    public DyeColor getDyeColor(ItemStack stack) {
        if (stack.hasTag()) {
            CompoundNBT nbt = stack.getTag();
            if (nbt != null && nbt.contains("display", Constants.NBT.TAG_COMPOUND)) {
                CompoundNBT displayNbt = nbt.getCompound("display");
                if (displayNbt != null && displayNbt.contains("color", Constants.NBT.TAG_INT)) {
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
            stack.setTag(new CompoundNBT());
        }
        CompoundNBT nbt = stack.getTag();
        if (!nbt.contains("display", Constants.NBT.TAG_COMPOUND)) {
            nbt.put("display", new CompoundNBT());
        }
        nbt.getCompound("display").putInt("color", color.getId());
    }
    
    public void removeDyeColor(ItemStack stack) {
        if (stack.hasTag()) {
            CompoundNBT nbt = stack.getTag();
            if (nbt != null && nbt.contains("display", Constants.NBT.TAG_COMPOUND)) {
                CompoundNBT displayNbt = nbt.getCompound("display");
                if (displayNbt != null && displayNbt.contains("color", Constants.NBT.TAG_INT)) {
                    displayNbt.remove("color");
                }
            }
        }
    }
}
