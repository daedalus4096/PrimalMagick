package com.verdantartifice.primalmagic.common.events;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.init.InitBlocks;
import com.verdantartifice.primalmagic.common.init.InitContainers;
import com.verdantartifice.primalmagic.common.init.InitItems;
import com.verdantartifice.primalmagic.common.init.InitRecipes;
import com.verdantartifice.primalmagic.common.init.InitTileEntities;

import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handlers for registration related events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagic.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class RegistrationEvents {
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        InitBlocks.initBlocks(event.getRegistry());
    }
    
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        InitItems.initBlockItems(event.getRegistry());
        InitItems.initItems(event.getRegistry());
    }
    
    @SubscribeEvent
    public static void registerContainers(RegistryEvent.Register<ContainerType<?>> event) {
        InitContainers.initContainers(event.getRegistry());
    }
    
    @SubscribeEvent
    public static void registerRecipeSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        InitRecipes.initRecipeSerializers(event.getRegistry());
        InitRecipes.initWandTransforms();
    }
    
    @SubscribeEvent
    public static void registerTileEntityTypes(RegistryEvent.Register<TileEntityType<?>> event) {
        InitTileEntities.initTileEntityTypes(event.getRegistry());
    }
}
