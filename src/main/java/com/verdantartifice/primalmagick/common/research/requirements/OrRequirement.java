package com.verdantartifice.primalmagick.common.research.requirements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import com.mojang.serialization.Codec;

import net.minecraft.world.entity.player.Player;

/**
 * Compound requirement that is met if the player has completed any of the component requirements.
 * 
 * @author Daedalus4096
 */
public class OrRequirement extends AbstractRequirement {
    public static final Codec<OrRequirement> CODEC = AbstractRequirement.CODEC.listOf().fieldOf("subRequirements").xmap(OrRequirement::new, req -> req.subs).codec();
    
    protected final List<AbstractRequirement> subs = new ArrayList<>();
    
    public OrRequirement(List<AbstractRequirement> components) {
        this.subs.addAll(components);
    }
    
    public OrRequirement(AbstractRequirement... components) {
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
    public RequirementCategory getCategory() {
        return RequirementCategory.COMPOUND;
    }

    @Override
    public Stream<AbstractRequirement> streamByCategory(RequirementCategory category) {
        Stream<AbstractRequirement> selfStream = category == this.getCategory() ? Stream.of(this) : Stream.empty();
        return Stream.concat(selfStream, this.subs.stream().flatMap(req -> req.streamByCategory(category)));
    }

    @Override
    protected RequirementType<?> getType() {
        return RequirementsPM.OR.get();
    }
}
