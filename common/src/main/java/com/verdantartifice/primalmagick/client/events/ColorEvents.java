package com.verdantartifice.primalmagick.client.events;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.client.color.block.BlockTintSourcesPM;
import com.verdantartifice.primalmagick.client.color.item.SourceTint;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.function.BiConsumer;

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
}
