package com.verdantartifice.primalmagick.datagen.linguistics;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.grids.rewards.ComprehensionReward;

public class ComprehensionRewardBuilder {
    protected final BookLanguage language;
    protected int points;
    
    protected ComprehensionRewardBuilder(@Nonnull BookLanguage language, int points) {
        this.language = language;
        this.points = points;
    }
    
    public static ComprehensionRewardBuilder reward(@Nonnull BookLanguage language) {
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
        protected final BookLanguage language;
        protected final int points;
        
        public Result(@Nonnull BookLanguage language, int points) {
            this.language = language;
            this.points = points;
        }

        @Override
        public void serialize(JsonObject json) {
            json.addProperty("type", ComprehensionReward.TYPE);
            json.addProperty("language", this.language.languageId().toString());
            json.addProperty("points", this.points);
        }
    }
}
