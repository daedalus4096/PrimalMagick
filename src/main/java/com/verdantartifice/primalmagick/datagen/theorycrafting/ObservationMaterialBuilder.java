package com.verdantartifice.primalmagick.datagen.theorycrafting;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.theorycrafting.ObservationProjectMaterial;

public class ObservationMaterialBuilder {
    protected final int count;
    protected final boolean consumed;
    protected double weight = 1D;
    protected double bonusReward = 0.0D;
    protected CompoundResearchKey requiredResearch;

    protected ObservationMaterialBuilder(int count, boolean consumed) {
        this.count = count;
        this.consumed = consumed;
    }
    
    public static ObservationMaterialBuilder observation(int count, boolean consumed) {
        return new ObservationMaterialBuilder(count, consumed);
    }
    
    public ObservationMaterialBuilder weight(double weight) {
        this.weight = weight;
        return this;
    }
    
    public ObservationMaterialBuilder requiredResearch(@Nullable CompoundResearchKey key) {
        this.requiredResearch = key;
        return this;
    }
    
    public ObservationMaterialBuilder requiredResearch(@Nullable SimpleResearchKey key) {
        return requiredResearch(CompoundResearchKey.from(key));
    }
    
    public ObservationMaterialBuilder requiredResearch(@Nullable String keyStr) {
        return requiredResearch(CompoundResearchKey.parse(keyStr));
    }
    
    public ObservationMaterialBuilder bonusReward(double bonus) {
        this.bonusReward = bonus;
        return this;
    }
    
    private void validate() {
        if (this.count <= 0) {
            throw new IllegalStateException("Invalid observation count for observation project material");
        }
        if (this.weight <= 0D) {
            throw new IllegalStateException("Invalid weight for observation project material");
        }
        if (this.bonusReward < 0.0D) {
            throw new IllegalStateException("Invalid bonus reward for observation project material");
        }
    }
    
    public IFinishedProjectMaterial build() {
        this.validate();
        return new ObservationMaterialBuilder.Result(this.count, this.consumed, this.weight, this.bonusReward, this.requiredResearch);
    }
    
    public static class Result implements IFinishedProjectMaterial {
        protected final int count;
        protected final boolean consumed;
        protected final double weight;
        protected final double bonusReward;
        protected final CompoundResearchKey requiredResearch;

        public Result(int count, boolean consumed, double weight, double bonusReward, @Nullable CompoundResearchKey requiredResearch) {
            this.count = count;
            this.consumed = consumed;
            this.weight = weight;
            this.bonusReward = bonusReward;
            this.requiredResearch = requiredResearch == null ? null : requiredResearch.copy();
        }

        @Override
        public void serialize(JsonObject json) {
            json.addProperty("type", ObservationProjectMaterial.TYPE);
            json.addProperty("count", this.count);
            json.addProperty("consumed", this.consumed);
            json.addProperty("weight", this.weight);
            if (this.bonusReward > 0.0D) {
                json.addProperty("bonus_reward", this.bonusReward);
            }
            if (this.requiredResearch != null) {
                json.addProperty("required_research", this.requiredResearch.toString());
            }
        }
    }
}
