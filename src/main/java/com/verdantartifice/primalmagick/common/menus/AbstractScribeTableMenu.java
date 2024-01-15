package com.verdantartifice.primalmagick.common.menus;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.common.menus.base.AbstractTileSidedInventoryMenu;
import com.verdantartifice.primalmagick.common.tiles.devices.ScribeTableTileEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.Level;

/**
 * Base server data container for the scribe table GUIs.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractScribeTableMenu extends AbstractTileSidedInventoryMenu<ScribeTableTileEntity> implements ContainerListener {
    protected final Player player;
    protected final Level level;

    public AbstractScribeTableMenu(@Nonnull MenuType<?> type, int id, @Nonnull Inventory playerInv, BlockPos tilePos, ScribeTableTileEntity entity) {
        super(type, id, ScribeTableTileEntity.class, playerInv.player.level(), tilePos, entity);
        this.player = playerInv.player;
        this.level = this.player.level();
        
        // Mode-specific menu slots
        this.createModeSlots();
        
        // Slots (M)-(M+26), where M = mode slot count: Player backpack
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        
        // Slots (M+27)-(M+35), where M = mode slot count: Player hotbar
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInv, i, 8 + i * 18, 142));
        }
    }
    
    protected abstract void createModeSlots();
    
    @Override
    public void containerChanged(Container pContainer) {
        // TODO Auto-generated method stub

    }
}
