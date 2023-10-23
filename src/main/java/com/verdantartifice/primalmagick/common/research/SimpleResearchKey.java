package com.verdantartifice.primalmagick.common.research;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.runes.RuneType;
import com.verdantartifice.primalmagick.common.util.ItemUtils;

import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Data object identifying a specific research entry, or a specific stage in that research entry.
 * 
 * @author Daedalus4096
 */
public class SimpleResearchKey implements IResearchKey {
    public static final SimpleResearchKey EMPTY = new SimpleResearchKey("");
    public static final SimpleResearchKey FIRST_STEPS = new SimpleResearchKey("FIRST_STEPS");
    
    public static final Codec<SimpleResearchKey> CODEC = Codec.STRING.xmap(SimpleResearchKey::parse, SimpleResearchKey::toString);
    
    protected static final String ITEM_SCAN_PREFIX = "!";
    protected static final String ENTITY_SCAN_PREFIX = "*";
    protected static final String CRAFTED_PREFIX = "[#]";
    protected static final String RUNE_ENCHANT_PREFIX = "&";
    protected static final String PARTIAL_RUNE_ENCHANT_PREFIX = "^";
    
    protected final String rootKey;
    protected final OptionalInt stage;
    
    protected SimpleResearchKey(@Nonnull String rootKey) {
        this(rootKey, OptionalInt.empty());
    }
    
    protected SimpleResearchKey(@Nonnull String rootKey, int stage) {
        this(rootKey, OptionalInt.of(stage));
    }
    
    protected SimpleResearchKey(@Nonnull String rootKey, @Nonnull OptionalInt stage) {
        this.rootKey = rootKey;
        this.stage = stage;
    }
    
    public static SimpleResearchKey of(ResearchName name) {
        return new SimpleResearchKey(name.rootName());
    }
    
    public static SimpleResearchKey of(ResearchName name, int stage) {
        return new SimpleResearchKey(name.rootName(), stage);
    }
    
    public static Optional<SimpleResearchKey> find(String keyStr) {
        return ResearchNames.find(keyStr).map(ResearchName::simpleKey);
    }
    
    public static SimpleResearchKey parse(String keyStr) {
        if (keyStr == null) {
            // Invalid key string
            throw new IllegalArgumentException("Research key may not be null");
        } else if (keyStr.contains("@")) {
            // Key string indicates a specific stage of a research entry
            String[] tokens = keyStr.split("@");
            OptionalInt stage;
            try {
                stage = OptionalInt.of(Integer.parseInt(tokens[1]));
            } catch (NumberFormatException e) {
                stage = OptionalInt.empty();
            }
            return new SimpleResearchKey(tokens[0], stage);
        } else {
            // Key string indicates a research entry without a specific stage
            return new SimpleResearchKey(keyStr, OptionalInt.empty());
        }
    }
    
    public static SimpleResearchKey parseItemScan(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            throw new IllegalArgumentException("Item stack may not be null or empty");
        } else {
            // Generate a research key based on the given itemstack's hash code after its NBT data has been stripped
            return parse(ITEM_SCAN_PREFIX + Integer.toString(ItemUtils.getHashCode(stack, true)));
        }
    }
    
    public static SimpleResearchKey parseEntityScan(EntityType<?> type) {
        if (type == null) {
            throw new IllegalArgumentException("Entity type may not be null");
        } else {
            return parse(ENTITY_SCAN_PREFIX + ForgeRegistries.ENTITY_TYPES.getKey(type).toString());
        }
    }
    
    public static SimpleResearchKey parseCrafted(int hashCode) {
        return parse(CRAFTED_PREFIX + Integer.toString(hashCode));
    }
    
    public static SimpleResearchKey parseRuneEnchantment(Enchantment enchant) {
        if (enchant == null) {
            throw new IllegalArgumentException("Enchantment may not be null");
        } else {
            return parse(RUNE_ENCHANT_PREFIX + ForgeRegistries.ENCHANTMENTS.getKey(enchant).toString());
        }
    }
    
    public static SimpleResearchKey parsePartialRuneEnchantment(Enchantment enchant, RuneType runeType) {
        if (enchant == null) {
            throw new IllegalArgumentException("Enchantment may not be null");
        } else if (runeType == null) {
            throw new IllegalArgumentException("Rune type may not be null");
        } else if (runeType == RuneType.POWER) {
            throw new IllegalArgumentException("Rune type may not be a power rune");
        } else {
            return parse(PARTIAL_RUNE_ENCHANT_PREFIX + ForgeRegistries.ENCHANTMENTS.getKey(enchant).toString() + "." + runeType.getSerializedName());
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
        return this.stage.isPresent();
    }
    
    public int getStage() {
        return this.stage.orElse(-1);
    }
    
    @Override
    public boolean isEmpty() {
        return StringUtil.isNullOrEmpty(this.rootKey) && this.stage.isEmpty();
    }
    
    @Nonnull
    public SimpleResearchKey stripStage() {
        return new SimpleResearchKey(this.rootKey, OptionalInt.empty());
    }
    
    @Override
    public boolean isKnownBy(@Nullable Player player) {
        if (player == null) {
            return false;
        }
        if (EMPTY.equals(this)) {
            return true;
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
    
    @Override
    public boolean isKnownByStrict(@Nullable Player player) {
        if (player == null) {
            return false;
        }
        if (EMPTY.equals(this)) {
            return true;
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
        return Objects.hash(rootKey, stage);
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
        return Objects.equals(rootKey, other.rootKey) && Objects.equals(stage, other.stage);
    }
}
