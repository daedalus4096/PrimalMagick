package com.verdantartifice.primalmagick.common.research.requirements;

import com.google.common.base.Preconditions;
import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.research.keys.AbstractResearchKey;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;

import java.util.stream.Stream;

/**
 * Requirement that the player has completed a given research entry.
 * 
 * @author Daedalus4096
 */
public class ResearchRequirement extends AbstractRequirement<ResearchRequirement> {
    public static MapCodec<ResearchRequirement> codec() {
        return AbstractResearchKey.dispatchCodec().fieldOf("rootKey").xmap(ResearchRequirement::new, req -> req.rootKey);
    }
    
    public static StreamCodec<RegistryFriendlyByteBuf, ResearchRequirement> streamCodec() {
        return AbstractResearchKey.dispatchStreamCodec().map(ResearchRequirement::new, req -> req.rootKey);
    }
    
    protected final AbstractResearchKey<?> rootKey;
    
    public ResearchRequirement(AbstractResearchKey<?> rootKey) {
        this.rootKey = Preconditions.checkNotNull(rootKey);
    }
    
    public AbstractResearchKey<?> getRootKey() {
        return this.rootKey;
    }

    @Override
    public boolean isMetBy(Player player) {
        return player == null ? false : this.rootKey.isKnownBy(player);
    }

    @Override
    public void consumeComponents(Player player) {
        // No action needed; research is never consumed
    }

    @Override
    public boolean forceComplete(Player player) {
        // Complete the required research
        if (this.rootKey instanceof ResearchEntryKey entryKey) {
            return ResearchManager.completeResearch(player, entryKey, true, true, false);
        } else {
            Services.CAPABILITIES.knowledge(player).ifPresent(k -> {
                k.addResearch(this.rootKey);
            });
            return true;
        }
    }

    @Override
    public RequirementCategory getCategory() {
        return this.rootKey.getRequirementCategory();
    }

    @Override
    public Stream<AbstractRequirement<?>> streamByCategory(RequirementCategory category) {
        return category == this.getCategory() ? Stream.of(this) : Stream.empty();
    }

    @Override
    public boolean contains(AbstractResearchKey<?> researchKey) {
        return this.rootKey.equals(researchKey);
    }

    @Override
    public Stream<AbstractResearchKey<?>> streamKeys() {
        return Stream.of(this.rootKey);
    }

    @Override
    protected RequirementType<ResearchRequirement> getType() {
        return RequirementsPM.RESEARCH.get();
    }
}
