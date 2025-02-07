package com.verdantartifice.primalmagick.datagen.linguistics;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.verdantartifice.primalmagick.common.books.grids.rewards.AbstractReward;
import com.verdantartifice.primalmagick.common.books.grids.rewards.KnowledgeReward;
import com.verdantartifice.primalmagick.common.research.KnowledgeType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

public class KnowledgeRewardBuilder {
    protected static final Logger LOGGER = LogManager.getLogger();
    
    private final KnowledgeType knowledgeType;
    private int levels;
    
    protected KnowledgeRewardBuilder(@Nonnull KnowledgeType knowledgeType, int levels) {
        this.knowledgeType = knowledgeType;
        this.levels = levels;
    }
    
    public static KnowledgeRewardBuilder reward(@Nonnull KnowledgeType knowledgeType) {
        return new KnowledgeRewardBuilder(knowledgeType, 1);
    }
    
    public KnowledgeRewardBuilder levels(int levels) {
        this.levels = levels;
        return this;
    }
    
    private void validate() {
        if (this.knowledgeType == null) {
            throw new IllegalStateException("No knowledge type for knowledge linguistics grid node reward");
        }
        if (this.levels < 0) {
            throw new IllegalStateException("Invalid level value for knowledge linguistics grid node reward");
        }
    }
    
    public IFinishedGridNodeReward build() {
        this.validate();
        return new Result(this.knowledgeType, this.levels);
    }
    
    public static class Result implements IFinishedGridNodeReward {
        private final KnowledgeType knowledgeType;
        private final int levels;
        
        public Result(@Nonnull KnowledgeType knowledgeType, int levels) {
            this.knowledgeType = knowledgeType;
            this.levels = levels;
        }

        @Override
        public JsonElement serialize() {
            return AbstractReward.dispatchCodec().encodeStart(JsonOps.INSTANCE, new KnowledgeReward(this.knowledgeType, this.levels)).resultOrPartial(LOGGER::error).orElseThrow();
        }
    }
}
