package com.verdantartifice.primalmagick.datagen.blocks;

import java.util.stream.Stream;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.blocks.misc.PillarBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.AbstractPhasingLeavesBlock;
import com.verdantartifice.primalmagick.common.blocks.trees.AbstractPhasingLogBlock;
import com.verdantartifice.primalmagick.common.blockstates.properties.TimePhase;

import net.minecraft.core.Direction.Axis;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Data provider for mod block states and associated blocks and items.
 * 
 * @author Daedalus4096
 */
public class BlockStateProviderPM extends BlockStateProvider {
    protected static final ResourceLocation SOLID = new ResourceLocation("solid");
    protected static final ResourceLocation CUTOUT = new ResourceLocation("cutout");
    protected static final ResourceLocation CUTOUT_MIPPED = new ResourceLocation("cutout_mipped");
    protected static final ResourceLocation TRANSLUCENT = new ResourceLocation("translucent");

    public BlockStateProviderPM(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, PrimalMagick.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // Generate marble blocks
        this.simpleBlockWithItem(BlocksPM.MARBLE_RAW.get());
        this.slabBlockWithItem(BlocksPM.MARBLE_SLAB.get(), BlocksPM.MARBLE_RAW.get());
        this.stairsBlockWithItem(BlocksPM.MARBLE_STAIRS.get(), this.blockTexture(BlocksPM.MARBLE_RAW.get()));
        this.wallBlockWithItem(BlocksPM.MARBLE_WALL.get(), this.blockTexture(BlocksPM.MARBLE_RAW.get()));
        this.simpleBlockWithItem(BlocksPM.MARBLE_BRICKS.get());
        this.slabBlockWithItem(BlocksPM.MARBLE_BRICK_SLAB.get(), BlocksPM.MARBLE_BRICKS.get());
        this.stairsBlockWithItem(BlocksPM.MARBLE_BRICK_STAIRS.get(), this.blockTexture(BlocksPM.MARBLE_BRICKS.get()));
        this.wallBlockWithItem(BlocksPM.MARBLE_BRICK_WALL.get(), this.blockTexture(BlocksPM.MARBLE_BRICKS.get()));
        this.pillarBlockWithItem(BlocksPM.MARBLE_PILLAR.get());
        this.simpleBlockWithItem(BlocksPM.MARBLE_CHISELED.get());
        this.cubeColumnBlockWithItem(BlocksPM.MARBLE_RUNED.get(), this.blockTexture(BlocksPM.MARBLE_RAW.get()));
        this.simpleBlockWithItem(BlocksPM.MARBLE_TILES.get());
        
        // Generate enchanted marble blocks
        this.simpleBlockWithItem(BlocksPM.MARBLE_ENCHANTED.get());
        this.slabBlockWithItem(BlocksPM.MARBLE_ENCHANTED_SLAB.get(), BlocksPM.MARBLE_ENCHANTED.get());
        this.stairsBlockWithItem(BlocksPM.MARBLE_ENCHANTED_STAIRS.get(), this.blockTexture(BlocksPM.MARBLE_ENCHANTED.get()));
        this.wallBlockWithItem(BlocksPM.MARBLE_ENCHANTED_WALL.get(), this.blockTexture(BlocksPM.MARBLE_ENCHANTED.get()));
        this.simpleBlockWithItem(BlocksPM.MARBLE_ENCHANTED_BRICKS.get());
        this.slabBlockWithItem(BlocksPM.MARBLE_ENCHANTED_BRICK_SLAB.get(), BlocksPM.MARBLE_ENCHANTED_BRICKS.get());
        this.stairsBlockWithItem(BlocksPM.MARBLE_ENCHANTED_BRICK_STAIRS.get(), this.blockTexture(BlocksPM.MARBLE_ENCHANTED_BRICKS.get()));
        this.wallBlockWithItem(BlocksPM.MARBLE_ENCHANTED_BRICK_WALL.get(), this.blockTexture(BlocksPM.MARBLE_ENCHANTED_BRICKS.get()));
        this.pillarBlockWithItem(BlocksPM.MARBLE_ENCHANTED_PILLAR.get());
        this.simpleBlockWithItem(BlocksPM.MARBLE_ENCHANTED_CHISELED.get());
        this.cubeColumnBlockWithItem(BlocksPM.MARBLE_ENCHANTED_RUNED.get(), this.blockTexture(BlocksPM.MARBLE_ENCHANTED.get()));
        
        // Generate smoked marble blocks
        this.simpleBlockWithItem(BlocksPM.MARBLE_SMOKED.get());
        this.slabBlockWithItem(BlocksPM.MARBLE_SMOKED_SLAB.get(), BlocksPM.MARBLE_SMOKED.get());
        this.stairsBlockWithItem(BlocksPM.MARBLE_SMOKED_STAIRS.get(), this.blockTexture(BlocksPM.MARBLE_SMOKED.get()));
        this.wallBlockWithItem(BlocksPM.MARBLE_SMOKED_WALL.get(), this.blockTexture(BlocksPM.MARBLE_SMOKED.get()));
        this.simpleBlockWithItem(BlocksPM.MARBLE_SMOKED_BRICKS.get());
        this.slabBlockWithItem(BlocksPM.MARBLE_SMOKED_BRICK_SLAB.get(), BlocksPM.MARBLE_SMOKED_BRICKS.get());
        this.stairsBlockWithItem(BlocksPM.MARBLE_SMOKED_BRICK_STAIRS.get(), this.blockTexture(BlocksPM.MARBLE_SMOKED_BRICKS.get()));
        this.wallBlockWithItem(BlocksPM.MARBLE_SMOKED_BRICK_WALL.get(), this.blockTexture(BlocksPM.MARBLE_SMOKED_BRICKS.get()));
        this.pillarBlockWithItem(BlocksPM.MARBLE_SMOKED_PILLAR.get());
        this.simpleBlockWithItem(BlocksPM.MARBLE_SMOKED_CHISELED.get());
        this.cubeColumnBlockWithItem(BlocksPM.MARBLE_SMOKED_RUNED.get(), this.blockTexture(BlocksPM.MARBLE_SMOKED.get()));
        
        // Generate hallowed marble blocks
        this.simpleBlockWithItem(BlocksPM.MARBLE_HALLOWED.get());
        this.slabBlockWithItem(BlocksPM.MARBLE_HALLOWED_SLAB.get(), BlocksPM.MARBLE_HALLOWED.get());
        this.stairsBlockWithItem(BlocksPM.MARBLE_HALLOWED_STAIRS.get(), this.blockTexture(BlocksPM.MARBLE_HALLOWED.get()));
        this.wallBlockWithItem(BlocksPM.MARBLE_HALLOWED_WALL.get(), this.blockTexture(BlocksPM.MARBLE_HALLOWED.get()));
        this.simpleBlockWithItem(BlocksPM.MARBLE_HALLOWED_BRICKS.get());
        this.slabBlockWithItem(BlocksPM.MARBLE_HALLOWED_BRICK_SLAB.get(), BlocksPM.MARBLE_HALLOWED_BRICKS.get());
        this.stairsBlockWithItem(BlocksPM.MARBLE_HALLOWED_BRICK_STAIRS.get(), this.blockTexture(BlocksPM.MARBLE_HALLOWED_BRICKS.get()));
        this.wallBlockWithItem(BlocksPM.MARBLE_HALLOWED_BRICK_WALL.get(), this.blockTexture(BlocksPM.MARBLE_HALLOWED_BRICKS.get()));
        this.pillarBlockWithItem(BlocksPM.MARBLE_HALLOWED_PILLAR.get());
        this.simpleBlockWithItem(BlocksPM.MARBLE_HALLOWED_CHISELED.get());
        this.cubeColumnBlockWithItem(BlocksPM.MARBLE_HALLOWED_RUNED.get(), this.blockTexture(BlocksPM.MARBLE_HALLOWED.get()));
        
        // Generate sunwood blocks
        this.phasingLogBlockWithItem(BlocksPM.SUNWOOD_LOG.get(), TRANSLUCENT);
        this.phasingLogBlockWithItem(BlocksPM.STRIPPED_SUNWOOD_LOG.get(), TRANSLUCENT);
        this.phasingWoodBlockWithItem(BlocksPM.SUNWOOD_WOOD.get(), this.blockTexture(BlocksPM.SUNWOOD_LOG.get()), TRANSLUCENT);
        this.phasingWoodBlockWithItem(BlocksPM.STRIPPED_SUNWOOD_WOOD.get(), this.blockTexture(BlocksPM.STRIPPED_SUNWOOD_LOG.get()), TRANSLUCENT);
        this.phasingLeavesBlockWithItem(BlocksPM.SUNWOOD_LEAVES.get(), TRANSLUCENT);
        this.saplingBlockWithItem(BlocksPM.SUNWOOD_SAPLING.get());
    }

