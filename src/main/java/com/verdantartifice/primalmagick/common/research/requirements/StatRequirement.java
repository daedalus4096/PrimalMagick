package com.verdantartifice.primalmagick.common.research.requirements;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.stats.Stat;
import com.verdantartifice.primalmagick.common.stats.StatsManager;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.player.Player;

/**
 * Requirement that the player has accumulated at least the given amount of the given statistic.
 * 
 * @author Daedalus4096
 */
public class StatRequirement extends AbstractRequirement {
    public static final Codec<StatRequirement> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("stat").xmap(loc -> StatsManager.getStat(loc), stat -> stat.getLocation()).forGetter(req -> req.stat),
            ExtraCodecs.POSITIVE_INT.fieldOf("requiredValue").forGetter(req -> req.requiredValue)
        ).apply(instance, StatRequirement::new));
    
    protected final Stat stat;
    protected final int requiredValue;
    
    public StatRequirement(Stat stat, int requiredValue) {
        if (requiredValue <= 0) {
            throw new IllegalArgumentException("Required value must be positive");
        }
        this.stat = Preconditions.checkNotNull(stat);
        this.requiredValue = requiredValue;
    }

    @Override
    public boolean isMetBy(Player player) {
        if (player == null) {
            return false;
        } else {
            return (StatsManager.getValue(player, this.stat) >= this.requiredValue);
        }
    }

    @Override
    protected RequirementType<?> getType() {
        // TODO Auto-generated method stub
        return null;
    }
}
