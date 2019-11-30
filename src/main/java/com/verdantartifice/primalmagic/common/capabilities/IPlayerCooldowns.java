package com.verdantartifice.primalmagic.common.capabilities;

import javax.annotation.Nullable;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IPlayerCooldowns extends INBTSerializable<CompoundNBT> {
    public boolean isOnCooldown(@Nullable CooldownType type);
    public void setCooldown(@Nullable CooldownType type, int durationTicks);
    public void clearCooldowns();
    
    public void sync(@Nullable ServerPlayerEntity player);
    
    public enum CooldownType {
        SPELL
    }
}