    private ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

    private String name(Block block) {
        return key(block).getPath();
    }
    
    private void simpleBlockWithItem(Block block) {
        this.simpleBlockWithItem(block, this.cubeAll(block));
    }
    
    private void slabBlockWithItem(SlabBlock block, Block doubleSlabBlock) {
        this.slabBlockWithItem(block, doubleSlabBlock, this.blockTexture(doubleSlabBlock));
    }
    
    private void slabBlockWithItem(SlabBlock block, Block doubleSlabBlock, ResourceLocation texture) {
        String blockName = this.name(block);
        ModelFile bottomModel = this.models().slab(blockName, texture, texture, texture);
        ModelFile topModel = this.models().slabTop(blockName + "_top", texture, texture, texture);
        this.slabBlock(block, bottomModel, topModel, this.models().getExistingFile(this.key(doubleSlabBlock)));
        this.simpleBlockItem(block, bottomModel);
    }
    
    private void stairsBlockWithItem(StairBlock block, ResourceLocation texture) {
        String baseName = this.name(block);
        ModelFile stairs = this.models().stairs(baseName, texture, texture, texture);
        ModelFile stairsInner = this.models().stairsInner(baseName + "_inner", texture, texture, texture);
        ModelFile stairsOuter = this.models().stairsOuter(baseName + "_outer", texture, texture, texture);
        this.stairsBlock(block, stairs, stairsInner, stairsOuter);
        this.simpleBlockItem(block, stairs);
    }
    
