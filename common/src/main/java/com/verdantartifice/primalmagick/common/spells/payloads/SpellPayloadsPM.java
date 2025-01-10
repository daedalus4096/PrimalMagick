package com.verdantartifice.primalmagick.common.spells.payloads;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Supplier;

public class SpellPayloadsPM {
    public static final IRegistryItem<SpellPayloadType<?>, SpellPayloadType<EmptySpellPayload>> EMPTY = register(EmptySpellPayload.TYPE, 100, EmptySpellPayload::getInstance, EmptySpellPayload::getRequirement, EmptySpellPayload.CODEC, EmptySpellPayload.STREAM_CODEC);
    
    public static final IRegistryItem<SpellPayloadType<?>, SpellPayloadType<EarthDamageSpellPayload>> EARTH_DAMAGE = register(EarthDamageSpellPayload.TYPE, 200, EarthDamageSpellPayload::getInstance, EarthDamageSpellPayload::getRequirement, EarthDamageSpellPayload.CODEC, EarthDamageSpellPayload.STREAM_CODEC);
    public static final IRegistryItem<SpellPayloadType<?>, SpellPayloadType<FrostDamageSpellPayload>> FROST_DAMAGE = register(FrostDamageSpellPayload.TYPE, 300, FrostDamageSpellPayload::getInstance, FrostDamageSpellPayload::getRequirement, FrostDamageSpellPayload.CODEC, FrostDamageSpellPayload.STREAM_CODEC);
    public static final IRegistryItem<SpellPayloadType<?>, SpellPayloadType<LightningDamageSpellPayload>> LIGHTNING_DAMAGE = register(LightningDamageSpellPayload.TYPE, 400, LightningDamageSpellPayload::getInstance, LightningDamageSpellPayload::getRequirement, LightningDamageSpellPayload.CODEC, LightningDamageSpellPayload.STREAM_CODEC);
    public static final IRegistryItem<SpellPayloadType<?>, SpellPayloadType<SolarDamageSpellPayload>> SOLAR_DAMAGE = register(SolarDamageSpellPayload.TYPE, 500, SolarDamageSpellPayload::getInstance, SolarDamageSpellPayload::getRequirement, SolarDamageSpellPayload.CODEC, SolarDamageSpellPayload.STREAM_CODEC);
    public static final IRegistryItem<SpellPayloadType<?>, SpellPayloadType<LunarDamageSpellPayload>> LUNAR_DAMAGE = register(LunarDamageSpellPayload.TYPE, 600, LunarDamageSpellPayload::getInstance, LunarDamageSpellPayload::getRequirement, LunarDamageSpellPayload.CODEC, LunarDamageSpellPayload.STREAM_CODEC);
    public static final IRegistryItem<SpellPayloadType<?>, SpellPayloadType<BloodDamageSpellPayload>> BLOOD_DAMAGE = register(BloodDamageSpellPayload.TYPE, 700, BloodDamageSpellPayload::getInstance, BloodDamageSpellPayload::getRequirement, BloodDamageSpellPayload.CODEC, BloodDamageSpellPayload.STREAM_CODEC);
    public static final IRegistryItem<SpellPayloadType<?>, SpellPayloadType<FlameDamageSpellPayload>> FLAME_DAMAGE = register(FlameDamageSpellPayload.TYPE, 800, FlameDamageSpellPayload::getInstance, FlameDamageSpellPayload::getRequirement, FlameDamageSpellPayload.CODEC, FlameDamageSpellPayload.STREAM_CODEC);
    public static final IRegistryItem<SpellPayloadType<?>, SpellPayloadType<VoidDamageSpellPayload>> VOID_DAMAGE = register(VoidDamageSpellPayload.TYPE, 900, VoidDamageSpellPayload::getInstance, VoidDamageSpellPayload::getRequirement, VoidDamageSpellPayload.CODEC, VoidDamageSpellPayload.STREAM_CODEC);
    public static final IRegistryItem<SpellPayloadType<?>, SpellPayloadType<HolyDamageSpellPayload>> HOLY_DAMAGE = register(HolyDamageSpellPayload.TYPE, 1000, HolyDamageSpellPayload::getInstance, HolyDamageSpellPayload::getRequirement, HolyDamageSpellPayload.CODEC, HolyDamageSpellPayload.STREAM_CODEC);
    
