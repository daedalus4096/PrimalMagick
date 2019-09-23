package com.verdantartifice.primalmagic.common.items.wands;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface IWand {
    public int getMana(@Nullable ItemStack stack, @Nullable Source source);
    
    @Nonnull
    public SourceList getAllMana(@Nullable ItemStack stack);
    
    public int getMaxMana(@Nullable ItemStack stack);
    
    public int addMana(@Nullable ItemStack stack, @Nullable Source source, int amount);
    
    public boolean consumeMana(@Nullable ItemStack stack, @Nullable PlayerEntity player, @Nullable Source source, int amount);
}
