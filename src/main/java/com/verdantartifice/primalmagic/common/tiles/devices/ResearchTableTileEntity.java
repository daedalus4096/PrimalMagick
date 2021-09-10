package com.verdantartifice.primalmagic.common.tiles.devices;

import com.verdantartifice.primalmagic.common.containers.ResearchTableContainer;
import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagic.common.tiles.base.TileInventoryPM;

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
 * Definition of a research table tile entity.  Holds the writing materials for the corresponding block.
 * 
 * @see {@link com.verdantartifice.primalmagic.common.blocks.devices.ResearchTableBlock}
 * @author Daedalus4096
 */
public class ResearchTableTileEntity extends TileInventoryPM implements MenuProvider {
    public ResearchTableTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.RESEARCH_TABLE.get(), pos, state, 2);
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
        ResearchTableContainer menu = new ResearchTableContainer(windowId, playerInv, this, ContainerLevelAccess.create(this.level, this.worldPosition));
        this.addListener(menu);
        return menu;
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent(this.getBlockState().getBlock().getDescriptionId());
    }
}
