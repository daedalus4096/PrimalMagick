package com.verdantartifice.primalmagic.common.capabilities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Capability interface for storing attunement data.  Attached to player entities.
 * 
 * @author Daedalus4096
 */
public interface IPlayerAttunements extends INBTSerializable<CompoundNBT> {
    /**
     * Remove all attunement data from the player.
     */
    public void clear();
    
    /**
     * Get the stored value of the given attunement for the player.
     * 
     * @param source the source of the attunement to be retrieved
     * @param type the type of the attunement to be retrieved
     * @return the value of the attunement, or zero if not found
     */
    public int getValue(@Nullable Source source, @Nullable AttunementType type);
    
    /**
     * Store the given value of the given attunement for the player.
     * 
     * @param source the source of the attunement to be stored
     * @param type the type of the attunement to be stored
     * @param value the value of the attunement to be stored
     */
    public void setValue(@Nullable Source source, @Nullable AttunementType type, int value);
    
    /**
     * Sync the given player's attunement data to the their client.
     * 
     * @param player the player whose client should receive the data
     */
    public void sync(@Nullable ServerPlayerEntity player);
    
    /**
     * Represents a type of magical attunement.  Permanent attunement is gained through research and
     * cannot be removed.  Induced attunement is gained or lost through rituals, but does not decay
     * over time.  Temporary attunement is gained by crafting items and casting spells, but decays
     * slowly over time.
     * 
     * @author Daedalus4096
     */
    public static enum AttunementType {
        PERMANENT(-1),
        INDUCED(50),
        TEMPORARY(50);
        
        private int maximum;    // The maximum attunement amount of this type that the player can have at once
        
        private AttunementType(int max) {
            this.maximum = max;
        }
        
        public boolean isCapped() {
            return (this.maximum > 0);
        }
        
        public int getMaximum() {
            return this.maximum;
        }
        
        @Nonnull
        public String getNameTranslationKey() {
            return "primalmagic.attunement_type." + this.name();
        }
    }
}
