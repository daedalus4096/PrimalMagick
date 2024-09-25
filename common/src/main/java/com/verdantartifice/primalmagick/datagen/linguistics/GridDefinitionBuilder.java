package com.verdantartifice.primalmagick.datagen.linguistics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2i;
import org.joml.Vector2ic;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.grids.GridDefinition;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class GridDefinitionBuilder {
    protected static final Logger LOGGER = LogManager.getLogger();
    
    protected final ResourceLocation key;
    protected ResourceKey<BookLanguage> bookLanguage;
    protected Vector2ic startPos;
    protected final List<IFinishedGridNode> nodes = new ArrayList<>();
    protected final HolderLookup.Provider lookupProvider;
    
    protected GridDefinitionBuilder(@Nonnull ResourceLocation key, HolderLookup.Provider lookupProvider) {
        this.key = key;
        this.lookupProvider = lookupProvider;
    }

    public static GridDefinitionBuilder grid(@Nonnull ResourceLocation key, HolderLookup.Provider lookupProvider) {
        return new GridDefinitionBuilder(key, lookupProvider);
    }
    
    public static GridDefinitionBuilder grid(@Nonnull String keyNamespace, @Nonnull String keyPath, HolderLookup.Provider lookupProvider) {
        return grid(ResourceLocation.fromNamespaceAndPath(keyNamespace, keyPath), lookupProvider);
    }
    
    public static GridDefinitionBuilder grid(@Nonnull String keyPath, HolderLookup.Provider lookupProvider) {
        return grid(ResourceUtils.loc(keyPath), lookupProvider);
    }
    
    public GridDefinitionBuilder language(@Nullable ResourceKey<BookLanguage> lang) {
        this.bookLanguage = lang;
        return this;
    }
    
    public GridDefinitionBuilder startPos(Vector2ic pos) {
        this.startPos = pos;
        return this;
    }
    
    public GridDefinitionBuilder startPos(int x, int y) {
        return this.startPos(new Vector2i(x, y));
    }
    
    public GridDefinitionBuilder node(IFinishedGridNode node) {
        this.nodes.add(node);
        return this;
    }
    
    private void validate(ResourceLocation id) {
        if (this.key == null) {
            throw new IllegalStateException("No key for linguistics grid " + id.toString());
        }
        if (this.bookLanguage == null) {
            throw new IllegalStateException("No language for linguistics grid " + id.toString());
        }
        if (this.startPos == null) {
            throw new IllegalStateException("No start position for linguistics grid " + id.toString());
        }
        if (this.startPos.x() < GridDefinition.MIN_POS || this.startPos.x() > GridDefinition.MAX_POS) {
            throw new IllegalStateException("Out of bounds start position X-coordinate for linguistics grid " + id.toString() + "; must be between " + GridDefinition.MIN_POS + " and " + GridDefinition.MAX_POS);
        }
        if (this.startPos.y() < GridDefinition.MIN_POS || this.startPos.y() > GridDefinition.MAX_POS) {
            throw new IllegalStateException("Out of bounds start position Y-coordinate for linguistics grid " + id.toString() + "; must be between " + GridDefinition.MIN_POS + " and " + GridDefinition.MAX_POS);
        }
        
        Set<Vector2ic> nodePositions = new HashSet<>();
        this.nodes.forEach(node -> {
            Vector2ic pos = node.getPosition();
            if (!nodePositions.add(pos)) {
                throw new IllegalStateException("Duplicate node position (" + pos.x() + "," + pos.y() + ") for linguistics grid " + id.toString());
            }
        });
        if (!nodePositions.contains(this.startPos)) {
            throw new IllegalStateException("Start position not among defined nodes for linguistics grid " + id.toString());
        }
        
        // Validate that the sum of all the nodes' defined comprehension values equals the expected comprehension of the language
        Holder.Reference<BookLanguage> langHolder = this.lookupProvider.lookupOrThrow(RegistryKeysPM.BOOK_LANGUAGES).getOrThrow(this.bookLanguage);
        int total = this.nodes.stream().map(IFinishedGridNode::getReward).map(r -> r.getComprehensionPoints(this.bookLanguage.location())).mapToInt(o -> o.orElse(0)).sum();
        int expected = langHolder.get().complexity();
        if (total != expected) {
            throw new IllegalStateException("Comprehension mismatch for linguistics grid " + id.toString() + "; expected " + expected + ", got " + total);
        }
    }
    
    public void build(Consumer<IFinishedGrid> consumer) {
        this.build(consumer, this.key);
    }
    
    public void build(Consumer<IFinishedGrid> consumer, String name) {
        this.build(consumer, ResourceLocation.parse(name));
    }
    
    public void build(Consumer<IFinishedGrid> consumer, ResourceLocation id) {
        this.validate(id);
        consumer.accept(new Result(this.key, this.bookLanguage.location(), this.startPos, this.nodes));
    }
    
    public static class Result implements IFinishedGrid {
        protected final ResourceLocation key;
        protected final ResourceLocation bookLanguage;
        protected final Vector2ic startPos;
        protected final List<IFinishedGridNode> nodes;
        
        public Result(ResourceLocation key, ResourceLocation bookLanguage, Vector2ic startPos, List<IFinishedGridNode> nodes) {
            this.key = key;
            this.bookLanguage = bookLanguage;
            this.startPos = startPos;
            this.nodes = nodes;
        }

        @Override
        public ResourceLocation getId() {
            return this.key;
        }

        @Override
        public void serialize(JsonObject json) {
            json.addProperty("key", this.key.toString());
            json.addProperty("language", this.bookLanguage.toString());
            json.addProperty("start_x", this.startPos.x());
            json.addProperty("start_y", this.startPos.y());
            
            JsonArray nodesArray = new JsonArray();
            this.nodes.forEach(node -> nodesArray.add(node.getNodeJson()));
            json.add("nodes", nodesArray);
        }
    }
}
