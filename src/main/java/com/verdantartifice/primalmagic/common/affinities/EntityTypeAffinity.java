package com.verdantartifice.primalmagic.common.affinities;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.verdantartifice.primalmagic.common.sources.SourceList;
import com.verdantartifice.primalmagic.common.util.JsonUtils;

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
    protected SourceList calculateTotal(RecipeManager recipeManager) {
        if (this.values != null) {
            return this.values;
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
            if (!ForgeRegistries.ENTITIES.containsKey(targetId)) {
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
