package com.verdantartifice.primalmagick.common.research.requirements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.player.Player;

/**
 * Compound requirement that is met if and only if the player meets at least the given number
 * of component requirements.
 * 
 * @author Daedalus4096
 */
public class QuorumRequirement extends AbstractRequirement {
    public static final Codec<QuorumRequirement> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.POSITIVE_INT.fieldOf("requiredCount").forGetter(req -> req.requiredCount), 
            AbstractRequirement.CODEC.listOf().fieldOf("components").forGetter(req -> req.components)
        ).apply(instance, QuorumRequirement::new));
    
    protected final List<AbstractRequirement> components = new ArrayList<>();
    protected final int requiredCount;
    
    public QuorumRequirement(int requiredCount, List<AbstractRequirement> components) {
        if (requiredCount <= 0) {
            throw new IllegalArgumentException("Required count must be positive");
        }
        this.requiredCount = requiredCount;
        this.components.addAll(components);
    }
    
    public QuorumRequirement(int requiredCount, AbstractRequirement... components) {
        this(requiredCount, Arrays.asList(components));
    }

    @Override
    public boolean isMetBy(Player player) {
        if (player == null) {
            return false;
        } else {
            return (this.components.stream().mapToInt(req -> req.isMetBy(player) ? 1 : 0).sum() >= this.requiredCount);
        }
    }

    @Override
    protected RequirementType<?> getType() {
        // TODO Auto-generated method stub
        return null;
    }

}
