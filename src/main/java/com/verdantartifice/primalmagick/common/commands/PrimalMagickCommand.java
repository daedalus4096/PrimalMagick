package com.verdantartifice.primalmagick.common.commands;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.OptionalInt;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.verdantartifice.primalmagick.common.affinities.AffinityManager;
import com.verdantartifice.primalmagick.common.affinities.IAffinity;
import com.verdantartifice.primalmagick.common.affinities.ItemAffinity;
import com.verdantartifice.primalmagick.common.attunements.AttunementManager;
import com.verdantartifice.primalmagick.common.attunements.AttunementType;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.books.BooksPM;
import com.verdantartifice.primalmagick.common.books.LinguisticsManager;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerArcaneRecipeBook;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerAttunements;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerLinguistics;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerStats;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.commands.arguments.AttunementTypeArgument;
import com.verdantartifice.primalmagick.common.commands.arguments.AttunementTypeInput;
import com.verdantartifice.primalmagick.common.commands.arguments.AttunementValueArgument;
import com.verdantartifice.primalmagick.common.commands.arguments.KnowledgeAmountArgument;
import com.verdantartifice.primalmagick.common.commands.arguments.KnowledgeTypeArgument;
import com.verdantartifice.primalmagick.common.commands.arguments.KnowledgeTypeInput;
import com.verdantartifice.primalmagick.common.commands.arguments.LanguageComprehensionArgument;
import com.verdantartifice.primalmagick.common.commands.arguments.ResearchArgument;
import com.verdantartifice.primalmagick.common.commands.arguments.ResearchInput;
import com.verdantartifice.primalmagick.common.commands.arguments.SourceArgument;
import com.verdantartifice.primalmagick.common.commands.arguments.SourceInput;
import com.verdantartifice.primalmagick.common.commands.arguments.StatValueArgument;
import com.verdantartifice.primalmagick.common.crafting.IArcaneRecipeBookItem;
import com.verdantartifice.primalmagick.common.crafting.recipe_book.ArcaneRecipeBookManager;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.books.StaticBookItem;
import com.verdantartifice.primalmagick.common.research.KnowledgeType;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.stats.Stat;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.util.DataPackUtils;

import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Definition of the /primalmagick debug command and its /pm alias.
 * 
 * @author Daedalus4096
 */
