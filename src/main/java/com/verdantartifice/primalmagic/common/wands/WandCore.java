package com.verdantartifice.primalmagic.common.wands;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;

public class WandCore {
    protected static final Map<String, WandCore> REGISTRY = new HashMap<>();
    
    public static final WandCore HEARTWOOD = new WandCore("heartwood", 1, null);
    
    protected final String tag;
    protected final int spellSlots;
    protected final Source affinity;
    protected final ModelResourceLocation mrl;

    public WandCore(@Nonnull String tag, int spellSlots, @Nullable Source affinity) {
        this(tag, spellSlots, affinity, new ModelResourceLocation(new ResourceLocation(PrimalMagic.MODID, tag + "_wand_core"), ""));
    }
    
    public WandCore(@Nonnull String tag, int spellSlots, @Nullable Source affinity, @Nonnull ModelResourceLocation mrl) {
        if (REGISTRY.containsKey(tag)) {
            throw new IllegalArgumentException("Wand core " + tag + " already registered!");
        }
        this.tag = tag;
        this.spellSlots = spellSlots;
        this.affinity = affinity;
        this.mrl = mrl;
        REGISTRY.put(tag, this);
    }
    
    @Nonnull
    public String getTag() {
        return this.tag;
    }
    
    public int getSpellSlots() {
        return this.spellSlots;
    }
    
    @Nullable
    public Source getAffinity() {
        return this.affinity;
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
