package com.verdantartifice.primalmagick.common.affinities;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EnchantmentBonusAffinity extends AbstractAffinity<EnchantmentBonusAffinity> {
    // FIXME Validate that target enchantment exists
    public static final MapCodec<EnchantmentBonusAffinity> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Identifier.CODEC.fieldOf("target").forGetter(EnchantmentBonusAffinity::getTarget),
            SourceList.CODEC.fieldOf("multiplier").forGetter(eba -> eba.multiplierValues)
        ).apply(instance, EnchantmentBonusAffinity::new));

    public static final StreamCodec<FriendlyByteBuf, EnchantmentBonusAffinity> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC, EnchantmentBonusAffinity::getTarget,
            SourceList.STREAM_CODEC, eba -> eba.multiplierValues,
            EnchantmentBonusAffinity::new);
    
    protected final SourceList multiplierValues;
    
    protected EnchantmentBonusAffinity(@NotNull Identifier target, @NotNull SourceList multiplierValues) {
        super(target);
        this.multiplierValues = multiplierValues;
    }

    @Override
    public @NotNull AffinityType<EnchantmentBonusAffinity> getType() {
        return AffinityTypesPM.ENCHANTMENT_BONUS.get();
    }

    @Override
    protected CompletableFuture<SourceList> calculateTotalAsync(@NotNull Level level, @NotNull List<Identifier> history) {
        return CompletableFuture.completedFuture(this.multiplierValues);
    }
}
