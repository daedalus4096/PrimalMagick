package com.verdantartifice.primalmagick.common.items.misc;

import com.verdantartifice.primalmagick.common.items.IHasCustomRendererForge;
import com.verdantartifice.primalmagick.common.items.entities.PixieHouseItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class PixieHouseItemForge extends PixieHouseItem implements IHasCustomRendererForge {
    private IClientItemExtensions renderProps;

    public PixieHouseItemForge(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(this.getRenderProperties());
    }

    public IClientItemExtensions getRenderProperties() {
        if (this.renderProps == null) {
            this.renderProps = this.getRenderPropertiesUncached();
        }
        return this.renderProps;
    }
}
