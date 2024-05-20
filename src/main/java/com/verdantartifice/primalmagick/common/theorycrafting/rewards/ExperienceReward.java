package com.verdantartifice.primalmagick.common.theorycrafting.rewards;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;

/**
 * Theorycrafting reward that grants experience points.
 * 
 * @author Daedalus4096
 */
public class ExperienceReward extends AbstractReward<ExperienceReward> {
    public static final Codec<ExperienceReward> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.POSITIVE_INT.fieldOf("points").forGetter(r -> r.points)
        ).apply(instance, ExperienceReward::new));
    
    private final int points;
    
    public ExperienceReward(int points) {
        this.points = points;
    }

    @Override
    protected RewardType<ExperienceReward> getType() {
        return RewardTypesPM.EXPERIENCE.get();
    }

    @Override
    public void grant(ServerPlayer player) {
        player.giveExperiencePoints(this.points);
    }

    @Override
    public Component getDescription() {
        Component label = Component.translatable("label.primalmagick.experience.points");
        return Component.translatable("label.primalmagick.research_table.reward", this.points, label);
    }

    @Nonnull
    public static ExperienceReward fromNetworkInner(FriendlyByteBuf buf) {
        return new ExperienceReward(buf.readVarInt());
    }

    @Override
    protected void toNetworkInner(FriendlyByteBuf buf) {
        buf.writeVarInt(this.points);
    }
}
