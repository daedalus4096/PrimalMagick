package com.verdantartifice.primalmagick.common.blocks.crafting;

import com.verdantartifice.primalmagick.common.containers.WandAssemblyTableContainer;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

/**
 * Block definition for the wand assembly table.  A wand assembly table allows a player to construct a
 * modular wand from component cores, caps, and gems.
 * 
 * @author Daedalus4096
 */
public class WandAssemblyTableBlock extends Block {
    public WandAssemblyTableBlock() {
        super(Block.Properties.of(Material.STONE, MaterialColor.QUARTZ).strength(1.5F, 6.0F).sound(SoundType.STONE));
    }
    
    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!worldIn.isClientSide && player instanceof ServerPlayer) {
            // Open the GUI for the wand assembly table
            NetworkHooks.openGui((ServerPlayer)player, new MenuProvider() {
                @Override
                public AbstractContainerMenu createMenu(int windowId, Inventory inv, Player player) {
                    return new WandAssemblyTableContainer(windowId, inv, ContainerLevelAccess.create(worldIn, pos));
                }

                @Override
                public Component getDisplayName() {
                    return new TranslatableComponent(WandAssemblyTableBlock.this.getDescriptionId());
                }
            });
        }
        return InteractionResult.SUCCESS;
    }
}
