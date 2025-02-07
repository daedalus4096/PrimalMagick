package com.verdantartifice.primalmagick.common.concoctions;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import javax.annotation.Nullable;
import java.util.function.IntFunction;

/**
 * Definition of a type of alchemical bomb fuse.  Determines how long it takes for the bomb to go off.
 * 
 * @author Daedalus4096
 */
public enum FuseType implements StringRepresentable {
    IMPACT(0, -1, "impact"),
    SHORT(1, 20, "short"),
    MEDIUM(2, 60, "medium"),
    LONG(3, 100, "long");
    
    private static final IntFunction<FuseType> BY_ID = ByIdMap.continuous(FuseType::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final Codec<FuseType> CODEC = StringRepresentable.fromValues(FuseType::values);
    public static final StreamCodec<ByteBuf, FuseType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, FuseType::getId);

    private final int id;
    private final int fuseLength;
    private final String tag;
    
    private FuseType(int id, int fuseLength, String tag) {
        this.id = id;
        this.fuseLength = fuseLength;
        this.tag = tag;
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getFuseLength() {
        return this.fuseLength;
    }

    @Override
    public String getSerializedName() {
        return this.tag;
    }

    public boolean hasTimer() {
        return this.fuseLength > 0;
    }
    
    @Nullable
    public FuseType getNext() {
        return switch (this) {
            case IMPACT -> SHORT;
            case SHORT -> MEDIUM;
            case MEDIUM -> LONG;
            case LONG -> IMPACT;
        };
    }
    
    public String getTranslationKey() {
        return "concoctions.primalmagick.fuse." + this.tag;
    }
    
    @Nullable
    public static FuseType fromName(@Nullable String name) {
        for (FuseType type : FuseType.values()) {
            if (type.getSerializedName().equals(name)) {
                return type;
            }
        }
        return null;
    }
}
