package com.verdantartifice.primalmagic.common.spells.payloads;

import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.spells.ISpellPackage;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.util.INBTSerializable;

public interface ISpellPayload extends INBTSerializable<CompoundNBT> {
    public boolean execute(RayTraceResult target, ISpellPackage spell);
    public Source getSource();
}
