package com.verdantartifice.primalmagic.common.capabilities;

import java.util.Queue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.misc.EntitySwapper;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IWorldEntitySwappers extends INBTSerializable<CompoundNBT> {
    public boolean enqueue(@Nullable EntitySwapper swapper);
    
    @Nonnull
    public Queue<EntitySwapper> getQueue();
    
    public boolean setQueue(@Nullable Queue<EntitySwapper> queue);
}
