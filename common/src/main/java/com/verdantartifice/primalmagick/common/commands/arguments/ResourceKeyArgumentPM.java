package com.verdantartifice.primalmagick.common.commands.arguments;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.verdantartifice.primalmagick.common.books.BookDefinition;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;

import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
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
    private static final Function<String, DynamicCommandExceptionType> ERROR_MESSAGE = messageKey -> new DynamicCommandExceptionType(err -> {
        return Component.translatable(messageKey, err);
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
    
    public static Holder.Reference<ResearchEntry> getResearchEntry(CommandContext<CommandSourceStack> pContext, String pArgument) throws CommandSyntaxException {
        // TODO Filter out internal research entries from suggestions
        return resolveKey(pContext, pArgument, RegistryKeysPM.RESEARCH_ENTRIES, ERROR_MESSAGE.apply("commands.primalmagick.research.noexist"));
    }
    
    public static Holder.Reference<ResearchDiscipline> getResearchDiscipline(CommandContext<CommandSourceStack> pContext, String pArgument) throws CommandSyntaxException {
        return resolveKey(pContext, pArgument, RegistryKeysPM.RESEARCH_DISCIPLINES, ERROR_MESSAGE.apply("commands.primalmagick.discipline.noexist"));
    }
    
    public static Holder.Reference<BookDefinition> getBook(CommandContext<CommandSourceStack> pContext, String pArgument) throws CommandSyntaxException {
        return resolveKey(pContext, pArgument, RegistryKeysPM.BOOKS, ERROR_MESSAGE.apply("commands.primalmagick.books.noexist"));
    }
    
    public static Holder.Reference<BookLanguage> getLanguage(CommandContext<CommandSourceStack> pContext, String pArgument) throws CommandSyntaxException {
        return resolveKey(pContext, pArgument, RegistryKeysPM.BOOK_LANGUAGES, ERROR_MESSAGE.apply("commands.primalmagick.books.nolanguage"));
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
    
    public static class Info<T> implements ArgumentTypeInfo<ResourceKeyArgumentPM<T>, ResourceKeyArgumentPM.Info<T>.Template> {
        @Override
        public void serializeToNetwork(Info<T>.Template pTemplate, FriendlyByteBuf pBuffer) {
            pBuffer.writeResourceKey(pTemplate.registryKey);
        }

        @Override
        public ResourceKeyArgumentPM.Info<T>.Template deserializeFromNetwork(FriendlyByteBuf pBuffer) {
            return new ResourceKeyArgumentPM.Info<T>.Template(pBuffer.readRegistryKey());
        }

        @Override
        public void serializeToJson(Info<T>.Template pTemplate, JsonObject pJson) {
            pJson.addProperty("registry", pTemplate.registryKey.location().toString());
        }

        @Override
        public Info<T>.Template unpack(ResourceKeyArgumentPM<T> pArgument) {
            return new ResourceKeyArgumentPM.Info<T>.Template(pArgument.registryKey);
        }
        
        public final class Template implements ArgumentTypeInfo.Template<ResourceKeyArgumentPM<T>> {
            final ResourceKey<? extends Registry<T>> registryKey;

            Template(ResourceKey<? extends Registry<T>> pRegistryKey) {
                this.registryKey = pRegistryKey;
            }

            @Override
            public ResourceKeyArgumentPM<T> instantiate(CommandBuildContext pContext) {
                return new ResourceKeyArgumentPM<>(this.registryKey);
            }

            @Override
            public ArgumentTypeInfo<ResourceKeyArgumentPM<T>, ?> type() {
                return Info.this;
            }
        }
    }
}
