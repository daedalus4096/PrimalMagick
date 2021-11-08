package com.verdantartifice.primalmagick.common.research;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.util.ItemUtils;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * Data object identifying a specific research entry, or a specific stage in that research entry.
 * 
 * @author Daedalus4096
 */
public class SimpleResearchKey {
    public static final SimpleResearchKey FIRST_STEPS = SimpleResearchKey.parse("FIRST_STEPS");
    
    protected static final String ITEM_SCAN_PREFIX = "!";
    protected static final String ENTITY_SCAN_PREFIX = "*";
    protected static final String CRAFTED_PREFIX = "[#]";
    protected static final String RUNE_ENCHANT_PREFIX = "&";
    
    protected String rootKey;
    protected Integer stage;
    
    protected SimpleResearchKey(@Nonnull String rootKey, @Nullable Integer stage) {
        this.rootKey = rootKey;
        this.stage = stage;
    }
    
    @Nullable
    public static SimpleResearchKey parse(@Nullable String keyStr) {
        if (keyStr == null) {
            // Invalid key string
            return null;
        } else if (keyStr.contains("@")) {
            // Key string indicates a specific stage of a research entry
            String[] tokens = keyStr.split("@");
            int stage;
            try {
                stage = Integer.parseInt(tokens[1]);
            } catch (NumberFormatException e) {
                stage = 0;
            }
            return new SimpleResearchKey(tokens[0], stage);
        } else {
            // Key string indicates a research entry without a specific stage
            return new SimpleResearchKey(keyStr, null);
        }
    }
    
    @Nullable
    public static SimpleResearchKey parseItemScan(@Nullable ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return null;
        } else {
            // Generate a research key based on the given itemstack's hash code after its NBT data has been stripped
            return parse(ITEM_SCAN_PREFIX + Integer.toString(ItemUtils.getHashCode(stack, true)));
        }
    }
    
    @Nullable
    public static SimpleResearchKey parseEntityScan(@Nullable EntityType<?> type) {
        if (type == null) {
            return null;
        } else {
            return parse(ENTITY_SCAN_PREFIX + type.getRegistryName().toString());
        }
    }
    
    @Nullable
    public static SimpleResearchKey parseCrafted(int hashCode) {
        return parse(CRAFTED_PREFIX + Integer.toString(hashCode));
    }
    
    @Nullable
    public static SimpleResearchKey parseRuneEnchantment(@Nullable Enchantment enchant) {
        if (enchant == null) {
            return null;
        } else {
            return parse(RUNE_ENCHANT_PREFIX + enchant.getRegistryName().toString());
        }
    }
    
    @Nonnull
    public SimpleResearchKey copy() {
        return new SimpleResearchKey(this.rootKey, this.stage);
    }
    
    @Nonnull
    public String getRootKey() {
        return this.rootKey;
    }
    
    public boolean hasStage() {
        return (this.stage != null);
    }
    
    public int getStage() {
        return this.hasStage() ? this.stage.intValue() : -1;
    }
    
    @Nonnull
    public SimpleResearchKey stripStage() {
        return new SimpleResearchKey(this.rootKey, null);
    }
    
    public boolean isKnownBy(@Nullable Player player) {
        if (player == null) {
            return false;
        }
        IPlayerKnowledge knowledge = PrimalMagickCapabilities.getKnowledge(player).orElse(null);
        if (knowledge == null) {
            return false;
        } else {
            // To be known, the given player must have progressed to at least the stage specified in
            // this key.  If no stage is specified, the player must have started the research.
            return knowledge.isResearchKnown(this);
        }
    }
    
    public boolean isKnownByStrict(@Nullable Player player) {
        if (player == null) {
            return false;
        }
        IPlayerKnowledge knowledge = PrimalMagickCapabilities.getKnowledge(player).orElse(null);
        if (knowledge == null) {
            return false;
        }
        
        // To be known strictly, the given player must have progressed to at least the stage specified
        // in this key.  If no stage is specified, the player must have completed the research.
        if (this.hasStage()) {
            if (!knowledge.isResearchKnown(this)) {
                return false;
            }
        } else if (!knowledge.isResearchComplete(this)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(this.rootKey);
        if (this.hasStage()) {
            builder.append('@');
            builder.append(this.getStage());
        }
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((rootKey == null) ? 0 : rootKey.hashCode());
        result = prime * result + ((stage == null) ? 0 : stage.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SimpleResearchKey other = (SimpleResearchKey) obj;
        if (rootKey == null) {
            if (other.rootKey != null)
                return false;
        } else if (!rootKey.equals(other.rootKey))
            return false;
        if (stage == null) {
            if (other.stage != null)
                return false;
        } else if (!stage.equals(other.stage))
            return false;
        return true;
    }
}
