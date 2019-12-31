package com.verdantartifice.primalmagic.common.wands;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;

public class WandGem {
    protected static final Map<String, WandGem> REGISTRY = new HashMap<>();
    
    public static final WandGem APPRENTICE = new WandGem("apprentice", Rarity.COMMON, 75);
    public static final WandGem CREATIVE = new WandGem("creative", Rarity.EPIC, -1);
    
    protected final String tag;
    protected final int capacity;
    protected final Rarity rarity;
    protected final ModelResourceLocation mrl;
    
    public WandGem(@Nonnull String tag, @Nonnull Rarity rarity, int capacity) {
        this(tag, rarity, capacity, new ModelResourceLocation(new ResourceLocation(PrimalMagic.MODID, tag + "_wand_gem"), ""));
    }
    
    public WandGem(@Nonnull String tag, @Nonnull Rarity rarity, int capacity, @Nonnull ModelResourceLocation mrl) {
        if (REGISTRY.containsKey(tag)) {
            throw new IllegalArgumentException("Wand gem " + tag + " already registered!");
        }
        this.tag = tag;
        this.capacity = capacity;
        this.rarity = rarity;
        this.mrl = mrl;
        REGISTRY.put(tag, this);
    }
    
    @Nonnull
    public String getTag() {
        return this.tag;
    }
    
    public int getCapacity() {
        return this.capacity;
    }
    
    @Nonnull
    public Rarity getRarity() {
        return this.rarity;
    }
    
    @Nonnull
    public ModelResourceLocation getModelResourceLocation() {
        return this.mrl;
    }
    
    @Nonnull
    public String getNameTranslationKey() {
        return "primalmagic.wand_gem." + this.tag + ".name";
    }
    
    @Nonnull
    public static Collection<WandGem> getAllWandGems() {
        return Collections.unmodifiableCollection(REGISTRY.values());
    }
    
    @Nullable
    public static WandGem getWandGem(@Nullable String tag) {
        return REGISTRY.get(tag);
    }
}
