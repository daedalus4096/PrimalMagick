package com.verdantartifice.primalmagic.common.blocks.crafting;

import com.verdantartifice.primalmagic.common.containers.RunicGrindstoneContainer;
import com.verdantartifice.primalmagic.common.misc.HarvestLevel;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.GrindstoneBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

/**
 * Block definition for a runic grindstone.  Works just like a regular grindstone, except it also
 * removes inscribed runes.
 * 
 * @author Daedalus4096
 */
public class RunicGrindstoneBlock extends GrindstoneBlock {
    public RunicGrindstoneBlock() {
        super(Block.Properties.create(Material.ANVIL, MaterialColor.IRON).hardnessAndResistance(2.0F, 6.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(HarvestLevel.WOOD.getLevel()));
    }
    
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        // Place the block so that the side facing the player has its rune right side up
        for (Direction dir : context.getNearestLookingDirections()) {
            BlockState state;
            if (dir.getAxis() == Direction.Axis.Y) {
                if (dir == Direction.UP) {
                    state = this.getDefaultState().with(FACE, AttachFace.CEILING).with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing());
                } else {
                    state = this.getDefaultState().with(FACE, AttachFace.FLOOR).with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite());
                }
            } else {
                state = this.getDefaultState().with(FACE, AttachFace.WALL).with(HORIZONTAL_FACING, dir.getOpposite());
            }
            
            if (state.isValidPosition(context.getWorld(), context.getPos())) {
                return state;
            }
        }
        return null;
    }
    
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote) {
            player.openContainer(state.getContainer(worldIn, pos));
        }
        return ActionResultType.SUCCESS;
    }
    
    @Override
    public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos) {
        return new SimpleNamedContainerProvider((windowId, playerInv, player) -> {
            return new RunicGrindstoneContainer(windowId, playerInv, IWorldPosCallable.of(worldIn, pos));
         }, new TranslationTextComponent(this.getTranslationKey()));
    }
}
