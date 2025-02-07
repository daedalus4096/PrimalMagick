package com.verdantartifice.primalmagick.common.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;

@AutoRegisterCapability
@SuppressWarnings("deprecation")
public interface IPlayerKnowledgeForge extends IPlayerKnowledge, INBTSerializable<CompoundTag> {
}
