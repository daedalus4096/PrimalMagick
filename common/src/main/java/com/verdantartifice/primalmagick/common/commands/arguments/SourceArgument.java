package com.verdantartifice.primalmagick.common.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * Debug command argument definition for a source.
 * 
 * @author Daedalus4096
 */
public class SourceArgument implements ArgumentType<ResourceLocation> {
    private static final Collection<String> EXAMPLES = Arrays.asList("foo", "foo:bar", "012");
    private static final DynamicCommandExceptionType ERROR_UNKNOWN_SOURCE = new DynamicCommandExceptionType((p_308368_) -> {
        return Component.translatableEscape("commands.primalmagick.source.noexist", p_308368_);
     });

    public static SourceArgument source() {
        return new SourceArgument();
    }
    
    public static Source getSource(CommandContext<CommandSourceStack> pContext, String pName) throws CommandSyntaxException {
        ResourceLocation loc = getId(pContext, pName);
        Source retVal = Sources.get(loc);
        if (retVal == null) {
            throw ERROR_UNKNOWN_SOURCE.create(loc);
        } else {
            return retVal;
        }
    }

    public static ResourceLocation getId(CommandContext<CommandSourceStack> pContext, String pName) {
        return pContext.getArgument(pName, ResourceLocation.class);
    }

    @Override
    public ResourceLocation parse(StringReader reader) throws CommandSyntaxException {
        return ResourceLocation.read(reader);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        // Suggest all defined sources for tab completion
        String remaining = builder.getRemaining();
        Sources.stream().map(s -> s.getId()).filter(k -> k.toString().startsWith(remaining)).forEach(k -> builder.suggest(k.toString()));
        return builder.buildFuture();
    }
}
