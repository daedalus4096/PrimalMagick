package com.verdantartifice.primalmagick.common.spells.vehicles;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class SpellVehiclesPM {
    public static final IRegistryItem<SpellVehicleType<?>, SpellVehicleType<EmptySpellVehicle>> EMPTY = register(EmptySpellVehicle.TYPE, 100, EmptySpellVehicle::getInstance, EmptySpellVehicle::getRequirement, EmptySpellVehicle.CODEC, EmptySpellVehicle.STREAM_CODEC);
    public static final IRegistryItem<SpellVehicleType<?>, SpellVehicleType<TouchSpellVehicle>> TOUCH = register(TouchSpellVehicle.TYPE, 200, TouchSpellVehicle::getInstance, TouchSpellVehicle::getRequirement, TouchSpellVehicle.CODEC, TouchSpellVehicle.STREAM_CODEC);
    public static final IRegistryItem<SpellVehicleType<?>, SpellVehicleType<ProjectileSpellVehicle>> PROJECTILE = register(ProjectileSpellVehicle.TYPE, 300, ProjectileSpellVehicle::getInstance, ProjectileSpellVehicle::getRequirement, ProjectileSpellVehicle.CODEC, ProjectileSpellVehicle.STREAM_CODEC);
    public static final IRegistryItem<SpellVehicleType<?>, SpellVehicleType<BoltSpellVehicle>> BOLT = register(BoltSpellVehicle.TYPE, 400, BoltSpellVehicle::getInstance, BoltSpellVehicle::getRequirement, BoltSpellVehicle.CODEC, BoltSpellVehicle.STREAM_CODEC);
    public static final IRegistryItem<SpellVehicleType<?>, SpellVehicleType<SelfSpellVehicle>> SELF = register(SelfSpellVehicle.TYPE, 500, SelfSpellVehicle::getInstance, SelfSpellVehicle::getRequirement, SelfSpellVehicle.CODEC, SelfSpellVehicle.STREAM_CODEC);
    
    protected static <T extends AbstractSpellVehicle<T>> IRegistryItem<SpellVehicleType<?>, SpellVehicleType<T>> register(String id,
            int sortOrder, Supplier<T> instanceSupplier, Supplier<AbstractRequirement<?>> requirementSupplier, MapCodec<T> codec,
            StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return Services.SPELL_VEHICLE_TYPES.register(id, () -> new SpellVehicleType<T>(ResourceUtils.loc(id), sortOrder, instanceSupplier, requirementSupplier, codec, streamCodec));
    }
}
