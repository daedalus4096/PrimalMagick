package com.verdantartifice.primalmagick.common.affinities;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.util.JsonUtils;

import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemAffinity extends AbstractAffinity {
    public static final Serializer SERIALIZER = new Serializer();

    protected ResourceLocation baseEntryId;
    protected IAffinity baseEntry;
    protected SourceList setValues;
    protected SourceList addValues;
    protected SourceList removeValues;
    protected Optional<ResourceLocation> sourceRecipe = Optional.empty();
    
    protected ItemAffinity(@Nonnull ResourceLocation target) {
        super(target);
    }
    
    public ItemAffinity(@Nonnull ResourceLocation target, @Nonnull SourceList values) {
        super(target);
        this.setValues = values;
    }

    @Override
    public AffinityType getType() {
        return AffinityType.ITEM;
    }

    @Override
    public IAffinitySerializer<?> getSerializer() {
        return SERIALIZER;
    }
    
    public Optional<ResourceLocation> getSourceRecipe() {
        return this.sourceRecipe;
    }
    
    public void setSourceRecipe(Optional<ResourceLocation> sourceRecipe) {
        this.sourceRecipe = sourceRecipe;
    }

    @Override
    protected CompletableFuture<SourceList> calculateTotalAsync(@Nonnull RecipeManager recipeManager, @Nonnull RegistryAccess registryAccess, @Nonnull List<ResourceLocation> history) {
        if (this.setValues != null) {
            return CompletableFuture.completedFuture(this.setValues);
        } else if (this.baseEntryId != null) {
            return AffinityManager.getInstance().getOrGenerateItemAffinityAsync(this.baseEntryId, recipeManager, registryAccess, history).thenCompose(baseEntry -> {
                return baseEntry == null ? CompletableFuture.completedFuture(SourceList.EMPTY) : baseEntry.getTotalAsync(recipeManager, registryAccess, history);
            }).thenApply(baseSources -> {
                if (this.addValues != null) {
                    baseSources = baseSources.add(this.addValues);
                }
                if (this.removeValues != null) {
                    baseSources = baseSources.remove(this.removeValues);
                }
                return baseSources;
            });
        } else {
            throw new IllegalStateException("Item affinity has neither set values nor a base entry");
        }
    }
    
    public static class Serializer implements IAffinitySerializer<ItemAffinity> {
        @Override
        public ItemAffinity read(ResourceLocation affinityId, JsonObject json) {
            String target = json.getAsJsonPrimitive("target").getAsString();
            if (target == null) {
                throw new JsonSyntaxException("Illegal affinity target in affinity JSON for " + affinityId.toString());
            }
            
            ResourceLocation targetId = new ResourceLocation(target);
            if (!ForgeRegistries.ITEMS.containsKey(targetId)) {
                throw new JsonSyntaxException("Unknown target item " + target + " in affinity JSON for " + affinityId.toString());
            }
            
            ItemAffinity entry = new ItemAffinity(targetId);
            if (json.has("set") && json.has("base")) {
                throw new JsonParseException("Affinity entry may not have both set and base attributes");
            } else if (json.has("set")) {
                entry.setValues = JsonUtils.toSourceList(json.get("set").getAsJsonObject());
            } else if (json.has("base")) {
                entry.baseEntryId = new ResourceLocation(json.getAsJsonPrimitive("base").getAsString());
                if (!ForgeRegistries.ITEMS.containsKey(entry.baseEntryId)) {
                    throw new JsonSyntaxException("Unknown base item " + target + " in affinity JSON for " + affinityId.toString());
                }
                if (json.has("add")) {
                    entry.addValues = JsonUtils.toSourceList(json.get("add").getAsJsonObject());
                }
                if (json.has("remove")) {
                    entry.removeValues = JsonUtils.toSourceList(json.get("remove").getAsJsonObject());
                }
            } else {
                throw new JsonParseException("Affinity entry must have either set or base attributes");
            }

            return entry;
        }

        @Override
        public ItemAffinity fromNetwork(FriendlyByteBuf buf) {
            ItemAffinity affinity = new ItemAffinity(buf.readResourceLocation());
            boolean isSet = buf.readBoolean();
            if (isSet) {
                affinity.setValues = SourceList.fromNetwork(buf);
            } else {
                affinity.baseEntryId = buf.readResourceLocation();
                if (buf.readBoolean()) {
                    affinity.addValues = SourceList.fromNetwork(buf);
                }
                if (buf.readBoolean()) {
                    affinity.removeValues = SourceList.fromNetwork(buf);
                }
            }
            return affinity;
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ItemAffinity affinity) {
            buf.writeResourceLocation(affinity.targetId);
            if (affinity.setValues != null) {
                buf.writeBoolean(true);
                SourceList.toNetwork(buf, affinity.setValues);
            } else {
                buf.writeBoolean(false);
                buf.writeResourceLocation(affinity.baseEntryId);
                if (affinity.addValues != null) {
                    buf.writeBoolean(true);
                    SourceList.toNetwork(buf, affinity.addValues);
                } else {
                    buf.writeBoolean(false);
                }
                if (affinity.removeValues != null) {
                    buf.writeBoolean(true);
                    SourceList.toNetwork(buf, affinity.removeValues);
                } else {
                    buf.writeBoolean(false);
                }
            }
        }
    }
}
