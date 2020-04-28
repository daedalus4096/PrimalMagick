package com.verdantartifice.primalmagic.client.events;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.blocks.misc.StainedSkyglassBlock;
import com.verdantartifice.primalmagic.common.blocks.misc.StainedSkyglassPaneBlock;
import com.verdantartifice.primalmagic.common.blocks.rituals.SaltTrailBlock;
import com.verdantartifice.primalmagic.common.items.ItemsPM;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Respond to client-only block/item color events.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid=PrimalMagic.MODID, value=Dist.CLIENT, bus=Mod.EventBusSubscriber.Bus.MOD)
public class BlockColorEvents {
    @SubscribeEvent
    public static void onBlockColorInit(ColorHandlerEvent.Block event) {
        event.getBlockColors().register((state, lightReader, pos, dummy) -> {
            return SaltTrailBlock.colorMultiplier(state.get(SaltTrailBlock.POWER));
        }, BlocksPM.SALT_TRAIL.get());
        
        event.getBlockColors().register((state, lightReader, pos, dummy) -> {
            return (state.getBlock() instanceof StainedSkyglassBlock) ? 
                    ((StainedSkyglassBlock)state.getBlock()).getColor().getColorValue() : 
                    DyeColor.WHITE.getColorValue();
        }, BlocksPM.STAINED_SKYGLASS_BLACK.get(), BlocksPM.STAINED_SKYGLASS_BLUE.get(), BlocksPM.STAINED_SKYGLASS_BROWN.get(), BlocksPM.STAINED_SKYGLASS_CYAN.get(), BlocksPM.STAINED_SKYGLASS_GRAY.get(), BlocksPM.STAINED_SKYGLASS_GREEN.get(), BlocksPM.STAINED_SKYGLASS_LIGHT_BLUE.get(), BlocksPM.STAINED_SKYGLASS_LIGHT_GRAY.get(), BlocksPM.STAINED_SKYGLASS_LIME.get(), BlocksPM.STAINED_SKYGLASS_MAGENTA.get(), BlocksPM.STAINED_SKYGLASS_ORANGE.get(), BlocksPM.STAINED_SKYGLASS_PINK.get(), BlocksPM.STAINED_SKYGLASS_PURPLE.get(), BlocksPM.STAINED_SKYGLASS_RED.get(), BlocksPM.STAINED_SKYGLASS_WHITE.get(), BlocksPM.STAINED_SKYGLASS_YELLOW.get());
        
        event.getBlockColors().register((state, lightReader, pos, dummy) -> {
            return (state.getBlock() instanceof StainedSkyglassPaneBlock) ? 
                    ((StainedSkyglassPaneBlock)state.getBlock()).getColor().getColorValue() : 
                    DyeColor.WHITE.getColorValue();
        }, BlocksPM.STAINED_SKYGLASS_PANE_RED.get());
    }
    
    @SubscribeEvent
    public static void onItemColorInit(ColorHandlerEvent.Item event) {
        event.getItemColors().register((stack, dummy) -> {
            Item item = stack.getItem();
            if (item instanceof BlockItem) {
                Block block = ((BlockItem)item).getBlock();
                if (block instanceof StainedSkyglassBlock) {
                    return ((StainedSkyglassBlock)block).getColor().getColorValue();
                }
            }
            return DyeColor.WHITE.getColorValue();
        }, ItemsPM.STAINED_SKYGLASS_BLACK.get(), ItemsPM.STAINED_SKYGLASS_BLUE.get(), ItemsPM.STAINED_SKYGLASS_BROWN.get(), ItemsPM.STAINED_SKYGLASS_CYAN.get(), ItemsPM.STAINED_SKYGLASS_GRAY.get(), ItemsPM.STAINED_SKYGLASS_GREEN.get(), ItemsPM.STAINED_SKYGLASS_LIGHT_BLUE.get(), ItemsPM.STAINED_SKYGLASS_LIGHT_GRAY.get(), ItemsPM.STAINED_SKYGLASS_LIME.get(), ItemsPM.STAINED_SKYGLASS_MAGENTA.get(), ItemsPM.STAINED_SKYGLASS_ORANGE.get(), ItemsPM.STAINED_SKYGLASS_PINK.get(), ItemsPM.STAINED_SKYGLASS_PURPLE.get(), ItemsPM.STAINED_SKYGLASS_RED.get(), ItemsPM.STAINED_SKYGLASS_WHITE.get(), ItemsPM.STAINED_SKYGLASS_YELLOW.get());

        event.getItemColors().register((stack, dummy) -> {
            Item item = stack.getItem();
            if (item instanceof BlockItem) {
                Block block = ((BlockItem)item).getBlock();
                if (block instanceof StainedSkyglassPaneBlock) {
                    return ((StainedSkyglassPaneBlock)block).getColor().getColorValue();
                }
            }
            return DyeColor.WHITE.getColorValue();
        }, ItemsPM.STAINED_SKYGLASS_PANE_RED.get());
    }
}
