package com.verdantartifice.primalmagick.client.gui.hud;

import com.verdantartifice.primalmagick.common.sources.Source;
import net.neoforged.neoforge.client.IItemDecorator;

/**
 * Extension of common mana storage item decorator logic, implementing the Neoforge-specific {@link IItemDecorator} interface.
 * 
 * @author Daedalus4096
 */
public class ManaStorageItemDecoratorNeoforge extends AbstractManaStorageItemDecorator implements IItemDecorator {
    public ManaStorageItemDecoratorNeoforge(Source source) {
        super(source);
    }
}
