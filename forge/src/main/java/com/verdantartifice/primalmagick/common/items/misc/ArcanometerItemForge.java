package com.verdantartifice.primalmagick.common.items.misc;

import com.verdantartifice.primalmagick.common.items.IHasCustomRendererForge;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class ArcanometerItemForge extends ArcanometerItem implements IHasCustomRendererForge {
    private IClientItemExtensions renderProps;

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
