package com.verdantartifice.primalmagic.common.affinities;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.verdantartifice.primalmagic.common.sources.SourceList;
import com.verdantartifice.primalmagic.common.util.JsonUtils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchantmentBonusAffinity extends AbstractAffinity {
    public static final Serializer SERIALIZER = new Serializer();
    
    protected SourceList multiplierValues;
    
    protected EnchantmentBonusAffinity(@Nonnull ResourceLocation target) {
        super(target);
    }

    @Override
    public AffinityType getType() {
        return AffinityType.ENCHANTMENT_BONUS;
    }

    @Override
    public IAffinitySerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    protected SourceList calculateTotal(@Nonnull RecipeManager recipeManager) {
        if (this.multiplierValues != null) {
            return this.multiplierValues;
        } else {
            throw new IllegalStateException("Enchantment bonus affinity has no values defined");
        }
    }

    public static class Serializer implements IAffinitySerializer<EnchantmentBonusAffinity> {
        @Override
        public EnchantmentBonusAffinity read(ResourceLocation affinityId, JsonObject json) {
            String target = json.getAsJsonPrimitive("target").getAsString();
            if (target == null) {
                throw new JsonSyntaxException("Illegal affinity target in affinity JSON for " + affinityId.toString());
            }
            
            ResourceLocation targetId = new ResourceLocation(target);
            if (!ForgeRegistries.ENCHANTMENTS.containsKey(targetId)) {
                throw new JsonSyntaxException("Unknown target enchantment type " + target + " in affinity JSON for " + affinityId.toString());
            }
            
            EnchantmentBonusAffinity entry = new EnchantmentBonusAffinity(targetId);
            if (json.has("multiplier"))  {
                entry.multiplierValues = JsonUtils.toSourceList(json.get("multiplier").getAsJsonObject());
            } else {
                throw new JsonSyntaxException("Affinity entry must have multiplier attribute");
            }
            
            return entry;
        }
    }
}
