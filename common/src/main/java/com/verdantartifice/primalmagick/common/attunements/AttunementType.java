package com.verdantartifice.primalmagick.common.attunements;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.Constants;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntFunction;

/**
 * Represents a type of magickal attunement.  Permanent attunement is gained through research and
 * cannot be removed.  Induced attunement is gained or lost through rituals, but does not decay
 * over time.  Temporary attunement is gained by crafting items and casting spells, but decays
 * slowly over time.
 * 
 * @author Daedalus4096
 */
public enum AttunementType implements StringRepresentable {
    PERMANENT(0, "permanent", -1),
    INDUCED(1, "induced", 50),
    TEMPORARY(2, "temporary", 50);

    private static final IntFunction<AttunementType> BY_ID = ByIdMap.continuous(AttunementType::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final Codec<AttunementType> CODEC = StringRepresentable.fromValues(AttunementType::values);
    public static final StreamCodec<ByteBuf, AttunementType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, AttunementType::getId);

    private final int id;
    private final String name;
    private final int maximum;    // The maximum attunement amount of this type that the player can have at once
    
    AttunementType(int id, String name, int max) {
        this.id = id;
        this.name = name;
        this.maximum = max;
    }

    public int getId() {
        return this.id;
    }

    public boolean isCapped() {
        return (this.maximum > 0);
    }
    
    public int getMaximum() {
        return this.maximum;
    }
    
    @NotNull
    public String getNameTranslationKey() {
        return String.join(".", "attunement_type", Constants.MOD_ID, this.getSerializedName());
    }

    @Override
    @NotNull
    public String getSerializedName() {
        return this.name;
    }

    @Nullable
    public static AttunementType fromName(@Nullable String name) {
        for (AttunementType type : AttunementType.values()) {
            if (type.getSerializedName().equals(name)) {
                return type;
            }
        }
        return null;
    }
}
