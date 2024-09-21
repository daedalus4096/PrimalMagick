package com.verdantartifice.primalmagick.common.research;

import java.util.Optional;
import java.util.function.IntFunction;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.PrimalMagick;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

public enum ResearchTier implements StringRepresentable {
    BASIC(0, "basic", 1, 4, Optional.empty()),
    EXPERT(1, "expert", 5, 20, Optional.of(IconDefinition.of(PrimalMagick.resource("textures/research/expertise_expert.png")))),
    MASTER(2, "master", 25, 100, Optional.of(IconDefinition.of(PrimalMagick.resource("textures/research/expertise_master.png")))),
    SUPREME(3, "supreme", 125, 500, Optional.of(IconDefinition.of(PrimalMagick.resource("textures/research/expertise_supreme.png"))));
    
    private static final IntFunction<ResearchTier> BY_ID = ByIdMap.continuous(ResearchTier::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final Codec<ResearchTier> CODEC = StringRepresentable.fromEnum(ResearchTier::values);
    public static final StreamCodec<ByteBuf, ResearchTier> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, ResearchTier::getId);

    private final int id;
    private final String name;
    private final int defaultExpertise;
    private final int defaultBonusExpertise;
    private final Optional<IconDefinition> iconDef;
    
    private ResearchTier(int id, String name, int defaultExp, int defaultBonusExp, Optional<IconDefinition> iconDef) {
        this.id = id;
        this.name = name;
        this.defaultExpertise = defaultExp;
        this.defaultBonusExpertise = defaultBonusExp;
        this.iconDef = iconDef;
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getDefaultExpertise() {
        return this.defaultExpertise;
    }
    
    public int getDefaultBonusExpertise() {
        return this.defaultBonusExpertise;
    }
    
    public Optional<IconDefinition> getIconDefinition() {
        return this.iconDef;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
