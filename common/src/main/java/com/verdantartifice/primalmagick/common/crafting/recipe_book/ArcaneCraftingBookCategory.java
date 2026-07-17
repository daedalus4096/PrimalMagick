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
    ARCANE(0, "arcane"),
    RUNECARVING(1, "runecarving"),
    RITUAL(2, "ritual");

    public static final Codec<ArcaneCraftingBookCategory> CODEC = StringRepresentable.fromEnum(ArcaneCraftingBookCategory::values);
    public static final IntFunction<ArcaneCraftingBookCategory> BY_ID = ByIdMap.continuous(ArcaneCraftingBookCategory::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StreamCodec<ByteBuf, ArcaneCraftingBookCategory> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, ArcaneCraftingBookCategory::id);

    private final int id;
    private final String name;

    ArcaneCraftingBookCategory(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @NotNull
    public String getSerializedName() {
        return this.name;
    }

    private int id() {
        return this.id;
    }
}
