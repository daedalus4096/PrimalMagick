package com.verdantartifice.primalmagick.common.spells.payloads;

import java.util.function.Supplier;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

public class SpellPayloadsPM {
    private static final DeferredRegister<SpellPayloadType<?>> DEFERRED_TYPES = DeferredRegister.create(RegistryKeysPM.SPELL_PAYLOAD_TYPES, Constants.MOD_ID);
    public static final Supplier<IForgeRegistry<SpellPayloadType<?>>> TYPES = DEFERRED_TYPES.makeRegistry(RegistryBuilder::new);
    
    public static void init() {
        DEFERRED_TYPES.register(PrimalMagick.getModLoadingContext().getModEventBus());
    }
    
    public static final RegistryObject<SpellPayloadType<EmptySpellPayload>> EMPTY = register(EmptySpellPayload.TYPE, 100, EmptySpellPayload::getInstance, EmptySpellPayload::getRequirement, EmptySpellPayload.CODEC, EmptySpellPayload.STREAM_CODEC);
    
    public static final RegistryObject<SpellPayloadType<EarthDamageSpellPayload>> EARTH_DAMAGE = register(EarthDamageSpellPayload.TYPE, 200, EarthDamageSpellPayload::getInstance, EarthDamageSpellPayload::getRequirement, EarthDamageSpellPayload.CODEC, EarthDamageSpellPayload.STREAM_CODEC);
    public static final RegistryObject<SpellPayloadType<FrostDamageSpellPayload>> FROST_DAMAGE = register(FrostDamageSpellPayload.TYPE, 300, FrostDamageSpellPayload::getInstance, FrostDamageSpellPayload::getRequirement, FrostDamageSpellPayload.CODEC, FrostDamageSpellPayload.STREAM_CODEC);
    public static final RegistryObject<SpellPayloadType<LightningDamageSpellPayload>> LIGHTNING_DAMAGE = register(LightningDamageSpellPayload.TYPE, 400, LightningDamageSpellPayload::getInstance, LightningDamageSpellPayload::getRequirement, LightningDamageSpellPayload.CODEC, LightningDamageSpellPayload.STREAM_CODEC);
    public static final RegistryObject<SpellPayloadType<SolarDamageSpellPayload>> SOLAR_DAMAGE = register(SolarDamageSpellPayload.TYPE, 500, SolarDamageSpellPayload::getInstance, SolarDamageSpellPayload::getRequirement, SolarDamageSpellPayload.CODEC, SolarDamageSpellPayload.STREAM_CODEC);
    public static final RegistryObject<SpellPayloadType<LunarDamageSpellPayload>> LUNAR_DAMAGE = register(LunarDamageSpellPayload.TYPE, 600, LunarDamageSpellPayload::getInstance, LunarDamageSpellPayload::getRequirement, LunarDamageSpellPayload.CODEC, LunarDamageSpellPayload.STREAM_CODEC);
    public static final RegistryObject<SpellPayloadType<BloodDamageSpellPayload>> BLOOD_DAMAGE = register(BloodDamageSpellPayload.TYPE, 700, BloodDamageSpellPayload::getInstance, BloodDamageSpellPayload::getRequirement, BloodDamageSpellPayload.CODEC, BloodDamageSpellPayload.STREAM_CODEC);
    public static final RegistryObject<SpellPayloadType<FlameDamageSpellPayload>> FLAME_DAMAGE = register(FlameDamageSpellPayload.TYPE, 800, FlameDamageSpellPayload::getInstance, FlameDamageSpellPayload::getRequirement, FlameDamageSpellPayload.CODEC, FlameDamageSpellPayload.STREAM_CODEC);
    public static final RegistryObject<SpellPayloadType<VoidDamageSpellPayload>> VOID_DAMAGE = register(VoidDamageSpellPayload.TYPE, 900, VoidDamageSpellPayload::getInstance, VoidDamageSpellPayload::getRequirement, VoidDamageSpellPayload.CODEC, VoidDamageSpellPayload.STREAM_CODEC);
    public static final RegistryObject<SpellPayloadType<HolyDamageSpellPayload>> HOLY_DAMAGE = register(HolyDamageSpellPayload.TYPE, 1000, HolyDamageSpellPayload::getInstance, HolyDamageSpellPayload::getRequirement, HolyDamageSpellPayload.CODEC, HolyDamageSpellPayload.STREAM_CODEC);
    
