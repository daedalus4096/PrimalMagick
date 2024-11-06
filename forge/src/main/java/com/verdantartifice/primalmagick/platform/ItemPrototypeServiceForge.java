package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.items.misc.ArcanometerItem;
import com.verdantartifice.primalmagick.common.items.misc.ArcanometerItemForge;
import com.verdantartifice.primalmagick.platform.services.IItemPrototypeService;

import java.util.function.Supplier;

public class ItemPrototypeServiceForge implements IItemPrototypeService {
    @Override
    public Supplier<ArcanometerItem> arcanometer() {
        return ArcanometerItemForge::new;
    }
}
