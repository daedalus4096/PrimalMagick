package com.verdantartifice.primalmagick.platform.services;

import com.verdantartifice.primalmagick.common.items.misc.ArcanometerItem;

import java.util.function.Supplier;

public interface IItemPrototypeService {
    Supplier<ArcanometerItem> arcanometer();
}
