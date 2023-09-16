package com.verdantartifice.primalmagick.common.affinities;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.util.JsonUtils;

import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
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
    protected CompletableFuture<SourceList> calculateTotalAsync(@Nullable RecipeManager recipeManager, @Nonnull RegistryAccess registryAccess, @Nonnull List<ResourceLocation> history) {
        if (this.bonusValues != null) {
            return CompletableFuture.completedFuture(this.bonusValues);
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
            if (!ForgeRegistries.POTIONS.containsKey(targetId)) {
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

        @Override
        public PotionBonusAffinity fromNetwork(FriendlyByteBuf buf) {
            PotionBonusAffinity affinity = new PotionBonusAffinity(buf.readResourceLocation());
            affinity.bonusValues = SourceList.fromNetwork(buf);
            return affinity;
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, PotionBonusAffinity affinity) {
            buf.writeResourceLocation(affinity.targetId);
            SourceList.toNetwork(buf, affinity.bonusValues);
        }
    }
}
