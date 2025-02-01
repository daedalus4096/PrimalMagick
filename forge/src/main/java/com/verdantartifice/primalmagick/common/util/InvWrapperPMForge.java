package com.verdantartifice.primalmagick.common.util;

import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import net.minecraft.world.Container;
import net.minecraftforge.items.wrapper.InvWrapper;

public class InvWrapperPMForge extends InvWrapper implements IItemHandlerPM {
    public InvWrapperPMForge(Container container) {
        super(container);
    }

    @Override
    public Container asContainer() {
        return this.getInv();
    }
}
