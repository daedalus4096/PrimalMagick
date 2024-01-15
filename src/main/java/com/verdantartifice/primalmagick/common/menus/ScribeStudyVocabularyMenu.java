package com.verdantartifice.primalmagick.common.menus;

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
public class ScribeStudyVocabularyMenu extends AbstractScribeTableMenu {
    public ScribeStudyVocabularyMenu(int windowId, Inventory inv, BlockPos pos) {
        this(windowId, inv, pos, null);
    }
    
    public ScribeStudyVocabularyMenu(int windowId, Inventory inv, BlockPos pos, ScribeTableTileEntity entity) {
        super(MenuTypesPM.SCRIBE_STUDY_VOCABULARY.get(), windowId, inv, pos, entity);
    }
    
    @Override
    protected void createModeSlots() {
        // TODO Auto-generated method stub

    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        // TODO Auto-generated method stub
        return null;
    }

}