public class PrimalMagickCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext) {
        LiteralCommandNode<CommandSourceStack> node = dispatcher.register(Commands.literal("primalmagick")
            .requires((source) -> { return source.hasPermission(2); })
            .then(Commands.literal("reset")
                .then(Commands.argument("target", EntityArgument.player()).executes((context) -> { return resetAll(context.getSource(), EntityArgument.getPlayer(context, "target")); }))
            )
            .then(Commands.literal("research")
                .then(Commands.argument("target", EntityArgument.player())
                    // /pm research <target> list
                    .then(Commands.literal("list").executes((context) -> { return listResearch(context.getSource(), EntityArgument.getPlayer(context, "target")); }))
                    // /pm research <target> reset
                    .then(Commands.literal("reset").executes((context) -> { return resetResearch(context.getSource(), EntityArgument.getPlayer(context, "target")); }))
                    // /pm research <target> grant_all
                    .then(Commands.literal("grant_all").executes((context) -> { return grantAllResearch(context.getSource(), EntityArgument.getPlayer(context, "target")); }))
                    .then(Commands.literal("grant")
                        // /pm research <target> grant <research>
                        .then(Commands.argument("research", ResearchArgument.research()).executes((context) -> { return grantResearch(context.getSource(), EntityArgument.getPlayer(context, "target"), ResearchArgument.getResearch(context, "research")); }))
                    )
                    .then(Commands.literal("grant_parents")
                        // /pm research <target> grant_parents <research>
                        .then(Commands.argument("research", ResearchArgument.research()).executes((context) -> { return grantResearchParents(context.getSource(), EntityArgument.getPlayer(context, "target"), ResearchArgument.getResearch(context, "research")); }))
                    )
                    .then(Commands.literal("revoke")
                        // /pm research <target> revoke <research>
                        .then(Commands.argument("research", ResearchArgument.research()).executes((context) -> { return revokeResearch(context.getSource(), EntityArgument.getPlayer(context, "target"), ResearchArgument.getResearch(context, "research")); }))
                    )
                    .then(Commands.literal("details")
                        // /pm research <target> details <research>
                        .then(Commands.argument("research", ResearchArgument.research()).executes((context) -> { return detailResearch(context.getSource(), EntityArgument.getPlayer(context, "target"), ResearchArgument.getResearch(context, "research")); }))
                    )
                    .then(Commands.literal("progress")
                        // /pm research <target> progress <research>
                        .then(Commands.argument("research", ResearchArgument.research()).executes((context) -> { return progressResearch(context.getSource(), EntityArgument.getPlayer(context, "target"), ResearchArgument.getResearch(context, "research")); }))
                    )
                )
            )
            .then(Commands.literal("knowledge")
                .then(Commands.argument("target", EntityArgument.player())
                    // /pm knowledge <target> reset
                    .then(Commands.literal("reset").executes((context) -> { return resetKnowledge(context.getSource(), EntityArgument.getPlayer(context, "target")); }))
                    .then(Commands.literal("get")
                        // /pm knowledge <target> get <knowledge_type>
                        .then(Commands.argument("knowledge_type", KnowledgeTypeArgument.knowledgeType()).executes((context) -> { return getKnowledge(context.getSource(), EntityArgument.getPlayer(context, "target"), KnowledgeTypeArgument.getKnowledgeType(context, "knowledge_type")); }))
                    )
                    .then(Commands.literal("add")
                        .then(Commands.argument("knowledge_type", KnowledgeTypeArgument.knowledgeType())
                            // /pm knowledge <target> add <knowledge_type> <points>
                            .then(Commands.argument("points", KnowledgeAmountArgument.amount()).executes((context) -> { return addKnowledge(context.getSource(), EntityArgument.getPlayer(context, "target"), KnowledgeTypeArgument.getKnowledgeType(context, "knowledge_type"), IntegerArgumentType.getInteger(context, "points")); }))
                        )
                    )
                )
            )
            .then(Commands.literal("scans")
                .then(Commands.argument("target", EntityArgument.player())
                    .then(Commands.literal("grant")
                        // /pm scans <target> grant <item>
                        .then(Commands.argument("item", ItemArgument.item(buildContext)).executes((context) -> { return grantScanResearch(context.getSource(), EntityArgument.getPlayer(context, "target"), ItemArgument.getItem(context, "item")); }))
                    )
                    // /pm scans <target> grant_all
                    .then(Commands.literal("grant_all").executes((context) -> { return grantAllScanResearch(context.getSource(), EntityArgument.getPlayer(context, "target")); }))
                )
            )
            .then(Commands.literal("sources")
                .then(Commands.argument("target", EntityArgument.player())
                    // /pm sources <target> list
                    .then(Commands.literal("list").executes((context) -> { return listUnlockedSources(context.getSource(), EntityArgument.getPlayer(context, "target")); }))
                    // /pm sources <target> unlock_all
                    .then(Commands.literal("unlock_all").executes((context) -> { return unlockAllSources(context.getSource(), EntityArgument.getPlayer(context, "target")); }))
                    .then(Commands.literal("unlock")
                        // /pm sources <target> unlock <source>
                        .then(Commands.argument("source", SourceArgument.source()).executes((context) -> { return unlockSource(context.getSource(), EntityArgument.getPlayer(context, "target"), SourceArgument.getSource(context, "source")); }))
                    )
                )
            )
            .then(Commands.literal("stats")
                .then(Commands.argument("target", EntityArgument.player())
                    // /pm stats <target> reset
                    .then(Commands.literal("reset").executes((context) -> { return resetStats(context.getSource(), EntityArgument.getPlayer(context, "target")); }))
                    .then(Commands.literal("get")
                        // /pm stats <target> get <stat>
                        .then(Commands.argument("stat", ResourceLocationArgument.id()).suggests((ctx, sb) -> SharedSuggestionProvider.suggest(StatsManager.getStatLocations().stream().map(ResourceLocation::toString), sb)).executes((context) -> { return getStatValue(context.getSource(), EntityArgument.getPlayer(context, "target"), ResourceLocationArgument.getId(context, "stat")); }))
                    )
                    .then(Commands.literal("set")
                        .then(Commands.argument("stat", ResourceLocationArgument.id()).suggests((ctx, sb) -> SharedSuggestionProvider.suggest(StatsManager.getStatLocations().stream().map(ResourceLocation::toString), sb))
                            // /pm stats <target> set <stat> <value>
                            .then(Commands.argument("value", StatValueArgument.value()).executes((context) -> { return setStatValue(context.getSource(), EntityArgument.getPlayer(context, "target"), ResourceLocationArgument.getId(context, "stat"), IntegerArgumentType.getInteger(context, "value")); }))
                        )
                    )
                )
            )
            .then(Commands.literal("attunements")
                .then(Commands.argument("target", EntityArgument.player())
                    // /pm attunements <target> reset
                    .then(Commands.literal("reset").executes((context) -> { return resetAttunements(context.getSource(), EntityArgument.getPlayer(context, "target")); }))
                    .then(Commands.literal("get")
                        // /pm attunements <target> get <source>
                        .then(Commands.argument("source", SourceArgument.source()).executes((context) -> { return getAttunements(context.getSource(), EntityArgument.getPlayer(context, "target"), SourceArgument.getSource(context, "source")); }))
                    )
                    .then(Commands.literal("set")
                        .then(Commands.argument("source", SourceArgument.source())
                            .then(Commands.argument("type", AttunementTypeArgument.attunementType())
                                // /pm attunements <target> set <source> <type> <value>
                                .then(Commands.argument("value", AttunementValueArgument.value()).executes((context) -> { return setAttunement(context.getSource(), EntityArgument.getPlayer(context, "target"), SourceArgument.getSource(context, "source"), AttunementTypeArgument.getAttunementType(context, "type"), IntegerArgumentType.getInteger(context, "value")); }))
                            )
                        )
                    )
                )
            )
            .then(Commands.literal("affinities")
                .then(Commands.literal("lint")
                    // By default, invoke excluding vanilla and PM items on the assumption they should be in the intended place.
                    .executes((context) -> {return getSourcelessItems(context.getSource(), Arrays.asList("minecraft", "primalmagick"));})
                    .then(Commands.literal("all").executes((context) -> {return getSourcelessItems(context.getSource(),null);}))
                )
                .then(Commands.literal("generateDatapack")
                    // By default, invoke excluding vanilla and PM items on the assumption they should be in the intended places.
                    .executes(context -> { return writeSourcelessItemDatapack(context.getSource(), Arrays.asList("minecraft", "primalmagick"));})
                    .then(Commands.literal("all").executes((context) -> {return writeSourcelessItemDatapack(context.getSource(),null);}))
                )
                .then(Commands.literal("explain")
                    // /pm affinities explain <item>
                    .then(Commands.argument("item", ItemArgument.item(buildContext)).executes(context -> explainItemAffinity(context.getSource(), ItemArgument.getItem(context, "item"))))
                )
            )
            .then(Commands.literal("recipes")
                .then(Commands.argument("target", EntityArgument.player())
                    // /pm recipes <target> reset
                    .then(Commands.literal("reset").executes((context) -> { return resetRecipes(context.getSource(), EntityArgument.getPlayer(context, "target")); }))
                    // /pm recipes <target> list
                    .then(Commands.literal("list").executes((context) -> { return listArcaneRecipes(context.getSource(), EntityArgument.getPlayer(context, "target")); }))
                    // /pm recipes <target> sync
                    .then(Commands.literal("sync").executes((context) -> { return syncArcaneRecipes(context.getSource(), EntityArgument.getPlayer(context, "target")); }))
                    .then(Commands.literal("add")
                        // /pm recipes <target> add <recipe>
                        .then(Commands.argument("recipe", ResourceLocationArgument.id()).suggests(SuggestionProviders.ALL_RECIPES).executes((context) -> { return addArcaneRecipe(context.getSource(), EntityArgument.getPlayer(context, "target"), ResourceLocationArgument.getRecipe(context, "recipe")); }))
                    )
                    .then(Commands.literal("remove")
                        // /pm recipes <target> remove <recipe>
                        .then(Commands.argument("recipe", ResourceLocationArgument.id()).suggests(SuggestionProviders.ALL_RECIPES).executes((context) -> { return removeArcaneRecipe(context.getSource(), EntityArgument.getPlayer(context, "target"), ResourceLocationArgument.getRecipe(context, "recipe")); }))
                    )
                )
            )
            .then(Commands.literal("books")
                .then(Commands.argument("targets", EntityArgument.players())
                    .then(Commands.literal("give")
                        // /pm books <targets> give <bookId>
                        .then(Commands.argument("bookId", ResourceLocationArgument.id()).suggests((ctx, sb) -> SharedSuggestionProvider.suggestResource(BooksPM.BOOKS.get().getKeys().stream(), sb)).executes(context -> giveBook(context.getSource(), EntityArgument.getPlayers(context, "targets"), ResourceLocationArgument.getId(context, "bookId"), BookLanguagesPM.DEFAULT.getId(), OptionalInt.empty()))
                            // /pm books <targets> give <bookId> [<languageId>]
                            .then(Commands.argument("languageId", ResourceLocationArgument.id()).suggests((ctx, sb) -> SharedSuggestionProvider.suggestResource(BookLanguagesPM.LANGUAGES.get().getKeys().stream(), sb)).executes(context -> giveBook(context.getSource(), EntityArgument.getPlayers(context, "targets"), ResourceLocationArgument.getId(context, "bookId"), ResourceLocationArgument.getId(context, "languageId"), OptionalInt.empty()))
                                // /pm books <targets> give <bookId> [<languageId>] [<comprehension>]
                                .then(Commands.argument("comprehension", LanguageComprehensionArgument.value()).executes((context) -> giveBook(context.getSource(), EntityArgument.getPlayers(context, "targets"), ResourceLocationArgument.getId(context, "bookId"), ResourceLocationArgument.getId(context, "languageId"), OptionalInt.of(IntegerArgumentType.getInteger(context, "comprehension")))))
                            )
                        )
                    )
                )
            )
            .then(Commands.literal("linguistics")
                .then(Commands.argument("target", EntityArgument.player())
                    // /pm linguistics <target> reset
                    .then(Commands.literal("reset").executes((context) -> { return resetLinguistics(context.getSource(), EntityArgument.getPlayer(context, "target")); }))
                    .then(Commands.literal("comprehension")
                        .then(Commands.literal("get")
                             // /pm linguistics <target> comprehension get <languageId>
                            .then(Commands.argument("languageId", ResourceLocationArgument.id()).suggests((ctx, sb) -> SharedSuggestionProvider.suggestResource(BookLanguagesPM.LANGUAGES.get().getKeys().stream(), sb)).executes(context -> getLanguageComprehension(context.getSource(), EntityArgument.getPlayer(context, "target"), ResourceLocationArgument.getId(context, "languageId"))))
                        )
                        .then(Commands.literal("set")
                            .then(Commands.argument("languageId", ResourceLocationArgument.id()).suggests((ctx, sb) -> SharedSuggestionProvider.suggestResource(BookLanguagesPM.LANGUAGES.get().getKeys().stream(), sb))
                                // /pm linguistics <target> comprehension set <languageId> <value>
                                .then(Commands.argument("value", LanguageComprehensionArgument.value()).executes((context) -> { return setLanguageComprehension(context.getSource(), EntityArgument.getPlayer(context, "target"), ResourceLocationArgument.getId(context, "languageId"), IntegerArgumentType.getInteger(context, "value")); }))
                            )
                        )
                    )
                )
            )
        );
        dispatcher.register(Commands.literal("pm").requires((source) -> {
            return source.hasPermission(2);
        }).redirect(node));
    }

    private static int resetAll(CommandSourceStack source, ServerPlayer player) {
        resetResearch(source, player);
        resetKnowledge(source, player);
        resetAttunements(source, player);
        resetStats(source, player);
        resetRecipes(source, player);
        resetLinguistics(source, player);
        return 0;
    }

    private static int listResearch(CommandSourceStack source, ServerPlayer target) {
        IPlayerKnowledge knowledge = PrimalMagickCapabilities.getKnowledge(target).orElse(null);
        if (knowledge == null) {
            source.sendFailure(Component.translatable("commands.primalmagick.error"));
        } else {
            // List all unlocked research entries for the target player
            Set<SimpleResearchKey> researchSet = knowledge.getResearchSet();
            String[] researchList = researchSet.stream()
                                        .map(k -> k.getRootKey())
                                        .collect(Collectors.toSet())
                                        .toArray(new String[researchSet.size()]);
            String output = String.join(", ", researchList);
            source.sendSuccess(() -> Component.translatable("commands.primalmagick.research.list", target.getName(), output), true);
        }
        return 0;
    }
    
    private static int resetResearch(CommandSourceStack source, ServerPlayer target) {
        IPlayerKnowledge knowledge = PrimalMagickCapabilities.getKnowledge(target).orElse(null);
        if (knowledge == null) {
            source.sendFailure(Component.translatable("commands.primalmagick.error"));
        } else {
            // Remove all unlocked research entries from the target player
            knowledge.clearResearch();
            ResearchManager.scheduleSync(target);
            source.sendSuccess(() -> Component.translatable("commands.primalmagick.research.reset", target.getName()), true);
            if (source.getPlayer() == null || source.getPlayer().getId() != target.getId()) {
                target.sendSystemMessage(Component.translatable("commands.primalmagick.research.reset.target", source.getTextName()));
            }
        }
        return 0;
    }
    
    private static int grantResearch(CommandSourceStack source, ServerPlayer target, ResearchInput input) {
        IPlayerKnowledge knowledge = PrimalMagickCapabilities.getKnowledge(target).orElse(null);
        SimpleResearchKey key = input.getKey();
        if (knowledge == null) {
            source.sendFailure(Component.translatable("commands.primalmagick.error"));
        } else if (ResearchEntries.getEntry(key) == null) {
            source.sendFailure(Component.translatable("commands.primalmagick.research.noexist", key.toString()));
        } else {
            // Grant the specified research to the target player, along with all its parents
            ResearchManager.forceGrantWithAllParents(target, key);
            source.sendSuccess(() -> Component.translatable("commands.primalmagick.research.grant", target.getName(), key.toString()), true);
            if (source.getPlayer() == null || source.getPlayer().getId() != target.getId()) {
                target.sendSystemMessage(Component.translatable("commands.primalmagick.research.grant.target", source.getTextName(), key.toString()));
            }
        }
        return 0;
    }
    
    private static int grantResearchParents(CommandSourceStack source, ServerPlayer target, ResearchInput input) {
        IPlayerKnowledge knowledge = PrimalMagickCapabilities.getKnowledge(target).orElse(null);
        SimpleResearchKey key = input.getKey();
        if (knowledge == null) {
            source.sendFailure(Component.translatable("commands.primalmagick.error"));
        } else if (ResearchEntries.getEntry(key) == null) {
            source.sendFailure(Component.translatable("commands.primalmagick.research.noexist", key.toString()));
        } else {
            // Grant the parents of the specified research to the target player, but not the research itself
            ResearchManager.forceGrantParentsOnly(target, key);
            source.sendSuccess(() -> Component.translatable("commands.primalmagick.research.grant_parents", target.getName(), key.toString()), true);
            if (source.getPlayer() == null || source.getPlayer().getId() != target.getId()) {
                target.sendSystemMessage(Component.translatable("commands.primalmagick.research.grant_parents.target", source.getTextName(), key.toString()));
            }
        }
        return 0;
    }
    
    private static int grantAllResearch(CommandSourceStack source, ServerPlayer target) {
        IPlayerKnowledge knowledge = PrimalMagickCapabilities.getKnowledge(target).orElse(null);
        if (knowledge == null) {
            source.sendFailure(Component.translatable("commands.primalmagick.error"));
        } else {
            // Grant all research entries to the target player
            ResearchManager.forceGrantAll(target);
            source.sendSuccess(() -> Component.translatable("commands.primalmagick.research.grant_all", target.getName()), true);
            if (source.getPlayer() == null || source.getPlayer().getId() != target.getId()) {
                target.sendSystemMessage(Component.translatable("commands.primalmagick.research.grant_all.target", source.getTextName()));
            }
        }
        return 0;
    }
    
    private static int revokeResearch(CommandSourceStack source, ServerPlayer target, ResearchInput input) {
        IPlayerKnowledge knowledge = PrimalMagickCapabilities.getKnowledge(target).orElse(null);
        SimpleResearchKey key = input.getKey();
        if (knowledge == null) {
            source.sendFailure(Component.translatable("commands.primalmagick.error"));
        } else if (ResearchEntries.getEntry(key) == null) {
            source.sendFailure(Component.translatable("commands.primalmagick.research.noexist", key.toString()));
        } else {
            // Revoke the specified research from the target player, along with all its children
            ResearchManager.forceRevokeWithAllChildren(target, key);
            source.sendSuccess(() -> Component.translatable("commands.primalmagick.research.revoke", target.getName(), key.toString()), true);
            if (source.getPlayer() == null || source.getPlayer().getId() != target.getId()) {
                target.sendSystemMessage(Component.translatable("commands.primalmagick.research.revoke.target", source.getTextName(), key.toString()));
            }
        }
        return 0;
    }
    
    private static int detailResearch(CommandSourceStack source, ServerPlayer target, ResearchInput input) {
        IPlayerKnowledge knowledge = PrimalMagickCapabilities.getKnowledge(target).orElse(null);
        SimpleResearchKey key = input.getKey();
        if (knowledge == null) {
            source.sendFailure(Component.translatable("commands.primalmagick.error"));
        } else if (ResearchEntries.getEntry(key) == null) {
            source.sendFailure(Component.translatable("commands.primalmagick.research.noexist", key.toString()));
        } else {
            // List the status, current stage, and attached flags of the given research for the target player
            IPlayerKnowledge.ResearchStatus status = knowledge.getResearchStatus(key);
            int stage = knowledge.getResearchStage(key);
            Set<IPlayerKnowledge.ResearchFlag> flagSet = knowledge.getResearchFlags(key);
            String[] flagStrs = flagSet.stream()
                                    .map(f -> f.name())
                                    .collect(Collectors.toSet())
                                    .toArray(new String[flagSet.size()]);
            String flagOutput = (flagStrs.length == 0) ? "none" : String.join(", ", flagStrs);
            source.sendSuccess(() -> Component.translatable("commands.primalmagick.research.details.1", key.toString(), target.getName()), true);
            source.sendSuccess(() -> Component.translatable("commands.primalmagick.research.details.2", status.name()), true);
            source.sendSuccess(() -> Component.translatable("commands.primalmagick.research.details.3", stage), true);
            source.sendSuccess(() -> Component.translatable("commands.primalmagick.research.details.4", flagOutput), true);
        }
        return 0;
    }
    
    private static int progressResearch(CommandSourceStack source, ServerPlayer target, ResearchInput input) {
        IPlayerKnowledge knowledge = PrimalMagickCapabilities.getKnowledge(target).orElse(null);
        SimpleResearchKey key = input.getKey();
        if (knowledge == null) {
            source.sendFailure(Component.translatable("commands.primalmagick.error"));
        } else if (ResearchEntries.getEntry(key) == null) {
            source.sendFailure(Component.translatable("commands.primalmagick.research.noexist", key.toString()));
        } else {
            // Advance the given research to its next stage for the target player
            int oldStage = knowledge.getResearchStage(key);
            if (ResearchManager.progressResearch(target, key)) {
                int newStage = knowledge.getResearchStage(key);
                source.sendSuccess(() -> Component.translatable("commands.primalmagick.research.progress.success", key.toString(), target.getName(), oldStage, newStage), true);
                if (source.getPlayer() == null || source.getPlayer().getId() != target.getId()) {
                    target.sendSystemMessage(Component.translatable("commands.primalmagick.research.progress.target", key.toString(), source.getTextName(), oldStage, newStage));
                }
            } else {
                source.sendSuccess(() -> Component.translatable("commands.primalmagick.research.progress.failure", key.toString(), oldStage), true);
            }
        }
        return 0;
    }
    
    private static int resetKnowledge(CommandSourceStack source, ServerPlayer target) {
        IPlayerKnowledge knowledge = PrimalMagickCapabilities.getKnowledge(target).orElse(null);
        if (knowledge == null) {
            source.sendFailure(Component.translatable("commands.primalmagick.error"));
        } else {
            // Remove all observations and theories from the target player
            knowledge.clearKnowledge();
            source.sendSuccess(() -> Component.translatable("commands.primalmagick.knowledge.reset", target.getName()), true);
            if (source.getPlayer() == null || source.getPlayer().getId() != target.getId()) {
                target.sendSystemMessage(Component.translatable("commands.primalmagick.knowledge.reset.target", source.getTextName()));
            }
        }
        return 0;
    }
    
    private static int getKnowledge(CommandSourceStack source, ServerPlayer target, KnowledgeTypeInput knowledgeTypeInput) {
        IPlayerKnowledge knowledge = PrimalMagickCapabilities.getKnowledge(target).orElse(null);
        KnowledgeType type = knowledgeTypeInput.getType();
        if (knowledge == null) {
            source.sendFailure(Component.translatable("commands.primalmagick.error"));
        } else if (type == null) {
            source.sendFailure(Component.translatable("commands.primalmagick.knowledge_type.noexist"));
        } else {
            // Get the current levels and points for the given knowledge type for the target player
            int levels = knowledge.getKnowledge(type);
            int points = knowledge.getKnowledgeRaw(type);
            source.sendSuccess(() -> Component.translatable("commands.primalmagick.knowledge.get", target.getName(), levels, type.name(), points), true);
        }
        return 0;
    }
    
    private static int addKnowledge(CommandSourceStack source, ServerPlayer target, KnowledgeTypeInput knowledgeTypeInput, int points) {
        IPlayerKnowledge knowledge = PrimalMagickCapabilities.getKnowledge(target).orElse(null);
        KnowledgeType type = knowledgeTypeInput.getType();
        if (knowledge == null) {
            source.sendFailure(Component.translatable("commands.primalmagick.error"));
        } else if (type == null) {
            source.sendFailure(Component.translatable("commands.primalmagick.knowledge_type.noexist"));
        } else {
            // Add the given number of points to the given knowledge type for the target player
            if (ResearchManager.addKnowledge(target, type, points)) {
                source.sendSuccess(() -> Component.translatable("commands.primalmagick.knowledge.add.success", points, type.name(), target.getName()), true);
                if (source.getPlayer() == null || source.getPlayer().getId() != target.getId()) {
                    target.sendSystemMessage(Component.translatable("commands.primalmagick.knowledge.add.target", points, type.name(), source.getTextName()));
                }
            } else {
                source.sendSuccess(() -> Component.translatable("commands.primalmagick.knowledge.add.failure", target.getName()), true);
            }
        }
        return 0;
    }
    
    private static int grantScanResearch(CommandSourceStack source, ServerPlayer target, ItemInput item) {
        ItemStack stack;
        try {
            stack = item.createItemStack(1, false);
        } catch (CommandSyntaxException e) {
            source.sendFailure(Component.translatable("commands.primalmagick.scans.grant.failure", target.getName()));
            return 0;
        }
        // Scan the given item for the target player and grant them its research
        if (ResearchManager.setScanned(stack, target)) {
            source.sendSuccess(() -> Component.translatable("commands.primalmagick.scans.grant.success", target.getName(), ForgeRegistries.ITEMS.getKey(item.getItem()).toString()), true);
            if (source.getPlayer() == null || source.getPlayer().getId() != target.getId()) {
                target.sendSystemMessage(Component.translatable("commands.primalmagick.scans.grant.target", source.getTextName(), ForgeRegistries.ITEMS.getKey(item.getItem()).toString()));
            }
        } else {
            source.sendFailure(Component.translatable("commands.primalmagick.scans.grant.failure", target.getName()));            
        }
        return 0;
    }
    
    private static int grantAllScanResearch(CommandSourceStack source, ServerPlayer target) {
        // Scan all possible items for the target player and grant them their research
        int count = ResearchManager.setAllScanned(target);
        source.sendSuccess(() -> Component.translatable("commands.primalmagick.scans.grant_all", count, target.getName()), true);
        if (source.getPlayer() == null || source.getPlayer().getId() != target.getId()) {
            target.sendSystemMessage(Component.translatable("commands.primalmagick.scans.grant_all.target", count, source.getTextName()));
        }
        return 0;
    }

    private static int listUnlockedSources(CommandSourceStack source, ServerPlayer target) {
        // List the unlocked sources for the target player in prescribed order
        List<String> unlockedTags = Source.SORTED_SOURCES.stream()
                                        .filter((s) -> s.isDiscovered(target))
                                        .map((s) -> s.getTag().toUpperCase())
                                        .collect(Collectors.toList());
        String tagStr = String.join(", ", unlockedTags);
        source.sendSuccess(() -> Component.translatable("commands.primalmagick.sources.list", target.getName(), tagStr), true);
        return 0;
    }

    private static int unlockAllSources(CommandSourceStack source, ServerPlayer target) {
        // Grant the target player the unlock research for all sources.  Can't do this with grantResearch because source unlocks aren't in the grimoire.
        int unlocked = 0;
        for (Source toUnlock : Source.SOURCES.values()) {
            if (!toUnlock.isDiscovered(target)) {
                if (ResearchManager.completeResearch(target, toUnlock.getDiscoverKey())) {
                    unlocked++;
                }
            }
        }
        final int totalUnlocked = unlocked;
        source.sendSuccess(() -> Component.translatable("commands.primalmagick.sources.unlock_all", target.getName(), totalUnlocked), true);
        if (source.getPlayer() == null || source.getPlayer().getId() != target.getId()) {
            target.sendSystemMessage(Component.translatable("commands.primalmagick.sources.unlock_all.target", source.getTextName(), totalUnlocked));
        }
        return 0;
    }

    private static int unlockSource(CommandSourceStack source, ServerPlayer target, SourceInput input) {
        // Grant the target player the unlock research for the given source.  Can't do this with grantResearch because source unlocks aren't in the grimoire.
        String tag = input.getSourceTag();
        Source toUnlock = Source.getSource(tag.toLowerCase());
        if (toUnlock == null) {
            source.sendFailure(Component.translatable("commands.primalmagick.source.noexist", tag));
        } else if (toUnlock.isDiscovered(target)) {
            source.sendFailure(Component.translatable("commands.primalmagick.sources.unlock.already_unlocked", target.getName(), tag.toUpperCase()));
        } else if (ResearchManager.completeResearch(target, toUnlock.getDiscoverKey())) {
            source.sendSuccess(() -> Component.translatable("commands.primalmagick.sources.unlock.success", target.getName(), tag.toUpperCase()), true);
            if (source.getPlayer() == null || source.getPlayer().getId() != target.getId()) {
                target.sendSystemMessage(Component.translatable("commands.primalmagick.sources.unlock.target", source.getTextName(), tag.toUpperCase()));
            }
        } else {
            source.sendFailure(Component.translatable("commands.primalmagick.sources.unlock.failure", target.getName(), tag.toUpperCase()));
        }
        return 0;
    }

    private static int getStatValue(CommandSourceStack source, ServerPlayer target, ResourceLocation statLoc) {
        // Look up the requested stat value for the given player and display it
        Stat stat = StatsManager.getStat(statLoc);
        if (stat == null) {
            source.sendFailure(Component.translatable("commands.primalmagick.stats.noexist", statLoc));
        } else {
            Component statName = Component.translatable(stat.getTranslationKey());
            Component statValue = StatsManager.getFormattedValue(target, stat);
            source.sendSuccess(() -> Component.translatable("commands.primalmagick.stats.get", target.getName(), statName, statValue), true);
        }
        return 0;
    }

    private static int setStatValue(CommandSourceStack source, ServerPlayer target, ResourceLocation statLoc, int value) {
        // Set the given value for the given stat for the given player
        Stat stat = StatsManager.getStat(statLoc);
        if (stat == null) {
            source.sendFailure(Component.translatable("commands.primalmagick.stats.noexist", statLoc));
        } else {
            StatsManager.setValue(target, stat, value);
            Component statName = Component.translatable(stat.getTranslationKey());
            Component statValue = StatsManager.getFormattedValue(target, stat);
            source.sendSuccess(() -> Component.translatable("commands.primalmagick.stats.set", target.getName(), statName, statValue), true);
            if (source.getPlayer() == null || source.getPlayer().getId() != target.getId()) {
                target.sendSystemMessage(Component.translatable("commands.primalmagick.stats.set.target", source.getTextName(), statName, statValue));
            }
        }
        return 0;
    }

    private static int resetStats(CommandSourceStack source, ServerPlayer target) {
        // Remove all accrued stats from the player
        IPlayerStats stats = PrimalMagickCapabilities.getStats(target);
        if (stats == null) {
            source.sendFailure(Component.translatable("commands.primalmagick.error"));
        } else {
            stats.clear();
            StatsManager.scheduleSync(target);
            source.sendSuccess(() -> Component.translatable("commands.primalmagick.stats.reset", target.getName()), true);
            if (source.getPlayer() == null || source.getPlayer().getId() != target.getId()) {
                target.sendSystemMessage(Component.translatable("commands.primalmagick.stats.reset.target", source.getTextName()));
            }
        }
        return 0;
    }

    private static int resetAttunements(CommandSourceStack source, ServerPlayer target) {
        // Remove all accrued attunements from the player
        IPlayerAttunements attunements = PrimalMagickCapabilities.getAttunements(target);
        if (attunements == null) {
            source.sendFailure(Component.translatable("commands.primalmagick.error"));
        } else {
            attunements.clear();
            AttunementManager.removeAllAttributeModifiers(target);
            AttunementManager.scheduleSync(target);
            source.sendSuccess(() -> Component.translatable("commands.primalmagick.attunements.reset", target.getName()), true);
            if (source.getPlayer() == null || source.getPlayer().getId() != target.getId()) {
                target.sendSystemMessage(Component.translatable("commands.primalmagick.attunements.reset.target", source.getTextName()));
            }
        }
        return 0;
    }

    private static int getAttunements(CommandSourceStack source, ServerPlayer target, SourceInput input) {
        // Get the partial and total attunements for the given source for the target player
        String tag = input.getSourceTag();
        Source toQuery = Source.getSource(tag.toLowerCase());
        if (toQuery == null) {
            source.sendFailure(Component.translatable("commands.primalmagick.source.noexist", tag));
        } else {
            Component sourceText = Component.translatable(toQuery.getNameTranslationKey());
            source.sendSuccess(() -> Component.translatable("commands.primalmagick.attunements.get.total", sourceText, source.getTextName(), AttunementManager.getTotalAttunement(target, toQuery)), true);
            for (AttunementType type : AttunementType.values()) {
                Component typeText = Component.translatable(type.getNameTranslationKey());
                source.sendSuccess(() -> Component.translatable("commands.primalmagick.attunements.get.partial", typeText, AttunementManager.getAttunement(target, toQuery, type)), true);
            }
        }
        return 0;
    }

    private static int setAttunement(CommandSourceStack source, ServerPlayer target, SourceInput input, AttunementTypeInput attunementType, int value) {
        // Set the partial attunement for the target player
        String tag = input.getSourceTag();
        Source toSet = Source.getSource(tag.toLowerCase());
        AttunementType type = attunementType.getType();
        if (toSet == null) {
            source.sendFailure(Component.translatable("commands.primalmagick.source.noexist", tag));
        } else if (type == null) {
            source.sendFailure(Component.translatable("commands.primalmagick.attunement_type.noexist"));
        } else {
            AttunementManager.setAttunement(target, toSet, type, value);
            Component sourceText = Component.translatable(toSet.getNameTranslationKey());
            Component typeText = Component.translatable(type.getNameTranslationKey());
            if (type.isCapped() && value > type.getMaximum()) {
                source.sendSuccess(() -> Component.translatable("commands.primalmagick.attunements.set.success.capped", target.getName(), typeText, sourceText, type.getMaximum(), value), true);
                if (source.getPlayer() == null || source.getPlayer().getId() != target.getId()) {
                    target.sendSystemMessage(Component.translatable("commands.primalmagick.attunements.set.target.capped", target.getName(), typeText, sourceText, type.getMaximum(), value));
                }
            } else {
                source.sendSuccess(() -> Component.translatable("commands.primalmagick.attunements.set.success", target.getName(), typeText, sourceText, value), true);
                if (source.getPlayer() == null || source.getPlayer().getId() != target.getId()) {
                    target.sendSystemMessage(Component.translatable("commands.primalmagick.attunements.set.target", target.getName(), typeText, sourceText, value));
                }
            }
        }
        return 0;
    }

    

    /**
     * getSourcelessItems iterates all item recipes and reports a JSON list of any that render to 0 sources.
     * Intended to assist with modpack linting.
     */
    private static int getSourcelessItems(CommandSourceStack source, Collection<String> excludeNamespaces) {
        ServerPlayer target = source.getPlayer();

        Logger LOGGER = LogManager.getLogger();

        ServerLevel level = source.getLevel();
        net.minecraft.world.item.crafting.RecipeManager recipeManager = source.getRecipeManager();
        RegistryAccess registryAccess = source.registryAccess();

        List<Item> sourcelessItems = listSourcelessItems(recipeManager, registryAccess, level, excludeNamespaces);

        String excludeNote = "";
        if ( (excludeNamespaces != null) && (excludeNamespaces.size() >0)) {
            excludeNote = " excluding resource namespaces: "+ String.join(", ", excludeNamespaces);
        }

        target.sendSystemMessage(Component.literal("Found " + Integer.toString(sourcelessItems.size()) + " items without sources"+ excludeNote + "; check system logs for details"), false);

        // note: technically this could result in a list with null elements. which is noncommunicative.
        LOGGER.info("Items with no sources: " + sourcelessItems.stream().map(item -> ForgeRegistries.ITEMS.getKey(item)).toList().toString());

        return 0;
    }

    private static int writeSourcelessItemDatapack(CommandSourceStack source, Collection<String> excludeNamespaces) {
        Logger LOGGER = LogManager.getLogger();
        ServerPlayer target = source.getPlayer();
        ServerLevel level = source.getLevel();
        net.minecraft.world.item.crafting.RecipeManager recipeManager = source.getRecipeManager();
        RegistryAccess registryAccess = source.registryAccess();

        List<Item> sourcelessItems = listSourcelessItems(recipeManager, registryAccess, level, excludeNamespaces);
        List<EntityType<?>> sourcelessEntities = listSourcelessEntityTypes(registryAccess, excludeNamespaces);

        byte[] itemsToDataPackTemplate;
        try {
            itemsToDataPackTemplate = DataPackUtils.ItemsToDataPackTemplate(sourcelessItems, sourcelessEntities);
        } catch (IOException e){
            LOGGER.atError().withThrowable(e).log("unable to generate datapack");
            return 1;
        }

        try {
            File tempFile = File.createTempFile("primalMagickDataPack", ".zip");
            String filePath = tempFile.getAbsolutePath();

            FileOutputStream fos = new FileOutputStream(tempFile);

            fos.write(itemsToDataPackTemplate);
            fos.close();

            // Being very careful not to make this a tool for users to use to enumerate server attributes.
            target.sendSystemMessage(Component.literal("Wrote datapack template for sourceless items and entities to disk; check system logs for location."));
            LOGGER.atInfo().log("Wrote Datapack to "+ filePath );
        } catch (IOException e) {
            LOGGER.atError().withThrowable(e).log("unable to write datapack");
            return 1;
        }

        return 0;
    }
    
    private static List<EntityType<?>> listSourcelessEntityTypes(RegistryAccess registryAccess, Collection<String> excludeNamespaces) {
        AffinityManager am = AffinityManager.getOrCreateInstance();
        Vector<EntityType<?>> retVal = new Vector<>();
        
        ForgeRegistries.ENTITY_TYPES.forEach(entityType -> {
            ResourceLocation resourceLocation = ForgeRegistries.ENTITY_TYPES.getKey(entityType);
            if (resourceLocation == null) {
                // If the Item can't be resolved in registry, it's got problems I can't care about.
                return;
            }
            String namespace = resourceLocation.getNamespace();
            if (excludeNamespaces != null && excludeNamespaces.contains(namespace)) {
                return;
            }
            
            CompletableFuture<SourceList> future = am.getAffinityValuesAsync(entityType, registryAccess);
            try {
                SourceList sources = future.get();
                if (sources.isEmpty()) {
                    retVal.add(entityType);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        
        return retVal;
    }

    private static List<Item> listSourcelessItems(net.minecraft.world.item.crafting.RecipeManager recipeManager, RegistryAccess registryAccess, ServerLevel level, Collection<String> excludeNamespaces ) {
        AffinityManager am = AffinityManager.getOrCreateInstance();

        Vector<Item> items = new Vector<Item>();
        ForgeRegistries.ITEMS.forEach( (item) -> {
                ItemStack stack = item.getDefaultInstance();

                ResourceLocation resourceLocation = ForgeRegistries.ITEMS.getKey(item);
                if (resourceLocation == null) {
                    // If the Item can't be resolved in registry, it's got problems I can't care about.
                    return;
                }
                String namespace = resourceLocation.getNamespace();
                if (excludeNamespaces != null && excludeNamespaces.contains(namespace)){
                    return;
                }

                CompletableFuture<SourceList> f = am.getAffinityValuesAsync(stack, level);

                try {
                    SourceList sources = f.get();
                    if (sources.isEmpty()){
                        if (getRecipeCountForItem(recipeManager, registryAccess, item) == 0) {
                            items.add(item);
                        }
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        );

        return items;
    }

    private static long getRecipeCountForItem(net.minecraft.world.item.crafting.RecipeManager recipeManager, RegistryAccess registryAccess, Item item){

        long count = recipeManager.getRecipes().stream()
            .map(RecipeHolder::value)
            .filter(r -> r.getResultItem(registryAccess) != null && (r.getResultItem(registryAccess).getItem().equals(item)))
            .count()
        ;
        return count; 
    }

    private static int resetRecipes(CommandSourceStack source, ServerPlayer target) {
        IPlayerArcaneRecipeBook recipeBook = PrimalMagickCapabilities.getArcaneRecipeBook(target).orElse(null);
        if (recipeBook == null) {
            source.sendFailure(Component.translatable("commands.primalmagick.error"));
        } else {
            recipeBook.get().clear();
            ArcaneRecipeBookManager.scheduleSync(target);
            source.sendSuccess(() -> Component.translatable("commands.primalmagick.recipes.reset", target.getName()), true);
            if (source.getPlayer() == null || source.getPlayer().getId() != target.getId()) {
                target.sendSystemMessage(Component.translatable("commands.primalmagick.recipes.reset.target", source.getTextName()));
            }
        }
        return 0;
    }

    private static int listArcaneRecipes(CommandSourceStack source, ServerPlayer target) {
        IPlayerArcaneRecipeBook recipeBook = PrimalMagickCapabilities.getArcaneRecipeBook(target).orElse(null);
        if (recipeBook == null) {
            source.sendFailure(Component.translatable("commands.primalmagick.error"));
        } else {
            // List all known arcane research book entries for the target player
            Set<ResourceLocation> knownSet = recipeBook.get().getKnown();
            String[] knownList = knownSet.stream()
                                        .map(r -> r.toString())
                                        .collect(Collectors.toSet())
                                        .toArray(new String[knownSet.size()]);
            String knownOutput = String.join(", ", knownList);
            source.sendSuccess(() -> Component.translatable("commands.primalmagick.recipes.list.known", target.getName(), knownOutput), true);

            // List all highlighted arcane research book entries for the target player
            Set<ResourceLocation> highlightSet = recipeBook.get().getHighlight();
            String[] highlightList = highlightSet.stream()
                                        .map(r -> r.toString())
                                        .collect(Collectors.toSet())
                                        .toArray(new String[highlightSet.size()]);
            String highlightOutput = String.join(", ", highlightList);
            source.sendSuccess(() -> Component.translatable("commands.primalmagick.recipes.list.highlight", target.getName(), highlightOutput), true);
        }
        return 0;
    }
    
    private static int syncArcaneRecipes(CommandSourceStack source, ServerPlayer target) {
        if (!ArcaneRecipeBookManager.syncRecipesWithResearch(target)) {
            source.sendFailure(Component.translatable("commands.primalmagick.error"));
        } else {
            source.sendSuccess(() -> Component.translatable("commands.primalmagick.recipes.sync", target.getName()), true);
            if (source.getPlayer() == null || source.getPlayer().getId() != target.getId()) {
                target.sendSystemMessage(Component.translatable("commands.primalmagick.recipes.sync.target", source.getTextName()));
            }
        }
        return 0;
    }

    private static int addArcaneRecipe(CommandSourceStack source, ServerPlayer target, RecipeHolder<?> recipe) {
        IPlayerArcaneRecipeBook recipeBook = PrimalMagickCapabilities.getArcaneRecipeBook(target).orElse(null);
        if (recipeBook == null) {
            source.sendFailure(Component.translatable("commands.primalmagick.error"));
        } else if (!(recipe.value() instanceof IArcaneRecipeBookItem)) {
            source.sendFailure(Component.translatable("commands.primalmagick.recipes.recipe_not_arcane"));
        } else {
            ArcaneRecipeBookManager.addRecipes(Collections.singletonList(recipe), target);
            source.sendSuccess(() -> Component.translatable("commands.primalmagick.recipes.add", target.getName(), recipe.id().toString()), true);
            if (source.getPlayer() == null || source.getPlayer().getId() != target.getId()) {
                target.sendSystemMessage(Component.translatable("commands.primalmagick.recipes.add.target", source.getTextName(), recipe.id().toString()));
            }
        }
        return 0;
    }

    private static int removeArcaneRecipe(CommandSourceStack source, ServerPlayer target, RecipeHolder<?> recipe) {
        IPlayerArcaneRecipeBook recipeBook = PrimalMagickCapabilities.getArcaneRecipeBook(target).orElse(null);
        if (recipeBook == null) {
            source.sendFailure(Component.translatable("commands.primalmagick.error"));
        } else if (!(recipe.value() instanceof IArcaneRecipeBookItem)) {
            source.sendFailure(Component.translatable("commands.primalmagick.recipes.recipe_not_arcane"));
        } else {
            ArcaneRecipeBookManager.removeRecipes(Collections.singletonList(recipe), target);
            source.sendSuccess(() -> Component.translatable("commands.primalmagick.recipes.remove", target.getName(), recipe.id().toString()), true);
            if (source.getPlayer() == null || source.getPlayer().getId() != target.getId()) {
                target.sendSystemMessage(Component.translatable("commands.primalmagick.recipes.remove.target", source.getTextName(), recipe.id().toString()));
            }
        }
        return 0;
    }

    private static int giveBook(CommandSourceStack source, Collection<ServerPlayer> targets, ResourceLocation bookId, ResourceLocation bookLanguageId, OptionalInt comprehension) {
        if (!BooksPM.BOOKS.get().containsKey(bookId)) {
            source.sendFailure(Component.translatable("commands.primalmagick.books.noexist", bookId.toString()));
        } else if (!BookLanguagesPM.LANGUAGES.get().containsKey(bookLanguageId)) { 
            source.sendFailure(Component.translatable("commands.primalmagick.books.nolanguage", bookLanguageId.toString()));
        } else {
            ItemStack bookStack = new ItemStack(ItemsPM.STATIC_BOOK.get());
            StaticBookItem.setBookDefinition(bookStack, BooksPM.BOOKS.get().getValue(bookId));
            StaticBookItem.setBookLanguage(bookStack, BookLanguagesPM.LANGUAGES.get().getValue(bookLanguageId));
            StaticBookItem.setGeneration(bookStack, 0);
            StaticBookItem.setTranslatedComprehension(bookStack, comprehension);
            
            for (ServerPlayer serverPlayer : targets) {
                ItemStack bookCopy = bookStack.copy();
                if (!serverPlayer.getInventory().add(bookCopy)) {
                    ItemEntity bookEntity = serverPlayer.drop(bookCopy, false);
                    if (bookEntity != null) {
                        bookEntity.setNoPickUpDelay();
                        bookEntity.setTarget(serverPlayer.getUUID());
                    }
                }
            }
            
            if (targets.size() == 1) {
                source.sendSuccess(() -> Component.translatable("commands.give.success.single", 1, bookStack.getDisplayName(), targets.iterator().next().getDisplayName()), true);
            } else {
                source.sendSuccess(() -> Component.translatable("commands.give.success.multiple", 1, bookStack.getDisplayName(), targets.size()), true);
            }
        }
        return targets.size();
    }

    private static int resetLinguistics(CommandSourceStack source, ServerPlayer target) {
        IPlayerLinguistics linguistics = PrimalMagickCapabilities.getLinguistics(target).orElse(null);
        if (linguistics == null) {
            source.sendFailure(Component.translatable("commands.primalmagick.error"));
        } else {
            // Remove all unlocked linguistics data from the target player
            linguistics.clear();
            LinguisticsManager.scheduleSync(target);
            source.sendSuccess(() -> Component.translatable("commands.primalmagick.linguistics.reset", target.getName()), true);
            if (source.getPlayer() == null || source.getPlayer().getId() != target.getId()) {
                target.sendSystemMessage(Component.translatable("commands.primalmagick.linguistics.reset.target", source.getTextName()));
            }
        }
        return 0;
    }

    private static int getLanguageComprehension(CommandSourceStack source, ServerPlayer target, ResourceLocation bookLanguageId) {
        if (!BookLanguagesPM.LANGUAGES.get().containsKey(bookLanguageId)) {
            source.sendFailure(Component.translatable("commands.primalmagick.books.nolanguage", bookLanguageId.toString()));
        } else {
            BookLanguage lang = BookLanguagesPM.LANGUAGES.get().getValue(bookLanguageId);
            source.sendSuccess(() -> Component.translatable("commands.primalmagick.linguistics.comprehension.get", target.getName(), bookLanguageId, LinguisticsManager.getComprehension(target, lang)), true);
        }
        return 0;
    }

    private static int setLanguageComprehension(CommandSourceStack source, ServerPlayer target, ResourceLocation bookLanguageId, int value) {
        if (!BookLanguagesPM.LANGUAGES.get().containsKey(bookLanguageId)) {
            source.sendFailure(Component.translatable("commands.primalmagick.books.nolanguage", bookLanguageId.toString()));
        } else {
            BookLanguage lang = BookLanguagesPM.LANGUAGES.get().getValue(bookLanguageId);
            LinguisticsManager.setComprehension(target, lang, value);
            int newValue = LinguisticsManager.getComprehension(target, lang);
            if (value > lang.complexity()) {
                source.sendSuccess(() -> Component.translatable("commands.primalmagick.linguistics.comprehension.set.success.capped", target.getName(), bookLanguageId, lang.complexity(), newValue), true);
                if (source.getPlayer() == null || source.getPlayer().getId() != target.getId()) {
                    target.sendSystemMessage(Component.translatable("commands.primalmagick.linguistics.comprehension.set.target.capped", source.getTextName(), bookLanguageId, lang.complexity(), newValue));
                }
            } else {
                source.sendSuccess(() -> Component.translatable("commands.primalmagick.linguistics.comprehension.set.success", target.getName(), bookLanguageId, newValue), true);
                if (source.getPlayer() == null || source.getPlayer().getId() != target.getId()) {
                    target.sendSystemMessage(Component.translatable("commands.primalmagick.linguistics.comprehension.set.target", source.getTextName(), bookLanguageId, newValue));
                }
            }
        }
        return 0;
    }

    private static int explainItemAffinity(CommandSourceStack source, ItemInput item) {
        // Get the affinity data for the item
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(item.getItem());
        IAffinity affinityData = AffinityManager.getInstance().getOrGenerateItemAffinityAsync(itemId, source.getRecipeManager(), source.registryAccess(), new ArrayList<>()).join();
        if (affinityData instanceof ItemAffinity itemAffinity) {
            itemAffinity.getSourceRecipe().ifPresentOrElse(recipeLoc -> {
                source.sendSuccess(() -> Component.translatable("commands.primalmagick.affinities.explain.from_recipe", itemId.toString(), recipeLoc.toString()), true);
            }, () -> {
                source.sendSuccess(() -> Component.translatable("commands.primalmagick.affinities.explain.from_data", itemId.toString()), true);
            });
        } else {
            source.sendFailure(Component.translatable("commands.primalmagick.affinities.explain.not_found", itemId.toString()));
        }
                
        return 0;
    }
}
