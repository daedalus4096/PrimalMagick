package com.verdantartifice.primalmagic.common.spells;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.sources.SourceList;
import com.verdantartifice.primalmagic.common.spells.mods.ISpellMod;
import com.verdantartifice.primalmagic.common.spells.payloads.ISpellPayload;
import com.verdantartifice.primalmagic.common.spells.vehicles.ISpellVehicle;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

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
    public String getName() {
        return this.name;
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
        // TODO Calculate actual value
        return 40;
    }
    
    @Nonnull
    public SourceList getManaCost() {
        SourceList retVal = new SourceList();
        if (this.payload != null) {
            retVal.add(this.payload.getManaCost());
        }
        if (this.vehicle != null) {
            retVal = this.vehicle.modifyManaCost(retVal);
        }
        if (this.primaryMod != null) {
            retVal = this.primaryMod.modifyManaCost(retVal);
        }
        if (this.secondaryMod != null) {
            retVal = this.secondaryMod.modifyManaCost(retVal);
        }
        return retVal;
    }
    
    public void cast(World world, PlayerEntity caster) {
        if (this.payload != null) {
            this.payload.playSounds(world, caster);
        }
        if (this.vehicle != null) {
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
    
    @Nullable
    public <T extends ISpellMod> T getMod(Class<T> clazz, String tiebreakerProperty) {
        T primary = clazz.isInstance(this.primaryMod) ? clazz.cast(this.primaryMod) : null;
        T secondary = clazz.isInstance(this.secondaryMod) ? clazz.cast(this.secondaryMod) : null;
        if (primary != null && secondary != null) {
            return secondary.getPropertyValue(tiebreakerProperty) > primary.getPropertyValue(tiebreakerProperty) ? secondary : primary;
        } else if (primary != null) {
            return primary;
        } else {
            return secondary;
        }
    }
}
