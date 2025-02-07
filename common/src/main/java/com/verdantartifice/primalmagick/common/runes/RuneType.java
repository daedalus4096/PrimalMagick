package com.verdantartifice.primalmagick.common.runes;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.IntFunction;

/**
 * Enum describing the type of a rune.
 * 
 * @author Daedalus4096
 */
public enum RuneType implements StringRepresentable {
    VERB(0, "verb"),
    NOUN(1, "noun"),
    SOURCE(2, "source"),
    POWER(3, "power");
    
    private static final IntFunction<RuneType> BY_ID = ByIdMap.continuous(RuneType::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final Codec<RuneType> CODEC = StringRepresentable.fromEnum(RuneType::values);
    public static final StreamCodec<ByteBuf, RuneType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, RuneType::getId);

    private final int id;
    private final String name;
    
    private RuneType(int id, String str) {
        this.id = id;
        this.name = str;
    }
    
    public int getId() {
        return this.id;
    }

    @Override
    @Nonnull
    public String getSerializedName() {
        return this.name;
    }
    
    @Nullable
    public static RuneType fromName(@Nullable String name) {
        for (RuneType rune : values()) {
            if (rune.getSerializedName().equals(name)) {
                return rune;
            }
        }
        return null;
    }
}
