package com.verdantartifice.primalmagick.common.spells;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.spells.mods.ISpellMod;
import com.verdantartifice.primalmagick.common.spells.mods.QuickenSpellMod;
import com.verdantartifice.primalmagick.common.spells.payloads.ISpellPayload;
import com.verdantartifice.primalmagick.common.spells.vehicles.ISpellVehicle;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Definition of a spell package data structure.  A spell package represents a complete, named, castable
 * spell.  A valid package has a name, a vehicle component, a payload component, and zero to two mod 
 * components.  For more information on spell components, see their respective interfaces.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.common.spells.vehicles.ISpellVehicle}
 * @see {@link com.verdantartifice.primalmagick.common.spells.payloads.ISpellPayload}
 * @see {@link com.verdantartifice.primalmagick.common.spells.mods.ISpellMod}
 */
public class SpellPackage implements INBTSerializable<CompoundTag> {
    protected String name = "";
    protected ISpellVehicle vehicle = null;
    protected ISpellPayload payload = null;
    protected ISpellMod primaryMod = null;
    protected ISpellMod secondaryMod = null;
    
    public SpellPackage() {}
    
    public SpellPackage(String name) {
        this.setName(name);
    }
    
    public SpellPackage(CompoundTag tag) {
        this.deserializeNBT(tag);
    }
    
    @Nonnull
    public Component getName() {
        // Color spell names according to their rarity, like with items
        return Component.literal(this.name).withStyle(this.getRarity().getStyleModifier());
    }
    
    public void setName(@Nullable String name) {
        if (name != null) {
            this.name = name;
        }
    }

    @Nullable
    public ISpellVehicle getVehicle() {
        return this.vehicle;
    }

    public void setVehicle(@Nullable ISpellVehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Nullable
    public ISpellPayload getPayload() {
        return this.payload;
    }

    public void setPayload(@Nullable ISpellPayload payload) {
        this.payload = payload;
    }

    @Nullable
    public ISpellMod getPrimaryMod() {
        return this.primaryMod;
    }

    public void setPrimaryMod(@Nullable ISpellMod primaryMod) {
        this.primaryMod = primaryMod;
    }

    @Nullable
    public ISpellMod getSecondaryMod() {
        return this.secondaryMod;
    }

    public void setSecondaryMod(@Nullable ISpellMod secondaryMod) {
        this.secondaryMod = secondaryMod;
    }
    
    public boolean isValid() {
        // A valid package has a name, a vehicle component, and a payload component; mods are optional
        return this.vehicle != null && this.vehicle.isActive() &&
                this.payload != null && this.payload.isActive() &&
                this.name != null && !this.name.isEmpty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        if (this.name != null) {
            nbt.putString("SpellName", this.name);
        }
        if (this.vehicle != null) {
            nbt.put("SpellVehicle", this.vehicle.serializeNBT());
        }
        if (this.payload != null) {
            nbt.put("SpellPayload", this.payload.serializeNBT());
        }
        if (this.primaryMod != null) {
            nbt.put("PrimaryMod", this.primaryMod.serializeNBT());
        }
        if (this.secondaryMod != null) {
            nbt.put("SecondaryMod", this.secondaryMod.serializeNBT());
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.name = nbt.getString("SpellName");
        this.vehicle = nbt.contains("SpellVehicle") ? SpellFactory.getVehicleFromNBT(nbt.getCompound("SpellVehicle")) : null;
        this.payload = nbt.contains("SpellPayload") ? SpellFactory.getPayloadFromNBT(nbt.getCompound("SpellPayload")) : null;
        this.primaryMod = nbt.contains("PrimaryMod") ? SpellFactory.getModFromNBT(nbt.getCompound("PrimaryMod")) : null;
        this.secondaryMod = nbt.contains("SecondaryMod") ? SpellFactory.getModFromNBT(nbt.getCompound("SecondaryMod")) : null;
    }

    public int getCooldownTicks() {
        // Determine the length of the cooldown triggered by this spell; reduced by the Quicken mod
        int retVal = 30;
        QuickenSpellMod quickenMod = this.getMod(QuickenSpellMod.class, "haste");
        if (quickenMod != null) {
            retVal -= (5 * quickenMod.getPropertyValue("haste"));
        }
        return Mth.clamp(retVal, 0, 30);
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
        
        Source source = this.payload.getSource();
        int baseModifier = 0;
        int multiplier = 1;
        
        // Collect all appropriate modifiers before doing the calculation to prevent mod order dependency
        if (this.vehicle != null) {
            baseModifier += this.vehicle.getBaseManaCostModifier();
            multiplier *= this.vehicle.getManaCostMultiplier();
        }
        if (this.primaryMod != null) {
            baseModifier += this.primaryMod.getBaseManaCostModifier();
            multiplier *= this.primaryMod.getManaCostMultiplier();
        }
        if (this.secondaryMod != null) {
            baseModifier += this.secondaryMod.getBaseManaCostModifier();
            multiplier *= this.secondaryMod.getManaCostMultiplier();
        }
        
        return SourceList.EMPTY.add(source, (baseManaCost + baseModifier) * multiplier);
    }
    
    public void cast(Level world, LivingEntity caster, ItemStack spellSource) {
        if (this.payload != null) {
            this.payload.playSounds(world, caster.blockPosition());
        }
        if (this.vehicle != null) {
            if (caster instanceof Player) {
                StatsManager.incrementValue((Player)caster, StatsPM.SPELLS_CAST);
            }
            this.vehicle.execute(this, world, caster, spellSource);
        }
    }
    
    public int getActiveModCount() {
        int retVal = 0;
        if (this.primaryMod != null && this.primaryMod.isActive()) {
            retVal++;
        }
        if (this.secondaryMod != null && this.secondaryMod.isActive()) {
            retVal++;
        }
        return retVal;
    }
    
    @Nonnull
    public Rarity getRarity() {
        // A spell's rarity is based on how any mods it has
        int mods = this.getActiveModCount();
        switch (mods) {
        case 2:
            return Rarity.EPIC;
        case 1:
            return Rarity.RARE;
        default:
            return Rarity.UNCOMMON;
        }
    }
    
    @Nullable
    public <T extends ISpellMod> T getMod(Class<T> clazz, String tiebreakerProperty) {
        // Determine if either of the attached mod components are of the requested class
        T primary = clazz.isInstance(this.primaryMod) ? clazz.cast(this.primaryMod) : null;
        T secondary = clazz.isInstance(this.secondaryMod) ? clazz.cast(this.secondaryMod) : null;
        
        if (primary != null && secondary != null) {
            // If both mods are of the requested type, only return the one with a higher value in the specified property
            return secondary.getPropertyValue(tiebreakerProperty) > primary.getPropertyValue(tiebreakerProperty) ? secondary : primary;
        } else if (primary != null) {
            return primary;
        } else {
            return secondary;
        }
    }
    
    @Nullable
    public ResourceLocation getIcon() {
        if (this.payload != null) {
            return this.payload.getSource().getImage();
        }
        return null;
    }
}
