package com.verdantartifice.primalmagick.common.blocks.crafting;

import com.verdantartifice.primalmagick.common.menus.ArcaneWorkbenchMenu;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Block definition for the arcane workbench.  An arcane workbench is like a normal workbench, but can
 * be used to craft arcane recipes requiring mana.
 * 
 * @author Daedalus4096
 */
public class ArcaneWorkbenchBlock extends Block {
    public ArcaneWorkbenchBlock() {
        super(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(1.5F, 6.0F).sound(SoundType.WOOD).noOcclusion());
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        // TODO Assemble more detailed shape for base table
        return Shapes.block();
    }
    
    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!worldIn.isClientSide && player instanceof ServerPlayer serverPlayer) {
            // Open the GUI for the arcane workbench
            serverPlayer.openMenu(new MenuProvider() {
                @Override
                public AbstractContainerMenu createMenu(int windowId, Inventory inv, Player player) {
                    return new ArcaneWorkbenchMenu(windowId, inv, ContainerLevelAccess.create(worldIn, pos));
                }

                @Override
                public Component getDisplayName() {
                    return Component.translatable(ArcaneWorkbenchBlock.this.getDescriptionId());
                }
            });
        }
        return InteractionResult.SUCCESS;
    }
}
