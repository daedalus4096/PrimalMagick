package com.verdantartifice.primalmagick.common.items.essence;

import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

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
    
    public Optional<EssenceType> getUpgrade() {
        // Get the next quality up, or empty if it's already highest
        return switch (this) {
            case DUST -> Optional.of(SHARD);
            case SHARD -> Optional.of(CRYSTAL);
            case CRYSTAL -> Optional.of(CLUSTER);
            case CLUSTER -> Optional.empty();
        };
    }
    
    public Optional<ResourceKey<ResearchEntry>> getUpgradeResearchEntry() {
        return switch (this) {
            case DUST -> Optional.of(ResearchEntries.SHARD_SYNTHESIS);
            case SHARD -> Optional.of(ResearchEntries.CRYSTAL_SYNTHESIS);
            case CRYSTAL -> Optional.of(ResearchEntries.CLUSTER_SYNTHESIS);
            case CLUSTER -> Optional.empty();
        };
    }
    
    public Optional<EssenceType> getDowngrade() {
        // Get the next quality down, or empty if it's already lowest
        return switch (this) {
            case CLUSTER -> Optional.of(CRYSTAL);
            case CRYSTAL -> Optional.of(SHARD);
            case SHARD -> Optional.of(DUST);
            case DUST -> Optional.empty();
        };
    }
    
    public Optional<ResourceKey<ResearchEntry>> getDowngradeResearchEntry() {
        return switch (this) {
            case CLUSTER -> Optional.of(ResearchEntries.CLUSTER_DESYNTHESIS);
            case CRYSTAL -> Optional.of(ResearchEntries.CRYSTAL_DESYNTHESIS);
            case SHARD -> Optional.of(ResearchEntries.SHARD_DESYNTHESIS);
            case DUST -> Optional.empty();
        };
    }
    
    public Optional<Item> getUpgradeMedium() {
        // Get the type of quartz that must be used to upgrade to this essence type
        return switch (this) {
            case CLUSTER -> Optional.of(Items.QUARTZ_BLOCK);
            case CRYSTAL -> Optional.of(Items.QUARTZ);
            case SHARD -> Optional.of(ItemsPM.QUARTZ_NUGGET.get());
            case DUST -> Optional.empty();
        };
    }
    
    public boolean isDiscovered(@Nullable Player player) {
        if (player == null) {
            return false;
        } else {
            return switch (this) {
                case DUST -> true;
                case SHARD -> new ResearchEntryKey(ResearchEntries.SHARD_SYNTHESIS).isKnownBy(player);
                case CRYSTAL -> new ResearchEntryKey(ResearchEntries.CRYSTAL_SYNTHESIS).isKnownBy(player);
                case CLUSTER -> new ResearchEntryKey(ResearchEntries.CLUSTER_SYNTHESIS).isKnownBy(player);
            };
        }
    }
}
