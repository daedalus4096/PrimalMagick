package com.verdantartifice.primalmagic.common.wands;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;

public class WandCap {
    protected static final Map<String, WandCap> REGISTRY = new HashMap<>();
    
    public static final WandCap IRON = new WandCap("iron", 1.1F);
    
    protected final String tag;
    protected final float costModifier;
    protected final ModelResourceLocation mrl;
    
    public WandCap(@Nonnull String tag, float costModifier) {
        this(tag, costModifier, new ModelResourceLocation(new ResourceLocation(PrimalMagic.MODID, tag + "_wand_cap"), ""));
    }
    
    public WandCap(@Nonnull String tag, float costModifier, @Nonnull ModelResourceLocation mrl) {
        if (REGISTRY.containsKey(tag)) {
            throw new IllegalArgumentException("Wand cap " + tag + " already registered!");
        }
        this.tag = tag;
        this.costModifier = costModifier;
        this.mrl = mrl;
        REGISTRY.put(tag, this);
    }
    
    @Nonnull
    public String getTag() {
        return this.tag;
    }
    
    public float getCostModifier() {
        return this.costModifier;
    }
    
    @Nonnull
    public ModelResourceLocation getModelResourceLocation() {
        return this.mrl;
    }
    
    @Nonnull
    public String getNameTranslationKey() {
        return "primalmagic.wand_cap." + this.tag + ".name";
    }
    
    @Nonnull
    public static Collection<WandCap> getAllWandCaps() {
        return Collections.unmodifiableCollection(REGISTRY.values());
    }
    
    @Nullable
    public static WandCap getWandCap(@Nullable String tag) {
        return REGISTRY.get(tag);
    }
}
