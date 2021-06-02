package com.verdantartifice.primalmagic.common.stats;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.stats.IStatFormatter;
import net.minecraft.util.ResourceLocation;

/**
 * Definition of a statistic tracked by the mod, such as how many times the grimoire is opened.  Does
 * not use the vanilla statistics system so that we can control visibility of the stat and display
 * stats in a fixed order, rather than randomly.
 * 
 * @author Daedalus4096
 */
public class Stat {
    protected ResourceLocation location;
    protected IStatFormatter formatter;
    protected boolean hidden;
    protected boolean internal;
    
    protected Stat(@Nonnull ResourceLocation location, @Nonnull IStatFormatter formatter, boolean hidden, boolean internal) {
        this.location = location;
        this.formatter = formatter;
        this.hidden = hidden;
        this.internal = internal;
    }
    
    @Nonnull
    public static Stat create(@Nonnull String name, @Nonnull IStatFormatter formatter, boolean hidden) {
        return create(name, formatter, hidden, false);
    }
    
    @Nonnull
    public static Stat create(@Nonnull String name, @Nonnull IStatFormatter formatter, boolean hidden, boolean internal) {
        // Create the new stat and register it with the stats manager
        Stat retVal = new Stat(new ResourceLocation(PrimalMagic.MODID, name), formatter, hidden, internal);
        StatsManager.registerStat(retVal);
        return retVal;
    }
    
    @Nonnull
    public ResourceLocation getLocation() {
        return this.location;
    }
    
    @Nonnull
    public IStatFormatter getFormatter() {
        return this.formatter;
    }
    
    public boolean isHidden() {
        return this.hidden;
    }
    
    public boolean isInternal() {
        return this.internal;
    }
    
    @Nonnull
    public String getTranslationKey() {
        return "stat." + this.location.getNamespace() + "." + this.location.getPath();
    }
}
