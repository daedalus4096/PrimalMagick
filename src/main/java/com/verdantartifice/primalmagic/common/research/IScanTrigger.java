package com.verdantartifice.primalmagic.common.research;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.IItemProvider;

public interface IScanTrigger {
    boolean matches(ServerPlayerEntity player, IItemProvider itemProvider);
    void onMatch(ServerPlayerEntity player, IItemProvider itemProvider);
}
