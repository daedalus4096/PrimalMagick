package com.verdantartifice.primalmagic.common.blocks.mana;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.util.VoxelShapeUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Block definition for the auto-charger.  An auto-charger will automatically siphon mana from
 * nearby mana fonts and use it to charge the contained wand.
 * 
 * @author Daedalus4096
 */
public class AutoChargerBlock extends BaseEntityBlock {
    protected static final VoxelShape SHAPE = VoxelShapeUtils.fromModel(new ResourceLocation(PrimalMagic.MODID, "block/auto_charger"));
    
    public AutoChargerBlock() {
        super(Block.Properties.of(Material.STONE, MaterialColor.QUARTZ).strength(3.0F, 12.0F).sound(SoundType.STONE));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        // TODO Auto-generated method stub
        return null;
    }

}
