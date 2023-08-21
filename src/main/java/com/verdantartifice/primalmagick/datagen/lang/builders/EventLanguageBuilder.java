package com.verdantartifice.primalmagick.datagen.lang.builders;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Helper for specifying arbitrary event-related localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class EventLanguageBuilder extends AbstractTokenizedLanguageBuilder {
    public EventLanguageBuilder(String cmd, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(cmd, "event", untracker, adder);
    }
}
