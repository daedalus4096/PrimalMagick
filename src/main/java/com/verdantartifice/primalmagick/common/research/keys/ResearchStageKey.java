package com.verdantartifice.primalmagick.common.research.keys;

import java.util.Objects;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.mutable.MutableBoolean;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.requirements.RequirementCategory;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.player.Player;

public class ResearchStageKey extends AbstractResearchKey<ResearchStageKey> {
    public static final Codec<ResearchStageKey> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceKey.codec(RegistryKeysPM.RESEARCH_ENTRIES).fieldOf("rootKey").forGetter(ResearchStageKey::getRootKey), 
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("stage").forGetter(ResearchStageKey::getStage)
        ).apply(instance, ResearchStageKey::new));
    
    protected final ResourceKey<ResearchEntry> rootKey;
    protected final int stage;
    
    public ResearchStageKey(ResourceKey<ResearchEntry> rootKey, int stage) {
        this.rootKey = rootKey;
        this.stage = stage;
    }
    
    public ResourceKey<ResearchEntry> getRootKey() {
        return this.rootKey;
    }
    
    public int getStage() {
        return this.stage;
    }

    @Override
    public String toString() {
        return this.rootKey.location().toString() + "@" + this.stage;
    }

    @Override
    public RequirementCategory getRequirementCategory() {
        return RequirementCategory.RESEARCH;
    }

    @Override
    protected ResearchKeyType<ResearchStageKey> getType() {
        return ResearchKeyTypesPM.RESEARCH_STAGE.get();
    }

    @Override
    public int hashCode() {
        return Objects.hash(rootKey, stage);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ResearchStageKey other = (ResearchStageKey) obj;
        return Objects.equals(rootKey, other.rootKey) && stage == other.stage;
    }

    @Override
    public boolean isKnownBy(Player player) {
        // Rather than testing if the research entry is complete (because it probably won't be if you're using
        // this key), test if the research stage is at least as high as the one specified in this key.
        if (player == null) {
            return false;
        } else {
            MutableBoolean retVal = new MutableBoolean(false);
            PrimalMagickCapabilities.getKnowledge(player).ifPresent(knowledge -> {
                retVal.setValue(knowledge.getResearchStage(this) + 1 >= this.getStage());
            });
            return retVal.booleanValue();
        }
    }

    @Nonnull
    static ResearchStageKey fromNetworkInner(FriendlyByteBuf buf) {
        return new ResearchStageKey(buf.readResourceKey(RegistryKeysPM.RESEARCH_ENTRIES), buf.readVarInt());
    }
    
    @Override
    protected void toNetworkInner(FriendlyByteBuf buf) {
        buf.writeResourceKey(RegistryKeysPM.RESEARCH_ENTRIES);
        buf.writeVarInt(this.stage);
    }
}
