package com.verdantartifice.primalmagick.common.crafting.recipe_book;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

public enum DissolutionBookCategory implements StringRepresentable {
    ORE(0, "ore"),
    MISC(1, "misc");

    public static final Codec<DissolutionBookCategory> CODEC = StringRepresentable.fromEnum(DissolutionBookCategory::values);
    public static final IntFunction<DissolutionBookCategory> BY_ID = ByIdMap.continuous(DissolutionBookCategory::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StreamCodec<ByteBuf, DissolutionBookCategory> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, DissolutionBookCategory::id);

    private final int id;
    private final String name;

    DissolutionBookCategory(int id, String name) {
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
