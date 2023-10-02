package com.verdantartifice.primalmagick.common.items.essence;

import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.research.ResearchNames;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;

/**
 * Definition of an essence type, e.g. the quality of an essence item.
 * 
 * @author Daedalus4096
 */
public enum EssenceType implements StringRepresentable {
    DUST("dust", Rarity.COMMON, 5, 1),
    SHARD("shard", Rarity.UNCOMMON, 20, 10),
    CRYSTAL("crystal", Rarity.RARE, 50, 100),
    CLUSTER("cluster", Rarity.EPIC, 100, 1000);
    
    private static final Supplier<SimpleResearchKey> SHARD_RESEARCH = ResearchNames.simpleKey(ResearchNames.SHARD_SYNTHESIS);
    private static final Supplier<SimpleResearchKey> CRYSTAL_RESEARCH = ResearchNames.simpleKey(ResearchNames.CRYSTAL_SYNTHESIS);
    private static final Supplier<SimpleResearchKey> CLUSTER_RESEARCH = ResearchNames.simpleKey(ResearchNames.CLUSTER_SYNTHESIS);
    
    private final String name;
    private final Rarity rarity;
    private final int affinity;
    private final int mana;
    
    private EssenceType(@Nonnull String name, @Nonnull Rarity rarity, int affinity, int mana) {
        this.name = name;
        this.rarity = rarity;
        this.affinity = affinity;
        this.mana = mana;
    }

    @Override
    @Nonnull
    public String getSerializedName() {
        return this.name;
    }
    
    @Nonnull
    public Rarity getRarity() {
        return this.rarity;
    }
    
    public int getAffinity() {
        return this.affinity;
    }
    
    /**
     * Gets the amount of real mana this essence type is worth when broken down in a machine such
     * as the Wand Charger.
     * 
     * @return the mana equivalent of this essence type
     */
    public int getManaEquivalent() {
        return this.mana;
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
    
    public boolean isDiscovered(@Nullable Player player) {
        if (player == null) {
            return false;
        } else {
            switch (this) {
            case DUST:
                return true;
            case SHARD:
                return SHARD_RESEARCH.get().isKnownByStrict(player);
            case CRYSTAL:
                return CRYSTAL_RESEARCH.get().isKnownByStrict(player);
            case CLUSTER:
                return CLUSTER_RESEARCH.get().isKnownByStrict(player);
            default:
                return false;
            }
        }
    }
}
