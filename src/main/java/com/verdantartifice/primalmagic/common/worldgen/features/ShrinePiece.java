package com.verdantartifice.primalmagic.common.worldgen.features;

import java.util.Random;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blocks.BlocksPM;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraftforge.common.util.Constants;

/**
 * Definition of a piece of a primal shrine structure.
 * 
 * @author Daedalus4096
 * @see {@link net.minecraft.world.gen.feature.structure.DesertPyramidPiece}
 */
public class ShrinePiece extends TemplateStructurePiece {
    protected static final ResourceLocation TEMPLATE = new ResourceLocation(PrimalMagic.MODID, "shrine");
    
    protected final ShrineStructure.Type type;
    
    public ShrinePiece(StructureManager templateManager, ShrineStructure.Type type, BlockPos pos) {
        super(StructurePieceTypesPM.SHRINE, 0);
        this.type = type;
        this.templatePosition = pos;
        this.setupTemplate(templateManager);
    }

    public ShrinePiece(StructureManager templateManager, CompoundTag nbt) {
        super(StructurePieceTypesPM.SHRINE, nbt);
        this.type = ShrineStructure.Type.byName(nbt.getString("Source"));
        this.setupTemplate(templateManager);
    }
    
    protected void setupTemplate(StructureManager templateManager) {
        StructureTemplate template = templateManager.getOrCreate(TEMPLATE);
        StructurePlaceSettings settings = new StructurePlaceSettings().addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        this.setup(template, this.templatePosition, settings);
    }
    
    @Override
    protected void addAdditionalSaveData(CompoundTag tagCompound) {
        super.addAdditionalSaveData(tagCompound);
        tagCompound.putString("Source", this.type.getSerializedName());
    }

    @Override
    protected void handleDataMarker(String function, BlockPos pos, ServerLevelAccessor worldIn, Random rand, BoundingBox sbb) {
        if ("font".equals(function)) {
            worldIn.setBlock(pos, this.getFont().defaultBlockState(), Constants.BlockFlags.DEFAULT);
        }
    }

    @Nonnull
    protected Block getFont() {
        if (this.type == null) {
            return Blocks.AIR;
        } else if (this.type.equals(ShrineStructure.Type.EARTH)) {
            return BlocksPM.ANCIENT_FONT_EARTH.get();
        } else if (this.type.equals(ShrineStructure.Type.SEA)) {
            return BlocksPM.ANCIENT_FONT_SEA.get();
        } else if (this.type.equals(ShrineStructure.Type.SKY)) {
            return BlocksPM.ANCIENT_FONT_SKY.get();
        } else if (this.type.equals(ShrineStructure.Type.SUN)) {
            return BlocksPM.ANCIENT_FONT_SUN.get();
        } else if (this.type.equals(ShrineStructure.Type.MOON)) {
            return BlocksPM.ANCIENT_FONT_MOON.get();
        } else {
            return Blocks.AIR;
        }
    }
    
    @Nonnull
    protected Block getInfusedStone() {
        if (this.type == null) {
            return Blocks.AIR;
        } else if (this.type.equals(ShrineStructure.Type.EARTH)) {
            return BlocksPM.INFUSED_STONE_EARTH.get();
        } else if (this.type.equals(ShrineStructure.Type.SEA)) {
            return BlocksPM.INFUSED_STONE_SEA.get();
        } else if (this.type.equals(ShrineStructure.Type.SKY)) {
            return BlocksPM.INFUSED_STONE_SKY.get();
        } else if (this.type.equals(ShrineStructure.Type.SUN)) {
            return BlocksPM.INFUSED_STONE_SUN.get();
        } else if (this.type.equals(ShrineStructure.Type.MOON)) {
            return BlocksPM.INFUSED_STONE_MOON.get();
        } else {
            return Blocks.AIR;
        }
    }
    
    @Override
    public boolean postProcess(WorldGenLevel worldIn, StructureFeatureManager structureManager, ChunkGenerator generator, Random randomIn, BoundingBox structureBoundingBoxIn, ChunkPos chunkPos, BlockPos blockPos) {
        int i = worldIn.getHeight(Heightmap.Types.WORLD_SURFACE_WG, this.templatePosition.getX(), this.templatePosition.getZ());
        this.templatePosition = new BlockPos(this.templatePosition.getX(), i, this.templatePosition.getZ());
        
        // Generate the shrine; must be done first so that the bounding box is updated with the calculated template position
        boolean success = super.postProcess(worldIn, structureManager, generator, randomIn, structureBoundingBoxIn, chunkPos, blockPos);
        
        // Generate infused stone under the shrine
        BlockState bs = this.getInfusedStone().defaultBlockState();
        BlockPos.MutableBlockPos mbp = new BlockPos.MutableBlockPos();
        for (int x = 2; x < 11; x++) {
            for (int y = -3; y < 0; y++) {
                for (int z = 2; z < 11; z++) {
                    // Only replace blocks that aren't air
                    mbp.set(this.getWorldX(x, z), this.getWorldY(y), this.getWorldZ(x, z));
                    if (!worldIn.isEmptyBlock(mbp)) {
                        // Only a 30% chance to spawn infused stone at each valid position
                        if (randomIn.nextInt(10) < 3) {
                            this.placeBlock(worldIn, bs, x, y, z, structureBoundingBoxIn);
                        }
                    }
                }
            }
        }
        
        return success;
    }
}
