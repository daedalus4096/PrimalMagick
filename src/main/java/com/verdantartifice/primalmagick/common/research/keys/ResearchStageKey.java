package com.verdantartifice.primalmagick.common.research.keys;

import java.util.Objects;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.mutable.MutableBoolean;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.research.requirements.RequirementCategory;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.player.Player;

public class ResearchStageKey extends AbstractResearchKey<ResearchStageKey> {
    public static final Codec<ResearchStageKey> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("rootKey").forGetter(ResearchStageKey::getRootKey), 
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("stage").forGetter(ResearchStageKey::getStage)
        ).apply(instance, ResearchStageKey::new));
    
    protected final String rootKey; // TODO Replace with a ResourceKey once the research system refactor is complete
    protected final int stage;
    
    public ResearchStageKey(String rootKey, int stage) {
        this.rootKey = rootKey;
        this.stage = stage;
    }
    
    public String getRootKey() {
        return this.rootKey;
    }
    
    public int getStage() {
        return this.stage;
    }

    @Override
    public String toString() {
        return this.rootKey + "@" + this.stage;
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
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(stage);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        ResearchStageKey other = (ResearchStageKey) obj;
        return stage == other.stage;
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
    public static ResearchStageKey fromNetwork(FriendlyByteBuf buf) {
        return new ResearchStageKey(buf.readUtf(), buf.readVarInt());
    }
    
    @Override
    public void toNetworkInner(FriendlyByteBuf buf) {
        buf.writeUtf(this.rootKey);
        buf.writeVarInt(this.stage);
    }
}
