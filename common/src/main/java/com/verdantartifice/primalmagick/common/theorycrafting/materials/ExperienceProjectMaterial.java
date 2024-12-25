package com.verdantartifice.primalmagick.common.theorycrafting.materials;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;

import java.util.Optional;
import java.util.Set;

/**
 * Definition of a project material that requires experience levels, which may or may not be consumed as part
 * of the research project.
 * 
 * @author Daedalus4096
 */
public class ExperienceProjectMaterial extends AbstractProjectMaterial<ExperienceProjectMaterial> {
    public static MapCodec<ExperienceProjectMaterial> codec() { 
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                ExtraCodecs.POSITIVE_INT.fieldOf("levels").forGetter(ExperienceProjectMaterial::getLevels),
                Codec.BOOL.fieldOf("consumed").forGetter(ExperienceProjectMaterial::isConsumed),
                Codec.DOUBLE.fieldOf("weight").forGetter(ExperienceProjectMaterial::getWeight),
                Codec.DOUBLE.fieldOf("bonusReward").forGetter(ExperienceProjectMaterial::getBonusReward),
                AbstractRequirement.dispatchCodec().optionalFieldOf("requirement").forGetter(ExperienceProjectMaterial::getRequirement)
            ).apply(instance, ExperienceProjectMaterial::new));
    }
    
    public static StreamCodec<RegistryFriendlyByteBuf, ExperienceProjectMaterial> streamCodec() {
        return StreamCodec.composite(
                ByteBufCodecs.VAR_INT,
                ExperienceProjectMaterial::getLevels,
                ByteBufCodecs.BOOL,
                ExperienceProjectMaterial::isConsumed,
                ByteBufCodecs.DOUBLE,
                ExperienceProjectMaterial::getWeight,
                ByteBufCodecs.DOUBLE,
                ExperienceProjectMaterial::getBonusReward,
                ByteBufCodecs.optional(AbstractRequirement.dispatchStreamCodec()),
                ExperienceProjectMaterial::getRequirement,
                ExperienceProjectMaterial::new);
    }

    protected final int levels;
    protected final boolean consumed;
    
    protected ExperienceProjectMaterial(int levels, boolean consumed, double weight, double bonusReward, Optional<AbstractRequirement<?>> requirement) {
        super(weight, bonusReward, requirement);
        this.levels = levels;
        this.consumed = consumed;
    }
    
    @Override
    protected ProjectMaterialType<ExperienceProjectMaterial> getType() {
        return ProjectMaterialTypesPM.EXPERIENCE.get();
    }

    @Override
    public boolean isSatisfied(Player player, Set<Block> surroundings) {
        return player.experienceLevel >= this.levels;
    }

    @Override
    public boolean consume(Player player) {
        player.giveExperienceLevels(-1 * this.levels);
        return true;
    }
    
    public int getLevels() {
        return this.levels;
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
        result = prime * result + levels;
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
        ExperienceProjectMaterial other = (ExperienceProjectMaterial) obj;
        if (consumed != other.consumed)
            return false;
        if (levels != other.levels)
            return false;
        return true;
    }

    public static Builder builder(int levels) {
        return new Builder(levels);
    }
    
    public static class Builder extends AbstractProjectMaterial.Builder<ExperienceProjectMaterial, Builder> {
        protected final int levels;
        protected boolean consumed = false;
        
        protected Builder(int levels) {
            this.levels = levels;
        }
        
        public Builder consumed() {
            this.consumed = true;
            return this;
        }

        @Override
        protected void validate() {
            super.validate();
            if (this.levels <= 0) {
                throw new IllegalStateException("Material levels must be positive");
            }
        }

        @Override
        public ExperienceProjectMaterial build() {
            this.validate();
            return new ExperienceProjectMaterial(this.levels, this.consumed, this.weight, this.bonusReward, this.getFinalRequirement());
        }
    }
}
