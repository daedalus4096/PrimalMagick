package com.verdantartifice.primalmagick.datagen.linguistics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.verdantartifice.primalmagick.common.books.grids.rewards.AbstractReward;
import com.verdantartifice.primalmagick.common.books.grids.rewards.EmptyReward;

public class EmptyRewardBuilder {
    protected static final Logger LOGGER = LogManager.getLogger();
    
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
        public JsonElement serialize() {
            return AbstractReward.dispatchCodec().encodeStart(JsonOps.INSTANCE, EmptyReward.INSTANCE).resultOrPartial(LOGGER::error).orElseThrow();
        }
    }
}
