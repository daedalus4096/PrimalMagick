package com.verdantartifice.primalmagick.common.research;

import java.util.function.IntFunction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

public enum KnowledgeType implements StringRepresentable {
    OBSERVATION(0, "observation", 16, ResourceUtils.loc("textures/research/knowledge_observation.png")),
    THEORY(1, "theory", 32, ResourceUtils.loc("textures/research/knowledge_theory.png"));
    
    private static final IntFunction<KnowledgeType> BY_ID = ByIdMap.continuous(KnowledgeType::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final Codec<KnowledgeType> CODEC = StringRepresentable.fromValues(KnowledgeType::values);
    public static final StreamCodec<ByteBuf, KnowledgeType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, KnowledgeType::getId);

    private final int id;
    private final String name;
    private final short progression;  // How many points make a complete level for this knowledge type
    private final ResourceLocation iconLocation;
    
    private KnowledgeType(int id, String name, int progression, @Nonnull ResourceLocation iconLocation) {
        this.id = id;
        this.name = name;
        this.progression = (short)progression;
        this.iconLocation = iconLocation;
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getProgression() {
        return this.progression;
    }
    
    @Nonnull
    public ResourceLocation getIconLocation() {
        return this.iconLocation;
    }
    
    @Nonnull
    public String getNameTranslationKey() {
        return String.join(".", "knowledge_type", Constants.MOD_ID, this.getSerializedName());
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
    
    @Nullable
    public static KnowledgeType fromName(@Nullable String name) {
        for (KnowledgeType knowledgeType : values()) {
            if (knowledgeType.getSerializedName().equals(name)) {
                return knowledgeType;
            }
        }
        return null;
    }
}