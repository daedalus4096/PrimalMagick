package com.verdantartifice.primalmagick.datagen.linguistics;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.verdantartifice.primalmagick.common.books.grids.rewards.AbstractReward;
import com.verdantartifice.primalmagick.common.books.grids.rewards.AttunementReward;
import com.verdantartifice.primalmagick.common.sources.Source;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

public class AttunementRewardBuilder {
    protected static final Logger LOGGER = LogManager.getLogger();
    
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
        public JsonElement serialize() {
            return AbstractReward.dispatchCodec().encodeStart(JsonOps.INSTANCE, new AttunementReward(this.source, this.points)).resultOrPartial(LOGGER::error).orElseThrow();
        }
    }
}
