package com.verdantartifice.primalmagick.common.spells;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.spells.mods.AbstractSpellMod;
import com.verdantartifice.primalmagick.common.spells.mods.ConfiguredSpellMod;
import com.verdantartifice.primalmagick.common.spells.mods.SpellModType;
import com.verdantartifice.primalmagick.common.spells.mods.SpellModsPM;
import com.verdantartifice.primalmagick.common.spells.payloads.ConfiguredSpellPayload;
import com.verdantartifice.primalmagick.common.spells.vehicles.ConfiguredSpellVehicle;
import com.verdantartifice.primalmagick.common.stats.ExpertiseManager;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Definition of a spell package data structure.  A spell package represents a complete, named, castable
 * spell.  A valid package has a name, a vehicle component, a payload component, and zero to two mod 
 * components.  For more information on spell components, see their respective interfaces.
 * 
 * @author Daedalus4096
 * @see com.verdantartifice.primalmagick.common.spells.vehicles.ISpellVehicle
 * @see com.verdantartifice.primalmagick.common.spells.payloads.ISpellPayload
 * @see com.verdantartifice.primalmagick.common.spells.mods.ISpellMod
 */
public record SpellPackage(String name, ConfiguredSpellVehicle<?> vehicle, ConfiguredSpellPayload<?> payload, Optional<ConfiguredSpellMod<?>> primaryMod, Optional<ConfiguredSpellMod<?>> secondaryMod) {
    private static final int BASE_COOLDOWN_TICKS = 30;
    private static final Logger LOGGER = LogManager.getLogger();
    
