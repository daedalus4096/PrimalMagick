package com.verdantartifice.primalmagick.common.theorycrafting.rewards;

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

public class RewardTypesPM {
    private static final DeferredRegister<RewardType<?>> DEFERRED_TYPES = DeferredRegister.create(RegistryKeysPM.PROJECT_REWARD_TYPES, PrimalMagick.MODID);
    public static final Supplier<IForgeRegistry<RewardType<?>>> TYPES = DEFERRED_TYPES.makeRegistry(RegistryBuilder::new);
    
    public static void init() {
        DEFERRED_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static final RegistryObject<RewardType<ExperienceReward>> EXPERIENCE = register("experience", ExperienceReward.CODEC, ExperienceReward.STREAM_CODEC);
    public static final RegistryObject<RewardType<ItemReward>> ITEM = register("item", ItemReward.CODEC, ItemReward.STREAM_CODEC);
    public static final RegistryObject<RewardType<LootTableReward>> LOOT_TABLE = register("loot_table", LootTableReward.CODEC, LootTableReward.STREAM_CODEC);

    protected static <T extends AbstractReward<T>> RegistryObject<RewardType<T>> register(String id, MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return DEFERRED_TYPES.register(id, () -> new RewardType<T>(PrimalMagick.resource(id), codec, streamCodec));
    }
}
