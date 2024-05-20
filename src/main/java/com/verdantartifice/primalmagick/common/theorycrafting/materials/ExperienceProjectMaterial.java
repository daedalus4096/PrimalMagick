package com.verdantartifice.primalmagick.common.theorycrafting.materials;

import java.util.Set;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

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
            Codec.BOOL.fieldOf("consumed").forGetter(ExperienceProjectMaterial::isConsumed)
        ).apply(instance, ExperienceProjectMaterial::new));

    protected int levels;
    protected boolean consumed;
    
    public ExperienceProjectMaterial() {
        this(0, true);
    }
    
    public ExperienceProjectMaterial(int levels) {
        this(levels, true);
    }
    
    public ExperienceProjectMaterial(int levels, boolean consumed) {
        super();
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
    public ExperienceProjectMaterial copy() {
        ExperienceProjectMaterial retVal = new ExperienceProjectMaterial();
        retVal.levels = this.levels;
        retVal.consumed = this.consumed;
        retVal.selected = this.selected;
        retVal.weight = this.weight;
        retVal.bonusReward = this.bonusReward;
        if (this.requiredResearch != null) {
            retVal.requiredResearch = this.requiredResearch.copy();
        }
        return retVal;
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
    public static ExperienceProjectMaterial fromNetwork(FriendlyByteBuf buf) {
        return new ExperienceProjectMaterial(buf.readVarInt(), buf.readBoolean());
    }
    
    @Override
    protected void toNetworkInner(FriendlyByteBuf buf) {
        buf.writeVarInt(this.levels);
        buf.writeBoolean(this.consumed);
    }
}
