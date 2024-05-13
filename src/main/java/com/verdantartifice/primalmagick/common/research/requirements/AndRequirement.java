package com.verdantartifice.primalmagick.common.research.requirements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.research.keys.AbstractResearchKey;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

/**
 * Compound requirement that is met if the player has completed all of the component requirements.
 * 
 * @author Daedalus4096
 */
public class AndRequirement extends AbstractRequirement<AndRequirement> {
    public static final Codec<AndRequirement> CODEC = AbstractRequirement.CODEC.listOf().fieldOf("subRequirements").xmap(AndRequirement::new, req -> req.subs).codec();
    
    protected final List<AbstractRequirement<?>> subs = new ArrayList<>();
    
    public AndRequirement(List<AbstractRequirement<?>> components) {
        this.subs.addAll(components);
    }
    
    public AndRequirement(AbstractRequirement<?>... components) {
        this(Arrays.asList(components));
    }

    @Override
    public boolean isMetBy(Player player) {
        if (player == null) {
            return false;
        } else {
            return this.subs.stream().allMatch(req -> req.isMetBy(player));
        }
    }

    @Override
    public void consumeComponents(Player player) {
        // Consume components from all sub-requirements
        this.subs.forEach(req -> req.consumeComponents(player));
    }

    @Override
    public boolean forceComplete(Player player) {
        return this.subs.stream().map(req -> req.forceComplete(player)).allMatch(val -> val);
    }

    @Override
    public RequirementCategory getCategory() {
        return RequirementCategory.COMPOUND;
    }

    @Override
    public Stream<AbstractRequirement<?>> streamByCategory(RequirementCategory category) {
        Stream<AbstractRequirement<?>> selfStream = category == this.getCategory() ? Stream.of(this) : Stream.empty();
        return Stream.concat(selfStream, this.subs.stream().flatMap(req -> req.streamByCategory(category)));
    }

    @Override
    public boolean contains(AbstractResearchKey<?> researchKey) {
        return this.subs.stream().anyMatch(req -> req.contains(researchKey));
    }

    @Override
    protected RequirementType<AndRequirement> getType() {
        return RequirementsPM.AND.get();
    }

    @Nonnull
    public static AndRequirement fromNetwork(FriendlyByteBuf buf) {
        return new AndRequirement(buf.readList(AbstractRequirement::fromNetwork));
    }
    
    @Override
    protected void toNetworkInner(FriendlyByteBuf buf) {
        buf.writeCollection(this.subs, (b, s) -> s.toNetwork(b));
    }
}
