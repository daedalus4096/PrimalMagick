package com.verdantartifice.primalmagick.common.research.keys;

import javax.annotation.Nullable;

import org.apache.commons.lang3.mutable.MutableBoolean;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.research.requirements.RequirementCategory;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

/**
 * Base class representing an atom in the research hierarchy.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractResearchKey<T extends AbstractResearchKey<T>> {
    public static final Codec<AbstractResearchKey<?>> CODEC = ResearchKeyTypesPM.TYPES.get().getCodec().dispatch("key_type", AbstractResearchKey::getType, ResearchKeyType::codec);
    
    @Override
    public abstract String toString();
    
    /**
     * Returns the category of requirement to be used when this key is part of a {@link com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement}.
     * 
     * @return this key's corresponding requirement category
     */
    public abstract RequirementCategory getRequirementCategory();

    protected abstract ResearchKeyType<T> getType();
    
    public boolean isKnownBy(@Nullable Player player) {
        if (player == null) {
            return false;
        } else {
            MutableBoolean retVal = new MutableBoolean(false);
            PrimalMagickCapabilities.getKnowledge(player).ifPresent(knowledge -> {
                retVal.setValue(knowledge.isResearchComplete(this));
            });
            return retVal.booleanValue();
        }
    }
    
    public static AbstractResearchKey<?> fromNetwork(FriendlyByteBuf buf) {
        ResourceLocation typeId = buf.readResourceLocation();
        return ResearchKeyTypesPM.TYPES.get().getValue(typeId).networkReader().apply(buf);
    }
    
    public void toNetwork(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.getType().id());
        this.toNetworkInner(buf);
    }
    
    public abstract void toNetworkInner(FriendlyByteBuf buf);
}
