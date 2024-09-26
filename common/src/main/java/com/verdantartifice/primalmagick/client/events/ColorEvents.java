package com.verdantartifice.primalmagick.client.events;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.blocks.misc.StainedSkyglassBlock;
import com.verdantartifice.primalmagick.common.blocks.misc.StainedSkyglassPaneBlock;
import com.verdantartifice.primalmagick.common.blocks.rituals.RitualCandleBlock;
import com.verdantartifice.primalmagick.common.blocks.rituals.SaltTrailBlock;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.entities.ManaArrowItem;
import com.verdantartifice.primalmagick.common.items.food.AmbrosiaItem;
import com.verdantartifice.primalmagick.common.items.misc.AttunementShacklesItem;
import com.verdantartifice.primalmagick.common.items.misc.HummingArtifactItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.block.StemBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Function;

/**
 * Respond to client-only block/item color events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid= Constants.MOD_ID, value=Dist.CLIENT, bus=Mod.EventBusSubscriber.Bus.MOD)
public class ColorEvents {
    @SubscribeEvent
    public static void onBlockColorInit(RegisterColorHandlersEvent.Block event) {
        event.register((state, lightReader, pos, dummy) -> {
            return SaltTrailBlock.colorMultiplier(state.getValue(SaltTrailBlock.POWER));
        }, BlocksPM.get(BlocksPM.SALT_TRAIL));
        
        event.register((state, lightReader, pos, dummy) -> {
            return (state.getBlock() instanceof StainedSkyglassBlock) ? 
                    ((StainedSkyglassBlock)state.getBlock()).getColor().getFireworkColor() : 
                    DyeColor.WHITE.getFireworkColor();
        }, BlocksPM.get(BlocksPM.STAINED_SKYGLASS_BLACK), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_BLUE), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_BROWN), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_CYAN), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_GRAY), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_GREEN), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_LIGHT_BLUE), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_LIGHT_GRAY), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_LIME), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_MAGENTA), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_ORANGE), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PINK), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PURPLE), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_RED), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_WHITE), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_YELLOW));
        
        event.register((state, lightReader, pos, dummy) -> {
            return (state.getBlock() instanceof StainedSkyglassPaneBlock) ? 
                    ((StainedSkyglassPaneBlock)state.getBlock()).getColor().getFireworkColor() : 
                    DyeColor.WHITE.getFireworkColor();
        }, BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_BLACK), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_BLUE), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_BROWN), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_CYAN), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_GRAY), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_GREEN), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_LIGHT_BLUE), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_LIGHT_GRAY), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_LIME), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_MAGENTA), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_ORANGE), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_PINK), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_PURPLE), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_RED), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_WHITE), BlocksPM.get(BlocksPM.STAINED_SKYGLASS_PANE_YELLOW));
        
        event.register((state, lightReader, pos, dummy) -> {
            return (state.getBlock() instanceof RitualCandleBlock) ?
                    ((RitualCandleBlock)state.getBlock()).getColor().getFireworkColor() :
                    DyeColor.WHITE.getFireworkColor();
        }, BlocksPM.get(BlocksPM.RITUAL_CANDLE_BLACK), BlocksPM.get(BlocksPM.RITUAL_CANDLE_BLUE), BlocksPM.get(BlocksPM.RITUAL_CANDLE_BROWN), BlocksPM.get(BlocksPM.RITUAL_CANDLE_CYAN), BlocksPM.get(BlocksPM.RITUAL_CANDLE_GRAY), BlocksPM.get(BlocksPM.RITUAL_CANDLE_GREEN), BlocksPM.get(BlocksPM.RITUAL_CANDLE_LIGHT_BLUE), BlocksPM.get(BlocksPM.RITUAL_CANDLE_LIGHT_GRAY), BlocksPM.get(BlocksPM.RITUAL_CANDLE_LIME), BlocksPM.get(BlocksPM.RITUAL_CANDLE_MAGENTA), BlocksPM.get(BlocksPM.RITUAL_CANDLE_ORANGE), BlocksPM.get(BlocksPM.RITUAL_CANDLE_PINK), BlocksPM.get(BlocksPM.RITUAL_CANDLE_PURPLE), BlocksPM.get(BlocksPM.RITUAL_CANDLE_RED), BlocksPM.get(BlocksPM.RITUAL_CANDLE_WHITE), BlocksPM.get(BlocksPM.RITUAL_CANDLE_YELLOW));
        
        event.register((state, lightReader, pos, dummy) -> {
            return 14731036;
        }, BlocksPM.get(BlocksPM.ATTACHED_HYDROMELON_STEM));
        
        event.register((state, lightReader, pos, dummy) -> {
            int i = state.getValue(StemBlock.AGE);
            int j = i * 32;
            int k = 255 - i * 8;
            int l = i * 4;
            return j << 16 | k << 8 | l;
        }, BlocksPM.get(BlocksPM.HYDROMELON_STEM));
    }
    
    @SubscribeEvent
    public static void onItemColorInit(RegisterColorHandlersEvent.Item event) {
        event.register((stack, dummy) -> {
            if (stack.getItem() instanceof BlockItem blockItem) {
                if (blockItem.getBlock() instanceof StainedSkyglassBlock skyglassBlock) {
                    int color = skyglassBlock.getColor().getFireworkColor();
                    if (FastColor.ARGB32.alpha(color) == 0) {
                        // Assume color is opaque if alpha channel is set to zero
                        color = FastColor.ARGB32.opaque(color);
                    }
                    return color;
                }
            }
            return FastColor.ARGB32.opaque(DyeColor.WHITE.getFireworkColor());
        }, ItemsPM.STAINED_SKYGLASS_BLACK.get(), ItemsPM.STAINED_SKYGLASS_BLUE.get(), ItemsPM.STAINED_SKYGLASS_BROWN.get(), ItemsPM.STAINED_SKYGLASS_CYAN.get(), ItemsPM.STAINED_SKYGLASS_GRAY.get(), ItemsPM.STAINED_SKYGLASS_GREEN.get(), ItemsPM.STAINED_SKYGLASS_LIGHT_BLUE.get(), ItemsPM.STAINED_SKYGLASS_LIGHT_GRAY.get(), ItemsPM.STAINED_SKYGLASS_LIME.get(), ItemsPM.STAINED_SKYGLASS_MAGENTA.get(), ItemsPM.STAINED_SKYGLASS_ORANGE.get(), ItemsPM.STAINED_SKYGLASS_PINK.get(), ItemsPM.STAINED_SKYGLASS_PURPLE.get(), ItemsPM.STAINED_SKYGLASS_RED.get(), ItemsPM.STAINED_SKYGLASS_WHITE.get(), ItemsPM.STAINED_SKYGLASS_YELLOW.get());

        event.register((stack, dummy) -> {
            if (stack.getItem() instanceof BlockItem blockItem) {
                if (blockItem.getBlock() instanceof StainedSkyglassPaneBlock skyglassBlock) {
                    int color = skyglassBlock.getColor().getFireworkColor();
                    if (FastColor.ARGB32.alpha(color) == 0) {
                        // Assume color is opaque if alpha channel is set to zero
                        color = FastColor.ARGB32.opaque(color);
                    }
                    return color;
                }
            }
            return FastColor.ARGB32.opaque(DyeColor.WHITE.getFireworkColor());
        }, ItemsPM.STAINED_SKYGLASS_PANE_BLACK.get(), ItemsPM.STAINED_SKYGLASS_PANE_BLUE.get(), ItemsPM.STAINED_SKYGLASS_PANE_BROWN.get(), ItemsPM.STAINED_SKYGLASS_PANE_CYAN.get(), ItemsPM.STAINED_SKYGLASS_PANE_GRAY.get(), ItemsPM.STAINED_SKYGLASS_PANE_GREEN.get(), ItemsPM.STAINED_SKYGLASS_PANE_LIGHT_BLUE.get(), ItemsPM.STAINED_SKYGLASS_PANE_LIGHT_GRAY.get(), ItemsPM.STAINED_SKYGLASS_PANE_LIME.get(), ItemsPM.STAINED_SKYGLASS_PANE_MAGENTA.get(), ItemsPM.STAINED_SKYGLASS_PANE_ORANGE.get(), ItemsPM.STAINED_SKYGLASS_PANE_PINK.get(), ItemsPM.STAINED_SKYGLASS_PANE_PURPLE.get(), ItemsPM.STAINED_SKYGLASS_PANE_RED.get(), ItemsPM.STAINED_SKYGLASS_PANE_WHITE.get(), ItemsPM.STAINED_SKYGLASS_PANE_YELLOW.get());
        
        event.register((stack, dummy) -> {
            if (stack.getItem() instanceof BlockItem blockItem) {
                if (blockItem.getBlock() instanceof RitualCandleBlock candleBlock) {
                    int color = candleBlock.getColor().getFireworkColor();
                    if (FastColor.ARGB32.alpha(color) == 0) {
                        // Assume color is opaque if alpha channel is set to zero
                        color = FastColor.ARGB32.opaque(color);
                    }
                    return color;
                }
            }
            return FastColor.ARGB32.opaque(DyeColor.WHITE.getFireworkColor());
        }, ItemsPM.RITUAL_CANDLE_BLACK.get(), ItemsPM.RITUAL_CANDLE_BLUE.get(), ItemsPM.RITUAL_CANDLE_BROWN.get(), ItemsPM.RITUAL_CANDLE_CYAN.get(), ItemsPM.RITUAL_CANDLE_GRAY.get(), ItemsPM.RITUAL_CANDLE_GREEN.get(), ItemsPM.RITUAL_CANDLE_LIGHT_BLUE.get(), ItemsPM.RITUAL_CANDLE_LIGHT_GRAY.get(), ItemsPM.RITUAL_CANDLE_LIME.get(), ItemsPM.RITUAL_CANDLE_MAGENTA.get(), ItemsPM.RITUAL_CANDLE_ORANGE.get(), ItemsPM.RITUAL_CANDLE_PINK.get(), ItemsPM.RITUAL_CANDLE_PURPLE.get(), ItemsPM.RITUAL_CANDLE_RED.get(), ItemsPM.RITUAL_CANDLE_WHITE.get(), ItemsPM.RITUAL_CANDLE_YELLOW.get());
        
        event.register((stack, color) -> {
            return color == 0 ? stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).getColor() : -1;
        }, ItemsPM.CONCOCTION.get(), ItemsPM.ALCHEMICAL_BOMB.get());
        
        AmbrosiaItem.getAllAmbrosias().forEach(ambrosia -> event.register((stack, tintIndex) -> getStackColor(stack, tintIndex, ambrosia::getColor), ambrosia));
        ManaArrowItem.getManaArrows().forEach(arrow -> event.register((stack, tintIndex) -> getStackColor(stack, tintIndex, arrow::getColor), arrow));
        HummingArtifactItem.getAllHummingArtifacts().forEach(artifact -> event.register((stack, tintIndex) -> getStackColor(stack, tintIndex, artifact::getColor), artifact));
        AttunementShacklesItem.getAllShackles().forEach(shackles -> event.register((stack, tintIndex) -> getStackColor(stack, tintIndex, shackles::getColor), shackles));
    }
    
    private static int getStackColor(ItemStack stack, int tintIndex, Function<Integer, Integer> baseColorGetter) {
        int color = baseColorGetter.apply(tintIndex);
        if (FastColor.ARGB32.alpha(color) == 0) {
            // Assume color is opaque if alpha channel is set to zero
            color = FastColor.ARGB32.opaque(color);
        }
        return color;
    }
}
