package com.verdantartifice.primalmagick.common.theorycrafting.weights;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

/**
 * Weight function that returns a constant value.
 * 
 * @author Daedalus4096
 */
public class ConstantWeight implements IWeightFunction {
    public static final String TYPE = "constant";
    public static final IWeightFunctionSerializer<ConstantWeight> SERIALIZER = new Serializer();
    
    private final double weight;
    
    protected ConstantWeight(double weight) {
        this.weight = weight;
    }
    
    @Override
    public double getWeight(Player t) {
        return this.weight;
    }

    @Override
    public String getFunctionType() {
        return TYPE;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public IWeightFunctionSerializer<ConstantWeight> getSerializer() {
        return SERIALIZER;
    }

    public static class Serializer implements IWeightFunctionSerializer<ConstantWeight> {
        @Override
        public ConstantWeight read(ResourceLocation templateId, JsonObject json) {
            return new ConstantWeight(json.getAsJsonPrimitive("weight").getAsDouble());
        }

        @Override
        public ConstantWeight fromNetwork(FriendlyByteBuf buf) {
            return new ConstantWeight(buf.readDouble());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ConstantWeight template) {
            buf.writeDouble(template.weight);
        }
    }
}
