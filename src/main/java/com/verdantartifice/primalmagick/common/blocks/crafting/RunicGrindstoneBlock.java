package com.verdantartifice.primalmagick.common.blocks.crafting;

import com.verdantartifice.primalmagick.common.menus.RunicGrindstoneMenu;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrindstoneBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Block definition for a runic grindstone.  Works just like a regular grindstone, except it also
 * removes inscribed runes.
 * 
 * @author Daedalus4096
 */
public class RunicGrindstoneBlock extends GrindstoneBlock {
    public RunicGrindstoneBlock() {
        super(Block.Properties.of().mapColor(MapColor.METAL).pushReaction(PushReaction.BLOCK).strength(2.0F, 6.0F).sound(SoundType.STONE));
    }
    
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // Place the block so that the side facing the player has its rune right side up
        for (Direction dir : context.getNearestLookingDirections()) {
            BlockState state;
            if (dir.getAxis() == Direction.Axis.Y) {
                if (dir == Direction.UP) {
                    state = this.defaultBlockState().setValue(FACE, AttachFace.CEILING).setValue(FACING, context.getHorizontalDirection());
                } else {
                    state = this.defaultBlockState().setValue(FACE, AttachFace.FLOOR).setValue(FACING, context.getHorizontalDirection().getOpposite());
                }
            } else {
                state = this.defaultBlockState().setValue(FACE, AttachFace.WALL).setValue(FACING, dir.getOpposite());
            }
            
            if (state.canSurvive(context.getLevel(), context.getClickedPos())) {
                return state;
            }
        }
        return null;
    }
    
    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!worldIn.isClientSide && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(state.getMenuProvider(worldIn, pos));
        }
        return InteractionResult.SUCCESS;
    }
    
    @Override
    public MenuProvider getMenuProvider(BlockState state, Level worldIn, BlockPos pos) {
        return new SimpleMenuProvider((windowId, playerInv, player) -> {
            return new RunicGrindstoneMenu(windowId, playerInv, ContainerLevelAccess.create(worldIn, pos));
         }, Component.translatable(this.getDescriptionId()));
    }
}
