package com.verdantartifice.primalmagick.common.theorycrafting.materials;

import java.util.Optional;
import java.util.Set;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.research.KnowledgeType;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;

/**
 * Definition of a project material that requires one or more observations, which may or may not be consumed as part
 * of the research project.
 * 
 * @author Daedalus4096
 */
public class ObservationProjectMaterial extends AbstractProjectMaterial<ObservationProjectMaterial> {
    public static MapCodec<ObservationProjectMaterial> codec() {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                ExtraCodecs.POSITIVE_INT.fieldOf("count").forGetter(ObservationProjectMaterial::getCount),
                Codec.BOOL.fieldOf("consumed").forGetter(ObservationProjectMaterial::isConsumed),
                Codec.DOUBLE.fieldOf("weight").forGetter(ObservationProjectMaterial::getWeight),
                Codec.DOUBLE.fieldOf("bonusReward").forGetter(ObservationProjectMaterial::getBonusReward),
                AbstractRequirement.dispatchCodec().optionalFieldOf("requirement").forGetter(ObservationProjectMaterial::getRequirement)
            ).apply(instance, ObservationProjectMaterial::new));
    }
    
    public static StreamCodec<RegistryFriendlyByteBuf, ObservationProjectMaterial> streamCodec() {
        return StreamCodec.composite(
                ByteBufCodecs.VAR_INT,
                ObservationProjectMaterial::getCount,
                ByteBufCodecs.BOOL,
                ObservationProjectMaterial::isConsumed,
                ByteBufCodecs.DOUBLE,
                ObservationProjectMaterial::getWeight,
                ByteBufCodecs.DOUBLE,
                ObservationProjectMaterial::getBonusReward,
                ByteBufCodecs.optional(AbstractRequirement.dispatchStreamCodec()),
                ObservationProjectMaterial::getRequirement,
                ObservationProjectMaterial::new);
    }

    protected final int count;
    protected final boolean consumed;
    
    protected ObservationProjectMaterial(int count, boolean consumed, double weight, double bonusReward, Optional<AbstractRequirement<?>> requirement) {
        super(weight, bonusReward, requirement);
        this.count = count;
        this.consumed = consumed;
    }

    @Override
    protected ProjectMaterialType<ObservationProjectMaterial> getType() {
        return ProjectMaterialTypesPM.OBSERVATION.get();
    }

    @Override
    public boolean isSatisfied(Player player, Set<Block> surroundings) {
        IPlayerKnowledge knowledge = PrimalMagickCapabilities.getKnowledge(player).orElse(null);
        return (knowledge != null && knowledge.getKnowledge(KnowledgeType.OBSERVATION) >= this.count);
    }

    @Override
    public boolean consume(Player player) {
        // Deduct observation level(s) from the player's knowledge pool
        return ResearchManager.addKnowledge(player, KnowledgeType.OBSERVATION, -1 * this.count * KnowledgeType.OBSERVATION.getProgression());
    }
    
    public int getCount() {
        return this.count;
    }
    
    @Override
    public boolean isConsumed() {
        return this.consumed;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (consumed ? 1231 : 1237);
        result = prime * result + count;
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
        ObservationProjectMaterial other = (ObservationProjectMaterial) obj;
        if (consumed != other.consumed)
            return false;
        if (count != other.count)
            return false;
        return true;
    }

    public static Builder builder(int count) {
        return new Builder(count);
    }
    
    public static class Builder extends AbstractProjectMaterial.Builder<ObservationProjectMaterial, Builder> {
        protected final int count;
        protected boolean consumed = false;
        
        protected Builder(int count) {
            this.count = count;
        }
        
        public Builder consumed() {
            this.consumed = true;
            return this;
        }
        
        @Override
        protected void validate() {
            super.validate();
            if (this.count <= 0) {
                throw new IllegalStateException("Material count must be positive");
            }
        }

        @Override
        public ObservationProjectMaterial build() {
            this.validate();
            return new ObservationProjectMaterial(this.count, this.consumed, this.weight, this.bonusReward, this.getFinalRequirement());
        }
    }
}
