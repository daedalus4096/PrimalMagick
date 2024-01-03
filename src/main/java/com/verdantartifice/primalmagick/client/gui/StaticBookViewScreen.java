package com.verdantartifice.primalmagick.client.gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;

import com.verdantartifice.primalmagick.client.books.ClientBookHelper;
import com.verdantartifice.primalmagick.client.gui.widgets.StaticBookPageButton;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.books.BookType;
import com.verdantartifice.primalmagick.common.books.BookView;
import com.verdantartifice.primalmagick.common.books.BooksPM;
import com.verdantartifice.primalmagick.common.books.LinguisticsManager;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

/**
 * GUI screen for reading static books.
 * 
 * @author Daedalus4096
 */
public class StaticBookViewScreen extends Screen {
    protected static final Logger LOGGER = LogManager.getLogger();
    
    public static final int PAGE_INDICATOR_TEXT_Y_OFFSET = 16;
    public static final int PAGE_TEXT_X_OFFSET = 36;
    public static final int PAGE_TEXT_Y_OFFSET = 30;
    protected static final int TEXT_WIDTH = ClientBookHelper.TEXT_WIDTH;
    protected static final int TEXT_HEIGHT = ClientBookHelper.TEXT_HEIGHT;
    protected static final int LINE_HEIGHT = ClientBookHelper.LINE_HEIGHT;
    protected static final int IMAGE_WIDTH = 192;
    protected static final int IMAGE_HEIGHT = 192;

    protected final boolean playTurnSound;
    protected final ResourceKey<?> requestedBookKey;
    protected final ResourceLocation requestedLanguageId;
    protected final int requestedTranslatedComprehension;
    protected final BookType bookType;
    protected final Map<Vector2i, FormattedCharSequence> renderedLines = new HashMap<>();
    protected BookView bookView;
    private PageButton forwardButton;
    private PageButton backButton;
    private int currentPage;
    private int cachedPage = -1;
    private Component pageMsg = CommonComponents.EMPTY;

    public StaticBookViewScreen() {
        this(ResourceKey.create(RegistryKeysPM.BOOKS, BooksPM.TEST_BOOK.getId()), BookLanguagesPM.DEFAULT.getId(), 0, BookType.BOOK, false);
    }
    
    public StaticBookViewScreen(ResourceKey<?> bookKey, ResourceLocation languageId, int translatedComprehension, BookType bookType) {
        this(bookKey, languageId, translatedComprehension, bookType, true);
    }
    
    private StaticBookViewScreen(ResourceKey<?> bookKey, ResourceLocation languageId, int translatedComprehension, BookType bookType, boolean playTurnSound) {
        super(GameNarrator.NO_TITLE);
        this.playTurnSound = playTurnSound;
        this.requestedBookKey = bookKey;
        this.requestedLanguageId = languageId;
        this.requestedTranslatedComprehension = translatedComprehension;
        this.bookType = bookType;
    }
    
    /**
     * Moves the book to the specified page and returns true if it exists, {@code false} otherwise.
     */
    public boolean setPage(int newPage) {
        int clampedPage = Mth.clamp(newPage, 0, this.getNumPages() - 1);
        if (clampedPage != this.currentPage) {
            this.currentPage = clampedPage;
            this.updateButtonVisibility();
            this.cachedPage = -1;
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void init() {
        BookLanguage lang = BookLanguagesPM.LANGUAGES.get().getValue(this.requestedLanguageId);
        if (lang == null) {
            lang = BookLanguagesPM.DEFAULT.get();
        }
        int comp = LinguisticsManager.getComprehension(this.minecraft.player, lang);
        this.bookView = new BookView(this.requestedBookKey, lang.languageId(), Math.max(comp, this.requestedTranslatedComprehension));

        this.createMenuControls();
        this.createPageControlButtons();
    }

    protected void createMenuControls() {
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> this.onClose()).bounds(this.width / 2 - 100, IMAGE_HEIGHT + 4, IMAGE_WIDTH + 8, 20).build());
    }

    protected void createPageControlButtons() {
        int x = (this.width - IMAGE_WIDTH) / 2;
        this.forwardButton = this.addRenderableWidget(new StaticBookPageButton(x + 116, 159, true, (p_98297_) -> {
            this.pageForward();
        }, this.playTurnSound, this.bookType));
        this.backButton = this.addRenderableWidget(new StaticBookPageButton(x + 43, 159, false, (p_98287_) -> {
            this.pageBack();
        }, this.playTurnSound, this.bookType));
        this.updateButtonVisibility();
    }

    private int getNumPages() {
        return ClientBookHelper.getNumPages(this.bookView, this.font);
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
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        this.renderedLines.clear();
        
        int xPos = (this.width - IMAGE_WIDTH) / 2;
        int yPos = 2;

        if (this.cachedPage != this.currentPage) {
            // Set the page indicator text
            this.pageMsg = Component.translatable("book.pageIndicator", this.currentPage + 1, Math.max(this.getNumPages(), 1));
        }
        
        this.cachedPage = this.currentPage;
        
        // Draw the page indicator text
        int pageMsgWidth = this.font.width(this.pageMsg);
        guiGraphics.drawString(this.font, this.pageMsg, xPos - pageMsgWidth + IMAGE_WIDTH - 44, PAGE_INDICATOR_TEXT_Y_OFFSET + 2, 0, false);

        // Draw the text lines for the current page
        List<FormattedCharSequence> page = ClientBookHelper.getTextPage(this.bookView, this.cachedPage, this.font);
        for (int index = 0; index < page.size(); index++) {
            int finalX = xPos + PAGE_TEXT_X_OFFSET;
            int finalY = yPos + PAGE_TEXT_Y_OFFSET + (index * LINE_HEIGHT);
            this.renderedLines.put(new Vector2i(finalX, finalY), page.get(index));
            guiGraphics.drawString(this.font, page.get(index), finalX, finalY, 0, false);
        }
        
        // Draw any hover effects dictated by text style
        this.getRenderedLineEntryAt(mouseX, mouseY).ifPresent(entry -> {
            int startX = entry.getKey().x;
            FormattedCharSequence line = entry.getValue();
            Style style = this.font.getSplitter().componentStyleAtWidth(line, mouseX - startX);
            if (style != null && style.getHoverEvent() != null) {
                guiGraphics.renderComponentHoverEffect(this.font, style, mouseX, mouseY);
            }
        });
    }
    
    @Override
    public void renderBackground(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.renderBackground(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        
        int xPos = (this.width - IMAGE_WIDTH) / 2;
        int yPos = 2;
        pGuiGraphics.blit(ClientBookHelper.getSprites(this.bookType).background(), xPos, yPos, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
    }

    protected Optional<Map.Entry<Vector2i, FormattedCharSequence>> getRenderedLineEntryAt(int x, int y) {
        for (Map.Entry<Vector2i, FormattedCharSequence> entry : this.renderedLines.entrySet()) {
            Vector2i pos = entry.getKey();
            int lineWidth = this.font.width(entry.getValue());
            if (x >= pos.x && x <= pos.x + lineWidth && y >= pos.y && y <= pos.y + LINE_HEIGHT) {
                return Optional.ofNullable(entry);
            }
        }
        return Optional.empty();
    }
}
