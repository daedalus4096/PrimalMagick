package com.verdantartifice.primalmagick.common.research.requirements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.research.IconDefinition;
import com.verdantartifice.primalmagick.platform.Services;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;

import java.util.stream.Stream;

/**
 * Requirement that the player has accumulated at least the given amount of the given Minecraft statistic.
 * 
 * @author Daedalus4096
 */
public class VanillaCustomStatRequirement extends AbstractRequirement<VanillaCustomStatRequirement> implements IVanillaStatRequirement {
    public static final MapCodec<VanillaCustomStatRequirement> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("statValue").forGetter(VanillaCustomStatRequirement::getStatValueLoc),
            Codec.INT.fieldOf("threshold").forGetter(VanillaCustomStatRequirement::getThreshold),
            IconDefinition.CODEC.fieldOf("iconDefinition").forGetter(VanillaCustomStatRequirement::getIconDefinition)
        ).apply(instance, VanillaCustomStatRequirement::new));
    public static final StreamCodec<ByteBuf, VanillaCustomStatRequirement> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC,
            VanillaCustomStatRequirement::getStatValueLoc,
            ByteBufCodecs.VAR_INT,
            VanillaCustomStatRequirement::getThreshold,
            IconDefinition.STREAM_CODEC,
            VanillaCustomStatRequirement::getIconDefinition,
            VanillaCustomStatRequirement::new);
    
    protected final ResourceLocation statValueLocation;
    protected final int threshold;
    protected final IconDefinition iconDefinition;
    protected Stat<ResourceLocation> statCache = null;
    
    public VanillaCustomStatRequirement(ResourceLocation loc, int threshold, IconDefinition iconDefinition) {
        this.statValueLocation = loc;
        this.threshold = threshold;
        this.iconDefinition = iconDefinition;
    }
    
    @Override
    public Stat<?> getStat() {
        if (this.statCache == null) {
            // This is stupid.  This should be as simple as a call to Stats.CUSTOM.get(this.statValueLocation), but apparently
            // on the client side that results in a crash because it thinks the stat isn't registered?  Anyway, this is how the
            // vanilla statistics screen reads all the statistics on the client side, and it works.  Revisit someday and fix.
            ObjectArrayList<Stat<ResourceLocation>> statsList = new ObjectArrayList<>(Stats.CUSTOM.iterator());
            this.statCache = statsList.stream().filter(s -> s.getValue().equals(this.statValueLocation)).findFirst().orElse(null);
        }
        return this.statCache;
    }

    @Override
    public ResourceLocation getStatTypeLoc() {
        return BuiltInRegistries.STAT_TYPE.getKey(Stats.CUSTOM);
    }
    
    @Override
    public ResourceLocation getStatValueLoc() {
        return this.statValueLocation;
    }

    @Override
    public int getThreshold() {
        return this.threshold;
    }
    
    @Override
    public Component getStatDescription() {
        String key = String.join(".", "stat", this.statValueLocation.getNamespace(), this.statValueLocation.getPath());
        return Component.translatable(key);
    }

    @Override
    public IconDefinition getIconDefinition() {
        return this.iconDefinition;
    }

    public int getCurrentValue(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            return serverPlayer.getStats().getValue(this.getStat());
        } else if (Services.PLATFORM.isClientDist()) {
            return ClientUtils.getStatsCounter().getValue(this.getStat());
        } else {
            throw new IllegalStateException("Player is neither server nor client side!");
        }
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
            serverPlayer.getStats().setValue(serverPlayer, this.getStat(), this.threshold);
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
    protected RequirementType<VanillaCustomStatRequirement> getType() {
        return RequirementsPM.VANILLA_CUSTOM_STAT.get();
    }
}
