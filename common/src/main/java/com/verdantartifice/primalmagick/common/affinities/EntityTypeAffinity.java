package com.verdantartifice.primalmagick.common.affinities;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EntityTypeAffinity extends AbstractAffinity<EntityTypeAffinity> {
    public static final MapCodec<EntityTypeAffinity> CODEC = RecordCodecBuilder.<EntityTypeAffinity>mapCodec(instance -> instance.group(
            Identifier.CODEC.fieldOf("target").forGetter(EntityTypeAffinity::getTarget),
            SourceList.CODEC.fieldOf("values").forGetter(eta -> eta.values)
        ).apply(instance, EntityTypeAffinity::new)).validate(eta -> Services.ENTITY_TYPES_REGISTRY.containsKey(eta.targetId) ?
            DataResult.success(eta) :
            DataResult.error(() -> "Unknown target entity type " + eta.targetId
        ));

    public static final StreamCodec<FriendlyByteBuf, EntityTypeAffinity> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC, EntityTypeAffinity::getTarget,
            SourceList.STREAM_CODEC, eta -> eta.values,
            EntityTypeAffinity::new);
    
    protected final SourceList values;
    
    protected EntityTypeAffinity(@NotNull Identifier target, @NotNull SourceList values) {
        super(target);
        this.values = values;
    }

    @Override
    public @NotNull AffinityType<EntityTypeAffinity> getType() {
        return AffinityTypesPM.ENTITY_TYPE.get();
    }

    @Override
    protected CompletableFuture<SourceList> calculateTotalAsync(@NotNull Level level, @NotNull List<Identifier> history) {
        return CompletableFuture.completedFuture(this.values);
    }
}
