package com.verdantartifice.primalmagick.common.wands;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Definition of a wand core data structure.  Wand cores determine the number of spells that may be
 * inscribed onto the wand.  It specifies a base number of spell slots, and optionally a bonus slot,
 * into which only a spell of a given source type may be inscribed.  Cores also determine any primal
 * alignments the wand has, causing them to naturally regenerate mana of those sources.  Also contains
 * a static registry of all types of wand cores in the mod.
 * 
 * @author Daedalus4096
 */
@Immutable
public class WandCore implements IWandComponent {
    protected static final Map<String, WandCore> REGISTRY = new HashMap<>();
    
    public static final Codec<WandCore> CODEC = Codec.STRING.xmap(WandCore::getWandCore, WandCore::getTag);
    public static final StreamCodec<ByteBuf, WandCore> STREAM_CODEC = ByteBufCodecs.STRING_UTF8.map(WandCore::getWandCore, WandCore::getTag);
    
    public static final WandCore HEARTWOOD = new WandCore("heartwood", Rarity.COMMON, 1, null, Collections.emptyList());
    public static final WandCore OBSIDIAN = new WandCore("obsidian", Rarity.COMMON, 1, Sources.EARTH, Arrays.asList(Sources.EARTH));
    public static final WandCore CORAL = new WandCore("coral", Rarity.COMMON, 1, Sources.SEA, Arrays.asList(Sources.SEA));
    public static final WandCore BAMBOO = new WandCore("bamboo", Rarity.COMMON, 1, Sources.SKY, Arrays.asList(Sources.SKY));
    public static final WandCore SUNWOOD = new WandCore("sunwood", Rarity.COMMON, 1, Sources.SUN, Arrays.asList(Sources.SUN));
    public static final WandCore MOONWOOD = new WandCore("moonwood", Rarity.COMMON, 1, Sources.MOON, Arrays.asList(Sources.MOON));
    public static final WandCore BONE = new WandCore("bone", Rarity.UNCOMMON, 2, Sources.BLOOD, Arrays.asList(Sources.BLOOD));
    public static final WandCore BLAZE_ROD = new WandCore("blaze_rod", Rarity.UNCOMMON, 2, Sources.INFERNAL, Arrays.asList(Sources.INFERNAL));
    public static final WandCore PURPUR = new WandCore("purpur", Rarity.UNCOMMON, 2, Sources.VOID, Arrays.asList(Sources.VOID));
    public static final WandCore PRIMAL = new WandCore("primal", Rarity.UNCOMMON, 2, null, Arrays.asList(Sources.EARTH, Sources.SEA, Sources.SKY, Sources.SUN, Sources.MOON));
    public static final WandCore DARK_PRIMAL = new WandCore("dark_primal", Rarity.RARE, 3, null, Arrays.asList(Sources.EARTH, Sources.SEA, Sources.SKY, Sources.SUN, Sources.MOON, Sources.BLOOD, Sources.INFERNAL, Sources.VOID));
    public static final WandCore PURE_PRIMAL = new WandCore("pure_primal", Rarity.EPIC, 4, null, Arrays.asList(Sources.EARTH, Sources.SEA, Sources.SKY, Sources.SUN, Sources.MOON, Sources.BLOOD, Sources.INFERNAL, Sources.VOID, Sources.HALLOWED));
    
    protected final String tag;                     // Unique identifier for the wand core
    protected final Rarity rarity;                  // The core's rarity, used to color its name and determine completed wand rarity
    protected final int spellSlots;                 // The base number of spell slots offered by the core
    protected final Source bonusSlot;               // The source of the core's bonus spell slot, if any
    protected final List<Source> aligned;           // List of sources to which the core is aligned
    protected final ResourceLocation wandMrlNamespace;  // Resource location of the wand core's model, stored in a blockstate file
    protected final ResourceLocation staffMrlNamespace; // Resource location of the staff core's model, stored in a blockstate file

    public WandCore(@Nonnull String tag, @Nonnull Rarity rarity, int spellSlots, @Nullable Source bonusSlot, @Nonnull List<Source> aligned) {
        this(tag, rarity, spellSlots, bonusSlot, aligned, ResourceUtils.loc(tag + "_wand_core"), ResourceUtils.loc(tag + "_staff_core"));
    }
    
    public WandCore(@Nonnull String tag, @Nonnull Rarity rarity, int spellSlots, @Nullable Source bonusSlot, @Nonnull List<Source> aligned, @Nonnull ResourceLocation wmrln, @Nonnull ResourceLocation smrln) {
        if (REGISTRY.containsKey(tag)) {
            // Don't allow a given core to be registered more than once
            throw new IllegalArgumentException("Wand core " + tag + " already registered!");
        }
        this.tag = tag;
        this.rarity = rarity;
        this.spellSlots = spellSlots;
        this.bonusSlot = bonusSlot;
        this.aligned = aligned;
        this.wandMrlNamespace = wmrln;
        this.staffMrlNamespace = smrln;
        REGISTRY.put(tag, this);
    }
    
    @Override
    public String getTag() {
        return this.tag;
    }
    
    @Override
    public Rarity getRarity() {
        return this.rarity;
    }
    
    @Override
    public Type getComponentType() {
        return IWandComponent.Type.CORE;
    }
    
    public int getSpellSlots() {
        return this.spellSlots;
    }
    
    @Nullable
    public Source getBonusSlot() {
        return this.bonusSlot;
    }
    
    @Nonnull
    public List<Source> getAlignedSources() {
        return Collections.unmodifiableList(this.aligned);
    }
    
    @Nonnull
    public ResourceLocation getWandModelResourceLocationNamespace() {
        return this.wandMrlNamespace;
    }
    
    @Nonnull
    public ResourceLocation getStaffModelResourceLocationNamespace() {
        return this.staffMrlNamespace;
    }
    
    public static Collection<WandCore> getAllWandCores() {
        return Collections.unmodifiableCollection(REGISTRY.values());
    }
    
    @Nullable
    public static WandCore getWandCore(@Nullable String tag) {
        return REGISTRY.get(tag);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof WandCore wandCore)) return false;
        return spellSlots == wandCore.spellSlots && Objects.equals(tag, wandCore.tag) && rarity == wandCore.rarity && Objects.equals(bonusSlot, wandCore.bonusSlot) && Objects.equals(aligned, wandCore.aligned) && Objects.equals(wandMrlNamespace, wandCore.wandMrlNamespace) && Objects.equals(staffMrlNamespace, wandCore.staffMrlNamespace);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag, rarity, spellSlots, bonusSlot, aligned, wandMrlNamespace, staffMrlNamespace);
    }
}