    public static final IRegistryItem<SpellPayloadType<?>, SpellPayloadType<BreakSpellPayload>> BREAK = register(BreakSpellPayload.TYPE, 1100, BreakSpellPayload::getInstance, BreakSpellPayload::getRequirement, BreakSpellPayload.CODEC, BreakSpellPayload.STREAM_CODEC);
    public static final IRegistryItem<SpellPayloadType<?>, SpellPayloadType<ConjureStoneSpellPayload>> CONJURE_STONE = register(ConjureStoneSpellPayload.TYPE, 1200, ConjureStoneSpellPayload::getInstance, ConjureStoneSpellPayload::getRequirement, ConjureStoneSpellPayload.CODEC, ConjureStoneSpellPayload.STREAM_CODEC);
    public static final IRegistryItem<SpellPayloadType<?>, SpellPayloadType<ConjureWaterSpellPayload>> CONJURE_WATER = register(ConjureWaterSpellPayload.TYPE, 1300, ConjureWaterSpellPayload::getInstance, ConjureWaterSpellPayload::getRequirement, ConjureWaterSpellPayload.CODEC, ConjureWaterSpellPayload.STREAM_CODEC);
    public static final IRegistryItem<SpellPayloadType<?>, SpellPayloadType<ShearSpellPayload>> SHEAR = register(ShearSpellPayload.TYPE, 1400, ShearSpellPayload::getInstance, ShearSpellPayload::getRequirement, ShearSpellPayload.CODEC, ShearSpellPayload.STREAM_CODEC);
    public static final IRegistryItem<SpellPayloadType<?>, SpellPayloadType<FlightSpellPayload>> FLIGHT = register(FlightSpellPayload.TYPE, 1500, FlightSpellPayload::getInstance, FlightSpellPayload::getRequirement, FlightSpellPayload.CODEC, FlightSpellPayload.STREAM_CODEC);
    public static final IRegistryItem<SpellPayloadType<?>, SpellPayloadType<HealingSpellPayload>> HEALING = register(HealingSpellPayload.TYPE, 1600, HealingSpellPayload::getInstance, HealingSpellPayload::getRequirement, HealingSpellPayload.CODEC, HealingSpellPayload.STREAM_CODEC);
    public static final IRegistryItem<SpellPayloadType<?>, SpellPayloadType<ConjureLightSpellPayload>> CONJURE_LIGHT = register(ConjureLightSpellPayload.TYPE, 1700, ConjureLightSpellPayload::getInstance, ConjureLightSpellPayload::getRequirement, ConjureLightSpellPayload.CODEC, ConjureLightSpellPayload.STREAM_CODEC);
    public static final IRegistryItem<SpellPayloadType<?>, SpellPayloadType<PolymorphWolfSpellPayload>> POLYMORPH_WOLF = register(PolymorphWolfSpellPayload.TYPE, 1800, PolymorphWolfSpellPayload::getInstance, PolymorphWolfSpellPayload::getRequirement, PolymorphWolfSpellPayload.CODEC, PolymorphWolfSpellPayload.STREAM_CODEC);
    public static final IRegistryItem<SpellPayloadType<?>, SpellPayloadType<PolymorphSheepSpellPayload>> POLYMORPH_SHEEP = register(PolymorphSheepSpellPayload.TYPE, 1900, PolymorphSheepSpellPayload::getInstance, PolymorphSheepSpellPayload::getRequirement, PolymorphSheepSpellPayload.CODEC, PolymorphSheepSpellPayload.STREAM_CODEC);
    public static final IRegistryItem<SpellPayloadType<?>, SpellPayloadType<ConjureAnimalSpellPayload>> CONJURE_ANIMAL = register(ConjureAnimalSpellPayload.TYPE, 2000, ConjureAnimalSpellPayload::getInstance, ConjureAnimalSpellPayload::getRequirement, ConjureAnimalSpellPayload.CODEC, ConjureAnimalSpellPayload.STREAM_CODEC);
    public static final IRegistryItem<SpellPayloadType<?>, SpellPayloadType<ConjureLavaSpellPayload>> CONJURE_LAVA = register(ConjureLavaSpellPayload.TYPE, 2100, ConjureLavaSpellPayload::getInstance, ConjureLavaSpellPayload::getRequirement, ConjureLavaSpellPayload.CODEC, ConjureLavaSpellPayload.STREAM_CODEC);
    public static final IRegistryItem<SpellPayloadType<?>, SpellPayloadType<DrainSoulSpellPayload>> DRAIN_SOUL = register(DrainSoulSpellPayload.TYPE, 2200, DrainSoulSpellPayload::getInstance, DrainSoulSpellPayload::getRequirement, DrainSoulSpellPayload.CODEC, DrainSoulSpellPayload.STREAM_CODEC);
    public static final IRegistryItem<SpellPayloadType<?>, SpellPayloadType<TeleportSpellPayload>> TELEPORT = register(TeleportSpellPayload.TYPE, 2300, TeleportSpellPayload::getInstance, TeleportSpellPayload::getRequirement, TeleportSpellPayload.CODEC, TeleportSpellPayload.STREAM_CODEC);
    public static final IRegistryItem<SpellPayloadType<?>, SpellPayloadType<ConsecrateSpellPayload>> CONSECRATE = register(ConsecrateSpellPayload.TYPE, 2400, ConsecrateSpellPayload::getInstance, ConsecrateSpellPayload::getRequirement, ConsecrateSpellPayload.CODEC, ConsecrateSpellPayload.STREAM_CODEC);
    
    protected static <T extends AbstractSpellPayload<T>> IRegistryItem<SpellPayloadType<?>, SpellPayloadType<T>> register(String id, int sortOrder, Supplier<T> instanceSupplier, Supplier<AbstractRequirement<?>> requirementSupplier, MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return Services.SPELL_PAYLOAD_TYPES_REGISTRY.register(id, () -> new SpellPayloadType<T>(ResourceUtils.loc(id), sortOrder, instanceSupplier, requirementSupplier, codec, streamCodec));
    }
}
