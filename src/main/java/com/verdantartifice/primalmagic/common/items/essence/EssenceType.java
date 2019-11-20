package com.verdantartifice.primalmagic.common.items.essence;

import javax.annotation.Nonnull;

import net.minecraft.item.Rarity;
import net.minecraft.util.IStringSerializable;

public enum EssenceType implements IStringSerializable {
    DUST("dust", Rarity.COMMON, 5),
    SHARD("shard", Rarity.UNCOMMON, 20),
    CRYSTAL("crystal", Rarity.RARE, 50),
    CLUSTER("cluster", Rarity.EPIC, 100);
    
    private final String name;
    private final Rarity rarity;
    private final int affinity;
    
    private EssenceType(@Nonnull String name, @Nonnull Rarity rarity, int affinity) {
        this.name = name;
        this.rarity = rarity;
        this.affinity = affinity;
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
    
    public int getAffinity() {
        return this.affinity;
    }
}
