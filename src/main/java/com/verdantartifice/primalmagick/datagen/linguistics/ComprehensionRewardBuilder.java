package com.verdantartifice.primalmagick.datagen.linguistics;

import java.util.OptionalInt;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.grids.rewards.AbstractReward;
import com.verdantartifice.primalmagick.common.books.grids.rewards.ComprehensionReward;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class ComprehensionRewardBuilder {
    protected static final Logger LOGGER = LogManager.getLogger();
    
    protected final ResourceKey<BookLanguage> language;
    protected int points;
    
    protected ComprehensionRewardBuilder(@Nonnull ResourceKey<BookLanguage> language, int points) {
        this.language = language;
        this.points = points;
    }
    
    public static ComprehensionRewardBuilder reward(@Nonnull ResourceKey<BookLanguage> language) {
        return new ComprehensionRewardBuilder(language, 1);
    }
    
    public ComprehensionRewardBuilder points(int points) {
        this.points = points;
        return this;
    }
    
    private void validate() {
        if (this.language == null) {
            throw new IllegalStateException("No language for comprehension linguistics grid node reward");
        }
        if (this.points < 0) {
            throw new IllegalStateException("Invalid point value for comprehension linguistics grid node reward");
        }
    }
    
    public IFinishedGridNodeReward build() {
        this.validate();
        return new Result(this.language, this.points);
    }
    
    public static class Result implements IFinishedGridNodeReward {
        protected final ResourceKey<BookLanguage> language;
        protected final int points;
        
        public Result(@Nonnull ResourceKey<BookLanguage> language, int points) {
            this.language = language;
            this.points = points;
        }

        @Override
        public JsonElement serialize() {
            return AbstractReward.dispatchCodec().encodeStart(JsonOps.INSTANCE, new ComprehensionReward(this.language, this.points)).resultOrPartial(LOGGER::error).orElseThrow();
        }

        @Override
        public OptionalInt getComprehensionPoints(ResourceLocation bookLanguageId) {
            return this.language.location().equals(bookLanguageId) ? OptionalInt.of(this.points) : OptionalInt.empty();
        }
    }
}
