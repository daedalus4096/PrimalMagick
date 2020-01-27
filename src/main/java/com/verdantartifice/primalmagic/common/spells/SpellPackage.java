package com.verdantartifice.primalmagic.common.spells;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;
import com.verdantartifice.primalmagic.common.spells.mods.ISpellMod;
import com.verdantartifice.primalmagic.common.spells.mods.QuickenSpellMod;
import com.verdantartifice.primalmagic.common.spells.payloads.ISpellPayload;
import com.verdantartifice.primalmagic.common.spells.vehicles.ISpellVehicle;
import com.verdantartifice.primalmagic.common.stats.StatsManager;
import com.verdantartifice.primalmagic.common.stats.StatsPM;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Definition of a spell package data structure.  A spell package represents a complete, named, castable
 * spell.  A valid package has a name, a vehicle component, a payload component, and zero to two mod 
 * components.  For more information on spell components, see their respective interfaces.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagic.common.spells.vehicles.ISpellVehicle}
 * @see {@link com.verdantartifice.primalmagic.common.spells.payloads.ISpellPayload}
 * @see {@link com.verdantartifice.primalmagic.common.spells.mods.ISpellMod}
 */
public class SpellPackage implements INBTSerializable<CompoundNBT> {
    protected String name = "";
    protected ISpellVehicle vehicle = null;
    protected ISpellPayload payload = null;
    protected ISpellMod primaryMod = null;
    protected ISpellMod secondaryMod = null;
    
    public SpellPackage() {}
    
    public SpellPackage(String name) {
        this.setName(name);
    }
    
    public SpellPackage(CompoundNBT tag) {
        this.deserializeNBT(tag);
    }
    
    @Nonnull
    public ITextComponent getName() {
        // Color spell names according to their rarity, like with items
        return new StringTextComponent(this.name).applyTextStyle(this.getRarity().color);
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
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
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
    public void deserializeNBT(CompoundNBT nbt) {
        this.name = nbt.getString("SpellName");
        this.vehicle = nbt.contains("SpellVehicle") ? SpellFactory.getVehicleFromNBT(nbt.getCompound("SpellVehicle")) : null;
        this.payload = nbt.contains("SpellPayload") ? SpellFactory.getPayloadFromNBT(nbt.getCompound("SpellPayload")) : null;
        this.primaryMod = nbt.contains("PrimaryMod") ? SpellFactory.getModFromNBT(nbt.getCompound("PrimaryMod")) : null;
        this.secondaryMod = nbt.contains("SecondaryMod") ? SpellFactory.getModFromNBT(nbt.getCompound("SecondaryMod")) : null;
    }

    public int getCooldownTicks() {
        // Determine the length of the cooldown triggered by this spell; reduced by the Quicken mod
        int retVal = 60;
        QuickenSpellMod quickenMod = this.getMod(QuickenSpellMod.class, "haste");
        if (quickenMod != null) {
            retVal -= (10 * quickenMod.getPropertyValue("haste"));
        }
        return MathHelper.clamp(retVal, 0, 60);
    }
    
    @Nonnull
    public SourceList getManaCost() {
        // Calculate the total mana cost of this spell package.  The spell's base cost is determined by
        // its payload, then modified by its vehicle and mods, if present.
        if (this.payload == null) {
            return new SourceList();
        }
        
        Source source = this.payload.getSource();
        int baseManaCost = this.payload.getBaseManaCost();
        int baseModifier = 0;
        int multiplier = 1;
        
        // Collect all appropriate modifiers before doing the calculation to prevent mod order dependency
        if (this.vehicle != null) {
            baseModifier += this.vehicle.getBaseManaCostModifier();
        }
        if (this.primaryMod != null) {
            baseModifier += this.primaryMod.getBaseManaCostModifier();
            multiplier *= this.primaryMod.getManaCostMultiplier();
        }
        if (this.secondaryMod != null) {
            baseModifier += this.secondaryMod.getBaseManaCostModifier();
            multiplier *= this.secondaryMod.getManaCostMultiplier();
        }
        
        return new SourceList().add(source, (baseManaCost + baseModifier) * multiplier);
    }
    
    public void cast(World world, PlayerEntity caster) {
        if (this.payload != null) {
            this.payload.playSounds(world, caster.getPosition());
        }
        if (this.vehicle != null) {
            if (!world.isRemote && caster instanceof ServerPlayerEntity) {
                StatsManager.incrementValue((ServerPlayerEntity)caster, StatsPM.SPELLS_CAST);
            }
            this.vehicle.execute(this, world, caster);
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
}
