package com.verdantartifice.primalmagick.common.spells.vehicles;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.registries.RegistryCodecs;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;
import com.verdantartifice.primalmagick.common.spells.SpellPropertyConfiguration;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Base class for a spell vehicle.  Handles property management and serialization.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractSpellVehicle<T extends AbstractSpellVehicle<T>> implements ISpellVehicle {
    public static Codec<AbstractSpellVehicle<?>> dispatchCodec() {
        return RegistryCodecs.codec(SpellVehiclesPM.TYPES).dispatch("mod_type", AbstractSpellVehicle::getType, SpellVehicleType::codec);
    }
    
    public static StreamCodec<RegistryFriendlyByteBuf, AbstractSpellVehicle<?>> dispatchStreamCodec() {
        return RegistryCodecs.registryFriendlyStreamCodec(SpellVehiclesPM.TYPES).dispatch(AbstractSpellVehicle::getType, SpellVehicleType::streamCodec);
    }
    
    public abstract SpellVehicleType<T> getType();

    /**
     * Get the type name for this spell vehicle.
     * 
     * @return the type name for this spell vehicle
     */
    protected abstract String getVehicleType();

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public Component getTypeName() {
        return Component.translatable("spells.primalmagick.vehicle." + this.getVehicleType() + ".type");
    }
    
    @Override
    public Component getDefaultNamePiece() {
        return Component.translatable("spells.primalmagick.vehicle." + this.getVehicleType() + ".default_name");
    }
    
    @Override
    public int getBaseManaCostModifier(SpellPropertyConfiguration properties) {
        // No change by default
        return 0;
    }

    @Override
    public int getManaCostMultiplier(SpellPropertyConfiguration properties) {
        // No multiplication by default
        return 1;
    }

    @Override
    public List<SpellProperty> getProperties() {
        // Sort properties by their display names
        return this.getPropertiesInner().stream().sorted(Comparator.comparing(SpellProperty::id)).collect(Collectors.toList());
    }

    protected abstract List<SpellProperty> getPropertiesInner();

    @Override
    public SpellProperty getProperty(ResourceLocation id) {
        return this.getPropertiesInner().stream().filter(prop -> prop.id().equals(id)).findFirst().orElse(null);
    }
}
