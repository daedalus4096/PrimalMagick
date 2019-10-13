package com.verdantartifice.primalmagic.proxy;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.commands.PrimalMagicCommand;
import com.verdantartifice.primalmagic.common.commands.arguments.DisciplineArgument;
import com.verdantartifice.primalmagic.common.commands.arguments.KnowledgeAmountArgument;
import com.verdantartifice.primalmagic.common.commands.arguments.KnowledgeTypeArgument;
import com.verdantartifice.primalmagic.common.commands.arguments.ResearchArgument;
import com.verdantartifice.primalmagic.common.init.InitAffinities;
import com.verdantartifice.primalmagic.common.init.InitCapabilities;
import com.verdantartifice.primalmagic.common.init.InitRecipes;
import com.verdantartifice.primalmagic.common.init.InitResearch;
import com.verdantartifice.primalmagic.common.network.PacketHandler;

import net.minecraft.command.arguments.ArgumentSerializer;
import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

public class CommonProxy implements IProxyPM {
    @Override
    public void commonSetup(FMLCommonSetupEvent event) {
        PacketHandler.registerMessages();
        InitRecipes.initRecipeTypes();
        InitCapabilities.initCapabilities();
        InitResearch.initResearch();

        ArgumentTypes.register((new ResourceLocation(PrimalMagic.MODID, "research")).toString(), ResearchArgument.class, new ArgumentSerializer<>(ResearchArgument::research));
        ArgumentTypes.register((new ResourceLocation(PrimalMagic.MODID, "discipline")).toString(), DisciplineArgument.class, new ArgumentSerializer<>(DisciplineArgument::discipline));
        ArgumentTypes.register((new ResourceLocation(PrimalMagic.MODID, "knowledge_type")).toString(), KnowledgeTypeArgument.class, new ArgumentSerializer<>(KnowledgeTypeArgument::knowledgeType));
        ArgumentTypes.register((new ResourceLocation(PrimalMagic.MODID, "knowledge_amount").toString()), KnowledgeAmountArgument.class, new ArgumentSerializer<>(KnowledgeAmountArgument::amount));
    }
    
    @Override
    public void clientSetup(FMLClientSetupEvent event) {}

    @Override
    public void serverStarting(FMLServerStartingEvent event) {
        PrimalMagicCommand.register(event.getCommandDispatcher());
        InitAffinities.initAffinities();
    }
    
    @Override
    public boolean isShiftDown() {
        return false;
    }
}
