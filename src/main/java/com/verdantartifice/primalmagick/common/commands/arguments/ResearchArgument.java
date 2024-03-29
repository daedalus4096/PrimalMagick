package com.verdantartifice.primalmagick.common.commands.arguments;

import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;

/**
 * Debug command argument definition for a research entry.
 * 
 * @author Daedalus4096
 */
public class ResearchArgument implements ArgumentType<ResearchInput> {
    public static ResearchArgument research() {
        return new ResearchArgument();
    }

    @Override
    public ResearchInput parse(StringReader reader) throws CommandSyntaxException {
        ResearchParser parser = (new ResearchParser(reader)).parse();
        return new ResearchInput(parser.getKey());
    }

    public static <S> ResearchInput getResearch(CommandContext<S> context, String name) {
        return context.getArgument(name, ResearchInput.class);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        // Suggest all defined research entries for tab completion
        String remaining = builder.getRemaining().toUpperCase();
        for (ResearchEntry entry : ResearchEntries.getAllEntries()) {
            String key = entry.getKey().getRootKey().toUpperCase();
            if (key.startsWith(remaining)) {
                builder.suggest(key);
            }
        }
        return builder.buildFuture();
    }
}
