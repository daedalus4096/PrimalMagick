package com.verdantartifice.primalmagic.common.items.entities;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
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

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        // TODO Auto-generated method stub
        return super.onItemUseFirst(stack, context);
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
