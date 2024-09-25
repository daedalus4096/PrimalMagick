package com.verdantartifice.primalmagick.common.commands.arguments;

import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.verdantartifice.primalmagick.common.attunements.AttunementType;

/**
 * Debug command argument definition for an attunement type enum.
 * 
 * @author Daedalus4096
 */
public class AttunementTypeArgument implements ArgumentType<AttunementTypeInput> {
    public static AttunementTypeArgument attunementType() {
        return new AttunementTypeArgument();
    }
    
    @Override
    public AttunementTypeInput parse(StringReader reader) throws CommandSyntaxException {
        AttunementTypeParser parser = (new AttunementTypeParser(reader)).parse();
        return new AttunementTypeInput(parser.getType());
    }

    public static <S> AttunementTypeInput getAttunementType(CommandContext<S> context, String name) {
        return context.getArgument(name, AttunementTypeInput.class);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        // Suggest all defined attunement types for tab completion
        String remaining = builder.getRemaining().toUpperCase();
        for (AttunementType type : AttunementType.values()) {
            String key = type.name().toUpperCase();
            if (key.startsWith(remaining)) {
                builder.suggest(key);
            }
        }
        return builder.buildFuture();
    }
}
