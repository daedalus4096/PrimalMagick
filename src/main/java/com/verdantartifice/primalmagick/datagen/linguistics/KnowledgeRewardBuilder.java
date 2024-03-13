package com.verdantartifice.primalmagick.datagen.linguistics;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.common.books.grids.rewards.KnowledgeReward;
import com.verdantartifice.primalmagick.common.research.KnowledgeType;

public class KnowledgeRewardBuilder {
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
        public void serialize(JsonObject json) {
            json.addProperty("type", KnowledgeReward.TYPE);
            json.addProperty("knowledge_type", this.knowledgeType.getSerializedName());
            json.addProperty("levels", this.levels);
        }
    }
}
