package com.verdantartifice.primalmagic.common.affinities;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.verdantartifice.primalmagic.common.sources.SourceList;
import com.verdantartifice.primalmagic.common.util.JsonUtils;

import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class PotionBonusAffinity extends AbstractAffinity {
    public static final Serializer SERIALIZER = new Serializer();
    
    protected SourceList bonusValues;
    
    protected PotionBonusAffinity(@Nonnull ResourceLocation target) {
        super(target);
    }
    
    @Override
    public AffinityType getType() {
        return AffinityType.POTION_BONUS;
    }

    @Override
    public IAffinitySerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    protected SourceList calculateTotal(@Nonnull RecipeManager recipeManager) {
        if (this.bonusValues != null) {
            return this.bonusValues;
        } else {
            throw new IllegalStateException("Potion bonus affinity has no values defined");
        }
    }

    public static class Serializer implements IAffinitySerializer<PotionBonusAffinity> {
        @Override
        public PotionBonusAffinity read(ResourceLocation affinityId, JsonObject json) {
            String target = json.getAsJsonPrimitive("target").getAsString();
            if (target == null) {
                throw new JsonSyntaxException("Illegal affinity target in affinity JSON for " + affinityId.toString());
            }
            
            ResourceLocation targetId = new ResourceLocation(target);
            if (!ForgeRegistries.POTION_TYPES.containsKey(targetId)) {
                throw new JsonSyntaxException("Unknown target potion type " + target + " in affinity JSON for " + affinityId.toString());
            }
            
            PotionBonusAffinity entry = new PotionBonusAffinity(targetId);
            if (json.has("bonus"))  {
                entry.bonusValues = JsonUtils.toSourceList(json.get("bonus").getAsJsonObject());
            } else {
                throw new JsonSyntaxException("Affinity entry must have bonus attribute");
            }
            
            return entry;
        }
    }
}
