package com.verdantartifice.primalmagic.common.blocks.crafting;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.misc.HarvestLevel;
import com.verdantartifice.primalmagic.common.util.VoxelShapeUtils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

/**
 * Block definition for the runescribing altar.  May be used to apply combinations of runes to
 * items in order to imbue them with controllable enchantments.
 * 
 * @author Daedalus4096
 */
public class RunescribingAltarBlock extends Block {
    protected static final VoxelShape SHAPE = VoxelShapeUtils.fromModel(new ResourceLocation(PrimalMagic.MODID, "block/runescribing_altar_basic"));
    
    protected final int runesAllowed;

    public RunescribingAltarBlock(int runesAllowed) {
        super(Block.Properties.create(Material.ROCK, MaterialColor.QUARTZ).hardnessAndResistance(1.5F, 6.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(HarvestLevel.WOOD.getLevel()));
        this.runesAllowed = runesAllowed;
    }
    
    public int getRunesAllowed() {
        return this.runesAllowed;
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }
}
