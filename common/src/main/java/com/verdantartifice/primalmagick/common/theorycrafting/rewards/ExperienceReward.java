package com.verdantartifice.primalmagick.common.theorycrafting.rewards;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;

/**
 * Theorycrafting reward that grants experience points.
 * 
 * @author Daedalus4096
 */
public class ExperienceReward extends AbstractReward<ExperienceReward> {
    public static final MapCodec<ExperienceReward> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ExtraCodecs.POSITIVE_INT.fieldOf("points").forGetter(r -> r.points)
        ).apply(instance, ExperienceReward::new));
    
    public static final StreamCodec<ByteBuf, ExperienceReward> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            reward -> reward.points,
            ExperienceReward::new);
    
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
}
