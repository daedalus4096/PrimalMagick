package com.verdantartifice.primalmagick.client.gui.hud;

import com.verdantartifice.primalmagick.common.sources.Source;
import net.minecraftforge.client.IItemDecorator;

/**
 * Extension of common mana storage item decorator logic, implementing the Forge-specific {@link IItemDecorator} interface.
 * 
 * @author Daedalus4096
 */
public class ManaStorageItemDecoratorForge extends AbstractManaStorageItemDecorator implements IItemDecorator {
    public ManaStorageItemDecoratorForge(Source source) {
        super(source);
    }
}
