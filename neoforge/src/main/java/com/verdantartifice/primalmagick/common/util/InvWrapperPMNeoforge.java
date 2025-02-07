package com.verdantartifice.primalmagick.common.util;

import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import net.minecraft.world.Container;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

public class InvWrapperPMNeoforge extends InvWrapper implements IItemHandlerPM {
    public InvWrapperPMNeoforge(Container container) {
        super(container);
    }

    @Override
    public Container asContainer() {
        return this.getInv();
    }
}
