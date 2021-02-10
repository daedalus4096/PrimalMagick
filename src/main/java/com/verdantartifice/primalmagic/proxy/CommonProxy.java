package com.verdantartifice.primalmagic.proxy;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.commands.PrimalMagicCommand;
import com.verdantartifice.primalmagic.common.commands.arguments.AttunementTypeArgument;
import com.verdantartifice.primalmagic.common.commands.arguments.AttunementValueArgument;
import com.verdantartifice.primalmagic.common.commands.arguments.DisciplineArgument;
import com.verdantartifice.primalmagic.common.commands.arguments.KnowledgeAmountArgument;
import com.verdantartifice.primalmagic.common.commands.arguments.KnowledgeTypeArgument;
import com.verdantartifice.primalmagic.common.commands.arguments.ResearchArgument;
import com.verdantartifice.primalmagic.common.commands.arguments.SourceArgument;
import com.verdantartifice.primalmagic.common.commands.arguments.StatValueArgument;
import com.verdantartifice.primalmagic.common.containers.ContainersPM;
import com.verdantartifice.primalmagic.common.crafting.RecipeSerializersPM;
import com.verdantartifice.primalmagic.common.effects.EffectsPM;
import com.verdantartifice.primalmagic.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagic.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagic.common.init.InitAffinities;
import com.verdantartifice.primalmagic.common.init.InitAttunements;
import com.verdantartifice.primalmagic.common.init.InitCapabilities;
import com.verdantartifice.primalmagic.common.init.InitRecipes;
import com.verdantartifice.primalmagic.common.init.InitResearch;
import com.verdantartifice.primalmagic.common.init.InitRunes;
import com.verdantartifice.primalmagic.common.init.InitSpells;
import com.verdantartifice.primalmagic.common.init.InitStats;
import com.verdantartifice.primalmagic.common.init.InitWorldGen;
import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.sounds.SoundsPM;
import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagic.common.worldgen.features.ConfiguredFeaturesPM;
import com.verdantartifice.primalmagic.common.worldgen.features.FeaturesPM;

import net.minecraft.command.arguments.ArgumentSerializer;
import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

/**
 * Base class for a sided-proxy for the mod, handling setup common to both sides.
 * 
 * @author Daedalus4096
 */
public class CommonProxy implements IProxyPM {
    @Override
    public void initDeferredRegistries() {
        BlocksPM.init();
        ItemsPM.init();
        TileEntityTypesPM.init();
        ContainersPM.init();
        EntityTypesPM.init();
        EffectsPM.init();
        RecipeSerializersPM.init();
        SoundsPM.init();
        FeaturesPM.init();
        EnchantmentsPM.init();
    }
    
    @Override
    public void commonSetup(FMLCommonSetupEvent event) {
        PacketHandler.registerMessages();
        
        InitRecipes.initRecipeTypes();
        InitRecipes.initWandTransforms();
        InitCapabilities.initCapabilities();
        InitAttunements.initAttunementAttributeModifiers();
        InitResearch.initResearch();
        InitWorldGen.initWorldGen();
        InitSpells.initSpells();
        InitStats.initStats();
        InitRunes.initRuneEnchantments();
        
        FeaturesPM.setupStructures();
        ConfiguredFeaturesPM.registerConfiguredStructures();

        ArgumentTypes.register((new ResourceLocation(PrimalMagic.MODID, "research")).toString(), ResearchArgument.class, new ArgumentSerializer<>(ResearchArgument::research));
        ArgumentTypes.register((new ResourceLocation(PrimalMagic.MODID, "discipline")).toString(), DisciplineArgument.class, new ArgumentSerializer<>(DisciplineArgument::discipline));
        ArgumentTypes.register((new ResourceLocation(PrimalMagic.MODID, "knowledge_type")).toString(), KnowledgeTypeArgument.class, new ArgumentSerializer<>(KnowledgeTypeArgument::knowledgeType));
        ArgumentTypes.register((new ResourceLocation(PrimalMagic.MODID, "knowledge_amount").toString()), KnowledgeAmountArgument.class, new ArgumentSerializer<>(KnowledgeAmountArgument::amount));
        ArgumentTypes.register((new ResourceLocation(PrimalMagic.MODID, "source")).toString(), SourceArgument.class, new ArgumentSerializer<>(SourceArgument::source));
        ArgumentTypes.register((new ResourceLocation(PrimalMagic.MODID, "stat_value")).toString(), StatValueArgument.class, new ArgumentSerializer<>(StatValueArgument::value));
        ArgumentTypes.register((new ResourceLocation(PrimalMagic.MODID, "attunement_type")).toString(), AttunementTypeArgument.class, new ArgumentSerializer<>(AttunementTypeArgument::attunementType));
        ArgumentTypes.register((new ResourceLocation(PrimalMagic.MODID, "attunement_value")).toString(), AttunementValueArgument.class, new ArgumentSerializer<>(AttunementValueArgument::value));
    }
    
    @Override
    public void clientSetup(FMLClientSetupEvent event) {}

    @Override
    public void serverStarting(FMLServerStartingEvent event) {
        PrimalMagicCommand.register(event.getServer().getCommandManager().getDispatcher());
        InitAffinities.initAffinities(event.getServer());
    }
    
    @Override
    public boolean isShiftDown() {
        return false;
    }
}
