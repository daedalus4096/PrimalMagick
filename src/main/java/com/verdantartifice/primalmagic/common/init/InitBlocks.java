package com.verdantartifice.primalmagic.common.init;

import java.lang.reflect.Constructor;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blocks.crafting.ArcaneWorkbenchBlock;
import com.verdantartifice.primalmagic.common.blocks.crafting.WandAssemblyTableBlock;
import com.verdantartifice.primalmagic.common.blocks.mana.AncientManaFontBlock;
import com.verdantartifice.primalmagic.common.blocks.misc.AnalysisTableBlock;
import com.verdantartifice.primalmagic.common.blocks.misc.MarblePillarBlock;
import com.verdantartifice.primalmagic.common.blocks.misc.WoodTableBlock;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.registries.IForgeRegistry;

public class InitBlocks {
    public static void initBlocks(IForgeRegistry<Block> registry) {
        Constructor<StairsBlock> stairsConstructor;
        try {
            stairsConstructor = StairsBlock.class.getDeclaredConstructor(BlockState.class, Block.Properties.class);
            stairsConstructor.setAccessible(true);
        } catch (Throwable t) {
            stairsConstructor = null;
            PrimalMagic.LOGGER.error("Unable to gain access to StairsBlock constructor");
            PrimalMagic.LOGGER.catching(t);
        }
        
        Block marbleRaw = new Block(Block.Properties.create(Material.ROCK, MaterialColor.QUARTZ).hardnessAndResistance(1.5F, 6.0F)).setRegistryName(PrimalMagic.MODID, "marble_raw");
        registry.register(marbleRaw);
        registry.register(new SlabBlock(Block.Properties.create(Material.ROCK, MaterialColor.QUARTZ).hardnessAndResistance(1.5F, 6.0F).sound(SoundType.STONE)).setRegistryName(PrimalMagic.MODID, "marble_slab"));
        if (stairsConstructor != null) {
            try {
                registry.register(stairsConstructor.newInstance(marbleRaw.getDefaultState(), Block.Properties.from(marbleRaw)).setRegistryName(PrimalMagic.MODID, "marble_stairs"));
            } catch (Throwable t) {
                PrimalMagic.LOGGER.error("Unable to register marble_stairs block");
                PrimalMagic.LOGGER.catching(t);
            }
        }
        registry.register(new Block(Block.Properties.create(Material.ROCK, MaterialColor.QUARTZ).hardnessAndResistance(1.5F, 6.0F).sound(SoundType.STONE)).setRegistryName(PrimalMagic.MODID, "marble_bricks"));
        registry.register(new MarblePillarBlock());
        registry.register(new Block(Block.Properties.create(Material.ROCK, MaterialColor.QUARTZ).hardnessAndResistance(1.5F, 6.0F)).setRegistryName(PrimalMagic.MODID, "marble_enchanted"));
        registry.register(new ArcaneWorkbenchBlock());
        registry.register(new AncientManaFontBlock(Source.EARTH));
        registry.register(new AncientManaFontBlock(Source.SEA));
        registry.register(new AncientManaFontBlock(Source.SKY));
        registry.register(new AncientManaFontBlock(Source.SUN));
        registry.register(new AncientManaFontBlock(Source.MOON));
        registry.register(new WandAssemblyTableBlock());
        registry.register(new WoodTableBlock());
        registry.register(new AnalysisTableBlock());
    }
}
