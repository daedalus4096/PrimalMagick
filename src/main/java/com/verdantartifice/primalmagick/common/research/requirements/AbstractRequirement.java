package com.verdantartifice.primalmagick.common.research.requirements;

import javax.annotation.Nullable;

import com.mojang.serialization.Codec;

import net.minecraft.world.entity.player.Player;

/**
 * Represents a predicate determining whether a player meets a given requirement for
 * progressing in the research system.
 */
public abstract class AbstractRequirement {
    public static final Codec<AbstractRequirement> CODEC = RequirementsPM.TYPES.get().getCodec().dispatch("requirement_type", AbstractRequirement::getType, RequirementType::codec);
    
    public abstract boolean isMetBy(@Nullable Player player);
    
    protected abstract RequirementType<?> getType();
}
