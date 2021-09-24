package com.verdantartifice.primalmagic.common.commands;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.verdantartifice.primalmagic.common.attunements.AttunementManager;
import com.verdantartifice.primalmagic.common.attunements.AttunementType;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerAttunements;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerStats;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.commands.arguments.AttunementTypeArgument;
import com.verdantartifice.primalmagic.common.commands.arguments.AttunementTypeInput;
import com.verdantartifice.primalmagic.common.commands.arguments.AttunementValueArgument;
import com.verdantartifice.primalmagic.common.commands.arguments.KnowledgeAmountArgument;
import com.verdantartifice.primalmagic.common.commands.arguments.KnowledgeTypeArgument;
import com.verdantartifice.primalmagic.common.commands.arguments.KnowledgeTypeInput;
import com.verdantartifice.primalmagic.common.commands.arguments.ResearchArgument;
import com.verdantartifice.primalmagic.common.commands.arguments.ResearchInput;
import com.verdantartifice.primalmagic.common.commands.arguments.SourceArgument;
import com.verdantartifice.primalmagic.common.commands.arguments.SourceInput;
import com.verdantartifice.primalmagic.common.commands.arguments.StatValueArgument;
import com.verdantartifice.primalmagic.common.research.ResearchEntries;
import com.verdantartifice.primalmagic.common.research.ResearchManager;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.stats.Stat;
import com.verdantartifice.primalmagic.common.stats.StatsManager;

import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

/**
 * Definition of the /primalmagic debug command and its /pm alias.
 * 
 * @author Daedalus4096
 */
