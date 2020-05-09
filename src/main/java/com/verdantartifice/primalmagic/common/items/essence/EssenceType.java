package com.verdantartifice.primalmagic.common.items.essence;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.items.ItemsPM;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.Rarity;
import net.minecraft.util.IStringSerializable;

/**
 * Definition of an essence type, e.g. the quality of an essence item.
 * 
 * @author Daedalus4096
 */
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
    
    @Nullable
    public EssenceType getUpgrade() {
        // Get the next quality up, or null if it's already highest
        switch (this) {
        case DUST:
            return SHARD;
        case SHARD:
            return CRYSTAL;
        case CRYSTAL:
            return CLUSTER;
        case CLUSTER:
        default:
            return null;
        }
    }
    
    @Nullable
    public EssenceType getDowngrade() {
        // Get the next quality down, or null if it's already lowest
        switch (this) {
        case CLUSTER:
            return CRYSTAL;
        case CRYSTAL:
            return SHARD;
        case SHARD:
            return DUST;
        case DUST:
        default:
            return null;
        }
    }
    
    @Nullable
    public Item getUpgradeMedium() {
        // Get the type of quartz that must be used to upgrade to this essence type
        switch (this) {
        case CLUSTER:
            return Items.QUARTZ_BLOCK;
        case CRYSTAL:
            return Items.QUARTZ;
        case SHARD:
            return ItemsPM.QUARTZ_NUGGET.get();
        case DUST:
        default:
            return null;
        }
    }
}
