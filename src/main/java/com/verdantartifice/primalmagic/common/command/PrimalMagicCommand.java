package com.verdantartifice.primalmagic.common.command;

import java.util.Set;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class PrimalMagicCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralCommandNode<CommandSource> node = dispatcher.register(Commands.literal("primalmagic")
            .requires((source) -> { return source.hasPermissionLevel(2); })
            .then(Commands.literal("help").executes((context) -> { return showHelp(context.getSource()); }))
            .then(Commands.literal("research")
                .then(Commands.argument("target", EntityArgument.player())
                    .then(Commands.literal("list").executes((context) -> { return listResearch(context.getSource(), EntityArgument.getPlayer(context, "target")); }))
                    .then(Commands.literal("reset").executes((context) -> { return resetResearch(context.getSource(), EntityArgument.getPlayer(context, "target")); }))
                    .then(Commands.literal("grant")
                        .then(Commands.argument("research", MessageArgument.message()).executes((context) -> { return grantResearch(context.getSource(), EntityArgument.getPlayer(context, "target"), MessageArgument.getMessage(context, "research")); }))
                    )
                )
            )
        );
        dispatcher.register(Commands.literal("pm").requires((source) -> {
            return source.hasPermissionLevel(2);
        }).redirect(node));
    }
    
    private static int showHelp(CommandSource source) {
        source.sendFeedback(new TranslationTextComponent("commands.primalmagic.help.1").applyTextStyle(TextFormatting.DARK_GREEN), true);
        source.sendFeedback(new TranslationTextComponent("commands.primalmagic.help.2").applyTextStyle(TextFormatting.DARK_GREEN), true);
        source.sendFeedback(new TranslationTextComponent("commands.primalmagic.help.3"), true);
        source.sendFeedback(new TranslationTextComponent("commands.primalmagic.help.4").applyTextStyle(TextFormatting.DARK_GREEN), true);
        source.sendFeedback(new TranslationTextComponent("commands.primalmagic.help.5"), true);
        source.sendFeedback(new TranslationTextComponent("commands.primalmagic.help.6").applyTextStyle(TextFormatting.DARK_GREEN), true);
        source.sendFeedback(new TranslationTextComponent("commands.primalmagic.help.7"), true);
        return 0;
    }
    
    private static int listResearch(CommandSource source, ServerPlayerEntity target) {
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(target);
        if (knowledge == null) {
            source.sendFeedback(new TranslationTextComponent("commands.primalmagic.error").applyTextStyle(TextFormatting.RED), true);
        } else {
            Set<String> researchSet = knowledge.getResearchSet();
            String[] researchList = researchSet.toArray(new String[researchSet.size()]);
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
            knowledge.clear();
            knowledge.sync(target);
            source.sendFeedback(new TranslationTextComponent("commands.primalmagic.research.reset", target.getName()), true);
        }
        return 0;
    }
    
    private static int grantResearch(CommandSource source, ServerPlayerEntity target, ITextComponent research) {
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(target);
        if (knowledge == null) {
            source.sendFeedback(new TranslationTextComponent("commands.primalmagic.error").applyTextStyle(TextFormatting.RED), true);
        } else {
            knowledge.addResearch(research.getString());
            knowledge.sync(target);
            source.sendFeedback(new TranslationTextComponent("commands.primalmagic.research.grant", target.getName(), research.getString()), true);
        }
        return 0;
    }
}
