package com.verdantartifice.primalmagick.common.spells.payloads;

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

public class SpellPayloadsPM {
    private static final DeferredRegister<SpellPayloadType<?>> DEFERRED_TYPES = DeferredRegister.create(RegistryKeysPM.SPELL_PAYLOAD_TYPES, PrimalMagick.MODID);
    public static final Supplier<IForgeRegistry<SpellPayloadType<?>>> TYPES = DEFERRED_TYPES.makeRegistry(RegistryBuilder::new);
    
    public static void init() {
        DEFERRED_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<SpellPayloadType<EarthDamageSpellPayload>> EARTH_DAMAGE = register(EarthDamageSpellPayload.TYPE, EarthDamageSpellPayload.CODEC, EarthDamageSpellPayload.STREAM_CODEC);
    public static final RegistryObject<SpellPayloadType<FrostDamageSpellPayload>> FROST_DAMAGE = register(FrostDamageSpellPayload.TYPE, FrostDamageSpellPayload.CODEC, FrostDamageSpellPayload.STREAM_CODEC);
    public static final RegistryObject<SpellPayloadType<FlameDamageSpellPayload>> FLAME_DAMAGE = register(FlameDamageSpellPayload.TYPE, FlameDamageSpellPayload.CODEC, FlameDamageSpellPayload.STREAM_CODEC);
    
    protected static <T extends AbstractSpellPayload<T>> RegistryObject<SpellPayloadType<T>> register(String id, MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return DEFERRED_TYPES.register(id, () -> new SpellPayloadType<T>(PrimalMagick.resource(id), codec, streamCodec));
    }
}
