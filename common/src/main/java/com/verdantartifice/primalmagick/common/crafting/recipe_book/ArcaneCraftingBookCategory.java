package com.verdantartifice.primalmagick.common.crafting.recipe_book;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

public enum ArcaneCraftingBookCategory implements StringRepresentable {
    BUILDING("building", 0),
    REDSTONE("redstone", 1),
    EQUIPMENT("equipment", 2),
    MISC("misc", 3),
    ARCANE("arcane", 4);

    public static final Codec<ArcaneCraftingBookCategory> CODEC = StringRepresentable.fromEnum(ArcaneCraftingBookCategory::values);
    public static final IntFunction<ArcaneCraftingBookCategory> BY_ID = ByIdMap.continuous(ArcaneCraftingBookCategory::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StreamCodec<ByteBuf, ArcaneCraftingBookCategory> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, ArcaneCraftingBookCategory::id);

    private final String name;
    private final int id;

    ArcaneCraftingBookCategory(String name, int id) {
        this.name = name;
        this.id = id;
    }

    @NotNull
    public String getSerializedName() {
        return this.name;
    }

    private int id() {
        return this.id;
    }
}
