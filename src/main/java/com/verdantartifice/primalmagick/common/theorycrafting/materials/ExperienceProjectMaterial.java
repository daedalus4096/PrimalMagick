package com.verdantartifice.primalmagick.common.theorycrafting.materials;

import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;

/**
 * Definition of a project material that requires experience levels, which may or may not be consumed as part
 * of the research project.
 * 
 * @author Daedalus4096
 */
public class ExperienceProjectMaterial extends AbstractProjectMaterial<ExperienceProjectMaterial> {
    public static final Codec<ExperienceProjectMaterial> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("levels").forGetter(ExperienceProjectMaterial::getLevels),
            Codec.BOOL.fieldOf("consumed").forGetter(ExperienceProjectMaterial::isConsumed),
            Codec.DOUBLE.fieldOf("weight").forGetter(ExperienceProjectMaterial::getWeight),
            Codec.DOUBLE.fieldOf("bonusReward").forGetter(ExperienceProjectMaterial::getBonusReward),
            AbstractRequirement.CODEC.optionalFieldOf("requirement").forGetter(ExperienceProjectMaterial::getRequirement)
        ).apply(instance, ExperienceProjectMaterial::new));

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

    @Nonnull
    public static ExperienceProjectMaterial fromNetworkInner(FriendlyByteBuf buf, double weight, double bonusReward, Optional<AbstractRequirement<?>> requirement) {
        return new ExperienceProjectMaterial(buf.readVarInt(), buf.readBoolean(), weight, bonusReward, requirement);
    }
    
    @Override
    protected void toNetworkInner(FriendlyByteBuf buf) {
        buf.writeVarInt(this.levels);
        buf.writeBoolean(this.consumed);
    }
    
    public static class Builder extends AbstractProjectMaterial.Builder<ExperienceProjectMaterial, Builder> {
        protected final int levels;
        protected final boolean consumed;
        
        public Builder(int levels, boolean consumed) {
            this.levels = levels;
            this.consumed = consumed;
        }

        @Override
        public ExperienceProjectMaterial build() {
            return new ExperienceProjectMaterial(this.levels, this.consumed, this.weight, this.bonusReward, this.getFinalRequirement());
        }
    }
}