public class PrimalMagicCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralCommandNode<CommandSourceStack> node = dispatcher.register(Commands.literal("primalmagic")
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
                        .then(Commands.argument("item", ItemArgument.item()).executes((context) -> { return grantScanResearch(context.getSource(), EntityArgument.getPlayer(context, "target"), ItemArgument.getItem(context, "item")); }))
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
        return 0;
    }

    private static int listResearch(CommandSourceStack source, ServerPlayer target) {
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(target).orElse(null);
        if (knowledge == null) {
            source.sendFailure(new TranslatableComponent("commands.primalmagic.error"));
        } else {
            // List all unlocked research entries for the target player
            Set<SimpleResearchKey> researchSet = knowledge.getResearchSet();
            String[] researchList = researchSet.stream()
                                        .map(k -> k.getRootKey())
                                        .collect(Collectors.toSet())
                                        .toArray(new String[researchSet.size()]);
            String output = String.join(", ", researchList);
            source.sendSuccess(new TranslatableComponent("commands.primalmagic.research.list", target.getName(), output), true);
        }
        return 0;
    }
    
    private static int resetResearch(CommandSourceStack source, ServerPlayer target) {
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(target).orElse(null);
        if (knowledge == null) {
            source.sendFailure(new TranslatableComponent("commands.primalmagic.error"));
        } else {
            // Remove all unlocked research entries from the target player
            knowledge.clearResearch();
            ResearchManager.scheduleSync(target);
            source.sendSuccess(new TranslatableComponent("commands.primalmagic.research.reset", target.getName()), true);
            target.sendMessage(new TranslatableComponent("commands.primalmagic.research.reset.target", source.getTextName()), Util.NIL_UUID);
        }
        return 0;
    }
    
    private static int grantResearch(CommandSourceStack source, ServerPlayer target, ResearchInput input) {
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(target).orElse(null);
        SimpleResearchKey key = input.getKey();
        if (knowledge == null) {
            source.sendFailure(new TranslatableComponent("commands.primalmagic.error"));
        } else if (ResearchEntries.getEntry(key) == null) {
            source.sendFailure(new TranslatableComponent("commands.primalmagic.research.noexist", key.toString()));
        } else {
            // Grant the specified research to the target player, along with all its parents
            ResearchManager.forceGrantWithAllParents(target, key);
            source.sendSuccess(new TranslatableComponent("commands.primalmagic.research.grant", target.getName(), key.toString()), true);
            target.sendMessage(new TranslatableComponent("commands.primalmagic.research.grant.target", source.getTextName(), key.toString()), Util.NIL_UUID);
        }
        return 0;
    }
    
    private static int grantResearchParents(CommandSourceStack source, ServerPlayer target, ResearchInput input) {
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(target).orElse(null);
        SimpleResearchKey key = input.getKey();
        if (knowledge == null) {
            source.sendFailure(new TranslatableComponent("commands.primalmagic.error"));
        } else if (ResearchEntries.getEntry(key) == null) {
            source.sendFailure(new TranslatableComponent("commands.primalmagic.research.noexist", key.toString()));
        } else {
            // Grant the parents of the specified research to the target player, but not the research itself
            ResearchManager.forceGrantParentsOnly(target, key);
            source.sendSuccess(new TranslatableComponent("commands.primalmagic.research.grant_parents", target.getName(), key.toString()), true);
            target.sendMessage(new TranslatableComponent("commands.primalmagic.research.grant_parents.target", source.getTextName(), key.toString()), Util.NIL_UUID);
        }
        return 0;
    }
    
    private static int grantAllResearch(CommandSourceStack source, ServerPlayer target) {
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(target).orElse(null);
        if (knowledge == null) {
            source.sendFailure(new TranslatableComponent("commands.primalmagic.error"));
        } else {
            // Grant all research entries to the target player
            ResearchManager.forceGrantAll(target);
            source.sendSuccess(new TranslatableComponent("commands.primalmagic.research.grant_all", target.getName()), true);
            target.sendMessage(new TranslatableComponent("commands.primalmagic.research.grant_all.target", source.getTextName()), Util.NIL_UUID);
        }
        return 0;
    }
    
    private static int revokeResearch(CommandSourceStack source, ServerPlayer target, ResearchInput input) {
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(target).orElse(null);
        SimpleResearchKey key = input.getKey();
        if (knowledge == null) {
            source.sendFailure(new TranslatableComponent("commands.primalmagic.error"));
        } else if (ResearchEntries.getEntry(key) == null) {
            source.sendFailure(new TranslatableComponent("commands.primalmagic.research.noexist", key.toString()));
        } else {
            // Revoke the specified research from the target player, along with all its children
            ResearchManager.forceRevokeWithAllChildren(target, key);
            source.sendSuccess(new TranslatableComponent("commands.primalmagic.research.revoke", target.getName(), key.toString()), true);
            target.sendMessage(new TranslatableComponent("commands.primalmagic.research.revoke.target", source.getTextName(), key.toString()), Util.NIL_UUID);
        }
        return 0;
    }
    
    private static int detailResearch(CommandSourceStack source, ServerPlayer target, ResearchInput input) {
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(target).orElse(null);
        SimpleResearchKey key = input.getKey();
        if (knowledge == null) {
            source.sendFailure(new TranslatableComponent("commands.primalmagic.error"));
        } else if (ResearchEntries.getEntry(key) == null) {
            source.sendFailure(new TranslatableComponent("commands.primalmagic.research.noexist", key.toString()));
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
            source.sendSuccess(new TranslatableComponent("commands.primalmagic.research.details.1", key.toString(), target.getName()), true);
            source.sendSuccess(new TranslatableComponent("commands.primalmagic.research.details.2", status.name()), true);
            source.sendSuccess(new TranslatableComponent("commands.primalmagic.research.details.3", stage), true);
            source.sendSuccess(new TranslatableComponent("commands.primalmagic.research.details.4", flagOutput), true);
        }
        return 0;
    }
    
    private static int progressResearch(CommandSourceStack source, ServerPlayer target, ResearchInput input) {
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(target).orElse(null);
        SimpleResearchKey key = input.getKey();
        if (knowledge == null) {
            source.sendFailure(new TranslatableComponent("commands.primalmagic.error"));
        } else if (ResearchEntries.getEntry(key) == null) {
            source.sendFailure(new TranslatableComponent("commands.primalmagic.research.noexist", key.toString()));
        } else {
            // Advance the given research to its next stage for the target player
            int oldStage = knowledge.getResearchStage(key);
            if (ResearchManager.progressResearch(target, key)) {
                int newStage = knowledge.getResearchStage(key);
                source.sendSuccess(new TranslatableComponent("commands.primalmagic.research.progress.success", key.toString(), target.getName(), oldStage, newStage), true);
                target.sendMessage(new TranslatableComponent("commands.primalmagic.research.progress.target", key.toString(), source.getTextName(), oldStage, newStage), Util.NIL_UUID);
            } else {
                source.sendSuccess(new TranslatableComponent("commands.primalmagic.research.progress.failure", key.toString(), oldStage), true);
            }
        }
        return 0;
    }
    
    private static int resetKnowledge(CommandSourceStack source, ServerPlayer target) {
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(target).orElse(null);
        if (knowledge == null) {
            source.sendFailure(new TranslatableComponent("commands.primalmagic.error"));
        } else {
            // Remove all observations and theories from the target player
            knowledge.clearKnowledge();
            source.sendSuccess(new TranslatableComponent("commands.primalmagic.knowledge.reset", target.getName()), true);
            target.sendMessage(new TranslatableComponent("commands.primalmagic.knowledge.reset.target", source.getTextName()), Util.NIL_UUID);
        }
        return 0;
    }
    
    private static int getKnowledge(CommandSourceStack source, ServerPlayer target, KnowledgeTypeInput knowledgeTypeInput) {
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(target).orElse(null);
        IPlayerKnowledge.KnowledgeType type = knowledgeTypeInput.getType();
        if (knowledge == null) {
            source.sendFailure(new TranslatableComponent("commands.primalmagic.error"));
        } else if (type == null) {
            source.sendFailure(new TranslatableComponent("commands.primalmagic.knowledge_type.noexist"));
        } else {
            // Get the current levels and points for the given knowledge type for the target player
            int levels = knowledge.getKnowledge(type);
            int points = knowledge.getKnowledgeRaw(type);
            source.sendSuccess(new TranslatableComponent("commands.primalmagic.knowledge.get", target.getName(), levels, type.name(), points), true);
        }
        return 0;
    }
    
    private static int addKnowledge(CommandSourceStack source, ServerPlayer target, KnowledgeTypeInput knowledgeTypeInput, int points) {
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(target).orElse(null);
        IPlayerKnowledge.KnowledgeType type = knowledgeTypeInput.getType();
        if (knowledge == null) {
            source.sendFailure(new TranslatableComponent("commands.primalmagic.error"));
        } else if (type == null) {
            source.sendFailure(new TranslatableComponent("commands.primalmagic.knowledge_type.noexist"));
        } else {
            // Add the given number of points to the given knowledge type for the target player
            if (ResearchManager.addKnowledge(target, type, points)) {
                source.sendSuccess(new TranslatableComponent("commands.primalmagic.knowledge.add.success", points, type.name(), target.getName()), true);
                target.sendMessage(new TranslatableComponent("commands.primalmagic.knowledge.add.target", points, type.name(), source.getTextName()), Util.NIL_UUID);
            } else {
                source.sendSuccess(new TranslatableComponent("commands.primalmagic.knowledge.add.failure", target.getName()), true);
            }
        }
        return 0;
    }
    
    private static int grantScanResearch(CommandSourceStack source, ServerPlayer target, ItemInput item) {
        ItemStack stack;
        try {
            stack = item.createItemStack(1, false);
        } catch (CommandSyntaxException e) {
            source.sendFailure(new TranslatableComponent("commands.primalmagic.scans.grant.failure", target.getName()));
            return 0;
        }
        // Scan the given item for the target player and grant them its research
        if (ResearchManager.setScanned(stack, target)) {
            source.sendSuccess(new TranslatableComponent("commands.primalmagic.scans.grant.success", target.getName(), item.getItem().getRegistryName().toString()), true);
            target.sendMessage(new TranslatableComponent("commands.primalmagic.scans.grant.target", source.getTextName(), item.getItem().getRegistryName().toString()), Util.NIL_UUID);
        } else {
            source.sendFailure(new TranslatableComponent("commands.primalmagic.scans.grant.failure", target.getName()));            
        }
        return 0;
    }
    
    private static int grantAllScanResearch(CommandSourceStack source, ServerPlayer target) {
        // Scan all possible items for the target player and grant them their research
        int count = ResearchManager.setAllScanned(target);
        source.sendSuccess(new TranslatableComponent("commands.primalmagic.scans.grant_all", count, target.getName()), true);
        target.sendMessage(new TranslatableComponent("commands.primalmagic.scans.grant_all.target", count, source.getTextName()), Util.NIL_UUID);
        return 0;
    }

    private static int listUnlockedSources(CommandSourceStack source, ServerPlayer target) {
        // List the unlocked sources for the target player in prescribed order
        List<String> unlockedTags = Source.SORTED_SOURCES.stream()
                                        .filter((s) -> s.isDiscovered(target))
                                        .map((s) -> s.getTag().toUpperCase())
                                        .collect(Collectors.toList());
        String tagStr = String.join(", ", unlockedTags);
        source.sendSuccess(new TranslatableComponent("commands.primalmagic.sources.list", target.getName(), tagStr), true);
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
        source.sendSuccess(new TranslatableComponent("commands.primalmagic.sources.unlock_all", target.getName(), unlocked), true);
        target.sendMessage(new TranslatableComponent("commands.primalmagic.sources.unlock_all.target", source.getTextName(), unlocked), Util.NIL_UUID);
        return 0;
    }

    private static int unlockSource(CommandSourceStack source, ServerPlayer target, SourceInput input) {
        // Grant the target player the unlock research for the given source.  Can't do this with grantResearch because source unlocks aren't in the grimoire.
        String tag = input.getSourceTag();
        Source toUnlock = Source.getSource(tag.toLowerCase());
        if (toUnlock == null) {
            source.sendFailure(new TranslatableComponent("commands.primalmagic.source.noexist", tag));
        } else if (toUnlock.isDiscovered(target)) {
            source.sendFailure(new TranslatableComponent("commands.primalmagic.sources.unlock.already_unlocked", target.getName(), tag.toUpperCase()));
        } else if (ResearchManager.completeResearch(target, toUnlock.getDiscoverKey())) {
            source.sendSuccess(new TranslatableComponent("commands.primalmagic.sources.unlock.success", target.getName(), tag.toUpperCase()), true);
            target.sendMessage(new TranslatableComponent("commands.primalmagic.sources.unlock.target", source.getTextName(), tag.toUpperCase()), Util.NIL_UUID);
        } else {
            source.sendFailure(new TranslatableComponent("commands.primalmagic.sources.unlock.failure", target.getName(), tag.toUpperCase()));
        }
        return 0;
    }

    private static int getStatValue(CommandSourceStack source, ServerPlayer target, ResourceLocation statLoc) {
        // Look up the requested stat value for the given player and display it
        Stat stat = StatsManager.getStat(statLoc);
        if (stat == null) {
            source.sendFailure(new TranslatableComponent("commands.primalmagic.stats.noexist", statLoc));
        } else {
            Component statName = new TranslatableComponent(stat.getTranslationKey());
            Component statValue = StatsManager.getFormattedValue(target, stat);
            source.sendSuccess(new TranslatableComponent("commands.primalmagic.stats.get", target.getName(), statName, statValue), true);
        }
        return 0;
    }

    private static int setStatValue(CommandSourceStack source, ServerPlayer target, ResourceLocation statLoc, int value) {
        // Set the given value for the given stat for the given player
        Stat stat = StatsManager.getStat(statLoc);
        if (stat == null) {
            source.sendFailure(new TranslatableComponent("commands.primalmagic.stats.noexist", statLoc));
        } else {
            StatsManager.setValue(target, stat, value);
            Component statName = new TranslatableComponent(stat.getTranslationKey());
            Component statValue = StatsManager.getFormattedValue(target, stat);
            source.sendSuccess(new TranslatableComponent("commands.primalmagic.stats.set", target.getName(), statName, statValue), true);
            target.sendMessage(new TranslatableComponent("commands.primalmagic.stats.set.target", source.getTextName(), statName, statValue), Util.NIL_UUID);
        }
        return 0;
    }

    private static int resetStats(CommandSourceStack source, ServerPlayer target) {
        // Remove all accrued stats from the player
        IPlayerStats stats = PrimalMagicCapabilities.getStats(target);
        if (stats == null) {
            source.sendFailure(new TranslatableComponent("commands.primalmagic.error"));
        } else {
            stats.clear();
            StatsManager.scheduleSync(target);
            source.sendSuccess(new TranslatableComponent("commands.primalmagic.stats.reset", target.getName()), true);
            target.sendMessage(new TranslatableComponent("commands.primalmagic.stats.reset.target", source.getTextName()), Util.NIL_UUID);
        }
        return 0;
    }

    private static int resetAttunements(CommandSourceStack source, ServerPlayer target) {
        // Remove all accrued attunements from the player
        IPlayerAttunements attunements = PrimalMagicCapabilities.getAttunements(target);
        if (attunements == null) {
            source.sendFailure(new TranslatableComponent("commands.primalmagic.error"));
        } else {
            attunements.clear();
            AttunementManager.scheduleSync(target);
            source.sendSuccess(new TranslatableComponent("commands.primalmagic.attunements.reset", target.getName()), true);
            target.sendMessage(new TranslatableComponent("commands.primalmagic.attunements.reset.target", source.getTextName()), Util.NIL_UUID);
        }
        return 0;
    }

    private static int getAttunements(CommandSourceStack source, ServerPlayer target, SourceInput input) {
        // Get the partial and total attunements for the given source for the target player
        String tag = input.getSourceTag();
        Source toQuery = Source.getSource(tag.toLowerCase());
        if (toQuery == null) {
            source.sendFailure(new TranslatableComponent("commands.primalmagic.source.noexist", tag));
        } else {
            Component sourceText = new TranslatableComponent(toQuery.getNameTranslationKey());
            source.sendSuccess(new TranslatableComponent("commands.primalmagic.attunements.get.total", sourceText, source.getTextName(), AttunementManager.getTotalAttunement(target, toQuery)), true);
            for (AttunementType type : AttunementType.values()) {
                Component typeText = new TranslatableComponent(type.getNameTranslationKey());
                source.sendSuccess(new TranslatableComponent("commands.primalmagic.attunements.get.partial", typeText, AttunementManager.getAttunement(target, toQuery, type)), true);
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
            source.sendFailure(new TranslatableComponent("commands.primalmagic.source.noexist", tag));
        } else if (type == null) {
            source.sendFailure(new TranslatableComponent("commands.primalmagic.attunement_type.noexist"));
        } else {
            AttunementManager.setAttunement(target, toSet, type, value);
            Component sourceText = new TranslatableComponent(toSet.getNameTranslationKey());
            Component typeText = new TranslatableComponent(type.getNameTranslationKey());
            if (type.isCapped() && value > type.getMaximum()) {
                source.sendSuccess(new TranslatableComponent("commands.primalmagic.attunements.set.success.capped", target.getName(), typeText, sourceText, type.getMaximum(), value), true);
                target.sendMessage(new TranslatableComponent("commands.primalmagic.attunements.set.target.capped", target.getName(), typeText, sourceText, type.getMaximum(), value), Util.NIL_UUID);
            } else {
                source.sendSuccess(new TranslatableComponent("commands.primalmagic.attunements.set.success", target.getName(), typeText, sourceText, value), true);
                target.sendMessage(new TranslatableComponent("commands.primalmagic.attunements.set.target", target.getName(), typeText, sourceText, value), Util.NIL_UUID);
            }
        }
        return 0;
    }
}
