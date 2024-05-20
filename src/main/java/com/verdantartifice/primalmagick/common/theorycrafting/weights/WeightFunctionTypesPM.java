package com.verdantartifice.primalmagick.common.theorycrafting.weights;

import java.util.function.Supplier;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import net.minecraft.network.FriendlyByteBuf;
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
    
    public static final RegistryObject<WeightFunctionType<ConstantWeight>> CONSTANT = register("constant", ConstantWeight.CODEC, ConstantWeight::fromNetworkInner);

    protected static <T extends AbstractWeightFunction<T>> RegistryObject<WeightFunctionType<T>> register(String id, Codec<T> codec, FriendlyByteBuf.Reader<T> networkReader) {
        return DEFERRED_TYPES.register(id, () -> new WeightFunctionType<T>(PrimalMagick.resource(id), codec, networkReader));
    }
}
