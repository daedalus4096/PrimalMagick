package com.verdantartifice.primalmagick.common.research.requirements;

import java.util.stream.Stream;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.research.keys.AbstractResearchKey;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

/**
 * Requirement that the player has completed a given research entry.
 * 
 * @author Daedalus4096
 */
public class ResearchRequirement extends AbstractRequirement<ResearchRequirement> {
    public static final Codec<ResearchRequirement> CODEC = AbstractResearchKey.CODEC.fieldOf("rootKey").xmap(ResearchRequirement::new, req -> req.rootKey).codec();
    
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
            PrimalMagickCapabilities.getKnowledge(player).ifPresent(k -> {
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

    @Nonnull
    public static ResearchRequirement fromNetwork(FriendlyByteBuf buf) {
        return new ResearchRequirement(AbstractResearchKey.fromNetwork(buf));
    }
    
    @Override
    protected void toNetworkInner(FriendlyByteBuf buf) {
        this.rootKey.toNetwork(buf);
    }
}
