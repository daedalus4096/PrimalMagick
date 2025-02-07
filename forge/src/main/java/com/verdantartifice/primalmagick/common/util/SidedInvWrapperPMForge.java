package com.verdantartifice.primalmagick.common.util;

import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.Nullable;

public class SidedInvWrapperPMForge extends SidedInvWrapper implements IItemHandlerPM {
    public SidedInvWrapperPMForge(WorldlyContainer inv, @Nullable Direction side) {
        super(inv, side);
    }

    @Override
    public Container asContainer() {
        return this.inv;
    }
}