    public static Codec<SpellPackage> codec() {
        return RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("name").forGetter(SpellPackage::name),
                ConfiguredSpellVehicle.codec().fieldOf("vehicle").forGetter(SpellPackage::vehicle),
                ConfiguredSpellPayload.codec().fieldOf("payload").forGetter(SpellPackage::payload),
                ConfiguredSpellMod.codec().optionalFieldOf("primaryMod").forGetter(SpellPackage::primaryMod),
                ConfiguredSpellMod.codec().optionalFieldOf("secondaryMod").forGetter(SpellPackage::secondaryMod)
            ).apply(instance, SpellPackage::new));
    }
    
    public static StreamCodec<RegistryFriendlyByteBuf, SpellPackage> streamCodec() {
        return StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8,
                SpellPackage::name,
                ConfiguredSpellVehicle.streamCodec(),
                SpellPackage::vehicle,
                ConfiguredSpellPayload.streamCodec(),
                SpellPackage::payload,
                ByteBufCodecs.optional(ConfiguredSpellMod.streamCodec()),
                SpellPackage::primaryMod,
                ByteBufCodecs.optional(ConfiguredSpellMod.streamCodec()),
                SpellPackage::secondaryMod,
                SpellPackage::new);
    }
    
    @Nonnull
    public Component getDisplayName() {
        // Color spell names according to their rarity, like with items
        return Component.literal(this.name).withStyle(this.getRarity().color());
    }
    
    public boolean isValid() {
        // A valid package has a name, a vehicle component, and a payload component; mods are optional
        return this.vehicle != null && this.vehicle.getComponent().isActive() &&
                this.payload != null && this.payload.getComponent().isActive() &&
                this.name != null && !this.name.isEmpty();
    }

    @Nullable
    public Tag serializeNBT(HolderLookup.Provider registries) {
        return codec().encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), this)
                .resultOrPartial(msg -> LOGGER.error("Failed to encode spell package: {}", msg))
                .orElse(null);
    }
    
    @Nullable
    public static SpellPackage deserializeNBT(Tag nbt, HolderLookup.Provider registries) {
        return codec().parse(registries.createSerializationContext(NbtOps.INSTANCE), nbt)
                .resultOrPartial(msg -> LOGGER.error("Failed to decode spell package: {}", msg))
                .orElse(null);
    }

    public int getCooldownTicks() {
        // Determine the length of the cooldown triggered by this spell; reduced by the Quicken mod
        MutableInt retVal = new MutableInt(BASE_COOLDOWN_TICKS);
        this.getMod(SpellModsPM.QUICKEN.get()).ifPresent(quickenMod -> {
            retVal.subtract(5 * quickenMod.getPropertyValue(SpellPropertiesPM.HASTE.get()));
        });
        return Mth.clamp(retVal.intValue(), 0, BASE_COOLDOWN_TICKS);
    }
    
    @Nonnull
    public SourceList getManaCost() {
        // Calculate the total mana cost of this spell package.  The spell's base cost is determined by
        // its payload, then modified by its vehicle and mods, if present.
        if (this.payload == null) {
            return SourceList.EMPTY;
        }
        
        int baseManaCost = this.payload.getBaseManaCost();
        if (baseManaCost == 0) {
            return SourceList.EMPTY;
        }
        
        Source source = this.payload.getComponent().getSource();
        int baseModifier = 0;
        int multiplier = 1;
        
        // Collect all appropriate modifiers before doing the calculation to prevent mod order dependency
        if (this.vehicle != null) {
            baseModifier += this.vehicle.getBaseManaCostModifier();
            multiplier *= this.vehicle.getManaCostMultiplier();
        }
        if (this.primaryMod.isPresent()) {
            baseModifier += this.primaryMod.get().getBaseManaCostModifier();
            multiplier *= this.primaryMod.get().getManaCostMultiplier();
        }
        if (this.secondaryMod.isPresent()) {
            baseModifier += this.secondaryMod.get().getBaseManaCostModifier();
            multiplier *= this.secondaryMod.get().getManaCostMultiplier();
        }
        
        return SourceList.EMPTY.add(source, (baseManaCost + baseModifier) * multiplier);
    }
    
    public void cast(Level world, LivingEntity caster, ItemStack spellSource) {
        if (this.payload != null) {
            this.payload.getComponent().playSounds(world, caster.blockPosition());
        }
        if (this.vehicle != null) {
            if (caster instanceof Player player) {
                ExpertiseManager.awardExpertise(player, this);
                StatsManager.incrementValue(player, StatsPM.SPELLS_CAST);
            }
            this.vehicle.getComponent().execute(this, world, caster, spellSource.copy());
        }
    }
    
    public int getActiveModCount() {
        MutableInt retVal = new MutableInt(0);
        this.primaryMod.filter(mod -> mod.getComponent().isActive()).ifPresent($ -> retVal.increment());
        this.secondaryMod.filter(mod -> mod.getComponent().isActive()).ifPresent($ -> retVal.increment());
        return retVal.intValue();
    }
    
    @Nonnull
    public Rarity getRarity() {
        // A spell's rarity is based on how any mods it has
        return switch (this.getActiveModCount()) {
            case 2 -> Rarity.EPIC;
            case 1 -> Rarity.RARE;
            default -> Rarity.UNCOMMON;
        };
    }
    
    @SuppressWarnings("unchecked")
    public <T extends AbstractSpellMod<T>> Optional<ConfiguredSpellMod<T>> getMod(SpellModType<T> type) {
        // Determine if either of the attached mod components are of the requested type
        ConfiguredSpellMod<T> primary = this.primaryMod.isPresent() && this.primaryMod.get().getComponent().getType().equals(type) ? (ConfiguredSpellMod<T>)this.primaryMod.get() : null;
        ConfiguredSpellMod<T> secondary = this.secondaryMod.isPresent() && this.secondaryMod.get().getComponent().getType().equals(type) ? (ConfiguredSpellMod<T>)this.secondaryMod.get() : null;
        
        if (primary != null && secondary != null) {
            // If both mods are of the requested type, only return the one with a higher value in the tiebreaker property
            SpellProperty tiebreaker = primary.getComponent().getType().tiebreaker().get();
            return secondary.getPropertyValue(tiebreaker) > primary.getPropertyValue(tiebreaker) ? Optional.of(secondary) : Optional.of(primary);
        } else if (primary != null) {
            return Optional.of(primary);
        } else {
            return Optional.ofNullable(secondary);
        }
    }
    
    
    @Nullable
    public ResourceLocation getIcon() {
        if (this.payload != null) {
            return this.payload.getComponent().getSource().getImage();
        }
        return null;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        protected String name = "";
        protected ConfiguredSpellVehicle.Builder vehicleBuilder = null;
        protected ConfiguredSpellPayload.Builder payloadBuilder = null;
        protected Optional<ConfiguredSpellMod.Builder> primaryModBuilder = Optional.empty();
        protected Optional<ConfiguredSpellMod.Builder> secondaryModBuilder = Optional.empty();
        
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        
        public ConfiguredSpellVehicle.Builder vehicle() {
            this.vehicleBuilder = new ConfiguredSpellVehicle.Builder(this);
            return this.vehicleBuilder;
        }
        
        public ConfiguredSpellPayload.Builder payload() {
            this.payloadBuilder = new ConfiguredSpellPayload.Builder(this);
            return this.payloadBuilder;
        }
        
        public ConfiguredSpellMod.Builder primaryMod() {
            ConfiguredSpellMod.Builder retVal = new ConfiguredSpellMod.Builder(this);
            this.primaryModBuilder = Optional.of(retVal);
            return retVal;
        }
        
        public ConfiguredSpellMod.Builder secondaryMod() {
            ConfiguredSpellMod.Builder retVal = new ConfiguredSpellMod.Builder(this);
            this.secondaryModBuilder = Optional.of(retVal);
            return retVal;
        }
        
        private void validate() {
            if (this.name == null) {
                throw new IllegalStateException("No name for spell package");
            } else if (this.vehicleBuilder == null) {
                throw new IllegalStateException("No vehicle for spell package " + this.name);
            } else if (this.payloadBuilder == null) {
                throw new IllegalStateException("No payload for spell package " + this.name);
            }
        }
        
        public SpellPackage build() {
            this.validate();
            return new SpellPackage(this.name, this.vehicleBuilder.build(), this.payloadBuilder.build(), this.primaryModBuilder.map(ConfiguredSpellMod.Builder::build), 
                    this.secondaryModBuilder.map(ConfiguredSpellMod.Builder::build));
        }
    }
}
