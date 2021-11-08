package com.verdantartifice.primalmagick.common.commands.arguments;

import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.verdantartifice.primalmagick.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagick.common.research.ResearchDisciplines;

/**
 * Debug command argument definition for a research discipline.
 * 
 * @author Daedalus4096
 */
public class DisciplineArgument implements ArgumentType<DisciplineInput> {
    public static DisciplineArgument discipline() {
        return new DisciplineArgument();
    }

    @Override
    public DisciplineInput parse(StringReader reader) throws CommandSyntaxException {
        DisciplineParser parser = (new DisciplineParser(reader)).parse();
        return new DisciplineInput(parser.getDisciplineKey());
    }

    public static <S> DisciplineInput getDiscipline(CommandContext<S> context, String name) {
        return context.getArgument(name, DisciplineInput.class);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        // Suggest all registered research disciplines for tab completion
        String remaining = builder.getRemaining().toUpperCase();
        for (ResearchDiscipline discipline : ResearchDisciplines.getAllDisciplines()) {
            String key = discipline.getKey().toUpperCase();
            if (key.startsWith(remaining)) {
                builder.suggest(key);
            }
        }
        return builder.buildFuture();
    }
}