    private void wallBlockWithItem(WallBlock block, ResourceLocation texture) {
        this.wallBlock(block, texture);
        ModelFile wallInv = this.models().wallInventory(this.name(block) + "_inventory", texture);
        this.simpleBlockItem(block, wallInv);
    }
    
    private void cubeColumnBlockWithItem(Block block, ResourceLocation endTexture) {
        this.cubeColumnBlockWithItem(block, this.blockTexture(block), endTexture);
    }
    
    private void cubeColumnBlockWithItem(Block block, ResourceLocation sideTexture, ResourceLocation endTexture) {
        this.simpleBlockWithItem(block, this.models().cubeColumn(this.name(block), sideTexture, endTexture));
    }
    
    private void pillarBlockWithItem(PillarBlock block) {
        this.pillarBlockWithItem(block, this.blockTexture(block));
    }
    
    private void pillarBlockWithItem(PillarBlock block, ResourceLocation texture) {
        this.pillarBlockWithItem(block, texture, texture.withSuffix("_inner"), texture.withSuffix("_top"), texture.withSuffix("_bottom"), texture.withSuffix("_base"));
    }
    
    private void pillarBlockWithItem(PillarBlock block, ResourceLocation sideTexture, ResourceLocation innerTexture, ResourceLocation topTexture, ResourceLocation bottomTexture, ResourceLocation baseTexture) {
        ModelFile baseModel = this.models().withExistingParent(this.name(block), PrimalMagick.resource("block/pillar"))
                .texture("side", sideTexture)
                .texture("inner", innerTexture);
        ModelFile topModel = this.models().withExistingParent(this.name(block) + "_top", PrimalMagick.resource("block/pillar_top"))
                .texture("side", topTexture)
                .texture("inner", innerTexture)
                .texture("top", baseTexture);
        ModelFile bottomModel = this.models().withExistingParent(this.name(block) + "_bottom", PrimalMagick.resource("block/pillar_bottom"))
                .texture("side", bottomTexture)
                .texture("inner", innerTexture)
                .texture("bottom", baseTexture);
        this.getVariantBuilder(block)
                .partialState().with(PillarBlock.PROPERTY_TYPE, PillarBlock.Type.BASE).addModels(new ConfiguredModel(baseModel))
                .partialState().with(PillarBlock.PROPERTY_TYPE, PillarBlock.Type.TOP).addModels(new ConfiguredModel(topModel))
                .partialState().with(PillarBlock.PROPERTY_TYPE, PillarBlock.Type.BOTTOM).addModels(new ConfiguredModel(bottomModel));
        this.simpleBlockItem(block, baseModel);
    }
    
