package com.verdantartifice.primalmagic.common.crafting;

import com.verdantartifice.primalmagic.common.items.tools.TieredShieldItem;

import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

/**
 * Special recipe for decorating a magical metal shield with a banner.
 * 
 * @author Daedalus4096
 */
public class TieredShieldDecorationRecipe extends CustomRecipe {
    public TieredShieldDecorationRecipe(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level worldIn) {
        ItemStack shieldStack = ItemStack.EMPTY;
        ItemStack bannerStack = ItemStack.EMPTY;
        
        for (int index = 0; index < inv.getContainerSize(); index++) {
            ItemStack stack = inv.getItem(index);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof BannerItem) {
                    if (!bannerStack.isEmpty()) {
                        return false;
                    }
                    bannerStack = stack;
                } else {
                    if (!(stack.getItem() instanceof TieredShieldItem) || !shieldStack.isEmpty() || stack.getTagElement("BlockEntityTag") != null) {
                        return false;
                    }
                    shieldStack = stack;
                }
            }
        }
        
        return !shieldStack.isEmpty() && !bannerStack.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingContainer inv) {
        ItemStack shieldStack = ItemStack.EMPTY;
        ItemStack bannerStack = ItemStack.EMPTY;
        
        for (int index = 0; index < inv.getContainerSize(); index++) {
            ItemStack stack = inv.getItem(index);
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
            CompoundTag bannerNbt = bannerStack.getTagElement("BlockEntityTag");
            CompoundTag newNbt = bannerNbt == null ? new CompoundTag() : bannerNbt.copy();
            newNbt.putInt("Base", ((BannerItem)bannerStack.getItem()).getColor().getId());
            shieldStack.addTagElement("BlockEntityTag", newNbt);
            return shieldStack;
        }
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializersPM.TIERED_SHIELD_DECORATION.get();
    }

}
