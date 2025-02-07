package com.verdantartifice.primalmagick.datagen.lang.builders;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Helper for specifying tooltip localizations in a structured way that cannot be directly attached
 * to an item, such as for being dynamic or being shared among multiple items.
 * 
 * @author Daedalus4096
 */
public class TooltipLanguageBuilder extends AbstractTokenizedLanguageBuilder {
    public TooltipLanguageBuilder(String cmd, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(cmd, "tooltip", untracker, adder);
    }
}
