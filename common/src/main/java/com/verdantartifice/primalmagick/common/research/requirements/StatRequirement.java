package com.verdantartifice.primalmagick.common.research.requirements;

import com.google.common.base.Preconditions;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.stats.Stat;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.player.Player;

import java.util.stream.Stream;

/**
 * Requirement that the player has accumulated at least the given amount of the given Primal Magick statistic.
 * 
 * @author Daedalus4096
 */
public class StatRequirement extends AbstractRequirement<StatRequirement> {
    public static final MapCodec<StatRequirement> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("stat").xmap(loc -> StatsManager.getStat(loc), stat -> stat.key()).forGetter(req -> req.stat),
            ExtraCodecs.POSITIVE_INT.fieldOf("requiredValue").forGetter(req -> req.requiredValue)
        ).apply(instance, StatRequirement::new));
    public static final StreamCodec<ByteBuf, StatRequirement> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC.map(loc -> StatsManager.getStat(loc), stat -> stat.key()),
            StatRequirement::getStat,
            ByteBufCodecs.VAR_INT,
            StatRequirement::getRequiredValue,
            StatRequirement::new);
    
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
}
