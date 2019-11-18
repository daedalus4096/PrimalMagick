package com.verdantartifice.primalmagic.common.worldgen.features;

import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.ScatteredStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class ShrineStructure extends ScatteredStructure<NoFeatureConfig> {
    public ShrineStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> p_i51449_1_) {
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
        public Start(Structure<?> p_i51341_1_, int chunkX, int chunkZ, Biome biomeIn, MutableBoundingBox boundsIn, int referenceIn, long seed) {
            super(p_i51341_1_, chunkX, chunkZ, biomeIn, boundsIn, referenceIn, seed);
        }

        @Override
        public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn) {
            // TODO Auto-generated method stub
            
        }
    }
}
