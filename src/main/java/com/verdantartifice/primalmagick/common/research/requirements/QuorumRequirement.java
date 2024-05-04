package com.verdantartifice.primalmagick.common.research.requirements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.player.Player;

/**
 * Compound requirement that is met if and only if the player meets at least the given number
 * of component requirements.
 * 
 * @author Daedalus4096
 */
public class QuorumRequirement extends AbstractRequirement<QuorumRequirement> {
    public static final Codec<QuorumRequirement> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.POSITIVE_INT.fieldOf("requiredCount").forGetter(req -> req.requiredCount), 
            AbstractRequirement.CODEC.listOf().fieldOf("subRequirements").forGetter(req -> req.subs)
        ).apply(instance, QuorumRequirement::new));
    
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
    public RequirementCategory getCategory() {
        return RequirementCategory.COMPOUND;
    }

    @Override
    public Stream<AbstractRequirement<?>> streamByCategory(RequirementCategory category) {
        Stream<AbstractRequirement<?>> selfStream = category == this.getCategory() ? Stream.of(this) : Stream.empty();
        return Stream.concat(selfStream, this.subs.stream().flatMap(req -> req.streamByCategory(category)));
    }

    @Override
    protected RequirementType<QuorumRequirement> getType() {
        return RequirementsPM.QUORUM.get();
    }

    @Nonnull
    public static QuorumRequirement fromNetwork(FriendlyByteBuf buf) {
        return new QuorumRequirement(buf.readVarInt(), buf.readList(AbstractRequirement::fromNetwork));
    }
    
    @Override
    public void toNetworkInner(FriendlyByteBuf buf) {
        buf.writeVarInt(this.requiredCount);
        buf.writeCollection(this.subs, (b, s) -> s.toNetwork(b));
    }
}
