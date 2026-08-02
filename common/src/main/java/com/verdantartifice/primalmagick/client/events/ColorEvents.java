package com.verdantartifice.primalmagick.client.events;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.client.color.block.BlockTintSourcesPM;
import com.verdantartifice.primalmagick.client.color.item.SourceTint;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.blocks.misc.StainedSkyglassBlock;
import com.verdantartifice.primalmagick.common.blocks.misc.StainedSkyglassPaneBlock;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Respond to client-only block/item color events.
 * 
 * @author Daedalus4096
 */
public class ColorEvents {
    public static void onBlockColorInit(BlockColorRegistrar blockColors) {
        blockColors.register(List.of(BlockTintSourcesPM.salt()), BlocksPM.SALT_TRAIL.get());
        blockColors.register(List.of(BlockTintSourcesPM.tinted()), BlocksPM.STAINED_SKYGLASS_BLACK.get(), BlocksPM.STAINED_SKYGLASS_BLUE.get(), BlocksPM.STAINED_SKYGLASS_BROWN.get(), BlocksPM.STAINED_SKYGLASS_CYAN.get(), BlocksPM.STAINED_SKYGLASS_GRAY.get(), BlocksPM.STAINED_SKYGLASS_GREEN.get(), BlocksPM.STAINED_SKYGLASS_LIGHT_BLUE.get(), BlocksPM.STAINED_SKYGLASS_LIGHT_GRAY.get(), BlocksPM.STAINED_SKYGLASS_LIME.get(), BlocksPM.STAINED_SKYGLASS_MAGENTA.get(), BlocksPM.STAINED_SKYGLASS_ORANGE.get(), BlocksPM.STAINED_SKYGLASS_PINK.get(), BlocksPM.STAINED_SKYGLASS_PURPLE.get(), BlocksPM.STAINED_SKYGLASS_RED.get(), BlocksPM.STAINED_SKYGLASS_WHITE.get(), BlocksPM.STAINED_SKYGLASS_YELLOW.get());
        blockColors.register(List.of(BlockTintSourcesPM.tinted()), BlocksPM.STAINED_SKYGLASS_PANE_BLACK.get(), BlocksPM.STAINED_SKYGLASS_PANE_BLUE.get(), BlocksPM.STAINED_SKYGLASS_PANE_BROWN.get(), BlocksPM.STAINED_SKYGLASS_PANE_CYAN.get(), BlocksPM.STAINED_SKYGLASS_PANE_GRAY.get(), BlocksPM.STAINED_SKYGLASS_PANE_GREEN.get(), BlocksPM.STAINED_SKYGLASS_PANE_LIGHT_BLUE.get(), BlocksPM.STAINED_SKYGLASS_PANE_LIGHT_GRAY.get(), BlocksPM.STAINED_SKYGLASS_PANE_LIME.get(), BlocksPM.STAINED_SKYGLASS_PANE_MAGENTA.get(), BlocksPM.STAINED_SKYGLASS_PANE_ORANGE.get(), BlocksPM.STAINED_SKYGLASS_PANE_PINK.get(), BlocksPM.STAINED_SKYGLASS_PANE_PURPLE.get(), BlocksPM.STAINED_SKYGLASS_PANE_RED.get(), BlocksPM.STAINED_SKYGLASS_PANE_WHITE.get(), BlocksPM.STAINED_SKYGLASS_PANE_YELLOW.get());
        blockColors.register(List.of(BlockTintSourcesPM.tinted()), BlocksPM.RITUAL_CANDLE_BLACK.get(), BlocksPM.RITUAL_CANDLE_BLUE.get(), BlocksPM.RITUAL_CANDLE_BROWN.get(), BlocksPM.RITUAL_CANDLE_CYAN.get(), BlocksPM.RITUAL_CANDLE_GRAY.get(), BlocksPM.RITUAL_CANDLE_GREEN.get(), BlocksPM.RITUAL_CANDLE_LIGHT_BLUE.get(), BlocksPM.RITUAL_CANDLE_LIGHT_GRAY.get(), BlocksPM.RITUAL_CANDLE_LIME.get(), BlocksPM.RITUAL_CANDLE_MAGENTA.get(), BlocksPM.RITUAL_CANDLE_ORANGE.get(), BlocksPM.RITUAL_CANDLE_PINK.get(), BlocksPM.RITUAL_CANDLE_PURPLE.get(), BlocksPM.RITUAL_CANDLE_RED.get(), BlocksPM.RITUAL_CANDLE_WHITE.get(), BlocksPM.RITUAL_CANDLE_YELLOW.get());
        blockColors.register(List.of(BlockTintSources.constant(14731036)), BlocksPM.ATTACHED_HYDROMELON_STEM.get());
        blockColors.register(List.of(BlockTintSources.stem()), BlocksPM.HYDROMELON_STEM.get());
    }

