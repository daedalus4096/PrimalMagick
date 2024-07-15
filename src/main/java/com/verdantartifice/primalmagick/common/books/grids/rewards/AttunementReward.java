package com.verdantartifice.primalmagick.common.books.grids.rewards;

import java.util.Optional;

import com.google.common.base.Verify;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.attunements.AttunementManager;
import com.verdantartifice.primalmagick.common.attunements.AttunementType;
import com.verdantartifice.primalmagick.common.sources.Source;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

/**
 * Linguistics grid reward that grants permanent attunement to a source.
 * 
 * @author Daedalus4096
 */
public class AttunementReward extends AbstractReward<AttunementReward> {
    public static final MapCodec<AttunementReward> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Source.CODEC.fieldOf("source").forGetter(r -> r.source),
            Codec.INT.fieldOf("points").forGetter(r -> r.points)
        ).apply(instance, AttunementReward::new));
    public static final StreamCodec<ByteBuf, AttunementReward> STREAM_CODEC = StreamCodec.composite(
            Source.STREAM_CODEC, r -> r.source,
            ByteBufCodecs.VAR_INT, r -> r.points,
            AttunementReward::new);
    
    private Source source;
    private int points;
    private Optional<Component> pointsText = Optional.empty();

    protected AttunementReward(Source source, int points) {
        Verify.verifyNotNull(source, "Invalid source for attunement reward");
        this.source = source;
        this.setPoints(points);
    }
    
    @Override
    protected GridRewardType<AttunementReward> getType() {
        return GridRewardTypesPM.ATTUNEMENT.get();
    }

    protected void setPoints(int points) {
        this.points = points;
        this.pointsText = points > 1 ? Optional.of(Component.literal(Integer.toString(points))) : Optional.empty();
    }
    
    @Override
    public void grant(ServerPlayer player, RegistryAccess registryAccess) {
        AttunementManager.incrementAttunement(player, this.source, AttunementType.PERMANENT, this.points);
    }

    @Override
    public Component getDescription(Player player, RegistryAccess registryAccess) {
        Component amountText = Component.translatable("label.primalmagick.attunement_gain." + Mth.clamp(this.points, 0, 5));
        Component sourceText = this.source.isDiscovered(player) ? this.source.getNameText() : Component.translatable(Source.getUnknownTranslationKey());
        return Component.translatable("label.primalmagick.scribe_table.grid.reward.attunement", sourceText, amountText);
    }

    @Override
    public ResourceLocation getIconLocation(Player player) {
        return this.source.isDiscovered(player) ? this.source.getImage() : Source.getUnknownImage();
    }

    @Override
    public Optional<Component> getAmountText() {
        return this.pointsText;
    }
}
