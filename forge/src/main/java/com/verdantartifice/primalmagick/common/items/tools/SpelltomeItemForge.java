package com.verdantartifice.primalmagick.common.items.tools;

import com.verdantartifice.primalmagick.common.items.IHasCustomRendererForge;
import com.verdantartifice.primalmagick.common.misc.DeviceTier;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class SpelltomeItemForge extends SpelltomeItem implements IHasCustomRendererForge {
    private IClientItemExtensions renderProps;

    public SpelltomeItemForge(DeviceTier tier, Properties pProperties) {
        super(tier, pProperties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(this.getRenderProperties());
    }

    @Override
    public IClientItemExtensions getRenderProperties() {
        if (this.renderProps == null) {
            this.renderProps = this.getRenderPropertiesUncached();
        }
        return this.renderProps;
    }
}
