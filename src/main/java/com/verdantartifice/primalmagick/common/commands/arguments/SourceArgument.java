package com.verdantartifice.primalmagick.common.commands.arguments;

import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.verdantartifice.primalmagick.common.sources.Source;

/**
 * Debug command argument definition for a source.
 * 
 * @author Daedalus4096
 */
public class SourceArgument implements ArgumentType<SourceInput> {
    public static SourceArgument source() {
        return new SourceArgument();
    }
    
    @Override
    public SourceInput parse(StringReader reader) throws CommandSyntaxException {
        SourceParser parser = (new SourceParser(reader)).parse();
        return new SourceInput(parser.getSourceTag());
    }

    public static <S> SourceInput getSource(CommandContext<S> context, String name) {
        return context.getArgument(name, SourceInput.class);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        // Suggest all defined sources for tab completion
        String remaining = builder.getRemaining().toUpperCase();
        for (Source source : Source.SOURCES.values()) {
            String key = source.getTag().toUpperCase();
            if (key.startsWith(remaining)) {
                builder.suggest(key);
            }
        }
        return builder.buildFuture();
    }
}
