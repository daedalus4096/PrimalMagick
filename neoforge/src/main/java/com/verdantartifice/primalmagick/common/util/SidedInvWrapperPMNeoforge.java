package com.verdantartifice.primalmagick.common.util;

import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.Nullable;

public class SidedInvWrapperPMNeoforge extends SidedInvWrapper implements IItemHandlerPM {
    public SidedInvWrapperPMNeoforge(WorldlyContainer inv, @Nullable Direction side) {
        super(inv, side);
    }
}
