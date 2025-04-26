package com.verdantartifice.primalmagick.common.items;

import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;

public interface IItemHandlerChangeListener {
    void itemsChanged(int itemHandlerIndex, IItemHandlerPM itemHandler);
}
