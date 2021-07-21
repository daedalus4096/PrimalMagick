package com.verdantartifice.primalmagic.common.crafting;

import com.verdantartifice.primalmagic.common.items.tools.TieredShieldItem;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Special recipe for decorating a magical metal shield with a banner.
 * 
 * @author Daedalus4096
 */
public class TieredShieldDecorationRecipe extends SpecialRecipe {
    public TieredShieldDecorationRecipe(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        ItemStack shieldStack = ItemStack.EMPTY;
        ItemStack bannerStack = ItemStack.EMPTY;
        
        for (int index = 0; index < inv.getSizeInventory(); index++) {
            ItemStack stack = inv.getStackInSlot(index);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof BannerItem) {
                    if (!bannerStack.isEmpty()) {
                        return false;
                    }
                    bannerStack = stack;
                } else {
                    if (!(stack.getItem() instanceof TieredShieldItem) || !shieldStack.isEmpty() || stack.getChildTag("BlockEntityTag") != null) {
                        return false;
                    }
                    shieldStack = stack;
                }
            }
        }
        
        return !shieldStack.isEmpty() && !bannerStack.isEmpty();
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        ItemStack shieldStack = ItemStack.EMPTY;
        ItemStack bannerStack = ItemStack.EMPTY;
        
        for (int index = 0; index < inv.getSizeInventory(); index++) {
            ItemStack stack = inv.getStackInSlot(index);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof BannerItem) {
                    bannerStack = stack;
                } else if (stack.getItem() instanceof TieredShieldItem) {
                    shieldStack = stack.copy();
                }
            }
        }

        if (shieldStack.isEmpty()) {
            return shieldStack;
        } else {
            CompoundNBT bannerNbt = bannerStack.getChildTag("BlockEntityTag");
            CompoundNBT newNbt = bannerNbt == null ? new CompoundNBT() : bannerNbt.copy();
            newNbt.putInt("Base", ((BannerItem)bannerStack.getItem()).getColor().getId());
            shieldStack.setTagInfo("BlockEntityTag", newNbt);
            return shieldStack;
        }
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeSerializersPM.TIERED_SHIELD_DECORATION.get();
    }

}
