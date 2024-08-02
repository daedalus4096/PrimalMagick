package com.verdantartifice.primalmagick.common.theorycrafting.weights;

import java.util.function.Supplier;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

public class WeightFunctionTypesPM {
    private static final DeferredRegister<WeightFunctionType<?>> DEFERRED_TYPES = DeferredRegister.create(RegistryKeysPM.PROJECT_WEIGHT_FUNCTION_TYPES, PrimalMagick.MODID);
    public static final Supplier<IForgeRegistry<WeightFunctionType<?>>> TYPES = DEFERRED_TYPES.makeRegistry(RegistryBuilder::new);
    
    public static void init() {
        DEFERRED_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static final RegistryObject<WeightFunctionType<ConstantWeight>> CONSTANT = register("constant", ConstantWeight.CODEC, ConstantWeight.STREAM_CODEC);
    public static final RegistryObject<WeightFunctionType<ProgressiveWeight>> PROGRESSIVE = register("progressive", ProgressiveWeight.CODEC, ProgressiveWeight.STREAM_CODEC);

    protected static <T extends AbstractWeightFunction<T>> RegistryObject<WeightFunctionType<T>> register(String id, MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return DEFERRED_TYPES.register(id, () -> new WeightFunctionType<T>(PrimalMagick.resource(id), codec, streamCodec));
    }
}
