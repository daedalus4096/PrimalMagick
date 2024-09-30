package com.verdantartifice.primalmagick.client.events;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.blocks.misc.StainedSkyglassBlock;
import com.verdantartifice.primalmagick.common.blocks.misc.StainedSkyglassPaneBlock;
import com.verdantartifice.primalmagick.common.blocks.rituals.RitualCandleBlock;
import com.verdantartifice.primalmagick.common.blocks.rituals.SaltTrailBlock;
import com.verdantartifice.primalmagick.common.items.ItemRegistration;
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
        }, BlocksPM.SALT_TRAIL.get());
        
        event.register((state, lightReader, pos, dummy) -> {
            return (state.getBlock() instanceof StainedSkyglassBlock) ? 
                    ((StainedSkyglassBlock)state.getBlock()).getColor().getFireworkColor() : 
                    DyeColor.WHITE.getFireworkColor();
        }, BlocksPM.STAINED_SKYGLASS_BLACK.get(), BlocksPM.STAINED_SKYGLASS_BLUE.get(), BlocksPM.STAINED_SKYGLASS_BROWN.get(), BlocksPM.STAINED_SKYGLASS_CYAN.get(), BlocksPM.STAINED_SKYGLASS_GRAY.get(), BlocksPM.STAINED_SKYGLASS_GREEN.get(), BlocksPM.STAINED_SKYGLASS_LIGHT_BLUE.get(), BlocksPM.STAINED_SKYGLASS_LIGHT_GRAY.get(), BlocksPM.STAINED_SKYGLASS_LIME.get(), BlocksPM.STAINED_SKYGLASS_MAGENTA.get(), BlocksPM.STAINED_SKYGLASS_ORANGE.get(), BlocksPM.STAINED_SKYGLASS_PINK.get(), BlocksPM.STAINED_SKYGLASS_PURPLE.get(), BlocksPM.STAINED_SKYGLASS_RED.get(), BlocksPM.STAINED_SKYGLASS_WHITE.get(), BlocksPM.STAINED_SKYGLASS_YELLOW.get());
        
        event.register((state, lightReader, pos, dummy) -> {
            return (state.getBlock() instanceof StainedSkyglassPaneBlock) ? 
                    ((StainedSkyglassPaneBlock)state.getBlock()).getColor().getFireworkColor() : 
                    DyeColor.WHITE.getFireworkColor();
        }, BlocksPM.STAINED_SKYGLASS_PANE_BLACK.get(), BlocksPM.STAINED_SKYGLASS_PANE_BLUE.get(), BlocksPM.STAINED_SKYGLASS_PANE_BROWN.get(), BlocksPM.STAINED_SKYGLASS_PANE_CYAN.get(), BlocksPM.STAINED_SKYGLASS_PANE_GRAY.get(), BlocksPM.STAINED_SKYGLASS_PANE_GREEN.get(), BlocksPM.STAINED_SKYGLASS_PANE_LIGHT_BLUE.get(), BlocksPM.STAINED_SKYGLASS_PANE_LIGHT_GRAY.get(), BlocksPM.STAINED_SKYGLASS_PANE_LIME.get(), BlocksPM.STAINED_SKYGLASS_PANE_MAGENTA.get(), BlocksPM.STAINED_SKYGLASS_PANE_ORANGE.get(), BlocksPM.STAINED_SKYGLASS_PANE_PINK.get(), BlocksPM.STAINED_SKYGLASS_PANE_PURPLE.get(), BlocksPM.STAINED_SKYGLASS_PANE_RED.get(), BlocksPM.STAINED_SKYGLASS_PANE_WHITE.get(), BlocksPM.STAINED_SKYGLASS_PANE_YELLOW.get());
        
        event.register((state, lightReader, pos, dummy) -> {
            return (state.getBlock() instanceof RitualCandleBlock) ?
                    ((RitualCandleBlock)state.getBlock()).getColor().getFireworkColor() :
                    DyeColor.WHITE.getFireworkColor();
        }, BlocksPM.RITUAL_CANDLE_BLACK.get(), BlocksPM.RITUAL_CANDLE_BLUE.get(), BlocksPM.RITUAL_CANDLE_BROWN.get(), BlocksPM.RITUAL_CANDLE_CYAN.get(), BlocksPM.RITUAL_CANDLE_GRAY.get(), BlocksPM.RITUAL_CANDLE_GREEN.get(), BlocksPM.RITUAL_CANDLE_LIGHT_BLUE.get(), BlocksPM.RITUAL_CANDLE_LIGHT_GRAY.get(), BlocksPM.RITUAL_CANDLE_LIME.get(), BlocksPM.RITUAL_CANDLE_MAGENTA.get(), BlocksPM.RITUAL_CANDLE_ORANGE.get(), BlocksPM.RITUAL_CANDLE_PINK.get(), BlocksPM.RITUAL_CANDLE_PURPLE.get(), BlocksPM.RITUAL_CANDLE_RED.get(), BlocksPM.RITUAL_CANDLE_WHITE.get(), BlocksPM.RITUAL_CANDLE_YELLOW.get());
        
        event.register((state, lightReader, pos, dummy) -> {
            return 14731036;
        }, BlocksPM.ATTACHED_HYDROMELON_STEM.get());
        
        event.register((state, lightReader, pos, dummy) -> {
            int i = state.getValue(StemBlock.AGE);
            int j = i * 32;
            int k = 255 - i * 8;
            int l = i * 4;
            return j << 16 | k << 8 | l;
        }, BlocksPM.HYDROMELON_STEM.get());
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
        }, ItemRegistration.STAINED_SKYGLASS_BLACK.get(), ItemRegistration.STAINED_SKYGLASS_BLUE.get(), ItemRegistration.STAINED_SKYGLASS_BROWN.get(), ItemRegistration.STAINED_SKYGLASS_CYAN.get(), ItemRegistration.STAINED_SKYGLASS_GRAY.get(), ItemRegistration.STAINED_SKYGLASS_GREEN.get(), ItemRegistration.STAINED_SKYGLASS_LIGHT_BLUE.get(), ItemRegistration.STAINED_SKYGLASS_LIGHT_GRAY.get(), ItemRegistration.STAINED_SKYGLASS_LIME.get(), ItemRegistration.STAINED_SKYGLASS_MAGENTA.get(), ItemRegistration.STAINED_SKYGLASS_ORANGE.get(), ItemRegistration.STAINED_SKYGLASS_PINK.get(), ItemRegistration.STAINED_SKYGLASS_PURPLE.get(), ItemRegistration.STAINED_SKYGLASS_RED.get(), ItemRegistration.STAINED_SKYGLASS_WHITE.get(), ItemRegistration.STAINED_SKYGLASS_YELLOW.get());

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
        }, ItemRegistration.STAINED_SKYGLASS_PANE_BLACK.get(), ItemRegistration.STAINED_SKYGLASS_PANE_BLUE.get(), ItemRegistration.STAINED_SKYGLASS_PANE_BROWN.get(), ItemRegistration.STAINED_SKYGLASS_PANE_CYAN.get(), ItemRegistration.STAINED_SKYGLASS_PANE_GRAY.get(), ItemRegistration.STAINED_SKYGLASS_PANE_GREEN.get(), ItemRegistration.STAINED_SKYGLASS_PANE_LIGHT_BLUE.get(), ItemRegistration.STAINED_SKYGLASS_PANE_LIGHT_GRAY.get(), ItemRegistration.STAINED_SKYGLASS_PANE_LIME.get(), ItemRegistration.STAINED_SKYGLASS_PANE_MAGENTA.get(), ItemRegistration.STAINED_SKYGLASS_PANE_ORANGE.get(), ItemRegistration.STAINED_SKYGLASS_PANE_PINK.get(), ItemRegistration.STAINED_SKYGLASS_PANE_PURPLE.get(), ItemRegistration.STAINED_SKYGLASS_PANE_RED.get(), ItemRegistration.STAINED_SKYGLASS_PANE_WHITE.get(), ItemRegistration.STAINED_SKYGLASS_PANE_YELLOW.get());
        
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
        }, ItemRegistration.RITUAL_CANDLE_BLACK.get(), ItemRegistration.RITUAL_CANDLE_BLUE.get(), ItemRegistration.RITUAL_CANDLE_BROWN.get(), ItemRegistration.RITUAL_CANDLE_CYAN.get(), ItemRegistration.RITUAL_CANDLE_GRAY.get(), ItemRegistration.RITUAL_CANDLE_GREEN.get(), ItemRegistration.RITUAL_CANDLE_LIGHT_BLUE.get(), ItemRegistration.RITUAL_CANDLE_LIGHT_GRAY.get(), ItemRegistration.RITUAL_CANDLE_LIME.get(), ItemRegistration.RITUAL_CANDLE_MAGENTA.get(), ItemRegistration.RITUAL_CANDLE_ORANGE.get(), ItemRegistration.RITUAL_CANDLE_PINK.get(), ItemRegistration.RITUAL_CANDLE_PURPLE.get(), ItemRegistration.RITUAL_CANDLE_RED.get(), ItemRegistration.RITUAL_CANDLE_WHITE.get(), ItemRegistration.RITUAL_CANDLE_YELLOW.get());
        
        event.register((stack, color) -> {
            return color == 0 ? stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).getColor() : -1;
        }, ItemRegistration.CONCOCTION.get(), ItemRegistration.ALCHEMICAL_BOMB.get());
        
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
