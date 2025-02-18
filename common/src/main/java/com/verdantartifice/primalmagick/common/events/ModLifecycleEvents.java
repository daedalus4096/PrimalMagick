package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.common.init.InitAttunements;
import com.verdantartifice.primalmagick.common.init.InitCauldron;
import com.verdantartifice.primalmagick.common.init.InitRecipes;
import com.verdantartifice.primalmagick.common.init.InitResearch;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.network.PlayPacketRegistration;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.world.level.block.DispenserBlock;

import java.util.function.Consumer;

/**
 * Handlers for mod lifecycle related events.
 * 
 * @author Daedalus4096
 */
public class ModLifecycleEvents {
    public static void commonSetup(Consumer<Runnable> workConsumer) {
        workConsumer.accept(() -> {
            PlayPacketRegistration.registerMessages();
            Services.NETWORK.registerConfigMessages();

            InitRecipes.initWandTransforms();
            InitRecipes.initCompostables();
            InitAttunements.initAttunementAttributeModifiers();
            InitResearch.initResearch();
            InitCauldron.initCauldronInteractions();

            registerDispenserBehaviors();
        });
    }
    
    private static void registerDispenserBehaviors() {
        DispenserBlock.registerProjectileBehavior(ItemsPM.IGNYX.get());
    }
}
