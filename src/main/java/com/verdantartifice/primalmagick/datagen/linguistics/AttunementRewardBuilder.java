package com.verdantartifice.primalmagick.datagen.linguistics;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.common.books.grids.rewards.AttunementReward;
import com.verdantartifice.primalmagick.common.sources.Source;

public class AttunementRewardBuilder {
    protected final Source source;
    protected int points;
    
    protected AttunementRewardBuilder(@Nonnull Source source, int points) {
        this.source = source;
        this.points = points;
    }
    
    public static AttunementRewardBuilder reward(@Nonnull Source source) {
        return new AttunementRewardBuilder(source, 1);
    }
    
    public AttunementRewardBuilder points(int points) {
        this.points = points;
        return this;
    }
    
    private void validate() {
        if (this.source == null) {
            throw new IllegalStateException("No source for attunement linguistics grid node reward");
        }
        if (this.points < 0) {
            throw new IllegalStateException("Invalid point value for attunement linguistics grid node reward");
        }
    }
    
    public IFinishedGridNodeReward build() {
        this.validate();
        return new Result(this.source, this.points);
    }
    
    public static class Result implements IFinishedGridNodeReward {
        protected final Source source;
        protected final int points;
        
        public Result(@Nonnull Source source, int points) {
            this.source = source;
            this.points = points;
        }

        @Override
        public void serialize(JsonObject json) {
            json.addProperty("type", AttunementReward.TYPE);
            json.addProperty("source", this.source.getId().toString());
            json.addProperty("points", this.points);
        }
    }
}
