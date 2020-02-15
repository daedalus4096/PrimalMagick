package com.verdantartifice.primalmagic.common.worldgen.features;

import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.ScatteredStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

/**
 * Definition of a primal shrine structure.
 * 
 * @author Daedalus4096
 * @see {@link net.minecraft.world.gen.feature.structure.DesertPyramidStructure}
 */
public class ShrineStructure extends ScatteredStructure<ShrineConfig> {
    public ShrineStructure(Function<Dynamic<?>, ? extends ShrineConfig> p_i51449_1_) {
        super(p_i51449_1_);
    }

    @Override
    protected int getSeedModifier() {
        return 11893192;
    }

    @Override
    public IStartFactory getStartFactory() {
        return ShrineStructure.Start::new;
    }

    @Override
    public String getStructureName() {
        return "Primal_Shrine";
    }

    @Override
    public int getSize() {
        return 3;
    }

    public static class Start extends StructureStart {
        public Start(Structure<?> p_i51341_1_, int chunkX, int chunkZ, MutableBoundingBox boundsIn, int referenceIn, long seed) {
            super(p_i51341_1_, chunkX, chunkZ, boundsIn, referenceIn, seed);
        }

        @Override
        public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn) {
            ShrineConfig config = generator.getStructureConfig(biomeIn, FeaturesPM.SHRINE);
            BlockPos pos = new BlockPos(chunkX * 16, 90, chunkZ * 16);
            this.components.add(new ShrinePiece(templateManagerIn, config.source, pos));
            this.recalculateStructureSize();
        }
    }
}
