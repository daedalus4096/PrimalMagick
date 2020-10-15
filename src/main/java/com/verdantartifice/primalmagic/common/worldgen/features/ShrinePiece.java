package com.verdantartifice.primalmagic.common.worldgen.features;

import java.util.Random;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraftforge.common.util.Constants;

/**
 * Definition of a piece of a primal shrine structure.
 * 
 * @author Daedalus4096
 * @see {@link net.minecraft.world.gen.feature.structure.DesertPyramidPiece}
 */
public class ShrinePiece extends TemplateStructurePiece {
    protected static final ResourceLocation TEMPLATE = new ResourceLocation(PrimalMagic.MODID, "shrine");
    
    protected final Source source;
    
    public ShrinePiece(TemplateManager templateManager, Source source, BlockPos pos) {
        super(StructurePieceTypesPM.SHRINE, 0);
        this.source = source;
        this.templatePosition = pos;
        this.setupTemplate(templateManager);
    }

    public ShrinePiece(TemplateManager templateManager, CompoundNBT nbt) {
        super(StructurePieceTypesPM.SHRINE, nbt);
        this.source = Source.getSource(nbt.getString("Source"));
        this.setupTemplate(templateManager);
    }
    
    protected void setupTemplate(TemplateManager templateManager) {
        Template template = templateManager.getTemplateDefaulted(TEMPLATE);
        PlacementSettings settings = new PlacementSettings().addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
        this.setup(template, this.templatePosition, settings);
    }
    
    @Override
    protected void readAdditional(CompoundNBT tagCompound) {
        super.readAdditional(tagCompound);
        tagCompound.putString("Source", this.source.getTag());
    }

    @Override
    protected void handleDataMarker(String function, BlockPos pos, IWorld worldIn, Random rand, MutableBoundingBox sbb) {
        if ("font".equals(function)) {
            worldIn.setBlockState(pos, this.getFont().getDefaultState(), Constants.BlockFlags.DEFAULT);
        }
    }

    @Nonnull
    protected Block getFont() {
        if (this.source == null) {
            return Blocks.AIR;
        } else if (this.source.equals(Source.EARTH)) {
            return BlocksPM.ANCIENT_FONT_EARTH.get();
        } else if (this.source.equals(Source.SEA)) {
            return BlocksPM.ANCIENT_FONT_SEA.get();
        } else if (this.source.equals(Source.SKY)) {
            return BlocksPM.ANCIENT_FONT_SKY.get();
        } else if (this.source.equals(Source.SUN)) {
            return BlocksPM.ANCIENT_FONT_SUN.get();
        } else if (this.source.equals(Source.MOON)) {
            return BlocksPM.ANCIENT_FONT_MOON.get();
        } else {
            return Blocks.AIR;
        }
    }
    
    @Nonnull
    protected Block getInfusedStone() {
        if (this.source == null) {
            return Blocks.AIR;
        } else if (this.source.equals(Source.EARTH)) {
            return BlocksPM.INFUSED_STONE_EARTH.get();
        } else if (this.source.equals(Source.SEA)) {
            return BlocksPM.INFUSED_STONE_SEA.get();
        } else if (this.source.equals(Source.SKY)) {
            return BlocksPM.INFUSED_STONE_SKY.get();
        } else if (this.source.equals(Source.SUN)) {
            return BlocksPM.INFUSED_STONE_SUN.get();
        } else if (this.source.equals(Source.MOON)) {
            return BlocksPM.INFUSED_STONE_MOON.get();
        } else {
            return Blocks.AIR;
        }
    }
    
    @Override
    public boolean func_225577_a_(IWorld worldIn, ChunkGenerator<?> generator, Random randomIn, MutableBoundingBox structureBoundingBoxIn, ChunkPos p_74875_4_) {
        int i = worldIn.getHeight(Heightmap.Type.WORLD_SURFACE_WG, this.templatePosition.getX(), this.templatePosition.getZ());
        this.templatePosition = new BlockPos(this.templatePosition.getX(), i, this.templatePosition.getZ());
        
        // Generate the shrine; must be done first so that the bounding box is updated with the calculated template position
        boolean success = super.func_225577_a_(worldIn, generator, randomIn, structureBoundingBoxIn, p_74875_4_);
        
        // Generate infused stone under the shrine
        BlockState bs = this.getInfusedStone().getDefaultState();
        BlockPos.Mutable mbp = BlockPos.Mutable.retain();
        for (int x = 2; x < 11; x++) {
            for (int y = -3; y < 0; y++) {
                for (int z = 2; z < 11; z++) {
                    // Only replace blocks that aren't air
                    mbp.setPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));
                    if (!worldIn.isAirBlock(mbp)) {
                        // Only a 30% chance to spawn infused stone at each valid position
                        if (randomIn.nextInt(10) < 3) {
                            this.setBlockState(worldIn, bs, x, y, z, structureBoundingBoxIn);
                        }
                    }
                }
            }
        }
        
        return success;
    }
}
