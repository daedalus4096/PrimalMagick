package com.verdantartifice.primalmagick.common.research.requirements;

import java.util.stream.Stream;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.client.util.ClientUtils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Requirement that the player has accumulated at least the given amount of the given Minecraft statistic.
 * 
 * @author Daedalus4096
 */
public class VanillaCustomStatRequirement extends AbstractRequirement<VanillaCustomStatRequirement> implements IVanillaStatRequirement {
    public static final Codec<VanillaCustomStatRequirement> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("item").forGetter(VanillaCustomStatRequirement::getStatValueLoc),
            Codec.INT.fieldOf("threshold").forGetter(VanillaCustomStatRequirement::getThreshold)
        ).apply(instance, VanillaCustomStatRequirement::new));
    
    protected final Stat<ResourceLocation> stat;
    protected final int threshold;
    
    public VanillaCustomStatRequirement(ResourceLocation loc, int threshold) {
        this(Stats.CUSTOM.get(loc), threshold);
    }
    
    protected VanillaCustomStatRequirement(Stat<ResourceLocation> stat, int threshold) {
        this.stat = stat;
        this.threshold = threshold;
    }

    @Override
    public Stat<?> getStat() {
        return this.stat;
    }

    @Override
    public ResourceLocation getStatTypeLoc() {
        return ForgeRegistries.STAT_TYPES.getKey(this.stat.getType());
    }
    
    @Override
    public ResourceLocation getStatValueLoc() {
        return this.stat.getValue();
    }

    @Override
    public int getThreshold() {
        return this.threshold;
    }
    
    @Override
    public Component getStatDescription() {
        String key = String.join(".", "stat", this.stat.getValue().getNamespace(), this.stat.getValue().getPath());
        return Component.translatable(key);
    }

    public int getCurrentValue(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            return serverPlayer.getStats().getValue(this.stat);
        } else if (FMLEnvironment.dist.isClient()) {
            return ClientUtils.getStatsCounter().getValue(this.stat);
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
    protected RequirementType<VanillaCustomStatRequirement> getType() {
        return RequirementsPM.VANILLA_CUSTOM_STAT.get();
    }
    
    @Nonnull
    public static VanillaCustomStatRequirement fromNetwork(FriendlyByteBuf buf) {
        return new VanillaCustomStatRequirement(buf.readResourceLocation(), buf.readVarInt());
    }

    @Override
    protected void toNetworkInner(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.getStatValueLoc());
        buf.writeVarInt(this.threshold);
    }
}
