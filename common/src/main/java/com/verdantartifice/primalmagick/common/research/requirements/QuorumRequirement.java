package com.verdantartifice.primalmagick.common.research.requirements;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.research.keys.AbstractResearchKey;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Compound requirement that is met if and only if the player meets at least the given number
 * of component requirements.
 * 
 * @author Daedalus4096
 */
public class QuorumRequirement extends AbstractRequirement<QuorumRequirement> {
    public static MapCodec<QuorumRequirement> codec() {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
            ExtraCodecs.POSITIVE_INT.fieldOf("requiredCount").forGetter(req -> req.requiredCount), 
            AbstractRequirement.dispatchCodec().listOf().fieldOf("subRequirements").forGetter(req -> req.subs)
        ).apply(instance, QuorumRequirement::new));
    }
    
    public static StreamCodec<RegistryFriendlyByteBuf, QuorumRequirement> streamCodec() {
        return StreamCodec.composite(
                ByteBufCodecs.VAR_INT,
                req -> req.requiredCount,
                AbstractRequirement.dispatchStreamCodec().apply(ByteBufCodecs.list()),
                req -> req.subs,
                QuorumRequirement::new);
    }
    
    protected final List<AbstractRequirement<?>> subs = new ArrayList<>();
    protected final int requiredCount;
    
    public QuorumRequirement(int requiredCount, List<AbstractRequirement<?>> components) {
        if (requiredCount <= 0) {
            throw new IllegalArgumentException("Required count must be positive");
        }
        this.requiredCount = requiredCount;
        this.subs.addAll(components);
    }
    
    public QuorumRequirement(int requiredCount, AbstractRequirement<?>... components) {
        this(requiredCount, Arrays.asList(components));
    }

    @Override
    public boolean isMetBy(Player player) {
        if (player == null) {
            return false;
        } else {
            return (this.subs.stream().mapToInt(req -> req.isMetBy(player) ? 1 : 0).sum() >= this.requiredCount);
        }
    }

    @Override
    public void consumeComponents(Player player) {
        // Consume requirements from all sub-requirements that were met
        this.subs.stream().filter(req -> req.isMetBy(player)).forEach(req -> req.consumeComponents(player));
    }

    @Override
    public boolean forceComplete(Player player) {
        return this.subs.stream().mapToInt(req -> req.forceComplete(player) ? 1 : 0).sum() >= this.requiredCount;
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
    protected RequirementType<QuorumRequirement> getType() {
        return RequirementsPM.QUORUM.get();
    }
}
