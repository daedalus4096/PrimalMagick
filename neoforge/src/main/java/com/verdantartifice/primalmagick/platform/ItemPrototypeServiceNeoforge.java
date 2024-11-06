package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.items.misc.ArcanometerItem;
import com.verdantartifice.primalmagick.common.items.misc.ArcanometerItemNeoforge;
import com.verdantartifice.primalmagick.common.items.misc.ManaFontBlockItem;
import com.verdantartifice.primalmagick.common.items.misc.ManaFontBlockItemNeoforge;
import com.verdantartifice.primalmagick.platform.services.IItemPrototypeService;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class ItemPrototypeServiceNeoforge implements IItemPrototypeService {
    @Override
    public Supplier<ArcanometerItem> arcanometer() {
        return ArcanometerItemNeoforge::new;
    }

    @Override
    public <T extends Block> Supplier<ManaFontBlockItem> manaFont(Supplier<T> blockSupplier, Item.Properties properties) {
        return () -> new ManaFontBlockItemNeoforge(blockSupplier.get(), properties);
    }
}
