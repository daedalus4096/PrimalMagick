package com.verdantartifice.primalmagick.common.crafting;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.util.CodecUtils;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Like an Ingredient, but for testing blocks.  Used by rituals to determine if a given block
 * satisfies the requirement for a specified prop.
 * 
 * @author Daedalus4096
 * @see {@link net.minecraft.world.item.crafting.Ingredient}
 */
public class BlockIngredient implements Predicate<Block> {
    public static final BlockIngredient EMPTY = new BlockIngredient(Stream.empty());
    
    public static final Codec<BlockIngredient> CODEC = codec(true);
    public static final Codec<BlockIngredient> CODEC_NONEMPTY = codec(false);
    public static final StreamCodec<RegistryFriendlyByteBuf, BlockIngredient> CONTENTS_STREAM_CODEC = StreamCodec.of(BlockIngredient::toNetwork, BlockIngredient::fromNetwork);
    
    protected final BlockIngredient.Value[] acceptedBlocks;
    protected Block[] matchingBlocks = null;
    
    protected BlockIngredient(Stream<? extends BlockIngredient.Value> blockLists) {
        this.acceptedBlocks = blockLists.toArray(count -> {
            return new BlockIngredient.Value[count];
        });
    }
    
    private BlockIngredient(BlockIngredient.Value[] values) {
        this.acceptedBlocks = values;
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
    
    private static void toNetwork(RegistryFriendlyByteBuf buf, BlockIngredient ing) {
        ing.determineMatchingBlocks();
        buf.writeVarInt(ing.matchingBlocks.length);
        for (int index = 0; index < ing.matchingBlocks.length; index++) {
            buf.writeResourceLocation(Services.BLOCKS_REGISTRY.getKey(ing.matchingBlocks[index]));
        }
    }
    
    public boolean isEmpty() {
        return this.acceptedBlocks.length == 0;
     }

    public boolean hasNoMatchingBlocks() {
        return this.acceptedBlocks.length == 0 && (this.matchingBlocks == null || this.matchingBlocks.length == 0);
    }
    
    public Ingredient asIngredient() {
        return Ingredient.of(Arrays.stream(this.acceptedBlocks).flatMap(ibl -> ibl.getBlocks().stream()).toArray(ItemLike[]::new));
    }
    
    protected static BlockIngredient fromBlockListStream(Stream<? extends BlockIngredient.Value> stream) {
        BlockIngredient ing = new BlockIngredient(stream);
        return ing.acceptedBlocks.length == 0 ? EMPTY : ing;
    }
    
    public static BlockIngredient fromBlocks(Block... blocks) {
        return fromBlockListStream(Arrays.stream(blocks).map(b -> {
            return new BlockIngredient.SingleBlockValue(b);
        }));
    }
    
    public static BlockIngredient fromTag(TagKey<Block> tag) {
        return fromBlockListStream(Stream.of(new BlockIngredient.TagValue(tag)));
    }
    
    private static BlockIngredient fromNetwork(RegistryFriendlyByteBuf buf) {
        int size = buf.readVarInt();
        return fromBlockListStream(Stream.generate(() -> {
            ResourceLocation loc = buf.readResourceLocation();
            return new BlockIngredient.SingleBlockValue(Services.BLOCKS_REGISTRY.get(loc));
        }).limit(size));
    }
    
    private static Codec<BlockIngredient> codec(boolean allowEmpty) {
        Codec<BlockIngredient.Value[]> innerCodec = Codec.list(BlockIngredient.Value.CODEC).comapFlatMap(valueList -> {
            return !allowEmpty && valueList.size() < 1 ?
                    DataResult.error(() -> "Block array cannot be empty, at least one block must be defined") :
                    DataResult.success(valueList.toArray(BlockIngredient.Value[]::new));
        }, List::of);
        return Codec.either(innerCodec, BlockIngredient.Value.CODEC).flatComapMap(either -> {
            return either.map(BlockIngredient::new, val -> new BlockIngredient(new BlockIngredient.Value[] {val}));
        }, ing -> {
            if (ing.acceptedBlocks.length == 1) {
                return DataResult.success(Either.right(ing.acceptedBlocks[0]));
            } else {
                return ing.acceptedBlocks.length == 0 && !allowEmpty ?
                        DataResult.error(() -> "Block array cannot be empty, at least one block must be defined") :
                        DataResult.success(Either.left(ing.acceptedBlocks));
            }
        });
    }
    
    protected interface Value {
        Codec<BlockIngredient.Value> CODEC = Codec.xor(BlockIngredient.SingleBlockValue.CODEC, BlockIngredient.TagValue.CODEC).xmap(either -> {
            return either.map(l -> l, r -> r);
        }, val -> {
            if (val instanceof BlockIngredient.SingleBlockValue sbv) {
                return Either.left(sbv);
            } else if (val instanceof BlockIngredient.TagValue tv) {
                return Either.right(tv);
            } else {
                throw new UnsupportedOperationException("This is neither a single block value nor a tag value");
            }
        });
        
        Collection<Block> getBlocks();
    }
    
    protected static record SingleBlockValue(Block block) implements BlockIngredient.Value {
        protected static final Codec<BlockIngredient.SingleBlockValue> CODEC = RecordCodecBuilder.create(instance -> {
            return instance.group(CodecUtils.BLOCK_NONAIR_CODEC.fieldOf("block").forGetter(sbl -> sbl.block)).apply(instance, BlockIngredient.SingleBlockValue::new);
        });
        
        @Override
        public Collection<Block> getBlocks() {
            return Collections.singleton(this.block);
        }
    }
    
    protected static record TagValue(TagKey<Block> tag) implements BlockIngredient.Value {
        protected static final Codec<BlockIngredient.TagValue> CODEC = RecordCodecBuilder.create(instance -> {
            return instance.group(TagKey.codec(Registries.BLOCK).fieldOf("tag").forGetter(tl -> tl.tag)).apply(instance, BlockIngredient.TagValue::new);
        });
        
        @Override
        public Collection<Block> getBlocks() {
            List<Block> retVal = new ArrayList<>();
            Services.BLOCKS_REGISTRY.getTag(this.tag).forEach(retVal::add);
            return retVal;
        }
    }
}