    private void phasingLogBlockWithItem(AbstractPhasingLogBlock block, ResourceLocation renderType) {
        Stream.of(TimePhase.values()).forEach(phase -> {
            String phaseName = phase.getSerializedName();
            ResourceLocation sideTexture = this.blockTexture(block).withSuffix("_" + phaseName);
            ResourceLocation endTexture = this.blockTexture(block).withSuffix("_top_" + phaseName);
            ModelFile model = this.models().cubeColumn(this.name(block) + "_" + phaseName, sideTexture, endTexture).renderType(renderType);
            this.axisBlockPhase(block, model, model, phase);
        });

        String phaseName = TimePhase.FULL.getSerializedName();
        ResourceLocation sideTexture = this.blockTexture(block).withSuffix("_" + phaseName);
        ResourceLocation endTexture = this.blockTexture(block).withSuffix("_top_" + phaseName);
        this.simpleBlockItem(block, this.models().cubeColumn(this.name(block) + "_" + phaseName, sideTexture, endTexture));
    }
    
    private void phasingWoodBlockWithItem(AbstractPhasingLogBlock block, ResourceLocation texture, ResourceLocation renderType) {
        Stream.of(TimePhase.values()).forEach(phase -> {
            String phaseName = phase.getSerializedName();
            ResourceLocation phaseTexture = texture.withSuffix("_" + phaseName);
            ModelFile model = this.models().cubeColumn(this.name(block) + "_" + phaseName, phaseTexture, phaseTexture).renderType(renderType);
            this.axisBlockPhase(block, model, model, phase);
        });

        String phaseName = TimePhase.FULL.getSerializedName();
        ResourceLocation phaseTexture = texture.withSuffix("_" + phaseName);
        this.simpleBlockItem(block, this.models().cubeColumn(this.name(block) + "_" + phaseName, phaseTexture, phaseTexture));
    }
    
    private void axisBlockPhase(AbstractPhasingLogBlock block, ModelFile vertical, ModelFile horizontal, TimePhase phase) {
        this.getVariantBuilder(block)
            .partialState().with(AbstractPhasingLogBlock.PHASE, phase).with(AbstractPhasingLogBlock.AXIS, Axis.Y).modelForState().modelFile(vertical).addModel()
            .partialState().with(AbstractPhasingLogBlock.PHASE, phase).with(AbstractPhasingLogBlock.AXIS, Axis.Z).modelForState().modelFile(horizontal).rotationX(90).addModel()
            .partialState().with(AbstractPhasingLogBlock.PHASE, phase).with(AbstractPhasingLogBlock.AXIS, Axis.X).modelForState().modelFile(horizontal).rotationX(90).rotationY(90).addModel();
    }
    
    private void phasingLeavesBlockWithItem(AbstractPhasingLeavesBlock block, ResourceLocation renderType) {
        Stream.of(TimePhase.values()).forEach(phase -> {
            String phaseName = phase.getSerializedName();
            ResourceLocation phaseTexture = this.blockTexture(block).withSuffix("_" + phaseName);
            ModelFile model = this.models().withExistingParent(this.name(block) + "_" + phaseName, new ResourceLocation("block/leaves")).texture("all", phaseTexture).renderType(renderType);
            this.getVariantBuilder(block).partialState().with(AbstractPhasingLeavesBlock.PHASE, phase).modelForState().modelFile(model).addModel();
        });

        String phaseName = TimePhase.FULL.getSerializedName();
        ResourceLocation phaseTexture = this.blockTexture(block).withSuffix("_" + phaseName);
        this.simpleBlockItem(block, this.models().withExistingParent(this.name(block) + "_" + phaseName, new ResourceLocation("block/leaves")).texture("all", phaseTexture));
    }
    
    private void saplingBlockWithItem(Block block) {
        this.simpleBlock(block, this.models().cross(this.name(block), this.blockTexture(block)).renderType(CUTOUT));
        this.itemModels().getBuilder(this.key(block).toString()).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", this.blockTexture(block));
    }
}
