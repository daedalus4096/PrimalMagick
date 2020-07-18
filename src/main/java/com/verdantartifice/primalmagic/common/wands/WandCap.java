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

/**
 * Definition of a wand cap data structure.  Wand caps determine the discount or penalty applied to a
 * spell or crafting operation when used as a mana source.  Also contains a static registry of all
 * types of wand caps in the mod.
 * 
 * @author Daedalus4096
 */
public class WandCap {
    protected static final Map<String, WandCap> REGISTRY = new HashMap<>();
    
    public static final WandCap IRON = new WandCap("iron", Rarity.COMMON, 1.1D);
    public static final WandCap GOLD = new WandCap("gold", Rarity.COMMON, 1.0D);
    public static final WandCap PRIMALITE = new WandCap("primalite", Rarity.UNCOMMON, 0.95D);
    public static final WandCap HEXIUM = new WandCap("hexium", Rarity.RARE, 0.9D);
    public static final WandCap HALLOWSTEEL = new WandCap("hallowsteel", Rarity.EPIC, 0.85D);
    
    protected final String tag;                     // Unique identifier for the wand cap
    protected final Rarity rarity;                  // The cap's rarity, used to color its name and determine completed wand rarity
    protected final double baseCostModifier;        // The base discount or penalty to apply to mana usage, modified by other factors later
    protected final ModelResourceLocation wandMrl;  // Resource location of the wand cap's model, stored in a blockstate file
    protected final ModelResourceLocation staffMrl; // Resource location of the staff cap's model, stored in a blockstate file
    
    public WandCap(@Nonnull String tag, @Nonnull Rarity rarity, double costModifier) {
        this(tag, rarity, costModifier, new ModelResourceLocation(new ResourceLocation(PrimalMagic.MODID, tag + "_wand_cap"), ""), new ModelResourceLocation(new ResourceLocation(PrimalMagic.MODID, tag + "_staff_cap"), ""));
    }
    
    public WandCap(@Nonnull String tag, @Nonnull Rarity rarity, double costModifier, @Nonnull ModelResourceLocation wmrl, @Nonnull ModelResourceLocation smrl) {
        if (REGISTRY.containsKey(tag)) {
            // Don't allow a given cap to be registered more than once
            throw new IllegalArgumentException("Wand cap " + tag + " already registered!");
        }
        this.tag = tag;
        this.rarity = rarity;
        this.baseCostModifier = costModifier;
        this.wandMrl = wmrl;
        this.staffMrl = smrl;
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
    
    public double getBaseCostModifier() {
        return this.baseCostModifier;
    }
    
    @Nonnull
    public ModelResourceLocation getWandModelResourceLocation() {
        return this.wandMrl;
    }
    
    @Nonnull
    public ModelResourceLocation getStaffModelResourceLocation() {
        return this.staffMrl;
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
