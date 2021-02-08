package com.verdantartifice.primalmagic.common.worldgen.features;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

/**
 * Definition of a primal shrine structure.
 * 
 * @author Daedalus4096
 * @see {@link net.minecraft.world.gen.feature.structure.DesertPyramidStructure}
 */
public class ShrineStructure extends Structure<ShrineConfig> {
    public ShrineStructure(Codec<ShrineConfig> codec) {
        super(codec);
    }

    @Override
    protected int getSeedModifier() {
        return 11893192;
    }

    @Override
    public Structure.IStartFactory<ShrineConfig> getStartFactory() {
        return ShrineStructure.Start::new;
    }
    
    @Override
    public GenerationStage.Decoration func_236396_f_() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }

    @Override
    public String getStructureName() {
        return "primalmagic:shrine";
    }

    @Override
    public int getSize() {
        return 3;
    }

    public static class Start extends StructureStart<ShrineConfig> {
        public Start(Structure<ShrineConfig> structure, int chunkX, int chunkZ, MutableBoundingBox boundsIn, int referenceIn, long seed) {
            super(structure, chunkX, chunkZ, boundsIn, referenceIn, seed);
        }

        @Override
        public void func_230364_a_(DynamicRegistries dynamicRegistryManager, ChunkGenerator generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, ShrineConfig config) {
        	int x = (chunkX << 4) + 7;
            int z = (chunkZ << 4) + 7;
            int surfaceY = generator.getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG);
        	BlockPos pos = new BlockPos(x, surfaceY, z);
            this.components.add(new ShrinePiece(templateManagerIn, config.type, pos));
            this.recalculateStructureSize();
        }
    }
    
    public static enum Type implements IStringSerializable {
    	EARTH("earth"),
    	SEA("sea"),
    	SKY("sky"),
    	SUN("sun"),
    	MOON("moon");
    	
    	private final String name;

    	public static final Codec<ShrineStructure.Type> CODEC = IStringSerializable.createEnumCodec(ShrineStructure.Type::values, ShrineStructure.Type::byName);
        private static final Map<String, ShrineStructure.Type> BY_NAME = Arrays.stream(values()).collect(Collectors.toMap(ShrineStructure.Type::getString, (type) -> {
            return type;
        }));

    	private Type(String name) {
    		this.name = name;
    	}
    	
    	public static ShrineStructure.Type byName(String name) {
    		return BY_NAME.get(name);
    	}
    	
    	public String getString() {
    		return this.name;
    	}
    }
}