    public interface BlockColorRegistrar {
        void register(List<BlockTintSource> tintSources, Block... blocks);
    }

    public static void onItemTintSourceInit(BiConsumer<Identifier, MapCodec<? extends ItemTintSource>> tintMapper) {
        tintMapper.accept(ResourceUtils.loc("source"), SourceTint.MAP_CODEC);
    }

    public static void onItemColorInit(ItemColorRegistrar itemColors) {
        itemColors.register((stack, dummy) -> {
            if (stack.getItem() instanceof BlockItem blockItem) {
                if (blockItem.getBlock() instanceof StainedSkyglassBlock skyglassBlock) {
                    int color = skyglassBlock.getColor().getFireworkColor();
                    if (ARGB.alpha(color) == 0) {
                        // Assume color is opaque if alpha channel is set to zero
                        color = ARGB.opaque(color);
                    }
                    return color;
                }
            }
            return ARGB.opaque(DyeColor.WHITE.getFireworkColor());
        }, ItemsPM.STAINED_SKYGLASS_BLACK.get(), ItemsPM.STAINED_SKYGLASS_BLUE.get(), ItemsPM.STAINED_SKYGLASS_BROWN.get(), ItemsPM.STAINED_SKYGLASS_CYAN.get(), ItemsPM.STAINED_SKYGLASS_GRAY.get(), ItemsPM.STAINED_SKYGLASS_GREEN.get(), ItemsPM.STAINED_SKYGLASS_LIGHT_BLUE.get(), ItemsPM.STAINED_SKYGLASS_LIGHT_GRAY.get(), ItemsPM.STAINED_SKYGLASS_LIME.get(), ItemsPM.STAINED_SKYGLASS_MAGENTA.get(), ItemsPM.STAINED_SKYGLASS_ORANGE.get(), ItemsPM.STAINED_SKYGLASS_PINK.get(), ItemsPM.STAINED_SKYGLASS_PURPLE.get(), ItemsPM.STAINED_SKYGLASS_RED.get(), ItemsPM.STAINED_SKYGLASS_WHITE.get(), ItemsPM.STAINED_SKYGLASS_YELLOW.get());

        itemColors.register((stack, dummy) -> {
            if (stack.getItem() instanceof BlockItem blockItem) {
                if (blockItem.getBlock() instanceof StainedSkyglassPaneBlock skyglassBlock) {
                    int color = skyglassBlock.getColor().getFireworkColor();
                    if (ARGB.alpha(color) == 0) {
                        // Assume color is opaque if alpha channel is set to zero
                        color = ARGB.opaque(color);
                    }
                    return color;
                }
            }
            return ARGB.opaque(DyeColor.WHITE.getFireworkColor());
        }, ItemsPM.STAINED_SKYGLASS_PANE_BLACK.get(), ItemsPM.STAINED_SKYGLASS_PANE_BLUE.get(), ItemsPM.STAINED_SKYGLASS_PANE_BROWN.get(), ItemsPM.STAINED_SKYGLASS_PANE_CYAN.get(), ItemsPM.STAINED_SKYGLASS_PANE_GRAY.get(), ItemsPM.STAINED_SKYGLASS_PANE_GREEN.get(), ItemsPM.STAINED_SKYGLASS_PANE_LIGHT_BLUE.get(), ItemsPM.STAINED_SKYGLASS_PANE_LIGHT_GRAY.get(), ItemsPM.STAINED_SKYGLASS_PANE_LIME.get(), ItemsPM.STAINED_SKYGLASS_PANE_MAGENTA.get(), ItemsPM.STAINED_SKYGLASS_PANE_ORANGE.get(), ItemsPM.STAINED_SKYGLASS_PANE_PINK.get(), ItemsPM.STAINED_SKYGLASS_PANE_PURPLE.get(), ItemsPM.STAINED_SKYGLASS_PANE_RED.get(), ItemsPM.STAINED_SKYGLASS_PANE_WHITE.get(), ItemsPM.STAINED_SKYGLASS_PANE_YELLOW.get());
    }

    public interface ItemColorRegistrar {
        void register(ItemColor itemColor, ItemLike... blocks);
    }

    private static int getStackColor(ItemStack stack, int tintIndex, Function<Integer, Integer> baseColorGetter) {
        int color = baseColorGetter.apply(tintIndex);
        if (ARGB.alpha(color) == 0) {
            // Assume color is opaque if alpha channel is set to zero
            color = ARGB.opaque(color);
        }
        return color;
    }
}
