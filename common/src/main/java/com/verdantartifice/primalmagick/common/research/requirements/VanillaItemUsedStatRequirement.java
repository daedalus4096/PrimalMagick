package com.verdantartifice.primalmagick.common.research.requirements;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.misc.IconDefinition;
import com.verdantartifice.primalmagick.platform.Services;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.stats.StatsCounter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import java.util.stream.Stream;

/**
 * Requirement that the player has accumulated at least the given amount of the given Minecraft statistic.
 * 
 * @author Daedalus4096
 */
public class VanillaItemUsedStatRequirement extends AbstractRequirement<VanillaItemUsedStatRequirement> implements IVanillaStatRequirement {
    public static final MapCodec<VanillaItemUsedStatRequirement> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Identifier.CODEC.fieldOf("item").xmap(Services.ITEMS_REGISTRY::get, Services.ITEMS_REGISTRY::getKey).forGetter(req -> req.stat.getValue()),
            Codec.INT.fieldOf("threshold").forGetter(VanillaItemUsedStatRequirement::getThreshold)
        ).apply(instance, VanillaItemUsedStatRequirement::new));
    public static final StreamCodec<ByteBuf, VanillaItemUsedStatRequirement> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC.map(Services.ITEMS_REGISTRY::get, Services.ITEMS_REGISTRY::getKey),
            req -> req.stat.getValue(),
            ByteBufCodecs.VAR_INT,
            VanillaItemUsedStatRequirement::getThreshold,
            VanillaItemUsedStatRequirement::new);
    
    protected final Stat<Item> stat;
    protected final int threshold;
    
    public VanillaItemUsedStatRequirement(Item item, int threshold) {
        this(Stats.ITEM_USED.get(Preconditions.checkNotNull(item)), threshold);
    }

    protected VanillaItemUsedStatRequirement(Stat<Item> stat, int threshold) {
        this.stat = stat;
        this.threshold = threshold;
    }
    
    @Override
    public Stat<?> getStat() {
        return this.stat;
    }

    @Override
    public Identifier getStatTypeLoc() {
        return BuiltInRegistries.STAT_TYPE.getKey(this.stat.getType());
    }
    
    @Override
    public Identifier getStatValueLoc() {
        return Services.ITEMS_REGISTRY.getKey(this.stat.getValue());
    }

    @Override
    public int getThreshold() {
        return this.threshold;
    }
    
    @Override
    public Component getStatDescription() {
        Component baseLabel = this.stat.getType().getDisplayName();
        Component itemLabel = this.stat.getValue().getName();
        return Component.translatable("tooltip.primalmagick.stat_description.vanilla", baseLabel, itemLabel);
    }

    @Override
    public IconDefinition getIconDefinition() {
        return IconDefinition.of(this.stat.getValue());
    }

    public int getCurrentValue(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            return serverPlayer.getStats().getValue(this.stat);
        } else if (Services.PLATFORM.isClientDist()) {
            StatsCounter statsCounter = ClientUtils.getStatsCounter();
            if (statsCounter != null) {
                return statsCounter.getValue(this.stat);
            }
        }
        throw new IllegalStateException("Player is neither server nor client side!");
    }

    @Override
    public boolean isMetBy(Player player) {
        return this.getCurrentValue(player) >= this.threshold;
    }

    @Override
    public void consumeComponents(Player player) {
        // No action needed; statistics are never consumed
    }

    @Override
    public boolean forceComplete(Player player) {
        if (player instanceof ServerPlayer serverPlayer && this.threshold > this.getCurrentValue(serverPlayer)) {
            serverPlayer.getStats().setValue(serverPlayer, this.stat, this.threshold);
            return true;
        } else {
            return false;
        }
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
    protected RequirementType<VanillaItemUsedStatRequirement> getType() {
        return RequirementsPM.VANILLA_ITEM_USED_STAT.get();
    }
}
