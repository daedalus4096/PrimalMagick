package com.verdantartifice.primalmagick.common.events;

import java.util.List;
import java.util.stream.Collectors;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.entities.projectiles.IgnyxEntity;
import com.verdantartifice.primalmagick.common.entities.treefolk.TreefolkEntity;
import com.verdantartifice.primalmagick.common.init.InitAttunements;
import com.verdantartifice.primalmagick.common.init.InitCauldron;
import com.verdantartifice.primalmagick.common.init.InitRecipes;
import com.verdantartifice.primalmagick.common.init.InitResearch;
import com.verdantartifice.primalmagick.common.init.InitSpells;
import com.verdantartifice.primalmagick.common.init.InitStats;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.loot.conditions.LootConditionTypesPM;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.spells.SpellManager;

import net.minecraft.Util;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.SpawnPlacements.Type;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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
@Mod.EventBusSubscriber(modid=PrimalMagick.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModLifecycleEvents {
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        PacketHandler.registerMessages();
        
        InitRecipes.initWandTransforms();
        InitRecipes.initCompostables();
        InitAttunements.initAttunementAttributeModifiers();
        InitResearch.initResearch();
        InitSpells.initSpells();
        InitStats.initStats();
        InitCauldron.initCauldronInteractions();
        
        LootConditionTypesPM.register();

        registerEntityPlacements(event);
        registerDispenserBehaviors(event);
    }
    
    private static void registerEntityPlacements(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            SpawnPlacements.register(EntityTypesPM.TREEFOLK.get(), Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TreefolkEntity::canSpawnOn);
        });
    }
    
    private static void registerDispenserBehaviors(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            DispenserBlock.registerBehavior(ItemsPM.IGNYX.get(), new AbstractProjectileDispenseBehavior() {
                @Override
                protected Projectile getProjectile(Level level, Position pos, ItemStack stack) {
                    return Util.make(new IgnyxEntity(level, pos.x(), pos.y(), pos.z()), p -> {
                        p.setItem(stack);
                    });
                }
            });
        });
    }

    @SubscribeEvent
    public static void enqueueIMC(InterModEnqueueEvent event) {
        InterModComms.sendTo(PrimalMagick.MODID, "polymorphAllow", () -> EntityType.IRON_GOLEM);
        InterModComms.sendTo(PrimalMagick.MODID, "polymorphAllow", () -> EntityType.SNOW_GOLEM);
        InterModComms.sendTo(PrimalMagick.MODID, "polymorphAllow", () -> EntityType.VILLAGER);
        InterModComms.sendTo(PrimalMagick.MODID, "polymorphAllow", () -> EntityTypesPM.PRIMALITE_GOLEM.get());
        InterModComms.sendTo(PrimalMagick.MODID, "polymorphAllow", () -> EntityTypesPM.HEXIUM_GOLEM.get());
        InterModComms.sendTo(PrimalMagick.MODID, "polymorphAllow", () -> EntityTypesPM.HALLOWSTEEL_GOLEM.get());
        InterModComms.sendTo(PrimalMagick.MODID, "polymorphBan", () -> EntityType.ENDER_DRAGON);
        InterModComms.sendTo(PrimalMagick.MODID, "polymorphBan", () -> EntityType.WITHER);
        InterModComms.sendTo(PrimalMagick.MODID, "polymorphBan", () -> EntityType.WOLF);
        InterModComms.sendTo(PrimalMagick.MODID, "polymorphBan", () -> EntityTypesPM.INNER_DEMON.get());
        InterModComms.sendTo(PrimalMagick.MODID, "polymorphBan", () -> EntityType.WARDEN);
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
