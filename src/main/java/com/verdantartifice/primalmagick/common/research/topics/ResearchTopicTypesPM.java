package com.verdantartifice.primalmagick.common.research.topics;

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

public class ResearchTopicTypesPM {
    private static final DeferredRegister<ResearchTopicType<?>> DEFERRED_TYPES = DeferredRegister.create(RegistryKeysPM.RESEARCH_TOPIC_TYPES, PrimalMagick.MODID);
    public static final Supplier<IForgeRegistry<ResearchTopicType<?>>> TYPES = DEFERRED_TYPES.makeRegistry(RegistryBuilder::new);
    
    public static void init() {
        DEFERRED_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static final RegistryObject<ResearchTopicType<DisciplineResearchTopic>> DISCIPLINE = register("discipline", DisciplineResearchTopic.CODEC, DisciplineResearchTopic.STREAM_CODEC);
    
    protected static <T extends AbstractResearchTopic<T>> RegistryObject<ResearchTopicType<T>> register(String id, MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return DEFERRED_TYPES.register(id, () -> new ResearchTopicType<T>(PrimalMagick.resource(id), codec, streamCodec));
    }
}
