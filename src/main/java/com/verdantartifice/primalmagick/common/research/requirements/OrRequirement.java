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
 * Compound requirement that is met if the player has completed any of the component requirements.
 * 
 * @author Daedalus4096
 */
public class OrRequirement extends AbstractRequirement<OrRequirement> {
    public static Codec<OrRequirement> codec() {
        return AbstractRequirement.dispatchCodec().listOf().fieldOf("subRequirements").xmap(OrRequirement::new, req -> req.subs).codec();
    }
    
    protected final List<AbstractRequirement<?>> subs = new ArrayList<>();
    
    public OrRequirement(List<AbstractRequirement<?>> components) {
        this.subs.addAll(components);
    }
    
    public OrRequirement(AbstractRequirement<?>... components) {
        this(Arrays.asList(components));
    }

    @Override
    public boolean isMetBy(Player player) {
        if (player == null) {
            return false;
        } else {
            return this.subs.stream().anyMatch(req -> req.isMetBy(player));
        }
    }

    @Override
    public void consumeComponents(Player player) {
        // Consume requirements from all sub-requirements that were met
        this.subs.stream().filter(req -> req.isMetBy(player)).forEach(req -> req.consumeComponents(player));
    }

    @Override
    public boolean forceComplete(Player player) {
        return this.subs.stream().map(req -> req.forceComplete(player)).anyMatch(val -> val);
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
    public Stream<AbstractResearchKey<?>> streamKeys() {
        return this.subs.stream().flatMap(req -> req.streamKeys());
    }

    @Override
    protected RequirementType<OrRequirement> getType() {
        return RequirementsPM.OR.get();
    }

    @Nonnull
    static OrRequirement fromNetworkInner(FriendlyByteBuf buf) {
        return new OrRequirement(buf.readList(AbstractRequirement::fromNetwork));
    }
    
    @Override
    protected void toNetworkInner(FriendlyByteBuf buf) {
        buf.writeCollection(this.subs, (b, s) -> s.toNetwork(b));
    }
}
