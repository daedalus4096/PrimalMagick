package com.verdantartifice.primalmagic.proxy;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.commands.PrimalMagicCommand;
import com.verdantartifice.primalmagic.common.commands.arguments.ResearchArgument;
import com.verdantartifice.primalmagic.common.init.InitCapabilities;
import com.verdantartifice.primalmagic.common.init.InitResearch;
import com.verdantartifice.primalmagic.common.network.PacketHandler;

import net.minecraft.command.arguments.ArgumentSerializer;
import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

public class CommonProxy implements IProxyPM {
    @Override
    public void preInit(FMLCommonSetupEvent event) {
        PacketHandler.registerMessages();
        InitCapabilities.initCapabilities();
        InitResearch.initResearch();
    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {
        ArgumentTypes.register((new ResourceLocation(PrimalMagic.MODID, "research")).toString(), ResearchArgument.class, new ArgumentSerializer<>(ResearchArgument::research));
        PrimalMagicCommand.register(event.getCommandDispatcher());
    }
}
