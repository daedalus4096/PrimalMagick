package com.verdantartifice.primalmagick.client.gui;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.books.StaticBookItem;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.LecternMenu;
import net.minecraft.world.item.ItemStack;

/**
 * GUI screen for reading static books on a lectern.
 * 
 * @author Daedalus4096
 */
public class StaticBookLecternScreen extends StaticBookViewScreen implements MenuAccess<LecternMenu> {
    protected final LecternMenu menu;
    protected final ContainerListener listener = new ContainerListener() {
        @Override
        public void slotChanged(AbstractContainerMenu pContainerToSend, int pDataSlotIndex, ItemStack pStack) {
            StaticBookLecternScreen.this.bookChanged();
        }

        @Override
        public void dataChanged(AbstractContainerMenu pContainerMenu, int pDataSlotIndex, int pValue) {
            if (pDataSlotIndex == 0) {
                StaticBookLecternScreen.this.pageChanged();
            }
        }
    };
    
    public StaticBookLecternScreen(LecternMenu menu, Inventory pPlayerInventory, Component pTitle) {
        super(StaticBookItem.getBookId(menu.getBook()).orElse(PrimalMagick.resource("unknown")));
        this.menu = menu;
    }

    @Override
    public LecternMenu getMenu() {
        return this.menu;
    }

    @Override
    protected void init() {
        super.init();
        this.getMenu().addSlotListener(this.listener);
    }

    @Override
    public void onClose() {
        LOGGER.info("Closing static book lectern screen");
        this.minecraft.player.closeContainer();
        super.onClose();
    }

    @Override
    public void removed() {
        super.removed();
        this.getMenu().removeSlotListener(this.listener);
    }

    @Override
    protected void createMenuControls() {
        if (this.minecraft.player.mayBuild()) {
            this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> {
                LOGGER.info("Done button clicked, closing");
                this.onClose();
            }).bounds(this.width / 2 - 100, 196, 98, 20).build());
            this.addRenderableWidget(Button.builder(Component.translatable("lectern.take_book"), button -> this.sendButtonClick(3)).bounds(this.width / 2 + 2, 196, 98, 20).build());
        } else {
            super.createMenuControls();
        }
    }

    @Override
    protected void pageBack() {
        this.sendButtonClick(1);
    }

    @Override
    protected void pageForward() {
        this.sendButtonClick(2);
    }

    private void sendButtonClick(int pPageData) {
        this.minecraft.gameMode.handleInventoryButtonClick(this.getMenu().containerId, pPageData);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    protected void bookChanged() {
        ItemStack stack = this.getMenu().getBook();
        if (!stack.is(ItemsPM.STATIC_BOOK.get())) {
            LOGGER.warn("Lectern item is not a static book, closing");
            this.onClose();
        }
    }

    protected void pageChanged() {
        this.setPage(this.menu.getPage());
    }
}
