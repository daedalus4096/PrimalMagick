package com.verdantartifice.primalmagick.datagen.theorycrafting;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.theorycrafting.weights.ProgressiveWeight;

public class ProgressiveWeightFunctionBuilder {
    private final double startingWeight;
    private final List<Modifier> modifiers = new ArrayList<>();

    protected ProgressiveWeightFunctionBuilder(double startingWeight) {
        this.startingWeight = startingWeight;
    }
    
    public static ProgressiveWeightFunctionBuilder start(double startingWeight) {
        return new ProgressiveWeightFunctionBuilder(startingWeight);
    }
    
    public ProgressiveWeightFunctionBuilder modifier(SimpleResearchKey researchKey, double weightModifier) {
        this.modifiers.add(new Modifier(researchKey, weightModifier));
        return this;
    }
    
    public ProgressiveWeightFunctionBuilder modifier(String researchKeyStr, double weightModifier) {
        return this.modifier(SimpleResearchKey.find(researchKeyStr).orElseThrow(), weightModifier);
    }
    
    private void validate() {
        if (this.startingWeight <= 0D) {
            throw new IllegalStateException("Invalid starting value for progressive weight function");
        }
        if (this.modifiers.isEmpty()) {
            throw new IllegalStateException("Empty modifier list for progressive weight function; use a constant weight function instead");
        }
    }
    
    public IFinishedWeightFunction build() {
        this.validate();
        return new Result(this.startingWeight, this.modifiers);
    }
    
    protected static record Modifier(SimpleResearchKey researchKey, double weightModifier) {
        public void serialize(JsonObject json) {
            json.addProperty("research_key", this.researchKey().toString());
            json.addProperty("weight_modifier", this.weightModifier());
        }
        
        public JsonObject getModifierJson() {
            JsonObject json = new JsonObject();
            this.serialize(json);
            return json;
        }
    }
    
    public static class Result implements IFinishedWeightFunction {
        private final double startingWeight;
        private final List<Modifier> modifiers;

        public Result(double startingWeight, List<Modifier> modifiers) {
            this.startingWeight = startingWeight;
            this.modifiers = modifiers;
        }

        @Override
        public void serialize(JsonObject json) {
            json.addProperty("type", ProgressiveWeight.TYPE);
            json.addProperty("starting_weight", this.startingWeight);
            
            JsonArray modifierArray = new JsonArray();
            this.modifiers.forEach(modifier -> modifierArray.add(modifier.getModifierJson()));
            json.add("modifiers", modifierArray);
        }
    }
}
