package com.verdantartifice.primalmagic.common.items.essence;

import javax.annotation.Nonnull;

import net.minecraft.item.Rarity;
import net.minecraft.util.IStringSerializable;

public enum EssenceType implements IStringSerializable {
    DUST("dust", Rarity.COMMON),
    SHARD("shard", Rarity.UNCOMMON),
    CRYSTAL("crystal", Rarity.RARE),
    CLUSTER("cluster", Rarity.EPIC);
    
    private final String name;
    private final Rarity rarity;
    
    private EssenceType(@Nonnull String name, @Nonnull Rarity rarity) {
        this.name = name;
        this.rarity = rarity;
    }

    @Override
    @Nonnull
    public String getName() {
        return this.name;
    }
    
    @Nonnull
    public Rarity getRarity() {
        return this.rarity;
    }
}
