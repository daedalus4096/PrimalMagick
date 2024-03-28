package com.verdantartifice.primalmagick.datagen.linguistics;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.common.books.grids.rewards.EmptyReward;

public class EmptyRewardBuilder {
    protected EmptyRewardBuilder() {}
    
    public static EmptyRewardBuilder reward() {
        return new EmptyRewardBuilder();
    }
    
    public IFinishedGridNodeReward build() {
        return new Result();
    }
    
    public static class Result implements IFinishedGridNodeReward {
        public Result() {}

        @Override
        public void serialize(JsonObject json) {
            json.addProperty("type", EmptyReward.TYPE);
        }
    }
}
