package com.verdantartifice.primalmagick.common.theorycrafting.weights;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

/**
 * Weight function that returns a progressing value based on a player's completed research.
 * 
 * @author Daedalus4096
 */
public class ProgressiveWeight implements IWeightFunction {
    public static final String TYPE = "progressive";
    public static final IWeightFunctionSerializer<ProgressiveWeight> SERIALIZER = new Serializer();
    
    private final double startingWeight;
    private final List<Modifier> modifiers;
    
    protected ProgressiveWeight(double startingWeight, List<Modifier> modifiers) {
        this.startingWeight = startingWeight;
        this.modifiers = modifiers;
    }

    @Override
    public double getWeight(Player player) {
        double retVal = this.startingWeight;
        for (Modifier modifier : this.modifiers) {
            if (modifier.researchKey().isKnownByStrict(player)) {
                retVal += modifier.weightModifier();
            }
        }
        return Math.max(0D, retVal);
    }

    @Override
    public String getFunctionType() {
        return TYPE;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public IWeightFunctionSerializer<ProgressiveWeight> getSerializer() {
        return SERIALIZER;
    }
    
    protected static record Modifier(SimpleResearchKey researchKey, double weightModifier) {
        protected static final Serializer SERIALIZER = new Serializer();
        
        protected static class Serializer {
            public Modifier read(ResourceLocation templateId, JsonObject json) {
                SimpleResearchKey key = SimpleResearchKey.parse(json.getAsJsonPrimitive("research_key").getAsString());
                double value = json.getAsJsonPrimitive("weight_modifier").getAsDouble();
                return new Modifier(key, value);
            }

            public Modifier fromNetwork(FriendlyByteBuf buf) {
                SimpleResearchKey key = SimpleResearchKey.parse(buf.readUtf());
                double value = buf.readDouble();
                return new Modifier(key, value);
            }

            public void toNetwork(FriendlyByteBuf buf, Modifier modifier) {
                buf.writeUtf(modifier.researchKey().toString());
                buf.writeDouble(modifier.weightModifier());
            }
        }
    }
    
    public static class Serializer implements IWeightFunctionSerializer<ProgressiveWeight> {
        @Override
        public ProgressiveWeight read(ResourceLocation templateId, JsonObject json) {
            double start = json.getAsJsonPrimitive("starting_weight").getAsDouble();
            
            List<Modifier> modifiers = new ArrayList<>();
            JsonArray modifiersArray = json.getAsJsonArray("modifiers");
            for (JsonElement modifierElement : modifiersArray) {
                try {
                    JsonObject modifierObj = modifierElement.getAsJsonObject();
                    modifiers.add(Modifier.SERIALIZER.read(templateId, modifierObj));
                } catch (Exception e) {
                    throw new JsonSyntaxException("Invalid modifier in weight function JSON for " + templateId.toString(), e);
                }
            }
            
            return new ProgressiveWeight(start, modifiers);
        }

        @Override
        public ProgressiveWeight fromNetwork(FriendlyByteBuf buf) {
            double start = buf.readDouble();
            
            List<Modifier> modifiers = new ArrayList<>();
            Modifier.Serializer modifierSerializer = new Modifier.Serializer();
            int modifierCount = buf.readVarInt();
            for (int index = 0; index < modifierCount; index++) {
                modifiers.add(modifierSerializer.fromNetwork(buf));
            }
            
            return new ProgressiveWeight(start, modifiers);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ProgressiveWeight weight) {
            buf.writeDouble(weight.startingWeight);
            buf.writeVarInt(weight.modifiers.size());
            for (Modifier modifier : weight.modifiers) {
                Modifier.SERIALIZER.toNetwork(buf, modifier);
            }
        }
    }
}
