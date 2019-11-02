package com.verdantartifice.primalmagic.common.init;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blocks.crafting.ArcaneWorkbenchBlock;
import com.verdantartifice.primalmagic.common.blocks.crafting.WandAssemblyTableBlock;
import com.verdantartifice.primalmagic.common.blocks.mana.AncientManaFontBlock;
import com.verdantartifice.primalmagic.common.blocks.misc.AnalysisTableBlock;
import com.verdantartifice.primalmagic.common.blocks.misc.MarblePillarBlock;
import com.verdantartifice.primalmagic.common.blocks.misc.WoodTableBlock;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.registries.IForgeRegistry;

public class InitBlocks {
    public static void initBlocks(IForgeRegistry<Block> registry) {
        registry.register(new Block(Block.Properties.create(Material.ROCK, MaterialColor.QUARTZ).hardnessAndResistance(1.5F, 6.0F)).setRegistryName(PrimalMagic.MODID, "marble_raw"));
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
