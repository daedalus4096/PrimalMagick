package com.verdantartifice.primalmagick.common.rituals.steps;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.common.tiles.rituals.RitualAltarTileEntity;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.BiFunction;

public class RitualStepTypesPM {
    public static final IRegistryItem<RitualStepType<?>, RitualStepType<OfferingRitualStep>> OFFERING = register("offering", OfferingRitualStep.CODEC, OfferingRitualStep.STREAM_CODEC, RitualAltarTileEntity::doOfferingStep);
    public static final IRegistryItem<RitualStepType<?>, RitualStepType<PropRitualStep>> PROP = register("prop", PropRitualStep.CODEC, PropRitualStep.STREAM_CODEC, RitualAltarTileEntity::doPropStep);
    public static final IRegistryItem<RitualStepType<?>, RitualStepType<UniversalRitualStep>> UNIVERSAL = register("universal", UniversalRitualStep.CODEC, UniversalRitualStep.STREAM_CODEC, RitualAltarTileEntity::doUniversalPropStep);
    
    protected static <T extends AbstractRitualStep<T>> IRegistryItem<RitualStepType<?>, RitualStepType<T>> register(String id,
            MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec,
            BiFunction<RitualAltarTileEntity, T, Boolean> action) {
        return Services.RITUAL_STEP_TYPES.register(id, () -> new RitualStepType<T>(ResourceUtils.loc(id), codec, streamCodec, action));
    }
}
