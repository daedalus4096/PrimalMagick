package com.verdantartifice.primalmagick.common.crafting;

import java.util.Optional;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractTagCraftingRecipe<C extends Container> extends AbstractRecipe<C> {
    protected final TagKey<Item> outputTag;
    protected final int outputAmount;

    protected AbstractTagCraftingRecipe(String group, TagKey<Item> outputTag, int outputAmount) {
        super(group);
        this.outputTag = outputTag;
        this.outputAmount = outputAmount;
    }
    
    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return this.getResultItemFromTag(pRegistryAccess);
    }

    protected ItemStack getResultItemFromTag(RegistryAccess pRegistryAccess) {
        // Retrieve the first item in this recipe's tag that was defined in Primal Magick or, failing that, the first item in the tag.
        // TODO Memoize this function
        Optional<HolderSet.Named<Item>> tagOpt = pRegistryAccess.registryOrThrow(Registries.ITEM).getTag(this.outputTag);
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