    public static final RegistryObject<SpellPayloadType<BreakSpellPayload>> BREAK = register(BreakSpellPayload.TYPE, 1100, BreakSpellPayload::getInstance, BreakSpellPayload::getRequirement, BreakSpellPayload.CODEC, BreakSpellPayload.STREAM_CODEC);
    public static final RegistryObject<SpellPayloadType<ConjureStoneSpellPayload>> CONJURE_STONE = register(ConjureStoneSpellPayload.TYPE, 1200, ConjureStoneSpellPayload::getInstance, ConjureStoneSpellPayload::getRequirement, ConjureStoneSpellPayload.CODEC, ConjureStoneSpellPayload.STREAM_CODEC);
    public static final RegistryObject<SpellPayloadType<ConjureWaterSpellPayload>> CONJURE_WATER = register(ConjureWaterSpellPayload.TYPE, 1300, ConjureWaterSpellPayload::getInstance, ConjureWaterSpellPayload::getRequirement, ConjureWaterSpellPayload.CODEC, ConjureWaterSpellPayload.STREAM_CODEC);
    public static final RegistryObject<SpellPayloadType<ShearSpellPayload>> SHEAR = register(ShearSpellPayload.TYPE, 1400, ShearSpellPayload::getInstance, ShearSpellPayload::getRequirement, ShearSpellPayload.CODEC, ShearSpellPayload.STREAM_CODEC);
    public static final RegistryObject<SpellPayloadType<FlightSpellPayload>> FLIGHT = register(FlightSpellPayload.TYPE, 1500, FlightSpellPayload::getInstance, FlightSpellPayload::getRequirement, FlightSpellPayload.CODEC, FlightSpellPayload.STREAM_CODEC);
    public static final RegistryObject<SpellPayloadType<HealingSpellPayload>> HEALING = register(HealingSpellPayload.TYPE, 1600, HealingSpellPayload::getInstance, HealingSpellPayload::getRequirement, HealingSpellPayload.CODEC, HealingSpellPayload.STREAM_CODEC);
    public static final RegistryObject<SpellPayloadType<ConjureLightSpellPayload>> CONJURE_LIGHT = register(ConjureLightSpellPayload.TYPE, 1700, ConjureLightSpellPayload::getInstance, ConjureLightSpellPayload::getRequirement, ConjureLightSpellPayload.CODEC, ConjureLightSpellPayload.STREAM_CODEC);
    public static final RegistryObject<SpellPayloadType<PolymorphWolfSpellPayload>> POLYMORPH_WOLF = register(PolymorphWolfSpellPayload.TYPE, 1800, PolymorphWolfSpellPayload::getInstance, PolymorphWolfSpellPayload::getRequirement, PolymorphWolfSpellPayload.CODEC, PolymorphWolfSpellPayload.STREAM_CODEC);
    public static final RegistryObject<SpellPayloadType<PolymorphSheepSpellPayload>> POLYMORPH_SHEEP = register(PolymorphSheepSpellPayload.TYPE, 1900, PolymorphSheepSpellPayload::getInstance, PolymorphSheepSpellPayload::getRequirement, PolymorphSheepSpellPayload.CODEC, PolymorphSheepSpellPayload.STREAM_CODEC);
    public static final RegistryObject<SpellPayloadType<ConjureAnimalSpellPayload>> CONJURE_ANIMAL = register(ConjureAnimalSpellPayload.TYPE, 2000, ConjureAnimalSpellPayload::getInstance, ConjureAnimalSpellPayload::getRequirement, ConjureAnimalSpellPayload.CODEC, ConjureAnimalSpellPayload.STREAM_CODEC);
    public static final RegistryObject<SpellPayloadType<ConjureLavaSpellPayload>> CONJURE_LAVA = register(ConjureLavaSpellPayload.TYPE, 2100, ConjureLavaSpellPayload::getInstance, ConjureLavaSpellPayload::getRequirement, ConjureLavaSpellPayload.CODEC, ConjureLavaSpellPayload.STREAM_CODEC);
    public static final RegistryObject<SpellPayloadType<DrainSoulSpellPayload>> DRAIN_SOUL = register(DrainSoulSpellPayload.TYPE, 2200, DrainSoulSpellPayload::getInstance, DrainSoulSpellPayload::getRequirement, DrainSoulSpellPayload.CODEC, DrainSoulSpellPayload.STREAM_CODEC);
    public static final RegistryObject<SpellPayloadType<TeleportSpellPayload>> TELEPORT = register(TeleportSpellPayload.TYPE, 2300, TeleportSpellPayload::getInstance, TeleportSpellPayload::getRequirement, TeleportSpellPayload.CODEC, TeleportSpellPayload.STREAM_CODEC);
    public static final RegistryObject<SpellPayloadType<ConsecrateSpellPayload>> CONSECRATE = register(ConsecrateSpellPayload.TYPE, 2400, ConsecrateSpellPayload::getInstance, ConsecrateSpellPayload::getRequirement, ConsecrateSpellPayload.CODEC, ConsecrateSpellPayload.STREAM_CODEC);
    
    protected static <T extends AbstractSpellPayload<T>> RegistryObject<SpellPayloadType<T>> register(String id, int sortOrder, Supplier<T> instanceSupplier, Supplier<AbstractRequirement<?>> requirementSupplier, MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return DEFERRED_TYPES.register(id, () -> new SpellPayloadType<T>(ResourceUtils.loc(id), sortOrder, instanceSupplier, requirementSupplier, codec, streamCodec));
    }
}
