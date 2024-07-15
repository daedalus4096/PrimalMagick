package com.verdantartifice.primalmagick.common.books.grids.rewards;

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

public class GridRewardTypesPM {
    private static final DeferredRegister<GridRewardType<?>> DEFERRED_TYPES = DeferredRegister.create(RegistryKeysPM.GRID_REWARD_TYPES, PrimalMagick.MODID);
    public static final Supplier<IForgeRegistry<GridRewardType<?>>> TYPES = DEFERRED_TYPES.makeRegistry(RegistryBuilder::new);
    
    public static void init() {
        DEFERRED_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static final RegistryObject<GridRewardType<AttunementReward>> ATTUNEMENT = register("attunement", AttunementReward.CODEC, AttunementReward.STREAM_CODEC);
    
    protected static <T extends AbstractReward<T>> RegistryObject<GridRewardType<T>> register(String id, MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return DEFERRED_TYPES.register(id, () -> new GridRewardType<T>(PrimalMagick.resource(id), codec, streamCodec));
    }
}
