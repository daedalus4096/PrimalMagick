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
    public static final RegistryObject<SpellPayloadType<LightningDamageSpellPayload>> LIGHTNING_DAMAGE = register(LightningDamageSpellPayload.TYPE, LightningDamageSpellPayload.CODEC, LightningDamageSpellPayload.STREAM_CODEC);
    public static final RegistryObject<SpellPayloadType<SolarDamageSpellPayload>> SOLAR_DAMAGE = register(SolarDamageSpellPayload.TYPE, SolarDamageSpellPayload.CODEC, SolarDamageSpellPayload.STREAM_CODEC);
    public static final RegistryObject<SpellPayloadType<LunarDamageSpellPayload>> LUNAR_DAMAGE = register(LunarDamageSpellPayload.TYPE, LunarDamageSpellPayload.CODEC, LunarDamageSpellPayload.STREAM_CODEC);
    public static final RegistryObject<SpellPayloadType<BloodDamageSpellPayload>> BLOOD_DAMAGE = register(BloodDamageSpellPayload.TYPE, BloodDamageSpellPayload.CODEC, BloodDamageSpellPayload.STREAM_CODEC);
    public static final RegistryObject<SpellPayloadType<FlameDamageSpellPayload>> FLAME_DAMAGE = register(FlameDamageSpellPayload.TYPE, FlameDamageSpellPayload.CODEC, FlameDamageSpellPayload.STREAM_CODEC);
    public static final RegistryObject<SpellPayloadType<VoidDamageSpellPayload>> VOID_DAMAGE = register(VoidDamageSpellPayload.TYPE, VoidDamageSpellPayload.CODEC, VoidDamageSpellPayload.STREAM_CODEC);
    public static final RegistryObject<SpellPayloadType<HolyDamageSpellPayload>> HOLY_DAMAGE = register(HolyDamageSpellPayload.TYPE, HolyDamageSpellPayload.CODEC, HolyDamageSpellPayload.STREAM_CODEC);
    
    public static final RegistryObject<SpellPayloadType<BreakSpellPayload>> BREAK = register(BreakSpellPayload.TYPE, BreakSpellPayload.CODEC, BreakSpellPayload.STREAM_CODEC);
    public static final RegistryObject<SpellPayloadType<ConjureStoneSpellPayload>> CONJURE_STONE = register(ConjureStoneSpellPayload.TYPE, ConjureStoneSpellPayload.CODEC, ConjureStoneSpellPayload.STREAM_CODEC);
    public static final RegistryObject<SpellPayloadType<ConjureWaterSpellPayload>> CONJURE_WATER = register(ConjureWaterSpellPayload.TYPE, ConjureWaterSpellPayload.CODEC, ConjureWaterSpellPayload.STREAM_CODEC);
    public static final RegistryObject<SpellPayloadType<ConjureLightSpellPayload>> CONJURE_LIGHT = register(ConjureLightSpellPayload.TYPE, ConjureLightSpellPayload.CODEC, ConjureLightSpellPayload.STREAM_CODEC);
    public static final RegistryObject<SpellPayloadType<PolymorphWolfSpellPayload>> POLYMORPH_WOLF = register(PolymorphWolfSpellPayload.TYPE, PolymorphWolfSpellPayload.CODEC, PolymorphWolfSpellPayload.STREAM_CODEC);
    public static final RegistryObject<SpellPayloadType<PolymorphSheepSpellPayload>> POLYMORPH_SHEEP = register(PolymorphSheepSpellPayload.TYPE, PolymorphSheepSpellPayload.CODEC, PolymorphSheepSpellPayload.STREAM_CODEC);
    public static final RegistryObject<SpellPayloadType<ConjureAnimalSpellPayload>> CONJURE_ANIMAL = register(ConjureAnimalSpellPayload.TYPE, ConjureAnimalSpellPayload.CODEC, ConjureAnimalSpellPayload.STREAM_CODEC);
    public static final RegistryObject<SpellPayloadType<ConjureLavaSpellPayload>> CONJURE_LAVA = register(ConjureLavaSpellPayload.TYPE, ConjureLavaSpellPayload.CODEC, ConjureLavaSpellPayload.STREAM_CODEC);
    public static final RegistryObject<SpellPayloadType<ConsecrateSpellPayload>> CONSECRATE = register(ConsecrateSpellPayload.TYPE, ConsecrateSpellPayload.CODEC, ConsecrateSpellPayload.STREAM_CODEC);
    
    protected static <T extends AbstractSpellPayload<T>> RegistryObject<SpellPayloadType<T>> register(String id, MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return DEFERRED_TYPES.register(id, () -> new SpellPayloadType<T>(PrimalMagick.resource(id), codec, streamCodec));
    }
}
