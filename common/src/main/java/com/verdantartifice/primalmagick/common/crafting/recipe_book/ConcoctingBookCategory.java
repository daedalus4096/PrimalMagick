package com.verdantartifice.primalmagick.common.crafting.recipe_book;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

public enum ConcoctingBookCategory implements StringRepresentable {
    DRINKABLE(0, "drinkable"),
    BOMB(1, "bomb");

    public static final Codec<ConcoctingBookCategory> CODEC = StringRepresentable.fromEnum(ConcoctingBookCategory::values);
    public static final IntFunction<ConcoctingBookCategory> BY_ID = ByIdMap.continuous(ConcoctingBookCategory::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StreamCodec<ByteBuf, ConcoctingBookCategory> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, ConcoctingBookCategory::id);

    private final int id;
    private final String name;

    ConcoctingBookCategory(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    @NotNull
    public String getSerializedName() {
        return this.name;
    }

    private int id() {
        return this.id;
    }
}
