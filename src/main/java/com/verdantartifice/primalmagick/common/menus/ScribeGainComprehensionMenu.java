package com.verdantartifice.primalmagick.common.menus;

import org.joml.Vector2i;

import com.verdantartifice.primalmagick.common.tiles.devices.ScribeTableTileEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Server data container for the study vocabulary mode of the scribe table GUI.
 * 
 * @author Daedalus4096
 */
public class ScribeGainComprehensionMenu extends AbstractScribeTableMenu {
    public ScribeGainComprehensionMenu(int windowId, Inventory inv, BlockPos pos) {
        this(windowId, inv, pos, null);
    }
    
    public ScribeGainComprehensionMenu(int windowId, Inventory inv, BlockPos pos, ScribeTableTileEntity entity) {
        super(MenuTypesPM.SCRIBE_GAIN_COMPREHENSION.get(), windowId, inv, pos, entity);
    }
    
    @Override
    protected void createModeSlots() {
        // TODO Auto-generated method stub

    }

    @Override
    protected Vector2i getInventorySlotsOffset() {
        return new Vector2i(27, 56);
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        // TODO Auto-generated method stub
        return null;
    }

}
