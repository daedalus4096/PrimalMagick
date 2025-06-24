package com.verdantartifice.primalmagick.common.rewards;

import com.google.common.base.Preconditions;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.attunements.AttunementManager;
import com.verdantartifice.primalmagick.common.attunements.AttunementType;
import com.verdantartifice.primalmagick.common.sources.Source;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;

/**
 * Reward that grants attunement to a magickal source.
 *
 * @author Daedalus4096
 */
public class AttunementReward extends AbstractReward<AttunementReward> {
    public static final MapCodec<AttunementReward> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Source.CODEC.fieldOf("source").forGetter(r -> r.source),
            ExtraCodecs.POSITIVE_INT.fieldOf("amount").forGetter(r -> r.amount)
        ).apply(instance, AttunementReward::new));

    public static final StreamCodec<ByteBuf, AttunementReward> STREAM_CODEC = StreamCodec.composite(
            Source.STREAM_CODEC, reward -> reward.source,
            ByteBufCodecs.VAR_INT, reward -> reward.amount,
            AttunementReward::new
        );

    private final Source source;
    private final int amount;

    public AttunementReward(Source source, int amount) {
        this.source = Preconditions.checkNotNull(source);
        this.amount = amount;
    }

    @Override
    public void grant(ServerPlayer player) {
        AttunementManager.incrementAttunement(player, this.source, AttunementType.PERMANENT, this.amount);
    }

    @Override
    public Component getDescription(Player player) {
        int amt = Mth.clamp(this.amount, 0, 5);
        Component labelText = source.isDiscovered(player) ?
                Component.translatable(source.getNameTranslationKey()) :
                Component.translatable(Source.getUnknownTranslationKey());
        Component amountText = Component.translatable("label.primalmagick.attunement_gain." + amt);
        return Component.translatable("label.primalmagick.attunement_gain.text", labelText, amountText);
    }

    @Override
    protected RewardType<AttunementReward> getType() {
        return RewardTypesPM.ATTUNEMENT.get();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AttunementReward that)) return false;
        return amount == that.amount && Objects.equals(source, that.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, amount);
    }
}
