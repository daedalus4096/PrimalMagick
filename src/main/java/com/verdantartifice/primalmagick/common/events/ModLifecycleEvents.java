package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.entities.projectiles.IgnyxEntity;
import com.verdantartifice.primalmagick.common.init.InitAttunements;
import com.verdantartifice.primalmagick.common.init.InitCauldron;
import com.verdantartifice.primalmagick.common.init.InitRecipes;
import com.verdantartifice.primalmagick.common.init.InitResearch;
import com.verdantartifice.primalmagick.common.init.InitSpells;
import com.verdantartifice.primalmagick.common.init.InitStats;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.loot.conditions.LootConditionTypesPM;
import com.verdantartifice.primalmagick.common.network.PacketHandler;

import net.minecraft.Util;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

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

        registerDispenserBehaviors(event);
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
}
