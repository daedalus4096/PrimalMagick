package com.verdantartifice.primalmagick.common.affinities;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PotionBonusAffinity extends AbstractAffinity<PotionBonusAffinity> {
    public static final MapCodec<PotionBonusAffinity> CODEC = RecordCodecBuilder.<PotionBonusAffinity>mapCodec(instance -> instance.group(
            Identifier.CODEC.fieldOf("target").forGetter(PotionBonusAffinity::getTarget),
            SourceList.CODEC.fieldOf("bonus").forGetter(pba -> pba.bonusValues)
        ).apply(instance, PotionBonusAffinity::new)).validate(pba -> BuiltInRegistries.POTION.containsKey(pba.targetId) ?
            DataResult.success(pba) :
            DataResult.error(() -> "Unknown target potion type " + pba.targetId
        ));

    public static final StreamCodec<FriendlyByteBuf, PotionBonusAffinity> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC, PotionBonusAffinity::getTarget,
            SourceList.STREAM_CODEC, pba -> pba.bonusValues,
            PotionBonusAffinity::new);

    protected final SourceList bonusValues;
    
    protected PotionBonusAffinity(@NotNull Identifier target, @NotNull SourceList bonusValues) {
        super(target);
        this.bonusValues = bonusValues;
    }

    @Override
    public @NotNull AffinityType<PotionBonusAffinity> getType() {
        return AffinityTypesPM.POTION_BONUS.get();
    }

    @Override
    protected CompletableFuture<SourceList> calculateTotalAsync(@NotNull Level level, @NotNull List<Identifier> history) {
        return CompletableFuture.completedFuture(this.bonusValues);
    }
}
