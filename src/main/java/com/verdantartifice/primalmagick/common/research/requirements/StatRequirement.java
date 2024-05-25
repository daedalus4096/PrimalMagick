package com.verdantartifice.primalmagick.common.research.requirements;

import java.util.stream.Stream;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.stats.Stat;
import com.verdantartifice.primalmagick.common.stats.StatsManager;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.player.Player;

/**
 * Requirement that the player has accumulated at least the given amount of the given Primal Magick statistic.
 * 
 * @author Daedalus4096
 */
public class StatRequirement extends AbstractRequirement<StatRequirement> {
    public static final Codec<StatRequirement> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("stat").xmap(loc -> StatsManager.getStat(loc), stat -> stat.key()).forGetter(req -> req.stat),
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
    
    public Stat getStat() {
        return this.stat;
    }
    
    public int getRequiredValue() {
        return this.requiredValue;
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
    public void consumeComponents(Player player) {
        // No action needed; statistics are never consumed
    }

    @Override
    public boolean forceComplete(Player player) {
        StatsManager.setValueIfMax(player, this.stat, this.requiredValue);
        return true;
    }

    @Override
    public RequirementCategory getCategory() {
        return RequirementCategory.STAT;
    }

    @Override
    public Stream<AbstractRequirement<?>> streamByCategory(RequirementCategory category) {
        return category == this.getCategory() ? Stream.of(this) : Stream.empty();
    }

    @Override
    protected RequirementType<StatRequirement> getType() {
        return RequirementsPM.STAT.get();
    }

    @Nonnull
    public static StatRequirement fromNetwork(FriendlyByteBuf buf) {
        return new StatRequirement(StatsManager.getStat(buf.readResourceLocation()), buf.readVarInt());
    }
    
    @Override
    protected void toNetworkInner(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.stat.key());
        buf.writeVarInt(this.requiredValue);
    }
}
