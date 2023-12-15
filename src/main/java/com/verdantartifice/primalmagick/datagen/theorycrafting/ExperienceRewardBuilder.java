package com.verdantartifice.primalmagick.datagen.theorycrafting;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.common.theorycrafting.rewards.ExperienceReward;

public class ExperienceRewardBuilder {
    protected final int points;
    
    protected ExperienceRewardBuilder(int points) {
        this.points = points;
    }
    
    public static ExperienceRewardBuilder points(int points) {
        return new ExperienceRewardBuilder(points);
    }
    
    private void validate() {
        if (this.points <= 0) {
            throw new IllegalStateException("Invalid points value for experience project reward");
        }
    }
    
    public IFinishedProjectReward build() {
        this.validate();
        return new ExperienceRewardBuilder.Result(this.points);
    }
    
    public static class Result implements IFinishedProjectReward {
        protected final int points;
        
        public Result(int points) {
            this.points = points;
        }
        
        @Override
        public void serialize(JsonObject json) {
            json.addProperty("type", ExperienceReward.TYPE);
            json.addProperty("points", this.points);
        }
    }
}
