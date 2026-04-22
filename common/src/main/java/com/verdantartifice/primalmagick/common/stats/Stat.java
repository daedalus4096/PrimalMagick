package com.verdantartifice.primalmagick.common.stats;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.StatFormatter;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Definition of a statistic tracked by the mod, such as how many times the grimoire is opened.  Does
 * not use the vanilla statistics system so that we can control visibility of the stat and display
 * stats in a fixed order, rather than randomly.
 * 
 * @author Daedalus4096
 */
public record Stat(Identifier key, StatFormatter formatter, Optional<Identifier> iconLocationOpt, boolean hidden, boolean internal, boolean hasHint) {
    @Nonnull
    public String getTranslationKey() {
        return String.join(".", "stat", this.key.getNamespace(), this.key.getPath());
    }
    
    @Nonnull
    public Optional<String> getHintTranslationKey() {
        if (this.hasHint) {
            return Optional.of(String.join(".", "stat", this.key.getNamespace(), this.key.getPath(), "hint"));
        } else {
            return Optional.empty();
        }
    }
    
    public static Builder builder(Identifier loc) {
        return new Builder(loc);
    }
    
    public static Builder builder(String rawKey) {
        return new Builder(ResourceUtils.loc(rawKey));
    }
    
    public static class Builder {
        protected final Identifier key;
        protected StatFormatter formatter = StatFormatter.DEFAULT;
        protected Optional<Identifier> iconLocationOpt = Optional.empty();
        protected boolean hidden = false;
        protected boolean internal = false;
        protected boolean hasHint = false;
        
        public Builder(Identifier key) {
            this.key = key;
        }
        
        public Builder formatter(StatFormatter formatter) {
            this.formatter = formatter;
            return this;
        }
        
        public Builder icon(Identifier iconLoc) {
            this.iconLocationOpt = Optional.ofNullable(iconLoc);
            return this;
        }
        
        public Builder hidden() {
            this.hidden = true;
            return this;
        }
        
        public Builder internal() {
            this.internal = true;
            return this;
        }
        
        public Builder hasHint() {
            this.hasHint = true;
            return this;
        }
        
        public Stat build() {
            Stat retVal = new Stat(this.key, this.formatter, this.iconLocationOpt, this.hidden, this.internal, this.hasHint);
            StatsManager.registerStat(retVal);
            return retVal;
        }
    }
}
