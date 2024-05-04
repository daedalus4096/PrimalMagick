package com.verdantartifice.primalmagick.common.research.requirements;

import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.mojang.serialization.Codec;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

/**
 * Represents a predicate determining whether a player meets a given requirement for
 * progressing in the research system.
 */
public abstract class AbstractRequirement<T extends AbstractRequirement<T>> {
    public static final Codec<AbstractRequirement<?>> CODEC = RequirementsPM.TYPES.get().getCodec().dispatch("requirement_type", AbstractRequirement::getType, RequirementType::codec);
    
    public abstract boolean isMetBy(@Nullable Player player);
    public abstract void consumeComponents(@Nullable Player player);
    
    public abstract RequirementCategory getCategory();
    public abstract Stream<AbstractRequirement<?>> streamByCategory(RequirementCategory category);
    
    protected abstract RequirementType<T> getType();
    
    public static AbstractRequirement<?> fromNetwork(FriendlyByteBuf buf) {
        ResourceLocation typeId = buf.readResourceLocation();
        return RequirementsPM.TYPES.get().getValue(typeId).networkReader().apply(buf);
    }
    
    public void toNetwork(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.getType().id());
        this.toNetworkInner(buf);
    }
    
    protected abstract void toNetworkInner(FriendlyByteBuf buf);
}
