package com.verdantartifice.primalmagic.common.wands;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;

public class WandCore {
    protected static final Map<String, WandCore> REGISTRY = new HashMap<>();
    
    public static final WandCore HEARTWOOD = new WandCore("heartwood", Rarity.COMMON, 1, null, Collections.emptyList());
    
    protected final String tag;
    protected final Rarity rarity;
    protected final int spellSlots;
    protected final Source bonusSlot;
    protected final List<Source> aligned;
    protected final ModelResourceLocation mrl;

    public WandCore(@Nonnull String tag, @Nonnull Rarity rarity, int spellSlots, @Nullable Source bonusSlot, @Nonnull List<Source> aligned) {
        this(tag, rarity, spellSlots, bonusSlot, aligned, new ModelResourceLocation(new ResourceLocation(PrimalMagic.MODID, tag + "_wand_core"), ""));
    }
    
    public WandCore(@Nonnull String tag, @Nonnull Rarity rarity, int spellSlots, @Nullable Source bonusSlot, @Nonnull List<Source> aligned, @Nonnull ModelResourceLocation mrl) {
        if (REGISTRY.containsKey(tag)) {
            throw new IllegalArgumentException("Wand core " + tag + " already registered!");
        }
        this.tag = tag;
        this.rarity = rarity;
        this.spellSlots = spellSlots;
        this.bonusSlot = bonusSlot;
        this.aligned = aligned;
        this.mrl = mrl;
        REGISTRY.put(tag, this);
    }
    
    @Nonnull
    public String getTag() {
        return this.tag;
    }
    
    @Nonnull
    public Rarity getRarity() {
        return this.rarity;
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
    public ModelResourceLocation getModelResourceLocation() {
        return this.mrl;
    }
    
    @Nonnull
    public String getNameTranslationKey() {
        return "primalmagic.wand_core." + this.tag + ".name";
    }
    
    public static Collection<WandCore> getAllWandCores() {
        return Collections.unmodifiableCollection(REGISTRY.values());
    }
    
    @Nullable
    public static WandCore getWandCore(@Nullable String tag) {
        return REGISTRY.get(tag);
    }
}
