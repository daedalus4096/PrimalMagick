package com.verdantartifice.primalmagick.common.books.grids;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import org.joml.Vector2i;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.grids.rewards.ComprehensionReward;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.util.CodecUtils;
import com.verdantartifice.primalmagick.common.util.StreamCodecUtils;

import net.minecraft.core.HolderLookup;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

/**
 * Class encapsulating a data-defined definition for a linguistics grid.  These definitions determine
 * the layout and contents of the linguistics comprehension grids for each ancient language.  Node
 * coordinates increase right and down, with (0,0) being in the top-left corner to match screen coords.
 * 
 * @author Daedalus4096
 */
public class GridDefinition {
    public static final int MIN_POS = 0;
    public static final int MAX_POS = 7;
    
    public static Codec<GridDefinition> codec() {
        return RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("key").forGetter(GridDefinition::getKey),
                ResourceKey.codec(RegistryKeysPM.BOOK_LANGUAGES).fieldOf("language").forGetter(GridDefinition::getLanguage),
                CodecUtils.VECTOR2I.fieldOf("startPos").forGetter(GridDefinition::getStartPos),
                PlacedNode.codec().listOf().<Map<Vector2i, GridNodeDefinition>>xmap(nodeList -> {
                    ImmutableMap.Builder<Vector2i, GridNodeDefinition> builder = ImmutableMap.builder();
                    nodeList.forEach(pn -> builder.put(pn.pos(), pn.definition()));
                    return builder.build();
                }, nodeMap -> {
                    ImmutableList.Builder<PlacedNode> builder = ImmutableList.builder();
                    nodeMap.entrySet().forEach(entry -> builder.add(new PlacedNode(entry.getKey(), entry.getValue())));
                    return builder.build();
                }).fieldOf("nodes").forGetter(GridDefinition::getNodes)
            ).apply(instance, GridDefinition::new));
    }
    
    public static StreamCodec<FriendlyByteBuf, GridDefinition> streamCodec() {
        return StreamCodec.composite(
                ResourceLocation.STREAM_CODEC, GridDefinition::getKey,
                ResourceKey.streamCodec(RegistryKeysPM.BOOK_LANGUAGES), GridDefinition::getLanguage,
                StreamCodecUtils.VECTOR2I, GridDefinition::getStartPos,
                PlacedNode.streamCodec().apply(ByteBufCodecs.list()).<Map<Vector2i, GridNodeDefinition>>map(nodeList -> {
                    ImmutableMap.Builder<Vector2i, GridNodeDefinition> builder = ImmutableMap.builder();
                    nodeList.forEach(pn -> builder.put(pn.pos(), pn.definition()));
                    return builder.build();
                }, nodeMap -> {
                    ImmutableList.Builder<PlacedNode> builder = ImmutableList.builder();
                    nodeMap.entrySet().forEach(entry -> builder.add(new PlacedNode(entry.getKey(), entry.getValue())));
                    return builder.build();
                }), GridDefinition::getNodes,
                GridDefinition::new);
    }
    
    protected ResourceLocation key;
    protected ResourceKey<BookLanguage> language;
    protected Vector2i startPos;
    protected final Map<Vector2i, GridNodeDefinition> nodes = new HashMap<>();
    
    protected GridDefinition(ResourceLocation key, ResourceKey<BookLanguage> language, Vector2i startPos, Map<Vector2i, GridNodeDefinition> nodes) {
        this.key = key;
        this.language = language;
        this.startPos = startPos;
        this.nodes.putAll(nodes);
    }
    
    public ResourceLocation getKey() {
        return this.key;
    }
    
    public ResourceKey<BookLanguage> getLanguage() {
        return this.language;
    }
    
    public Vector2i getStartPos() {
        return this.startPos;
    }
    
    public Map<Vector2i, GridNodeDefinition> getNodes() {
        return Collections.unmodifiableMap(this.nodes);
    }
    
    public boolean isValidPos(int x, int y) {
        return this.isValidPos(new Vector2i(x, y));
    }
    
    public boolean isValidPos(Vector2i pos) {
        return this.nodes.containsKey(pos);
    }
    
    public Optional<GridNodeDefinition> getNode(int x, int y) {
        return this.getNode(new Vector2i(x, y));
    }
    
    public Optional<GridNodeDefinition> getNode(Vector2i pos) {
        return this.isValidPos(pos) ? Optional.ofNullable(this.nodes.get(pos)) : Optional.empty();
    }
    
    protected static record PlacedNode(Vector2i pos, GridNodeDefinition definition) {
        public static Codec<PlacedNode> codec() {
            return RecordCodecBuilder.create(instance -> instance.group(
                    CodecUtils.VECTOR2I.fieldOf("pos").forGetter(PlacedNode::pos),
                    GridNodeDefinition.codec().fieldOf("definition").forGetter(PlacedNode::definition)
                ).apply(instance, PlacedNode::new));
        }
        
        public static StreamCodec<FriendlyByteBuf, PlacedNode> streamCodec() {
            return StreamCodec.composite(
                    StreamCodecUtils.VECTOR2I, PlacedNode::pos,
                    GridNodeDefinition.streamCodec(), PlacedNode::definition,
                    PlacedNode::new);
        }
    }
    
    public static class Builder {
        protected final ResourceLocation key;
        protected final HolderLookup.Provider registries;
        protected final Map<Vector2i, GridNodeDefinition> nodes = new HashMap<>();
        protected ResourceKey<BookLanguage> language;
        protected Vector2i startPos;
        
        protected Builder(ResourceLocation key, HolderLookup.Provider registries) {
            this.key = Preconditions.checkNotNull(key);
            this.registries = registries;
        }
        
        public static Builder grid(String name, HolderLookup.Provider registries) {
            return new Builder(ResourceUtils.loc(name), registries);
        }
        
        public static Builder grid(ResourceLocation key, HolderLookup.Provider registries) {
            return new Builder(key, registries);
        }
        
        public Builder language(ResourceKey<BookLanguage> language) {
            this.language = Preconditions.checkNotNull(language);
            return this;
        }
        
        public Builder startPos(int x, int y) {
            this.startPos = new Vector2i(x, y);
            return this;
        }
        
        public Builder node(int x, int y, GridNodeDefinition def) {
            this.nodes.put(new Vector2i(x, y), Preconditions.checkNotNull(def));
            return this;
        }
        
        private void validate() {
            if (this.language == null) {
                throw new IllegalStateException("No language for linguistics grid " + key.toString());
            }
            if (this.startPos == null) {
                throw new IllegalStateException("No start position for linguistics grid " + key.toString());
            }
            if (this.startPos.x() < GridDefinition.MIN_POS || this.startPos.x() > GridDefinition.MAX_POS) {
                throw new IllegalStateException("Out of bounds start position X-coordinate for linguistics grid " + key.toString() + "; must be between " + GridDefinition.MIN_POS + " and " + GridDefinition.MAX_POS);
            }
            if (this.startPos.y() < GridDefinition.MIN_POS || this.startPos.y() > GridDefinition.MAX_POS) {
                throw new IllegalStateException("Out of bounds start position Y-coordinate for linguistics grid " + key.toString() + "; must be between " + GridDefinition.MIN_POS + " and " + GridDefinition.MAX_POS);
            }
            if (!this.nodes.keySet().contains(this.startPos)) {
                throw new IllegalStateException("Start position not among defined nodes for linguistics grid " + key.toString());
            }
            
            // Validate that the sum of all the nodes' defined comprehension values equals the expected comprehension of the language
            int total = 0;
            for (GridNodeDefinition def : this.nodes.values()) {
                if (def.getReward() instanceof ComprehensionReward compReward) {
                    total += compReward.getPoints();
                }
            }
            int expected = this.registries.lookupOrThrow(RegistryKeysPM.BOOK_LANGUAGES).getOrThrow(this.language).get().complexity();
            if (total != expected) {
                throw new IllegalStateException("Comprehension mismatch for linguistics grid " + key.toString() + "; expected " + expected + ", got " + total);
            }
        }
        
        public GridDefinition build() {
            this.validate();
            return new GridDefinition(this.key, this.language, this.startPos, this.nodes);
        }
    }
}
