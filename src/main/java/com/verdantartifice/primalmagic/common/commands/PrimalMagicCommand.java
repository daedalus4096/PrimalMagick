package com.verdantartifice.primalmagic.common.commands;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.commands.arguments.KnowledgeAmountArgument;
import com.verdantartifice.primalmagic.common.commands.arguments.KnowledgeTypeArgument;
import com.verdantartifice.primalmagic.common.commands.arguments.KnowledgeTypeInput;
import com.verdantartifice.primalmagic.common.commands.arguments.ResearchArgument;
import com.verdantartifice.primalmagic.common.commands.arguments.ResearchInput;
import com.verdantartifice.primalmagic.common.commands.arguments.SourceArgument;
import com.verdantartifice.primalmagic.common.commands.arguments.SourceInput;
import com.verdantartifice.primalmagic.common.research.ResearchEntries;
import com.verdantartifice.primalmagic.common.research.ResearchManager;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sources.AffinityManager;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.ItemArgument;
import net.minecraft.command.arguments.ItemInput;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class PrimalMagicCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralCommandNode<CommandSource> node = dispatcher.register(Commands.literal("primalmagic")
            .requires((source) -> { return source.hasPermissionLevel(2); })
            .then(Commands.literal("research")
                .then(Commands.argument("target", EntityArgument.player())
                    .then(Commands.literal("list").executes((context) -> { return listResearch(context.getSource(), EntityArgument.getPlayer(context, "target")); }))
                    .then(Commands.literal("reset").executes((context) -> { return resetResearch(context.getSource(), EntityArgument.getPlayer(context, "target")); }))
                    .then(Commands.literal("grant")
                        .then(Commands.argument("research", ResearchArgument.research()).executes((context) -> { return grantResearch(context.getSource(), EntityArgument.getPlayer(context, "target"), ResearchArgument.getResearch(context, "research")); }))
                    )
                    .then(Commands.literal("revoke")
                        .then(Commands.argument("research", ResearchArgument.research()).executes((context) -> { return revokeResearch(context.getSource(), EntityArgument.getPlayer(context, "target"), ResearchArgument.getResearch(context, "research")); }))
                    )
                    .then(Commands.literal("details")
                        .then(Commands.argument("research", ResearchArgument.research()).executes((context) -> { return detailResearch(context.getSource(), EntityArgument.getPlayer(context, "target"), ResearchArgument.getResearch(context, "research")); }))
                    )
                    .then(Commands.literal("progress")
                        .then(Commands.argument("research", ResearchArgument.research()).executes((context) -> { return progressResearch(context.getSource(), EntityArgument.getPlayer(context, "target"), ResearchArgument.getResearch(context, "research")); }))
                    )
                )
            )
            .then(Commands.literal("knowledge")
                .then(Commands.argument("target", EntityArgument.player())
                    .then(Commands.literal("reset").executes((context) -> { return resetKnowledge(context.getSource(), EntityArgument.getPlayer(context, "target")); }))
                    .then(Commands.literal("get")
                        .then(Commands.argument("knowledge_type", KnowledgeTypeArgument.knowledgeType()).executes((context) -> { return getKnowledge(context.getSource(), EntityArgument.getPlayer(context, "target"), KnowledgeTypeArgument.getKnowledgeType(context, "knowledge_type")); }))
                    )
                    .then(Commands.literal("add")
                        .then(Commands.argument("knowledge_type", KnowledgeTypeArgument.knowledgeType())
                            .then(Commands.argument("points", KnowledgeAmountArgument.amount()).executes((context) -> { return addKnowledge(context.getSource(), EntityArgument.getPlayer(context, "target"), KnowledgeTypeArgument.getKnowledgeType(context, "knowledge_type"), IntegerArgumentType.getInteger(context, "points")); }))
                        )
                    )
                )
            )
            .then(Commands.literal("scans")
                .then(Commands.argument("target", EntityArgument.player())
                    .then(Commands.literal("grant")
                        .then(Commands.argument("item", ItemArgument.item()).executes((context) -> { return grantScanResearch(context.getSource(), EntityArgument.getPlayer(context, "target"), ItemArgument.getItem(context, "item")); }))
                    )
                    .then(Commands.literal("grant_all").executes((context) -> { return grantAllScanResearch(context.getSource(), EntityArgument.getPlayer(context, "target")); }))
                )
            )
            .then(Commands.literal("sources")
                .then(Commands.argument("target", EntityArgument.player())
                    .then(Commands.literal("list").executes((context) -> { return listUnlockedSources(context.getSource(), EntityArgument.getPlayer(context, "target")); }))
                    .then(Commands.literal("unlock_all").executes((context) -> { return unlockAllSources(context.getSource(), EntityArgument.getPlayer(context, "target")); }))
                    .then(Commands.literal("unlock")
                        .then(Commands.argument("source", SourceArgument.source()).executes((context) -> { return unlockSource(context.getSource(), EntityArgument.getPlayer(context, "target"), SourceArgument.getSource(context, "source")); }))
                    )
                )
            )
        );
        dispatcher.register(Commands.literal("pm").requires((source) -> {
            return source.hasPermissionLevel(2);
        }).redirect(node));
    }

    private static int listResearch(CommandSource source, ServerPlayerEntity target) {
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(target);
        if (knowledge == null) {
            source.sendFeedback(new TranslationTextComponent("commands.primalmagic.error").applyTextStyle(TextFormatting.RED), true);
        } else {
            Set<SimpleResearchKey> researchSet = knowledge.getResearchSet();
            String[] researchList = researchSet.stream()
                                        .map(k -> k.getRootKey())
                                        .collect(Collectors.toSet())
                                        .toArray(new String[researchSet.size()]);
            String output = String.join(", ", researchList);
            source.sendFeedback(new TranslationTextComponent("commands.primalmagic.research.list", target.getName(), output), true);
        }
        return 0;
    }
    
    private static int resetResearch(CommandSource source, ServerPlayerEntity target) {
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(target);
        if (knowledge == null) {
            source.sendFeedback(new TranslationTextComponent("commands.primalmagic.error").applyTextStyle(TextFormatting.RED), true);
        } else {
            knowledge.clearResearch();
            knowledge.sync(target);
            source.sendFeedback(new TranslationTextComponent("commands.primalmagic.research.reset", target.getName()), true);
            target.sendMessage(new TranslationTextComponent("commands.primalmagic.research.reset.target", source.getName()));
        }
        return 0;
    }
    
    private static int grantResearch(CommandSource source, ServerPlayerEntity target, ResearchInput input) {
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(target);
        SimpleResearchKey key = input.getKey();
        if (knowledge == null) {
            source.sendFeedback(new TranslationTextComponent("commands.primalmagic.error").applyTextStyle(TextFormatting.RED), true);
        } else if (ResearchEntries.getEntry(key) == null) {
            source.sendFeedback(new TranslationTextComponent("commands.primalmagic.research.noexist", key.toString()).applyTextStyle(TextFormatting.RED), true);
        } else {
            ResearchManager.forceGrantWithAllParents(target, key);
            knowledge.sync(target);
            source.sendFeedback(new TranslationTextComponent("commands.primalmagic.research.grant", target.getName(), key.toString()), true);
            target.sendMessage(new TranslationTextComponent("commands.primalmagic.research.grant.target", source.getName(), key.toString()));
        }
        return 0;
    }
    
    private static int revokeResearch(CommandSource source, ServerPlayerEntity target, ResearchInput input) {
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(target);
        SimpleResearchKey key = input.getKey();
        if (knowledge == null) {
            source.sendFeedback(new TranslationTextComponent("commands.primalmagic.error").applyTextStyle(TextFormatting.RED), true);
        } else if (ResearchEntries.getEntry(key) == null) {
            source.sendFeedback(new TranslationTextComponent("commands.primalmagic.research.noexist", key.toString()).applyTextStyle(TextFormatting.RED), true);
        } else {
            ResearchManager.forceRevokeWithAllChildren(target, key);
            knowledge.sync(target);
            source.sendFeedback(new TranslationTextComponent("commands.primalmagic.research.revoke", target.getName(), key.toString()), true);
            target.sendMessage(new TranslationTextComponent("commands.primalmagic.research.revoke.target", source.getName(), key.toString()));
        }
        return 0;
    }
    
    private static int detailResearch(CommandSource source, ServerPlayerEntity target, ResearchInput input) {
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(target);
        SimpleResearchKey key = input.getKey();
        if (knowledge == null) {
            source.sendFeedback(new TranslationTextComponent("commands.primalmagic.error").applyTextStyle(TextFormatting.RED), true);
        } else if (ResearchEntries.getEntry(key) == null) {
            source.sendFeedback(new TranslationTextComponent("commands.primalmagic.research.noexist", key.toString()).applyTextStyle(TextFormatting.RED), true);
        } else {
            IPlayerKnowledge.ResearchStatus status = knowledge.getResearchStatus(key);
            int stage = knowledge.getResearchStage(key);
            Set<IPlayerKnowledge.ResearchFlag> flagSet = knowledge.getResearchFlags(key);
            String[] flagStrs = flagSet.stream()
                                    .map(f -> f.name())
                                    .collect(Collectors.toSet())
                                    .toArray(new String[flagSet.size()]);
            String flagOutput = (flagStrs.length == 0) ? "none" : String.join(", ", flagStrs);
            source.sendFeedback(new TranslationTextComponent("commands.primalmagic.research.details.1", key.toString(), target.getName()), true);
            source.sendFeedback(new TranslationTextComponent("commands.primalmagic.research.details.2", status.name()), true);
            source.sendFeedback(new TranslationTextComponent("commands.primalmagic.research.details.3", stage), true);
            source.sendFeedback(new TranslationTextComponent("commands.primalmagic.research.details.4", flagOutput), true);
        }
        return 0;
    }
    
    private static int progressResearch(CommandSource source, ServerPlayerEntity target, ResearchInput input) {
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(target);
        SimpleResearchKey key = input.getKey();
        if (knowledge == null) {
            source.sendFeedback(new TranslationTextComponent("commands.primalmagic.error").applyTextStyle(TextFormatting.RED), true);
        } else if (ResearchEntries.getEntry(key) == null) {
            source.sendFeedback(new TranslationTextComponent("commands.primalmagic.research.noexist", key.toString()).applyTextStyle(TextFormatting.RED), true);
        } else {
            int oldStage = knowledge.getResearchStage(key);
            if (ResearchManager.progressResearch(target, key)) {
                int newStage = knowledge.getResearchStage(key);
                source.sendFeedback(new TranslationTextComponent("commands.primalmagic.research.progress.success", key.toString(), target.getName(), oldStage, newStage), true);
                target.sendMessage(new TranslationTextComponent("commands.primalmagic.research.progress.target", key.toString(), source.getName(), oldStage, newStage));
            } else {
                source.sendFeedback(new TranslationTextComponent("commands.primalmagic.research.progress.failure", key.toString(), oldStage), true);
            }
        }
        return 0;
    }
    
    private static int resetKnowledge(CommandSource source, ServerPlayerEntity target) {
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(target);
        if (knowledge == null) {
            source.sendFeedback(new TranslationTextComponent("commands.primalmagic.error").applyTextStyle(TextFormatting.RED), true);
        } else {
            knowledge.clearKnowledge();
            knowledge.sync(target);
            source.sendFeedback(new TranslationTextComponent("commands.primalmagic.knowledge.reset", target.getName()), true);
            target.sendMessage(new TranslationTextComponent("commands.primalmagic.knowledge.reset.target", source.getName()));
        }
        return 0;
    }
    
    private static int getKnowledge(CommandSource source, ServerPlayerEntity target, KnowledgeTypeInput knowledgeTypeInput) {
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(target);
        IPlayerKnowledge.KnowledgeType type = knowledgeTypeInput.getType();
        if (knowledge == null) {
            source.sendFeedback(new TranslationTextComponent("commands.primalmagic.error").applyTextStyle(TextFormatting.RED), true);
        } else if (type == null) {
            source.sendFeedback(new TranslationTextComponent("commands.primalmagic.knowledge_type.noexist").applyTextStyle(TextFormatting.RED), true);
        } else {
            int levels = knowledge.getKnowledge(type);
            int points = knowledge.getKnowledgeRaw(type);
            source.sendFeedback(new TranslationTextComponent("commands.primalmagic.knowledge.get", target.getName().getString(), levels, type.name(), points), true);
        }
        return 0;
    }
    
    private static int addKnowledge(CommandSource source, ServerPlayerEntity target, KnowledgeTypeInput knowledgeTypeInput, int points) {
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(target);
        IPlayerKnowledge.KnowledgeType type = knowledgeTypeInput.getType();
        if (knowledge == null) {
            source.sendFeedback(new TranslationTextComponent("commands.primalmagic.error").applyTextStyle(TextFormatting.RED), true);
        } else if (type == null) {
            source.sendFeedback(new TranslationTextComponent("commands.primalmagic.knowledge_type.noexist").applyTextStyle(TextFormatting.RED), true);
        } else {
            if (ResearchManager.addKnowledge(target, type, points)) {
                source.sendFeedback(new TranslationTextComponent("commands.primalmagic.knowledge.add.success", points, type.name(), target.getName().getString()), true);
                target.sendMessage(new TranslationTextComponent("commands.primalmagic.knowledge.add.target", points, type.name(), source.getName()));
            } else {
                source.sendFeedback(new TranslationTextComponent("commands.primalmagic.knowledge.add.failure", target.getName().getString()), true);
            }
        }
        return 0;
    }
    
    private static int grantScanResearch(CommandSource source, ServerPlayerEntity target, ItemInput item) {
        ItemStack stack;
        try {
            stack = item.createStack(1, false);
        } catch (CommandSyntaxException e) {
            source.sendFeedback(new TranslationTextComponent("commands.primalmagic.scans.grant.failure", target.getName()).applyTextStyle(TextFormatting.RED), true);
            return 0;
        }
        if (AffinityManager.setScanned(stack, target)) {
            source.sendFeedback(new TranslationTextComponent("commands.primalmagic.scans.grant.success", target.getName(), item.getItem().getRegistryName().toString()), true);
            target.sendMessage(new TranslationTextComponent("commands.primalmagic.scans.grant.target", source.getName(), item.getItem().getRegistryName().toString()));
        } else {
            source.sendFeedback(new TranslationTextComponent("commands.primalmagic.scans.grant.failure", target.getName()).applyTextStyle(TextFormatting.RED), true);            
        }
        return 0;
    }
    
    private static int grantAllScanResearch(CommandSource source, ServerPlayerEntity target) {
        int count = AffinityManager.setAllScanned(target);
        source.sendFeedback(new TranslationTextComponent("commands.primalmagic.scans.grant_all", count, target.getName()), true);
        target.sendMessage(new TranslationTextComponent("commands.primalmagic.scans.grant_all.target", count, source.getName()));
        return 0;
    }

    private static int listUnlockedSources(CommandSource source, ServerPlayerEntity target) {
        List<String> unlockedTags = Source.SORTED_SOURCES.stream()
                                        .filter((s) -> s.isDiscovered(target))
                                        .map((s) -> s.getTag().toUpperCase())
                                        .collect(Collectors.toList());
        String tagStr = String.join(", ", unlockedTags);
        source.sendFeedback(new TranslationTextComponent("commands.primalmagic.sources.list", target.getName(), tagStr), true);
        return 0;
    }

    private static int unlockAllSources(CommandSource source, ServerPlayerEntity target) {
        int unlocked = 0;
        for (Source toUnlock : Source.SOURCES.values()) {
            if (!toUnlock.isDiscovered(target)) {
                if (ResearchManager.completeResearch(target, toUnlock.getDiscoverKey())) {
                    unlocked++;
                }
            }
        }
        source.sendFeedback(new TranslationTextComponent("commands.primalmagic.sources.unlock_all", target.getName(), unlocked), true);
        target.sendMessage(new TranslationTextComponent("commands.primalmagic.sources.unlock_all.target", source.getName(), unlocked));
        return 0;
    }

    private static int unlockSource(CommandSource source, ServerPlayerEntity target, SourceInput input) {
        String tag = input.getSourceTag();
        Source toUnlock = Source.getSource(tag.toLowerCase());
        if (toUnlock == null) {
            source.sendFeedback(new TranslationTextComponent("commands.primalmagic.source.noexist", tag).applyTextStyle(TextFormatting.RED), true);
        } else if (toUnlock.isDiscovered(target)) {
            source.sendFeedback(new TranslationTextComponent("commands.primalmagic.sources.unlock.already_unlocked", target.getName(), tag.toUpperCase()).applyTextStyle(TextFormatting.RED), true);
        } else if (ResearchManager.completeResearch(target, toUnlock.getDiscoverKey())) {
            source.sendFeedback(new TranslationTextComponent("commands.primalmagic.sources.unlock.success", target.getName(), tag.toUpperCase()), true);
            target.sendMessage(new TranslationTextComponent("commands.primalmagic.sources.unlock.target", source.getName(), tag.toUpperCase()));
        } else {
            source.sendFeedback(new TranslationTextComponent("commands.primalmagic.sources.unlock.failure", target.getName(), tag.toUpperCase()).applyTextStyle(TextFormatting.RED), true);
        }
        return 0;
    }
}
