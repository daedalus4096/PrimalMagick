package com.verdantartifice.primalmagick.common.theorycrafting.materials;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;

import com.mojang.datafixers.util.Function4;
import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.AndRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
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
    
    protected final double weight;
    protected final double bonusReward;
    protected final Optional<AbstractRequirement<?>> requirement;
    
    protected AbstractProjectMaterial(double weight, double bonusReward, Optional<AbstractRequirement<?>> requirement) {
        this.weight = weight;
        this.bonusReward = bonusReward;
        this.requirement = requirement;
    }
    
    protected abstract ProjectMaterialType<T> getType();
    
    public static AbstractProjectMaterial<?> fromNetwork(FriendlyByteBuf buf) {
        ResourceLocation typeId = buf.readResourceLocation();
        double weight = buf.readDouble();
        double bonusReward = buf.readDouble();
        Optional<AbstractRequirement<?>> requirement = buf.readOptional(AbstractRequirement::fromNetwork);
        return ProjectMaterialTypesPM.TYPES.get().getValue(typeId).networkReader().apply(buf, weight, bonusReward, requirement);
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
    
    public double getWeight() {
        return this.weight;
    }
    
    public double getBonusReward() {
        return this.bonusReward;
    }
    
    @Nonnull
    public Optional<AbstractRequirement<?>> getRequirement() {
        return this.requirement;
    }
    
    public boolean isAllowedInProject(ServerPlayer player) {
        return this.hasRequiredResearch(player);
    }
    
    public boolean hasRequiredResearch(Player player) {
        return this.requirement.map(req -> req.isMetBy(player)).orElse(true);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(bonusReward, requirement, weight);
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
                && Objects.equals(requirement, other.requirement)
                && Double.doubleToLongBits(weight) == Double.doubleToLongBits(other.weight);
    }
    
    @FunctionalInterface
    public interface Reader<T extends AbstractProjectMaterial<T>> extends Function4<FriendlyByteBuf, Double, Double, Optional<AbstractRequirement<?>>, T> {
    }
    
    protected abstract static class Builder<T extends AbstractProjectMaterial<T>, U extends Builder<T, U>> {
        // TODO Add parent builder reference
        protected double weight = 1D;
        protected double bonusReward = 0D;
        protected final List<AbstractRequirement<?>> requirements = new ArrayList<>();
        
        @SuppressWarnings("unchecked")
        private U self() {
            return (U)this;
        }
        
        public U weight(double weight) {
            this.weight = weight;
            return this.self();
        }
        
        public U bonusReward(double bonus) {
            this.bonusReward = bonus;
            return this.self();
        }

        public U requirement(AbstractRequirement<?> req) {
            this.requirements.add(req);
            return this.self();
        }
        
        public U requiredResearch(ResourceKey<ResearchEntry> rawKey) {
            return this.requirement(new ResearchRequirement(new ResearchEntryKey(rawKey)));
        }
        
        protected Optional<AbstractRequirement<?>> getFinalRequirement() {
            if (this.requirements.isEmpty()) {
                return Optional.empty();
            } else if (this.requirements.size() == 1) {
                return Optional.of(this.requirements.get(0));
            } else {
                return Optional.of(new AndRequirement(this.requirements));
            }
        }
        
        protected void validate() {
            if (this.weight <= 0D) {
                throw new IllegalStateException("Material weight must be positive");
            } else if (this.bonusReward < 0D) {
                throw new IllegalStateException("Material bonus reward must be non-negative");
            }
        }
        
        public abstract T build();
    }
}
