package com.verdantartifice.primalmagick.client.events;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.blocks.BlockRegistration;
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
        }, BlockRegistration.SALT_TRAIL.get());
        
        event.register((state, lightReader, pos, dummy) -> {
            return (state.getBlock() instanceof StainedSkyglassBlock) ? 
                    ((StainedSkyglassBlock)state.getBlock()).getColor().getFireworkColor() : 
                    DyeColor.WHITE.getFireworkColor();
        }, BlockRegistration.STAINED_SKYGLASS_BLACK.get(), BlockRegistration.STAINED_SKYGLASS_BLUE.get(), BlockRegistration.STAINED_SKYGLASS_BROWN.get(), BlockRegistration.STAINED_SKYGLASS_CYAN.get(), BlockRegistration.STAINED_SKYGLASS_GRAY.get(), BlockRegistration.STAINED_SKYGLASS_GREEN.get(), BlockRegistration.STAINED_SKYGLASS_LIGHT_BLUE.get(), BlockRegistration.STAINED_SKYGLASS_LIGHT_GRAY.get(), BlockRegistration.STAINED_SKYGLASS_LIME.get(), BlockRegistration.STAINED_SKYGLASS_MAGENTA.get(), BlockRegistration.STAINED_SKYGLASS_ORANGE.get(), BlockRegistration.STAINED_SKYGLASS_PINK.get(), BlockRegistration.STAINED_SKYGLASS_PURPLE.get(), BlockRegistration.STAINED_SKYGLASS_RED.get(), BlockRegistration.STAINED_SKYGLASS_WHITE.get(), BlockRegistration.STAINED_SKYGLASS_YELLOW.get());
        
        event.register((state, lightReader, pos, dummy) -> {
            return (state.getBlock() instanceof StainedSkyglassPaneBlock) ? 
                    ((StainedSkyglassPaneBlock)state.getBlock()).getColor().getFireworkColor() : 
                    DyeColor.WHITE.getFireworkColor();
        }, BlockRegistration.STAINED_SKYGLASS_PANE_BLACK.get(), BlockRegistration.STAINED_SKYGLASS_PANE_BLUE.get(), BlockRegistration.STAINED_SKYGLASS_PANE_BROWN.get(), BlockRegistration.STAINED_SKYGLASS_PANE_CYAN.get(), BlockRegistration.STAINED_SKYGLASS_PANE_GRAY.get(), BlockRegistration.STAINED_SKYGLASS_PANE_GREEN.get(), BlockRegistration.STAINED_SKYGLASS_PANE_LIGHT_BLUE.get(), BlockRegistration.STAINED_SKYGLASS_PANE_LIGHT_GRAY.get(), BlockRegistration.STAINED_SKYGLASS_PANE_LIME.get(), BlockRegistration.STAINED_SKYGLASS_PANE_MAGENTA.get(), BlockRegistration.STAINED_SKYGLASS_PANE_ORANGE.get(), BlockRegistration.STAINED_SKYGLASS_PANE_PINK.get(), BlockRegistration.STAINED_SKYGLASS_PANE_PURPLE.get(), BlockRegistration.STAINED_SKYGLASS_PANE_RED.get(), BlockRegistration.STAINED_SKYGLASS_PANE_WHITE.get(), BlockRegistration.STAINED_SKYGLASS_PANE_YELLOW.get());
        
        event.register((state, lightReader, pos, dummy) -> {
            return (state.getBlock() instanceof RitualCandleBlock) ?
                    ((RitualCandleBlock)state.getBlock()).getColor().getFireworkColor() :
                    DyeColor.WHITE.getFireworkColor();
        }, BlockRegistration.RITUAL_CANDLE_BLACK.get(), BlockRegistration.RITUAL_CANDLE_BLUE.get(), BlockRegistration.RITUAL_CANDLE_BROWN.get(), BlockRegistration.RITUAL_CANDLE_CYAN.get(), BlockRegistration.RITUAL_CANDLE_GRAY.get(), BlockRegistration.RITUAL_CANDLE_GREEN.get(), BlockRegistration.RITUAL_CANDLE_LIGHT_BLUE.get(), BlockRegistration.RITUAL_CANDLE_LIGHT_GRAY.get(), BlockRegistration.RITUAL_CANDLE_LIME.get(), BlockRegistration.RITUAL_CANDLE_MAGENTA.get(), BlockRegistration.RITUAL_CANDLE_ORANGE.get(), BlockRegistration.RITUAL_CANDLE_PINK.get(), BlockRegistration.RITUAL_CANDLE_PURPLE.get(), BlockRegistration.RITUAL_CANDLE_RED.get(), BlockRegistration.RITUAL_CANDLE_WHITE.get(), BlockRegistration.RITUAL_CANDLE_YELLOW.get());
        
        event.register((state, lightReader, pos, dummy) -> {
            return 14731036;
        }, BlockRegistration.ATTACHED_HYDROMELON_STEM.get());
        
        event.register((state, lightReader, pos, dummy) -> {
            int i = state.getValue(StemBlock.AGE);
            int j = i * 32;
            int k = 255 - i * 8;
            int l = i * 4;
            return j << 16 | k << 8 | l;
        }, BlockRegistration.HYRDOMELON_STEM.get());
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
