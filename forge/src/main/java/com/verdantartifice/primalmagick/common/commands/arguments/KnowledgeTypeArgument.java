package com.verdantartifice.primalmagick.common.commands.arguments;

import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.verdantartifice.primalmagick.common.research.KnowledgeType;

/**
 * Debug command argument definition for a knowledge type enum.
 * 
 * @author Daedalus4096
 */
public class KnowledgeTypeArgument implements ArgumentType<KnowledgeTypeInput> {
    public static KnowledgeTypeArgument knowledgeType() {
        return new KnowledgeTypeArgument();
    }
    
    @Override
    public KnowledgeTypeInput parse(StringReader reader) throws CommandSyntaxException {
        KnowledgeTypeParser parser = (new KnowledgeTypeParser(reader)).parse();
        return new KnowledgeTypeInput(parser.getType());
    }

    public static <S> KnowledgeTypeInput getKnowledgeType(CommandContext<S> context, String name) {
        return context.getArgument(name, KnowledgeTypeInput.class);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        // Suggest all defined knowledge types for tab completion
        String remaining = builder.getRemaining().toUpperCase();
        for (KnowledgeType type : KnowledgeType.values()) {
            String key = type.name().toUpperCase();
            if (key.startsWith(remaining)) {
                builder.suggest(key);
            }
        }
        return builder.buildFuture();
    }
}
