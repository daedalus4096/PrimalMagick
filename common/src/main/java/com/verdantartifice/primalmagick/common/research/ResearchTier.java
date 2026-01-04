package com.verdantartifice.primalmagick.common.research;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.misc.IconDefinition;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ResearchTier implements StringRepresentable, Comparable<ResearchTier> {
    private static final Map<Identifier, ResearchTier> TIERS = new HashMap<>();

    public static final Codec<ResearchTier> CODEC = Identifier.CODEC.xmap(ResearchTier::get, ResearchTier::getName);
    public static final StreamCodec<ByteBuf, ResearchTier> STREAM_CODEC = Identifier.STREAM_CODEC.map(ResearchTier::get, ResearchTier::getName);

    private final int id;
    private final Identifier name;
    private final int defaultExpertise;
    private final int defaultBonusExpertise;
    private final Optional<IconDefinition> iconDef;
    
    public ResearchTier(int id, Identifier name, int defaultExp, int defaultBonusExp, Optional<IconDefinition> iconDef) {
        this.id = id;
        this.name = name;
        this.defaultExpertise = defaultExp;
        this.defaultBonusExpertise = defaultBonusExp;
        this.iconDef = iconDef;
        TIERS.put(name, this);
    }
    
    public int getId() {
        return this.id;
    }

    public Identifier getName() {
        return this.name;
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
    public @NotNull String getSerializedName() {
        return this.name.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ResearchTier that)) return false;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public int compareTo(@NotNull ResearchTier other) {
        return Integer.compare(this.id, other.id);
    }

    @Nullable
    public static ResearchTier get(Identifier id) {
        return TIERS.get(id);
    }
}
