package com.verdantartifice.primalmagick.common.tiles.crafting;

import com.verdantartifice.primalmagick.common.containers.SpellcraftingAltarContainer;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.TilePM;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Definition of a spellcrafting altar tile entity.  Holds the tile inventory and controls its
 * custom renderer.
 * 
 * @author Daedalus4096
 */
public class SpellcraftingAltarTileEntity extends TilePM implements MenuProvider {
    public SpellcraftingAltarTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.SPELLCRAFTING_ALTAR.get(), pos, state);
    }
    
    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
        return new SpellcraftingAltarContainer(windowId, playerInv, ContainerLevelAccess.create(this.level, this.getBlockPos()));
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent(this.getBlockState().getBlock().getDescriptionId());
    }

}
