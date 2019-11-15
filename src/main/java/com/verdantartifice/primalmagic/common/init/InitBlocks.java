package com.verdantartifice.primalmagic.common.init;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blocks.base.SaplingBlockPM;
import com.verdantartifice.primalmagic.common.blocks.base.StairsBlockPM;
import com.verdantartifice.primalmagic.common.blocks.crafting.ArcaneWorkbenchBlock;
import com.verdantartifice.primalmagic.common.blocks.crafting.WandAssemblyTableBlock;
import com.verdantartifice.primalmagic.common.blocks.mana.AncientManaFontBlock;
import com.verdantartifice.primalmagic.common.blocks.misc.AnalysisTableBlock;
import com.verdantartifice.primalmagic.common.blocks.misc.PillarBlock;
import com.verdantartifice.primalmagic.common.blocks.misc.WoodTableBlock;
import com.verdantartifice.primalmagic.common.blocks.trees.SunwoodLeavesBlock;
import com.verdantartifice.primalmagic.common.blocks.trees.SunwoodLogBlock;
import com.verdantartifice.primalmagic.common.blocks.trees.SunwoodPlanksBlock;
import com.verdantartifice.primalmagic.common.blocks.trees.SunwoodSlabBlock;
import com.verdantartifice.primalmagic.common.blocks.trees.SunwoodStairsBlock;
import com.verdantartifice.primalmagic.common.blocks.trees.SunwoodTree;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.WallBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.registries.IForgeRegistry;

