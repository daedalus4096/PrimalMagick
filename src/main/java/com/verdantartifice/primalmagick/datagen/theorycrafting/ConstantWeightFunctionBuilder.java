package com.verdantartifice.primalmagick.datagen.theorycrafting;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.common.theorycrafting.weights.ConstantWeight;

public class ConstantWeightFunctionBuilder {
    protected final double weight;
    
    protected ConstantWeightFunctionBuilder(double weight) {
        this.weight = weight;
    }
    
    public static ConstantWeightFunctionBuilder weight(double value) {
        return new ConstantWeightFunctionBuilder(value);
    }
    
    private void validate() {
        if (this.weight <= 0D) {
            throw new IllegalStateException("Invalid value for constant weight function");
        }
    }
    
    public IFinishedWeightFunction build() {
        this.validate();
        return new Result(this.weight);
    }
    
    public static class Result implements IFinishedWeightFunction {
        protected final double weight;

        public Result(double weight) {
            this.weight = weight;
        }

        @Override
        public void serialize(JsonObject json) {
            json.addProperty("type", ConstantWeight.TYPE);
            json.addProperty("weight", this.weight);
        }
    }
}
