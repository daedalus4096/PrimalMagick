package com.verdantartifice.primalmagick.common.research.keys;

import java.util.Objects;

import org.apache.commons.lang3.mutable.MutableBoolean;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.IconDefinition;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.requirements.RequirementCategory;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.player.Player;

public class ResearchStageKey extends AbstractResearchKey<ResearchStageKey> {
    public static final MapCodec<ResearchStageKey> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceKey.codec(RegistryKeysPM.RESEARCH_ENTRIES).fieldOf("rootKey").forGetter(ResearchStageKey::getRootKey), 
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("stage").forGetter(ResearchStageKey::getStage)
        ).apply(instance, ResearchStageKey::new));
    public static final StreamCodec<ByteBuf, ResearchStageKey> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(RegistryKeysPM.RESEARCH_ENTRIES),
            ResearchStageKey::getRootKey,
            ByteBufCodecs.VAR_INT,
            ResearchStageKey::getStage,
            ResearchStageKey::new);
    
    private static final ResourceLocation ICON_UNKNOWN = ResourceUtils.loc("textures/research/research_unknown.png");

    protected final ResourceKey<ResearchEntry> rootKey;
    protected final int stage;
    protected final ResearchEntryKey strippedKey;
    
    public ResearchStageKey(ResourceKey<ResearchEntry> rootKey, int stage) {
        this.rootKey = rootKey;
        this.stage = stage;
        this.strippedKey = new ResearchEntryKey(rootKey);
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
    public IconDefinition getIcon(RegistryAccess registryAccess) {
        return registryAccess.registryOrThrow(RegistryKeysPM.RESEARCH_ENTRIES).getHolder(this.rootKey).flatMap(ref -> ref.get().iconOpt()).orElse(IconDefinition.of(ICON_UNKNOWN));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.rootKey.registry(), this.rootKey.location(), this.stage);
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
                int currentStage = knowledge.getResearchStage(this.strippedKey) + 1;
                retVal.setValue(currentStage >= this.getStage());
            });
            return retVal.booleanValue();
        }
    }
}
