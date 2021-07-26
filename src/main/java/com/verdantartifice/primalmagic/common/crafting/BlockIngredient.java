package com.verdantartifice.primalmagic.common.crafting;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import net.minecraft.world.level.block.Block;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.Tag;
import net.minecraft.tags.SerializationTags;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Like an Ingredient, but for testing blocks.  Used by rituals to determine if a given block
 * satisfies the requirement for a specified prop.
 * 
 * @author Daedalus4096
 * @see {@link net.minecraft.item.crafting.Ingredient}
 */
public class BlockIngredient implements Predicate<Block> {
    public static final BlockIngredient EMPTY = new BlockIngredient(Stream.empty());
    
    protected final BlockIngredient.IBlockList[] acceptedBlocks;
    protected Block[] matchingBlocks = null;
    
    protected BlockIngredient(Stream<? extends BlockIngredient.IBlockList> blockLists) {
        this.acceptedBlocks = blockLists.toArray(count -> {
            return new BlockIngredient.IBlockList[count];
        });
    }
    
    public Block[] getMatchingBlocks() {
        this.determineMatchingBlocks();
        return this.matchingBlocks;
    }
    
    protected void determineMatchingBlocks() {
        if (this.matchingBlocks == null) {
            this.matchingBlocks = Arrays.stream(this.acceptedBlocks).flatMap(blockList -> {
                return blockList.getBlocks().stream();
            }).distinct().toArray(count -> {
                return new Block[count];
            });
        }
    }

    @Override
    public boolean test(@Nullable Block testBlock) {
        if (testBlock == null) {
            return false;
        } else {
            this.determineMatchingBlocks();
            for (Block block : this.matchingBlocks) {
                if (testBlock == block) {
                    return true;
                }
            }
            return false;
        }
    }
    
    public void write(FriendlyByteBuf buf) {
        this.determineMatchingBlocks();
        buf.writeVarInt(this.matchingBlocks.length);
        for (int index = 0; index < this.matchingBlocks.length; index++) {
            buf.writeResourceLocation(this.matchingBlocks[index].getRegistryName());
        }
    }
    
    public JsonElement serialize() {
        if (this.acceptedBlocks.length == 1) {
            return this.acceptedBlocks[0].serialize();
        } else {
            JsonArray arr = new JsonArray();
            for (BlockIngredient.IBlockList list : this.acceptedBlocks) {
                arr.add(list.serialize());
            }
            return arr;
        }
    }
    
    public boolean hasNoMatchingBlocks() {
        return this.acceptedBlocks.length == 0 && (this.matchingBlocks == null || this.matchingBlocks.length == 0);
    }
    
    protected static BlockIngredient fromBlockListStream(Stream<? extends BlockIngredient.IBlockList> stream) {
        BlockIngredient ing = new BlockIngredient(stream);
        return ing.acceptedBlocks.length == 0 ? EMPTY : ing;
    }
    
    public static BlockIngredient fromBlocks(Block... blocks) {
        return fromBlockListStream(Arrays.stream(blocks).map(b -> {
            return new BlockIngredient.SingleBlockList(b);
        }));
    }
    
    public static BlockIngredient fromTag(Tag<Block> tag) {
        return fromBlockListStream(Stream.of(new BlockIngredient.TagList(tag)));
    }
    
    public static BlockIngredient read(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        return fromBlockListStream(Stream.generate(() -> {
            ResourceLocation loc = buf.readResourceLocation();
            return new BlockIngredient.SingleBlockList(ForgeRegistries.BLOCKS.getValue(loc));
        }).limit((long)size));
    }
    
    protected static BlockIngredient.IBlockList deserializeBlockList(JsonObject json) {
        if (json.has("block") && json.has("tag")) {
            throw new JsonParseException("A block ingredient entry is either a tag or a block, not both");
        } else if (json.has("block")) {
            ResourceLocation loc = new ResourceLocation(GsonHelper.getAsString(json, "block"));
            Block block = ForgeRegistries.BLOCKS.getValue(loc);
            if (block == null) {
                throw new JsonSyntaxException("Unknown block '" + loc.toString() + "'");
            } else {
                return new BlockIngredient.SingleBlockList(block);
            }
        } else if (json.has("tag")) {
            ResourceLocation loc = new ResourceLocation(GsonHelper.getAsString(json, "tag"));
            Tag<Block> tag = SerializationTags.getInstance().getTagOrThrow(Registry.BLOCK_REGISTRY, loc, (resourceLocation) -> {
                throw new JsonSyntaxException("Unknown block tag '" + resourceLocation.toString() + "'");
            });
            return new BlockIngredient.TagList(tag);
        } else {
            throw new JsonParseException("A block ingredient entry needs either a tag or a block");
        }
    }
    
    public static BlockIngredient deserialize(@Nullable JsonElement json) {
        if (json != null && !json.isJsonNull()) {
            if (json.isJsonObject()) {
                return fromBlockListStream(Stream.of(deserializeBlockList(json.getAsJsonObject())));
            } else if (json.isJsonArray()) {
                JsonArray arr = json.getAsJsonArray();
                if (arr.size() == 0) {
                    throw new JsonSyntaxException("Block array cannot be empty, at least one block must be defined");
                } else {
                    return fromBlockListStream(StreamSupport.stream(arr.spliterator(), false).map(element -> {
                        return deserializeBlockList(GsonHelper.convertToJsonObject(element, "block"));
                    }));
                }
            } else {
                throw new JsonSyntaxException("Expected block to be object or array of objects");
            }
        } else {
            throw new JsonSyntaxException("Block cannot be null");
        }
    }

    protected interface IBlockList {
        Collection<Block> getBlocks();
        JsonObject serialize();
    }
    
    protected static class SingleBlockList implements BlockIngredient.IBlockList {
        private final Block block;
        
        public SingleBlockList(Block block) {
            this.block = block;
        }
        
        @Override
        public Collection<Block> getBlocks() {
            return Collections.singleton(this.block);
        }

        @Override
        public JsonObject serialize() {
            JsonObject json = new JsonObject();
            json.addProperty("block", ForgeRegistries.BLOCKS.getKey(this.block).toString());
            return json;
        }
    }
    
    protected static class TagList implements BlockIngredient.IBlockList {
        private final Tag<Block> tag;
        
        public TagList(Tag<Block> tag) {
            this.tag = tag;
        }
        
        @Override
        public Collection<Block> getBlocks() {
            return this.tag.getValues();
        }

        @Override
        public JsonObject serialize() {
            JsonObject json = new JsonObject();
            json.addProperty("tag", SerializationTags.getInstance().getIdOrThrow(Registry.BLOCK_REGISTRY, this.tag, () -> {
                return new IllegalStateException("Unknown block tag");
            }).toString());
            return json;
        }
    }
}
