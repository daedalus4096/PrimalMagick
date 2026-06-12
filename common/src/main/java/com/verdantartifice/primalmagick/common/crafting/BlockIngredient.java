package com.verdantartifice.primalmagick.common.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Like an Ingredient, but for testing blocks.  Used by rituals to determine if a given block
 * satisfies the requirement for a specified prop.
 * 
 * @author Daedalus4096
 * @see net.minecraft.world.item.crafting.Ingredient
 */
public class BlockIngredient implements Predicate<BlockState>, StackedContents.IngredientInfo<Holder<Block>> {
    public static final BlockIngredient EMPTY = BlockIngredient.of(Stream.empty());

    public static final Codec<HolderSet<Block>> NON_AIR_HOLDER_SET_CODEC = HolderSetCodec.create(
            Registries.BLOCK,
            BuiltInRegistries.BLOCK.holderByNameCodec().validate(block -> block.is(Blocks.AIR.builtInRegistryHolder()) ? DataResult.error(() -> "Block must not be minecraft:air") : DataResult.success(block)),
            false);
    public static final Codec<BlockIngredient> CODEC = ExtraCodecs.nonEmptyHolderSet(NON_AIR_HOLDER_SET_CODEC).xmap(BlockIngredient::new, i -> i.values);
    public static final StreamCodec<RegistryFriendlyByteBuf, BlockIngredient> CONTENTS_STREAM_CODEC =
            ByteBufCodecs.holderSet(Registries.BLOCK).map(BlockIngredient::new, i -> i.values);
    public static final StreamCodec<RegistryFriendlyByteBuf, Optional<BlockIngredient>> OPTIONAL_CONTENTS_STREAM_CODEC =
            ByteBufCodecs.holderSet(Registries.BLOCK).map(
                    holderSet -> holderSet.size() == 0 ? Optional.empty() : Optional.of(new BlockIngredient(holderSet)),
                    ingredientOpt -> ingredientOpt.map(i -> i.values).orElse(HolderSet.empty()));

    private final HolderSet<Block> values;

    private BlockIngredient(HolderSet<Block> values) {
        values.unwrap().ifRight(directValues -> {
            if (directValues.isEmpty()) {
                throw new UnsupportedOperationException("Block ingredients can't be empty");
            } else if (directValues.contains(Blocks.AIR.builtInRegistryHolder())) {
                throw new UnsupportedOperationException("Block ingredient can't contain air");
            }
        });
        this.values = values;
    }

    public static boolean testOptionalIngredient(Optional<BlockIngredient> ingredient, BlockState blockState) {
        Objects.requireNonNull(blockState);
        return ingredient.map(val -> val.test(blockState)).orElse(false);
    }

    public Stream<Holder<Block>> blocks() {
        return this.values.stream();
    }

    public boolean isEmpty() {
        return this.values.size() == 0;
    }

    public boolean test(BlockState blockState) {
        return this.values.contains(blockState.typeHolder());
    }

    @Override
    public boolean acceptsItem(@NotNull Holder<Block> blockHolder) {
        return this.values.contains(blockHolder);
    }

    public boolean equals(Object other) {
        return other instanceof BlockIngredient otherIngredient && Objects.equals(this.values, otherIngredient.values);
    }

    public static BlockIngredient of(Block block) {
        return new BlockIngredient(HolderSet.direct(block.builtInRegistryHolder()));
    }

    public static BlockIngredient of(Block... blocks) {
        return of(Arrays.stream(blocks));
    }

    public static BlockIngredient of(Stream<? extends Block> stream) {
        return new BlockIngredient(HolderSet.direct(stream.map(Block::builtInRegistryHolder).toList()));
    }

    public static BlockIngredient of(HolderSet<Block> tag) {
        return new BlockIngredient(tag);
    }

    public SlotDisplay display() {
        return new SlotDisplay.Composite(this.values.stream().map(BlockIngredient::displayForSingleItem).toList());
    }

    public static SlotDisplay optionalIngredientToDisplay(Optional<BlockIngredient> ingredient) {
        return ingredient.map(BlockIngredient::display).orElse(SlotDisplay.Empty.INSTANCE);
    }

    private static SlotDisplay displayForSingleItem(Holder<Block> block) {
        return new SlotDisplay.ItemSlotDisplay(block.value().asItem().builtInRegistryHolder());
    }
}
