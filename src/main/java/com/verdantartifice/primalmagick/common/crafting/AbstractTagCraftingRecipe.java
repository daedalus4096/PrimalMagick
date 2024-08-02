package com.verdantartifice.primalmagick.common.crafting;

import java.util.Optional;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public abstract class AbstractTagCraftingRecipe<T extends RecipeInput> extends AbstractRecipe<T> {
    protected final TagKey<Item> outputTag;
    protected final int outputAmount;

    protected AbstractTagCraftingRecipe(String group, TagKey<Item> outputTag, int outputAmount) {
        super(group);
        this.outputTag = outputTag;
        this.outputAmount = outputAmount;
    }
    
    @Override
    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        return this.getResultItemFromTag(pRegistries);
    }

    protected ItemStack getResultItemFromTag(HolderLookup.Provider pRegistries) {
        // Retrieve the first item in this recipe's tag that was defined in Primal Magick or, failing that, the first item in the tag.
        // TODO Memoize this function
        Optional<HolderSet.Named<Item>> tagOpt = pRegistries.lookupOrThrow(Registries.ITEM).get(this.outputTag);
        Optional<Holder<Item>> modItemOpt = tagOpt.flatMap(tag -> tag.stream().filter(h -> h.is(key -> key.location().getNamespace().equals(PrimalMagick.MODID))).findFirst());
        if (modItemOpt.isPresent()) {
            return new ItemStack(modItemOpt.get().get(), this.outputAmount);
        } else {
            Optional<Holder<Item>> fallbackItemOpt = tagOpt.flatMap(tag -> tag.stream().findFirst());
            if (fallbackItemOpt.isPresent()) {
                return new ItemStack(fallbackItemOpt.get().get(), this.outputAmount);
            } else {
                return ItemStack.EMPTY;
            }
        }
    }
}
