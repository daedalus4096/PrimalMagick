package com.verdantartifice.primalmagick.datagen.linguistics;

import java.util.OptionalInt;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.grids.rewards.ComprehensionReward;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class ComprehensionRewardBuilder {
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
        public void serialize(JsonObject json) {
            json.addProperty("type", ComprehensionReward.TYPE);
            json.addProperty("language", this.language.location().toString());
            json.addProperty("points", this.points);
        }

        @Override
        public OptionalInt getComprehensionPoints(ResourceLocation bookLanguageId) {
            return this.language.location().equals(bookLanguageId) ? OptionalInt.of(this.points) : OptionalInt.empty();
        }
    }
}
