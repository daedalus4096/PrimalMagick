package com.verdantartifice.primalmagick.datagen.theorycrafting;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.theorycrafting.materials.ExperienceProjectMaterial;

public class ExperienceMaterialBuilder {
    protected final int levels;
    protected final boolean consumed;
    protected double weight = 1D;
    protected double bonusReward = 0.0D;
    protected CompoundResearchKey requiredResearch;

    protected ExperienceMaterialBuilder(int levels, boolean consumed) {
        this.levels = levels;
        this.consumed = consumed;
    }
    
    public static ExperienceMaterialBuilder experience(int levels, boolean consumed) {
        return new ExperienceMaterialBuilder(levels, consumed);
    }
    
    public ExperienceMaterialBuilder weight(double weight) {
        this.weight = weight;
        return this;
    }
    
    public ExperienceMaterialBuilder requiredResearch(@Nullable CompoundResearchKey key) {
        this.requiredResearch = key;
        return this;
    }
    
    public ExperienceMaterialBuilder requiredResearch(@Nullable SimpleResearchKey key) {
        return requiredResearch(CompoundResearchKey.from(key));
    }
    
    public ExperienceMaterialBuilder requiredResearch(@Nullable String keyStr) {
        return requiredResearch(CompoundResearchKey.parse(keyStr));
    }
    
    public ExperienceMaterialBuilder bonusReward(double bonus) {
        this.bonusReward = bonus;
        return this;
    }
    
    private void validate() {
        if (this.levels <= 0) {
            throw new IllegalStateException("Invalid levels for experience project material");
        }
        if (this.weight <= 0D) {
            throw new IllegalStateException("Invalid weight for experience project material");
        }
        if (this.bonusReward < 0.0D) {
            throw new IllegalStateException("Invalid bonus reward for experience project material");
        }
    }
    
    public IFinishedProjectMaterial build() {
        this.validate();
        return new ExperienceMaterialBuilder.Result(this.levels, this.consumed, this.weight, this.bonusReward, this.requiredResearch);
    }
    
    public static class Result implements IFinishedProjectMaterial {
        protected final int levels;
        protected final boolean consumed;
        protected final double weight;
        protected final double bonusReward;
        protected final CompoundResearchKey requiredResearch;

        public Result(int levels, boolean consumed, double weight, double bonusReward, @Nullable CompoundResearchKey requiredResearch) {
            this.levels = levels;
            this.consumed = consumed;
            this.weight = weight;
            this.bonusReward = bonusReward;
            this.requiredResearch = requiredResearch == null ? null : requiredResearch.copy();
        }

        @Override
        public void serialize(JsonObject json) {
            json.addProperty("type", ExperienceProjectMaterial.TYPE);
            json.addProperty("levels", this.levels);
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
