package com.verdantartifice.primalmagick.common.theorycrafting.weights;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class WeightFunctionTypesPM {
    public static void init() {
        // Pass the service initialization through this class so it gets class loaded and fields registered
        Services.WEIGHT_FUNCTION_TYPES_REGISTRY.init();
    }

    public static final IRegistryItem<WeightFunctionType<?>, WeightFunctionType<ConstantWeight>> CONSTANT = register("constant", ConstantWeight.CODEC, ConstantWeight.STREAM_CODEC);
    public static final IRegistryItem<WeightFunctionType<?>, WeightFunctionType<ProgressiveWeight>> PROGRESSIVE = register("progressive", ProgressiveWeight.CODEC, ProgressiveWeight.STREAM_CODEC);

    protected static <T extends AbstractWeightFunction<T>> IRegistryItem<WeightFunctionType<?>, WeightFunctionType<T>> register(String id, MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return Services.WEIGHT_FUNCTION_TYPES_REGISTRY.register(id, () -> new WeightFunctionType<>(ResourceUtils.loc(id), codec, streamCodec));
    }
}
