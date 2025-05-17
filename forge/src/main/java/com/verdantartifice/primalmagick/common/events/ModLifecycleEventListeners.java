package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.function.Supplier;

/**
 * Forge listeners for mod lifecycle related events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid= Constants.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModLifecycleEventListeners {
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        ModLifecycleEvents.commonSetup(event::enqueueWork);
        event.enqueueWork(ModLifecycleEventListeners::registerFlowerPotPlants);
    }

    private static void registerFlowerPotPlants() {
        if (Blocks.FLOWER_POT instanceof FlowerPotBlock emptyPotBlock) {
            registerPottedPlant(emptyPotBlock, BlocksPM.POTTED_SUNWOOD_SAPLING);
            registerPottedPlant(emptyPotBlock, BlocksPM.POTTED_MOONWOOD_SAPLING);
            registerPottedPlant(emptyPotBlock, BlocksPM.POTTED_HALLOWOOD_SAPLING);
        }
    }

    private static void registerPottedPlant(FlowerPotBlock emptyPotBlock, Supplier<FlowerPotBlock> flowerSupplier) {
        emptyPotBlock.addPlant(BuiltInRegistries.BLOCK.getKey(flowerSupplier.get().getPotted()), flowerSupplier);
    }
}