public class InitBlocks {
    public static void initBlocks(IForgeRegistry<Block> registry) {
        Block marbleRaw = new Block(Block.Properties.create(Material.ROCK, MaterialColor.QUARTZ).hardnessAndResistance(1.5F, 6.0F).sound(SoundType.STONE)).setRegistryName(PrimalMagic.MODID, "marble_raw");
        registry.register(marbleRaw);
        registry.register(new SlabBlock(Block.Properties.from(marbleRaw)).setRegistryName(PrimalMagic.MODID, "marble_slab"));
        registry.register(new StairsBlockPM(marbleRaw.getDefaultState(), Block.Properties.from(marbleRaw)).setRegistryName(PrimalMagic.MODID, "marble_stairs"));
        registry.register(new WallBlock(Block.Properties.from(marbleRaw)).setRegistryName(PrimalMagic.MODID, "marble_wall"));
        Block marbleBricks = new Block(Block.Properties.from(marbleRaw)).setRegistryName(PrimalMagic.MODID, "marble_bricks");
        registry.register(marbleBricks);
        registry.register(new SlabBlock(Block.Properties.from(marbleBricks)).setRegistryName(PrimalMagic.MODID, "marble_brick_slab"));
        registry.register(new StairsBlockPM(marbleBricks.getDefaultState(), Block.Properties.from(marbleBricks)).setRegistryName(PrimalMagic.MODID, "marble_brick_stairs"));
        registry.register(new WallBlock(Block.Properties.from(marbleBricks)).setRegistryName(PrimalMagic.MODID, "marble_brick_wall"));
        registry.register(new PillarBlock(Block.Properties.from(marbleRaw)).setRegistryName(PrimalMagic.MODID, "marble_pillar"));
        registry.register(new Block(Block.Properties.from(marbleRaw)).setRegistryName(PrimalMagic.MODID, "marble_chiseled"));
        registry.register(new Block(Block.Properties.from(marbleRaw)).setRegistryName(PrimalMagic.MODID, "marble_runed"));

        Block marbleEnchanted = new Block(Block.Properties.create(Material.ROCK, MaterialColor.QUARTZ).hardnessAndResistance(3.0F, 12.0F).sound(SoundType.STONE)).setRegistryName(PrimalMagic.MODID, "marble_enchanted");
        registry.register(marbleEnchanted);
        registry.register(new SlabBlock(Block.Properties.from(marbleEnchanted)).setRegistryName(PrimalMagic.MODID, "marble_enchanted_slab"));
        registry.register(new StairsBlockPM(marbleEnchanted.getDefaultState(), Block.Properties.from(marbleEnchanted)).setRegistryName(PrimalMagic.MODID, "marble_enchanted_stairs"));
        registry.register(new WallBlock(Block.Properties.from(marbleEnchanted)).setRegistryName(PrimalMagic.MODID, "marble_enchanted_wall"));
        Block marbleEnchantedBricks = new Block(Block.Properties.from(marbleEnchanted)).setRegistryName(PrimalMagic.MODID, "marble_enchanted_bricks");
        registry.register(marbleEnchantedBricks);
        registry.register(new SlabBlock(Block.Properties.from(marbleEnchantedBricks)).setRegistryName(PrimalMagic.MODID, "marble_enchanted_brick_slab"));
        registry.register(new StairsBlockPM(marbleEnchantedBricks.getDefaultState(), Block.Properties.from(marbleEnchantedBricks)).setRegistryName(PrimalMagic.MODID, "marble_enchanted_brick_stairs"));
        registry.register(new WallBlock(Block.Properties.from(marbleEnchantedBricks)).setRegistryName(PrimalMagic.MODID, "marble_enchanted_brick_wall"));
        registry.register(new PillarBlock(Block.Properties.from(marbleEnchanted)).setRegistryName(PrimalMagic.MODID, "marble_enchanted_pillar"));
        registry.register(new Block(Block.Properties.from(marbleEnchanted)).setRegistryName(PrimalMagic.MODID, "marble_enchanted_chiseled"));
        registry.register(new Block(Block.Properties.from(marbleEnchanted)).setRegistryName(PrimalMagic.MODID, "marble_enchanted_runed"));

        Block marbleSmoked = new Block(Block.Properties.create(Material.ROCK, MaterialColor.OBSIDIAN).hardnessAndResistance(1.5F, 6.0F).sound(SoundType.STONE)).setRegistryName(PrimalMagic.MODID, "marble_smoked");
        registry.register(marbleSmoked);
        registry.register(new SlabBlock(Block.Properties.from(marbleSmoked)).setRegistryName(PrimalMagic.MODID, "marble_smoked_slab"));
        registry.register(new StairsBlockPM(marbleSmoked.getDefaultState(), Block.Properties.from(marbleSmoked)).setRegistryName(PrimalMagic.MODID, "marble_smoked_stairs"));
        registry.register(new WallBlock(Block.Properties.from(marbleSmoked)).setRegistryName(PrimalMagic.MODID, "marble_smoked_wall"));
        Block marbleSmokedBricks = new Block(Block.Properties.from(marbleSmoked)).setRegistryName(PrimalMagic.MODID, "marble_smoked_bricks");
        registry.register(marbleSmokedBricks);
        registry.register(new SlabBlock(Block.Properties.from(marbleSmokedBricks)).setRegistryName(PrimalMagic.MODID, "marble_smoked_brick_slab"));
        registry.register(new StairsBlockPM(marbleSmokedBricks.getDefaultState(), Block.Properties.from(marbleSmokedBricks)).setRegistryName(PrimalMagic.MODID, "marble_smoked_brick_stairs"));
        registry.register(new WallBlock(Block.Properties.from(marbleSmokedBricks)).setRegistryName(PrimalMagic.MODID, "marble_smoked_brick_wall"));
        registry.register(new PillarBlock(Block.Properties.from(marbleSmoked)).setRegistryName(PrimalMagic.MODID, "marble_smoked_pillar"));
        registry.register(new Block(Block.Properties.from(marbleSmoked)).setRegistryName(PrimalMagic.MODID, "marble_smoked_chiseled"));
        registry.register(new Block(Block.Properties.from(marbleSmoked)).setRegistryName(PrimalMagic.MODID, "marble_smoked_runed"));

        Block strippedSunwoodLog = new SunwoodLogBlock(null).setRegistryName(PrimalMagic.MODID, "stripped_sunwood_log");
        registry.register(strippedSunwoodLog);
        registry.register(new SunwoodLogBlock(strippedSunwoodLog).setRegistryName(PrimalMagic.MODID, "sunwood_log"));
        Block strippedSunwoodWood = new SunwoodLogBlock(null).setRegistryName(PrimalMagic.MODID, "stripped_sunwood_wood");
        registry.register(strippedSunwoodWood);
        registry.register(new SunwoodLogBlock(strippedSunwoodWood).setRegistryName(PrimalMagic.MODID, "sunwood_wood"));
        registry.register(new SunwoodLeavesBlock());
        registry.register(new SaplingBlockPM(new SunwoodTree(), Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0.0F).sound(SoundType.PLANT)).setRegistryName(PrimalMagic.MODID, "sunwood_sapling"));
        Block sunwoodPlanks = new SunwoodPlanksBlock();
        registry.register(sunwoodPlanks);
        registry.register(new SunwoodSlabBlock());
        registry.register(new SunwoodStairsBlock(sunwoodPlanks.getDefaultState(), Block.Properties.from(sunwoodPlanks)).setRegistryName(PrimalMagic.MODID, "sunwood_stairs"));

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
