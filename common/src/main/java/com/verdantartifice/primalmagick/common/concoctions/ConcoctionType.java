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
 * Definition of a type of alchemical concoction.  Determines the maximum dosage of the vial.
 * 
 * @author Daedalus4096
 */
public enum ConcoctionType implements StringRepresentable {
    WATER(0, 1, "water"),
    TINCTURE(1, 3, "tincture"),
    PHILTER(2, 6, "philter"),
    ELIXIR(3, 9, "elixir"),
    BOMB(4, 6, "bomb");
    
    private static final IntFunction<ConcoctionType> BY_ID = ByIdMap.continuous(ConcoctionType::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final Codec<ConcoctionType> CODEC = StringRepresentable.fromValues(ConcoctionType::values);
    public static final StreamCodec<ByteBuf, ConcoctionType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, ConcoctionType::getId);

    private final int id;
    private final int maxDoses;
    private final String tag;
    
    private ConcoctionType(int id, int maxDoses, String tag) {
        this.id = id;
        this.maxDoses = maxDoses;
        this.tag = tag;
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getMaxDoses() {
        return this.maxDoses;
    }

    @Override
    public String getSerializedName() {
        return this.tag;
    }
    
    public boolean hasDrinkablePotion() {
        return this == TINCTURE || this == PHILTER || this == ELIXIR;
    }
    
    @Nullable
    public static ConcoctionType fromName(@Nullable String name) {
        for (ConcoctionType type : ConcoctionType.values()) {
            if (type.getSerializedName().equals(name)) {
                return type;
            }
        }
        return null;
    }
}
