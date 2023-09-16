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

public class EntityTypeAffinity extends AbstractAffinity {
    public static final Serializer SERIALIZER = new Serializer();
    
    protected SourceList values;
    
    protected EntityTypeAffinity(@Nonnull ResourceLocation target) {
        super(target);
    }

    @Override
    public AffinityType getType() {
        return AffinityType.ENTITY_TYPE;
    }

    @Override
    public IAffinitySerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    protected CompletableFuture<SourceList> calculateTotalAsync(@Nullable RecipeManager recipeManager, @Nonnull RegistryAccess registryAccess, @Nonnull List<ResourceLocation> history) {
        if (this.values != null) {
            return CompletableFuture.completedFuture(this.values);
        } else {
            throw new IllegalStateException("Entity type affinity has no values defined");
        }
    }

    public static class Serializer implements IAffinitySerializer<EntityTypeAffinity> {
        @Override
        public EntityTypeAffinity read(ResourceLocation affinityId, JsonObject json) {
            String target = json.getAsJsonPrimitive("target").getAsString();
            if (target == null) {
                throw new JsonSyntaxException("Illegal affinity target in affinity JSON for " + affinityId.toString());
            }
            
            ResourceLocation targetId = new ResourceLocation(target);
            if (!ForgeRegistries.ENTITY_TYPES.containsKey(targetId)) {
                throw new JsonSyntaxException("Unknown target entity type " + target + " in affinity JSON for " + affinityId.toString());
            }
            
            EntityTypeAffinity entry = new EntityTypeAffinity(targetId);
            if (json.has("values"))  {
                entry.values = JsonUtils.toSourceList(json.get("values").getAsJsonObject());
            } else {
                throw new JsonSyntaxException("Affinity entry must have values attribute");
            }
            
            return entry;
        }

        @Override
        public EntityTypeAffinity fromNetwork(FriendlyByteBuf buf) {
            EntityTypeAffinity affinity = new EntityTypeAffinity(buf.readResourceLocation());
            affinity.values = SourceList.fromNetwork(buf);
            return affinity;
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, EntityTypeAffinity affinity) {
            buf.writeResourceLocation(affinity.targetId);
            SourceList.toNetwork(buf, affinity.values);
        }
    }
}
