package com.verdantartifice.primalmagick.common.crafting;

import com.verdantartifice.primalmagick.common.items.tools.AbstractTieredShieldItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

/**
 * Special recipe for decorating a magickal metal shield with a banner.
 * 
 * @author Daedalus4096
 */
public class TieredShieldDecorationRecipe extends CustomRecipe {
    public TieredShieldDecorationRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput inv, Level worldIn) {
        ItemStack shieldStack = ItemStack.EMPTY;
        ItemStack bannerStack = ItemStack.EMPTY;
        
        for (int index = 0; index < inv.size(); index++) {
            ItemStack stack = inv.getItem(index);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof BannerItem) {
                    if (!bannerStack.isEmpty()) {
                        return false;
                    }
                    bannerStack = stack;
                } else {
                    if (!(stack.getItem() instanceof AbstractTieredShieldItem shieldItem) || !shieldItem.canDecorate() || !shieldStack.isEmpty()) {
                        return false;
                    }
                    BannerPatternLayers layers = stack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
                    if (!layers.layers().isEmpty()) {
                        return false;
                    }
                    shieldStack = stack;
                }
            }
        }
        
        return !shieldStack.isEmpty() && !bannerStack.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingInput inv, HolderLookup.Provider registries) {
        ItemStack shieldStack = ItemStack.EMPTY;
        ItemStack bannerStack = ItemStack.EMPTY;
        
        for (int index = 0; index < inv.size(); index++) {
            ItemStack stack = inv.getItem(index);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof BannerItem) {
                    bannerStack = stack;
                } else if (stack.getItem() instanceof AbstractTieredShieldItem shieldItem && shieldItem.canDecorate()) {
                    shieldStack = stack.copy();
                }
            }
        }

        if (shieldStack.isEmpty()) {
            return shieldStack;
        } else {
            shieldStack.set(DataComponents.BANNER_PATTERNS, bannerStack.get(DataComponents.BANNER_PATTERNS));
            shieldStack.set(DataComponents.BASE_COLOR, ((BannerItem)bannerStack.getItem()).getColor());
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
