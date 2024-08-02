package com.verdantartifice.primalmagick.common.menus;

import javax.annotation.Nonnull;

import org.joml.Vector2i;

import com.verdantartifice.primalmagick.common.items.books.StaticBookItem;
import com.verdantartifice.primalmagick.common.menus.base.AbstractTileSidedInventoryMenu;
import com.verdantartifice.primalmagick.common.tags.BookLanguageTagsPM;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
import com.verdantartifice.primalmagick.common.tiles.devices.ScribeTableTileEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
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
        Vector2i slotOffset = this.getInventorySlotsOffset();
        
        // Mode-specific menu slots
        this.createModeSlots();
        
        // Slots (M)-(M+26), where M = mode slot count: Player backpack
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18 + slotOffset.x, 84 + i * 18 + slotOffset.y));
            }
        }
        
        // Slots (M+27)-(M+35), where M = mode slot count: Player hotbar
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInv, i, 8 + i * 18 + slotOffset.x, 142 + slotOffset.y));
        }
    }
    
    protected abstract void createModeSlots();

    protected boolean isAncientBookStack(ItemStack stack) {
        return stack.is(ItemTagsPM.STATIC_BOOKS) && StaticBookItem.getBookLanguage(stack).map(h -> h.is(BookLanguageTagsPM.ANCIENT)).orElse(false);
    }
    
    protected Vector2i getInventorySlotsOffset() {
        return new Vector2i(0, 0);
    }
    
    @Override
    public void containerChanged(Container pContainer) {
        // Do nothing by default
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.tile.removeListener(this);
    }
}
