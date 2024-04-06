package com.verdantartifice.primalmagick.common.commands.arguments;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.verdantartifice.primalmagick.common.books.BookDefinition;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

/**
 * Definition of a command argument that accepts resource keys for dynamic, data-defined registries.  Like
 * ResourceKeyArgument in vanilla Minecraft, but extended to work with additional registry types.
 * 
 * @author Daedalus4096
 * @see {@link net.minecraft.commands.arguments.ResourceKeyArgument}
 */
public class ResourceKeyArgumentPM<T> implements ArgumentType<ResourceKey<T>> {
    private static final Collection<String> EXAMPLES = Arrays.asList("foo", "foo:bar", "012");
    private static final DynamicCommandExceptionType ERROR_INVALID_BOOK = new DynamicCommandExceptionType((err) -> {
        return Component.translatable("command.primalmagick.books.noexist", err);
    });

    protected final ResourceKey<? extends Registry<T>> registryKey;

    protected ResourceKeyArgumentPM(ResourceKey<? extends Registry<T>> pRegistryKey) {
        this.registryKey = pRegistryKey;
    }
    
    public ResourceKey<? extends Registry<T>> getRegistryKey() {
        return this.registryKey;
    }
    
    public static <T> ResourceKeyArgumentPM<T> key(ResourceKey<? extends Registry<T>> pRegistryKey) {
        return new ResourceKeyArgumentPM<>(pRegistryKey);
    }
    
    public static Holder.Reference<BookDefinition> getBook(CommandContext<CommandSourceStack> pContext, String pArgument) throws CommandSyntaxException {
        return resolveKey(pContext, pArgument, RegistryKeysPM.BOOKS, ERROR_INVALID_BOOK);
    }

    private static <T> Holder.Reference<T> resolveKey(CommandContext<CommandSourceStack> pContext, String pArgument, ResourceKey<Registry<T>> pRegistryKey, DynamicCommandExceptionType pException) throws CommandSyntaxException {
        ResourceKey<T> resourcekey = getRegistryKey(pContext, pArgument, pRegistryKey, pException);
        return getRegistry(pContext, pRegistryKey).getHolder(resourcekey).orElseThrow(() -> {
            return pException.create(resourcekey.location());
        });
    }

    private static <T> ResourceKey<T> getRegistryKey(CommandContext<CommandSourceStack> pContext, String pArgument, ResourceKey<Registry<T>> pRegistryKey, DynamicCommandExceptionType pException) throws CommandSyntaxException {
        ResourceKey<?> resourcekey = pContext.getArgument(pArgument, ResourceKey.class);
        Optional<ResourceKey<T>> optional = resourcekey.cast(pRegistryKey);
        return optional.orElseThrow(() -> {
            return pException.create(resourcekey);
        });
    }

    private static <T> Registry<T> getRegistry(CommandContext<CommandSourceStack> pContext, ResourceKey<? extends Registry<T>> pRegistryKey) {
        return pContext.getSource().getServer().registryAccess().registryOrThrow(pRegistryKey);
    }

    @Override
    public ResourceKey<T> parse(StringReader pReader) throws CommandSyntaxException {
        return ResourceKey.create(this.registryKey, ResourceLocation.read(pReader));
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (context.getSource() instanceof SharedSuggestionProvider provider) {
            return provider.suggestRegistryElements(this.registryKey, SharedSuggestionProvider.ElementSuggestionType.ELEMENTS, builder, context);
        } else {
            return builder.buildFuture();
        }
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
    
    
}
