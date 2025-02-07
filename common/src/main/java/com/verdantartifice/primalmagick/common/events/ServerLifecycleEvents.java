package com.verdantartifice.primalmagick.common.events;

import com.mojang.brigadier.CommandDispatcher;
import com.verdantartifice.primalmagick.common.commands.PrimalMagickCommand;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;

/**
 * Handlers for server lifecycle related events.
 * 
 * @author Daedalus4096
 */
public class ServerLifecycleEvents {
    public static void onRegisterCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        PrimalMagickCommand.register(dispatcher, context);
    }
}
