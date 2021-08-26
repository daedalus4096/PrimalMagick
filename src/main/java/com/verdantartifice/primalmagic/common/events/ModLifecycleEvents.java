package com.verdantartifice.primalmagic.common.events;

import java.util.List;
import java.util.stream.Collectors;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.commands.arguments.AttunementTypeArgument;
import com.verdantartifice.primalmagic.common.commands.arguments.AttunementValueArgument;
import com.verdantartifice.primalmagic.common.commands.arguments.DisciplineArgument;
import com.verdantartifice.primalmagic.common.commands.arguments.KnowledgeAmountArgument;
import com.verdantartifice.primalmagic.common.commands.arguments.KnowledgeTypeArgument;
import com.verdantartifice.primalmagic.common.commands.arguments.ResearchArgument;
import com.verdantartifice.primalmagic.common.commands.arguments.SourceArgument;
import com.verdantartifice.primalmagic.common.commands.arguments.StatValueArgument;
import com.verdantartifice.primalmagic.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagic.common.entities.misc.TreefolkEntity;
import com.verdantartifice.primalmagic.common.init.InitAttunements;
import com.verdantartifice.primalmagic.common.init.InitCauldron;
import com.verdantartifice.primalmagic.common.init.InitRecipes;
import com.verdantartifice.primalmagic.common.init.InitResearch;
import com.verdantartifice.primalmagic.common.init.InitRunes;
import com.verdantartifice.primalmagic.common.init.InitSpells;
import com.verdantartifice.primalmagic.common.init.InitStats;
import com.verdantartifice.primalmagic.common.items.misc.LazySpawnEggItem;
import com.verdantartifice.primalmagic.common.loot.conditions.LootConditionTypesPM;
import com.verdantartifice.primalmagic.common.misc.DispenseLazySpawnEggBehavior;
import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.spells.SpellManager;
import com.verdantartifice.primalmagic.common.worldgen.features.ConfiguredFeaturesPM;
import com.verdantartifice.primalmagic.common.worldgen.features.FeaturesPM;

import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.commands.synchronization.EmptyArgumentSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.SpawnPlacements.Type;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;

/**
 * Handlers for mod lifecycle related events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagic.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModLifecycleEvents {
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        PacketHandler.registerMessages();
        
        InitRecipes.initRecipeTypes();
        InitRecipes.initWandTransforms();
        InitAttunements.initAttunementAttributeModifiers();
        InitResearch.initResearch();
        InitSpells.initSpells();
        InitStats.initStats();
        InitRunes.initRuneEnchantments();
        InitCauldron.initCauldronInteractions();
        
        FeaturesPM.setupFeatures();
        FeaturesPM.setupStructures();
        ConfiguredFeaturesPM.registerConfiguredStructures();
        LootConditionTypesPM.register();

        registerCommandArguments(event);
        registerEntityPlacements(event);
        registerDispenserBehaviors(event);
    }
    
    private static void registerCommandArguments(FMLCommonSetupEvent event) {
        ArgumentTypes.register((new ResourceLocation(PrimalMagic.MODID, "research")).toString(), ResearchArgument.class, new EmptyArgumentSerializer<>(ResearchArgument::research));
        ArgumentTypes.register((new ResourceLocation(PrimalMagic.MODID, "discipline")).toString(), DisciplineArgument.class, new EmptyArgumentSerializer<>(DisciplineArgument::discipline));
        ArgumentTypes.register((new ResourceLocation(PrimalMagic.MODID, "knowledge_type")).toString(), KnowledgeTypeArgument.class, new EmptyArgumentSerializer<>(KnowledgeTypeArgument::knowledgeType));
        ArgumentTypes.register((new ResourceLocation(PrimalMagic.MODID, "knowledge_amount").toString()), KnowledgeAmountArgument.class, new EmptyArgumentSerializer<>(KnowledgeAmountArgument::amount));
        ArgumentTypes.register((new ResourceLocation(PrimalMagic.MODID, "source")).toString(), SourceArgument.class, new EmptyArgumentSerializer<>(SourceArgument::source));
        ArgumentTypes.register((new ResourceLocation(PrimalMagic.MODID, "stat_value")).toString(), StatValueArgument.class, new EmptyArgumentSerializer<>(StatValueArgument::value));
        ArgumentTypes.register((new ResourceLocation(PrimalMagic.MODID, "attunement_type")).toString(), AttunementTypeArgument.class, new EmptyArgumentSerializer<>(AttunementTypeArgument::attunementType));
        ArgumentTypes.register((new ResourceLocation(PrimalMagic.MODID, "attunement_value")).toString(), AttunementValueArgument.class, new EmptyArgumentSerializer<>(AttunementValueArgument::value));
    }
    
    private static void registerEntityPlacements(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            SpawnPlacements.register(EntityTypesPM.TREEFOLK.get(), Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TreefolkEntity::canSpawnOn);
        });
    }
    
    private static void registerDispenserBehaviors(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            DispenseLazySpawnEggBehavior eggBehavior = new DispenseLazySpawnEggBehavior();
            for (LazySpawnEggItem egg : LazySpawnEggItem.getEggs()) {
                DispenserBlock.registerBehavior(egg, eggBehavior);
            }
        });
    }

    @SubscribeEvent
    public static void enqueueIMC(InterModEnqueueEvent event) {
        InterModComms.sendTo(PrimalMagic.MODID, "polymorphAllow", () -> EntityType.IRON_GOLEM);
        InterModComms.sendTo(PrimalMagic.MODID, "polymorphAllow", () -> EntityType.SNOW_GOLEM);
        InterModComms.sendTo(PrimalMagic.MODID, "polymorphAllow", () -> EntityType.VILLAGER);
        InterModComms.sendTo(PrimalMagic.MODID, "polymorphAllow", () -> EntityTypesPM.PRIMALITE_GOLEM.get());
        InterModComms.sendTo(PrimalMagic.MODID, "polymorphAllow", () -> EntityTypesPM.HEXIUM_GOLEM.get());
        InterModComms.sendTo(PrimalMagic.MODID, "polymorphAllow", () -> EntityTypesPM.HALLOWSTEEL_GOLEM.get());
        InterModComms.sendTo(PrimalMagic.MODID, "polymorphBan", () -> EntityType.ENDER_DRAGON);
        InterModComms.sendTo(PrimalMagic.MODID, "polymorphBan", () -> EntityType.WITHER);
        InterModComms.sendTo(PrimalMagic.MODID, "polymorphBan", () -> EntityType.WOLF);
        InterModComms.sendTo(PrimalMagic.MODID, "polymorphBan", () -> EntityTypesPM.INNER_DEMON.get());
    }
    
    @SubscribeEvent
    public static void processIMC(InterModProcessEvent event) {
        // Populate the polymorph allow list with entity types from incoming messages
        List<Object> allowMessageList = event.getIMCStream(m -> "polymorphAllow".equals(m)).map(m -> m.messageSupplier().get()).collect(Collectors.toList());
        for (Object obj : allowMessageList) {
            if (obj instanceof EntityType<?>) {
                SpellManager.setPolymorphAllowed((EntityType<?>)obj);
            }
        }
        
        // Populate the polymorph ban list with entity types from incoming messages
        List<Object> banMessageList = event.getIMCStream(m -> "polymorphBan".equals(m)).map(m -> m.messageSupplier().get()).collect(Collectors.toList());
        for (Object obj : banMessageList) {
            if (obj instanceof EntityType<?>) {
                SpellManager.setPolymorphBanned((EntityType<?>)obj);
            }
        }
    }
}
