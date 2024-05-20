package com.verdantartifice.primalmagick.common.theorycrafting.materials;

import java.util.Objects;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;

/**
 * Base class for a material required by a theorycrafting research project.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractProjectMaterial<T extends AbstractProjectMaterial<T>> {
    public static final Codec<AbstractProjectMaterial<?>> CODEC = ProjectMaterialTypesPM.TYPES.get().getCodec().dispatch("material_type", AbstractProjectMaterial::getType, ProjectMaterialType::codec);
    
    protected boolean selected;
    protected double weight;
    protected double bonusReward;
    protected CompoundResearchKey requiredResearch;
    
    protected AbstractProjectMaterial() {
        this.selected = false;
    }
    
    protected abstract ProjectMaterialType<T> getType();
    
    public static AbstractProjectMaterial<?> fromNetwork(FriendlyByteBuf buf) {
        ResourceLocation typeId = buf.readResourceLocation();
        return ProjectMaterialTypesPM.TYPES.get().getValue(typeId).networkReader().apply(buf);
    }
    
    public void toNetwork(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.getType().id());
        this.toNetworkInner(buf);
    }
    
    protected abstract void toNetworkInner(FriendlyByteBuf buf);
    
    /**
     * Determine if this material's requirements are satisfied by the given player.
     * 
     * @param player the player doing the research project
     * @param surroundings the set of blocks near the research table
     * @return true if the requirement is satisfied, false otherwise
     */
    public abstract boolean isSatisfied(Player player, Set<Block> surroundings);
    
    /**
     * Consume this project material's requirements from the given player.
     * 
     * @param player the player doing the research project
     * @return true if the consumption succeeded, false otherwise
     */
    public abstract boolean consume(Player player);
    
    /**
     * Determine whether this material should be consumed upon project completion.
     * 
     * @return whether this material should be consumed upon project completion
     */
    public abstract boolean isConsumed();
    
    public boolean isSelected() {
        return this.selected;
    }
    
    public double getWeight() {
        return this.weight;
    }
    
    public double getBonusReward() {
        return this.bonusReward;
    }
    
    @Nullable
    public CompoundResearchKey getRequiredResearch() {
        return this.requiredResearch;
    }
    
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    public void setWeight(double weight) {
        this.weight = weight;
    }
    
    public void setBonusReward(double bonus) {
        this.bonusReward = bonus;
    }
    
    public void setRequiredResearch(@Nonnull CompoundResearchKey key) {
        this.requiredResearch = key.copy();
    }
    
    public boolean isAllowedInProject(ServerPlayer player) {
        return this.hasRequiredResearch(player);
    }
    
    public boolean hasRequiredResearch(Player player) {
        if (this.requiredResearch == null) {
            return true;
        } else {
            return this.requiredResearch.isKnownByStrict(player);
        }
    }
    
    public abstract T copy();

    @Override
    public int hashCode() {
        return Objects.hash(bonusReward, requiredResearch, selected, weight);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AbstractProjectMaterial<?> other = (AbstractProjectMaterial<?>) obj;
        return Double.doubleToLongBits(bonusReward) == Double.doubleToLongBits(other.bonusReward)
                && Objects.equals(requiredResearch, other.requiredResearch) && selected == other.selected
                && Double.doubleToLongBits(weight) == Double.doubleToLongBits(other.weight);
    }
}
