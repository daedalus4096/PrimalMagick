package com.verdantartifice.primalmagick.client.gui;

import org.lwjgl.glfw.GLFW;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;

/**
 * GUI screen for reading static books.
 * 
 * @author Daedalus4096
 */
public class StaticBookViewScreen extends Screen {
    public static final ResourceLocation BG_TEXTURE = new ResourceLocation("textures/gui/book.png");
    public static final int PAGE_INDICATOR_TEXT_Y_OFFSET = 16;
    public static final int PAGE_TEXT_X_OFFSET = 36;
    public static final int PAGE_TEXT_Y_OFFSET = 30;
    protected static final int TEXT_WIDTH = 114;
    protected static final int TEXT_HEIGHT = 128;
    protected static final int IMAGE_WIDTH = 192;
    protected static final int IMAGE_HEIGHT = 192;

    protected final boolean playTurnSound;
    protected ResourceLocation bookId;
    private PageButton forwardButton;
    private PageButton backButton;
    private int currentPage;

    public StaticBookViewScreen() {
        this(PrimalMagick.resource("unknown"), false);
    }
    
    public StaticBookViewScreen(ResourceLocation bookId) {
        this(bookId, true);
    }
    
    private StaticBookViewScreen(ResourceLocation bookId, boolean playTurnSound) {
        super(GameNarrator.NO_TITLE);
        this.bookId = bookId;
        this.playTurnSound = playTurnSound;
    }
    
    public void setBookId(ResourceLocation bookId) {
        this.bookId = bookId;
        // TODO Update current page number
        // TODO Update button visibility
    }
    
    /**
     * Moves the book to the specified page and returns true if it exists, {@code false} otherwise.
     */
    public boolean setPage(int newPage) {
        // TODO Stub
        return false;
    }

    @Override
    protected void init() {
        this.createMenuControls();
        this.createPageControlButtons();
    }

    protected void createMenuControls() {
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> this.onClose()).bounds(this.width / 2 - 100, IMAGE_HEIGHT + 4, IMAGE_WIDTH + 8, 20).build());
    }

    protected void createPageControlButtons() {
        int x = (this.width - IMAGE_WIDTH) / 2;
        this.forwardButton = this.addRenderableWidget(new PageButton(x + 116, 159, true, (p_98297_) -> {
            this.pageForward();
        }, this.playTurnSound));
        this.backButton = this.addRenderableWidget(new PageButton(x + 43, 159, false, (p_98287_) -> {
            this.pageBack();
        }, this.playTurnSound));
        this.updateButtonVisibility();
    }

    private int getNumPages() {
        // TODO Stub
        return 0;
    }
    
    protected void pageBack() {
        if (this.currentPage > 0) {
            this.currentPage--;
        }
        this.updateButtonVisibility();
    }
    
    protected void pageForward() {
        if (this.currentPage < this.getNumPages() - 1) {
            this.currentPage++;
        }
        this.updateButtonVisibility();
    }

    private void updateButtonVisibility() {
        this.forwardButton.visible = (this.currentPage < this.getNumPages() - 1);
        this.backButton.visible = (this.currentPage > 0);
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (super.keyPressed(pKeyCode, pScanCode, pModifiers)) {
            return true;
        } else {
            switch (pKeyCode) {
            case GLFW.GLFW_KEY_PAGE_UP:
                this.backButton.onPress();
                return true;
            case GLFW.GLFW_KEY_PAGE_DOWN:
                this.forwardButton.onPress();
                return true;
            default:
                return false;
            }
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        int xPos = (this.width - IMAGE_WIDTH) / 2;
        int yPos = 2;
        guiGraphics.blit(BG_TEXTURE, xPos, yPos, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        
        // TODO Parse and draw book text

        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }
}
