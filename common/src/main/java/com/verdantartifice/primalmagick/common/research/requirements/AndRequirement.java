package com.verdantartifice.primalmagick.common.research.requirements;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.research.keys.AbstractResearchKey;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Compound requirement that is met if the player has completed all of the component requirements.
 * 
 * @author Daedalus4096
 */
public class AndRequirement extends AbstractRequirement<AndRequirement> {
    public static MapCodec<AndRequirement> codec() {
        return AbstractRequirement.dispatchCodec().listOf().fieldOf("subRequirements").xmap(AndRequirement::new, req -> req.subs);
    }
    
    public static StreamCodec<RegistryFriendlyByteBuf, AndRequirement> streamCodec() {
        return AbstractRequirement.dispatchStreamCodec().apply(ByteBufCodecs.list()).map(AndRequirement::new, req -> req.subs);
    }
    
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
    public Stream<AbstractResearchKey<?>> streamKeys() {
        return this.subs.stream().flatMap(req -> req.streamKeys());
    }

    @Override
    protected RequirementType<AndRequirement> getType() {
        return RequirementsPM.AND.get();
    }
}
