package com.verdantartifice.primalmagic.client.gui.grimoire;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.GlStateManager;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.gui.grimoire.buttons.GrimoireBackButton;
import com.verdantartifice.primalmagic.client.gui.grimoire.buttons.GrimoireDisciplineButton;
import com.verdantartifice.primalmagic.client.gui.grimoire.buttons.GrimoireEntryButton;
import com.verdantartifice.primalmagic.client.gui.grimoire.buttons.GrimoirePageButton;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.containers.GrimoireContainer;
import com.verdantartifice.primalmagic.common.research.ResearchAddendum;
import com.verdantartifice.primalmagic.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagic.common.research.ResearchDisciplines;
import com.verdantartifice.primalmagic.common.research.ResearchEntry;
import com.verdantartifice.primalmagic.common.research.ResearchStage;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class GrimoireScreen extends ContainerScreen<GrimoireContainer> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/grimoire.png");
    private static final PageImage IMAGE_LINE = PageImage.parse("primalmagic:textures/gui/grimoire.png:24:184:95:6:1");
    private static final float SCALE = 1.3F;
    
    public static final List<Object> HISTORY = new ArrayList<>();
    
    protected int scaledLeft;
    protected int scaledTop;
    protected int currentPage = 0;
    protected List<Page> pages = new ArrayList<>();
    protected IPlayerKnowledge knowledge;
    
    protected GrimoirePageButton nextPageButton;
    protected GrimoirePageButton prevPageButton;
    protected GrimoireBackButton backButton;
    
    public GrimoireScreen(GrimoireContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.xSize = 256;
        this.ySize = 181;
    }
    
    public PlayerInventory getPlayerInventory() {
        return this.playerInventory;
    }
    
    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
    
    @Override
    protected void init() {
        super.init();
        this.scaledLeft = (int)(this.width - this.xSize * SCALE) / 2;
        this.scaledTop = (int)(this.height - this.ySize * SCALE) / 2;
        this.knowledge = PrimalMagicCapabilities.getKnowledge(this.getMinecraft().player);
        if (this.knowledge == null) {
            throw new IllegalStateException("No knowledge provider found for player");
        }
        this.initPages();
        this.initButtons();
    }
    
    protected void initPages() {
        this.pages.clear();
        if (this.container.getTopic() == null) {
            this.parseIndexPages();
        } else if (this.container.getTopic() instanceof ResearchDiscipline) {
            this.parseDisciplinePages((ResearchDiscipline)this.container.getTopic());
        } else if (this.container.getTopic() instanceof ResearchEntry) {
            this.parseEntryPages((ResearchEntry)this.container.getTopic());
        }
    }
    
    protected void initButtons() {
        int current = 0;
        this.buttons.clear();
        this.children.clear();
        for (Page page : this.pages) {
            if ((current == this.currentPage || current == this.currentPage + 1) && current < this.pages.size()) {
                this.initPageButtons(page, current % 2, this.scaledLeft + 23, this.scaledTop + 9);
            }
            current++;
            if (current > this.currentPage + 1) {
                break;
            }
        }
        this.nextPageButton = new GrimoirePageButton(this.guiLeft + 262, this.guiTop + 190, this, true);
        this.prevPageButton = new GrimoirePageButton(this.guiLeft - 16, this.guiTop + 190, this, false);
        this.backButton = new GrimoireBackButton(this.guiLeft + 118, this.guiTop + 190, this);
        this.addButton(this.nextPageButton);
        this.addButton(this.prevPageButton);
        this.addButton(this.backButton);
        this.updateNavButtonVisibility();
    }

    private void initPageButtons(Page page, int side, int x, int y) {
        if (!page.hasButtons) {
            return;
        }
        
        // Make room for page title if applicable
        if (this.currentPage == 0 && side == 0) {
            y += 28;
        }
        if (this.currentPage > 0 || side == 1) {
            y += 4;
        }
        
        // Place buttons
        for (Object content : page.contents) {
            if (content instanceof ResearchDiscipline) {
                ResearchDiscipline discipline = (ResearchDiscipline)content;
                String text = (new TranslationTextComponent(discipline.getNameTranslationKey())).getFormattedText();
                this.addButton(new GrimoireDisciplineButton(x + (side * 152), y, text, this, discipline));
                y += 24;
            } else if (content instanceof ResearchEntry) {
                ResearchEntry entry = (ResearchEntry)content;
                String text = (new TranslationTextComponent(entry.getNameTranslationKey())).getFormattedText();
                this.addButton(new GrimoireEntryButton(x + (side * 152), y, text, this, entry));
                y += 24;
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(TEXTURE);

        int unscaledLeft = (this.width - this.xSize) / 2;
        int unscaledTop = (this.height - this.ySize) / 2;
        float scaledLeft = (this.width - this.xSize * SCALE) / 2.0F;
        float scaledTop = (this.height - this.ySize * SCALE) / 2.0F;

        GlStateManager.pushMatrix();
        GlStateManager.translatef(scaledLeft, scaledTop, 0.0F);
        GlStateManager.scalef(SCALE, SCALE, 1.0F);
        this.blit(0, 0, 0, 0, this.xSize, this.ySize);
        GlStateManager.popMatrix();
        
        int current = 0;
        for (Page page : this.pages) {
            if ((current == this.currentPage || current == this.currentPage + 1) && current < this.pages.size()) {
                this.drawPage(page, current % 2, unscaledLeft, unscaledTop - 10, mouseX, mouseY);
            }
            current++;
            if (current > this.currentPage + 1) {
                break;
            }
        }
    }
    
    protected void drawPage(Page page, int side, int x, int y, int mouseX, int mouseY) {
        // Draw page title if applicable
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        if (this.currentPage == 0 && side == 0) {
            this.blit(x + 4, y - 7, 24, 184, 96, 4);
            this.blit(x + 4, y + 10, 24, 184, 96, 4);
            String headerTextKey;
            if (this.container.getTopic() instanceof ResearchEntry) {
                headerTextKey = ((ResearchEntry)this.container.getTopic()).getNameTranslationKey();
            } else if (this.container.getTopic() instanceof ResearchDiscipline) {
                headerTextKey = ((ResearchDiscipline)this.container.getTopic()).getNameTranslationKey();
            } else {
                headerTextKey = "primalmagic.grimoire.index_header";
            }
            String headerText = new TranslationTextComponent(headerTextKey).getFormattedText();
            int offset = this.font.getStringWidth(headerText);
            int indent = 140;
            if (offset <= 140) {
                this.font.drawString(headerText, x - 15 + (indent / 2) - (offset / 2), y, Color.BLACK.getRGB());
            } else {
                float scale = 140.0F / offset;
                GlStateManager.pushMatrix();
                GlStateManager.translatef(x - 15 + (indent / 2) - (offset / 2 * scale), y + (1.0F * scale), 0.0F);
                GlStateManager.scalef(scale, scale, scale);
                this.font.drawString(headerText, 0, 0, Color.BLACK.getRGB());
                GlStateManager.popMatrix();
            }
            y += 28;
        }
        
        // Render page contents
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        for (Object content : page.contents) {
            if (content instanceof String) {
                String contentStr = (String)content;
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                this.font.drawString(contentStr.replace("~B", ""), x - 15 + (side * 152), y - 6, Color.BLACK.getRGB());
                y += this.font.FONT_HEIGHT;
                if (contentStr.endsWith("~B")) {
                    y += (int)(this.font.FONT_HEIGHT * 0.66D);
                }
            } else if (content instanceof PageImage) {
                PageImage contentImage = (PageImage)content;
                GlStateManager.pushMatrix();
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                this.getMinecraft().getTextureManager().bindTexture(contentImage.location);
                GlStateManager.translatef(x - 15 + (side * 152) + ((140 - contentImage.adjustedWidth) / 2), y - 5, 0.0F);
                GlStateManager.scalef(contentImage.scale, contentImage.scale, contentImage.scale);
                this.blit(0, 0, contentImage.x, contentImage.y, contentImage.width, contentImage.height);
                GlStateManager.popMatrix();
                y += (contentImage.adjustedHeight + 2);
            }
        }
        
        // TODO Draw stage requirements
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }
    
    private List<ResearchDiscipline> buildDisciplineList() {
        return ResearchDisciplines.getAllDisciplines().stream()
                                    .sorted(Comparator.comparing(d -> (new TranslationTextComponent(d.getNameTranslationKey())).getString()))
                                    .collect(Collectors.toList());
    }
    
    private List<ResearchEntry> buildEntryList(ResearchDiscipline discipline) {
        return discipline.getEntries().stream()
                .sorted(Comparator.comparing(e -> (new TranslationTextComponent(e.getNameTranslationKey())).getString()))
                .collect(Collectors.toList());
    }
    
    protected void parseIndexPages() {
        List<ResearchDiscipline> disciplines = this.buildDisciplineList();
        if (disciplines.isEmpty()) {
            return;
        }
        
        int heightRemaining = 182;
        Page tempPage = new Page();
        for (ResearchDiscipline discipline : disciplines) {
            tempPage.contents.add(discipline);
            tempPage.hasButtons = true;
            heightRemaining -= 24;
            if (heightRemaining < 24 && !tempPage.contents.isEmpty()) {
                heightRemaining = 210;
                this.pages.add(tempPage.copy());
                tempPage = new Page();
            }
        }
        if (!tempPage.contents.isEmpty()) {
            this.pages.add(tempPage.copy());
        }
    }
    
    protected void parseDisciplinePages(ResearchDiscipline discipline) {
        if (discipline == null) {
            return;
        }
        List<ResearchEntry> entries = this.buildEntryList(discipline);
        if (entries.isEmpty()) {
            return;
        }
        
        int heightRemaining = 182;
        Page tempPage = new Page();
        for (ResearchEntry entry : entries) {
            tempPage.contents.add(entry);
            tempPage.hasButtons = true;
            heightRemaining -= 24;
            if (heightRemaining < 24 && !tempPage.contents.isEmpty()) {
                heightRemaining = 210;
                this.pages.add(tempPage.copy());
                tempPage = new Page();
            }
        }
        if (!tempPage.contents.isEmpty()) {
            this.pages.add(tempPage.copy());
        }
    }
    
    protected void parseEntryPages(ResearchEntry entry) {
        if (entry == null || entry.getStages().isEmpty()) {
            return;
        }
        
        // Determine current research stage
        boolean complete = false;
        int currentStageIndex = this.knowledge.getResearchStage(entry.getKey()) - 1; // remember, it's one-based
        if (currentStageIndex >= entry.getStages().size()) {
            currentStageIndex = entry.getStages().size() - 1;
            complete = true;
        }
        if (currentStageIndex < 0) {
            currentStageIndex = 0;
        }
        ResearchStage stage = entry.getStages().get(currentStageIndex);
        List<ResearchAddendum> addenda = complete ? entry.getAddenda() : new ArrayList<>();
        
        // TODO generate recipe lists from stage and addenda
        
        String rawText = (new TranslationTextComponent(stage.getTextTranslationKey())).getString();
        
        // Append unlocked addendum text
        int addendumCount = 0;
        for (ResearchAddendum addendum : addenda) {
            if (addendum.getRequiredResearch() != null && addendum.getRequiredResearch().isKnownByStrict(this.getMinecraft().player)) {
                ITextComponent headerText = new TranslationTextComponent("primalmagic.grimoire.addendum_header", ++addendumCount);
                ITextComponent addendumText = new TranslationTextComponent(addendum.getTextTranslationKey());
                rawText += ("<PAGE>" + headerText.getFormattedText() + "<BR>" + addendumText.getFormattedText());
            }
        }
        
        // Process text
        rawText = rawText.replaceAll("<BR>", "~B\n\n");
        rawText = rawText.replaceAll("<LINE>", "~L");
        rawText = rawText.replaceAll("<PAGE>", "~P");
        List<PageImage> images = new ArrayList<>();
        String[] imgSplit = rawText.split("<IMG>");
        for (String imgStr : imgSplit) {
            int index = imgStr.indexOf("</IMG>");
            if (index >= 0) {
                String cleanStr = imgStr.substring(0, index);
                PageImage newImage = PageImage.parse(cleanStr);
                if (newImage == null) {
                    rawText = rawText.replaceFirst(cleanStr, "\n");
                } else {
                    images.add(newImage);
                    rawText = rawText.replaceFirst(cleanStr, "~I");
                }
            }
        }
        rawText = rawText.replaceAll("<IMG>", "");
        rawText = rawText.replaceAll("</IMG>", "");
        
        // Split raw text into formattable sections
        List<String> firstPassText = new ArrayList<>();
        String[] pageTokens = rawText.split("~P");
        for (int i = 0; i < pageTokens.length; i++) {
            String[] lineTokens = pageTokens[i].split("~L");
            for (int j = 0; j < lineTokens.length; j++) {
                String[] imgTokens = lineTokens[j].split("~I");
                for (int k = 0; k < imgTokens.length; k++) {
                    firstPassText.add(imgTokens[k]);
                    if (k != imgTokens.length - 1) {
                        firstPassText.add("~I");
                    }
                }
                if (j != lineTokens.length - 1) {
                    firstPassText.add("~L");
                }
            }
            if (i != pageTokens.length - 1) {
                firstPassText.add("~P");
            }
        }
        
        // Format text by font
        int lineHeight = this.font.FONT_HEIGHT;
        List<String> parsedText = new ArrayList<>();
        for (String str : firstPassText) {
            parsedText.addAll(this.font.listFormattedStringToWidth(str, 140));
        }
        
        // Determine available vertical space for first page
        int heightRemaining = 182;
        int dividerSpace = 0;
        if (!entry.getKey().isKnownByStrict(this.getMinecraft().player)) {
            if (!stage.getMustCraft().isEmpty()) {
                heightRemaining -= 18;
                dividerSpace = 15;
            }
            if (!stage.getMustObtain().isEmpty()) {
                heightRemaining -= 18;
                dividerSpace = 15;
            }
            if (!stage.getRequiredKnowledge().isEmpty()) {
                heightRemaining -= 18;
                dividerSpace = 15;
            }
            if (stage.getRequiredResearch() != null) {
                heightRemaining -= 18;
                dividerSpace = 15;
            }
        }
        heightRemaining -= dividerSpace;
        
        // Break parsed text into pages
        Page tempPage = new Page();
        List<PageImage> tempImages = new ArrayList<>();
        for (String line : parsedText) {
            if (line.contains("~I")) {
                if (!images.isEmpty()) {
                    tempImages.add(images.remove(0));
                }
                line = "";
            }
            if (line.contains("~L")) {
                tempImages.add(IMAGE_LINE);
                line = "";
            }
            if (line.contains("~P")) {
                this.pages.add(tempPage.copy());
                tempPage = new Page();
                heightRemaining = 210;
                line = "";
            }
            if (!line.isEmpty()) {
                line = line.trim();
                tempPage.contents.add(line);
                heightRemaining -= lineHeight;
                if (line.endsWith("~B")) {
                    heightRemaining -= (int)(lineHeight * 0.66D);
                }
            }
            while (!tempImages.isEmpty() && (heightRemaining >= (tempImages.get(0).adjustedHeight + 2))) {
                heightRemaining -= (tempImages.get(0).adjustedHeight + 2);
                tempPage.contents.add(tempImages.remove(0));
            }
            if ((heightRemaining < lineHeight) && !tempPage.contents.isEmpty()) {
                heightRemaining = 210;
                this.pages.add(tempPage.copy());
                tempPage = new Page();
            }
        }
        if (!tempPage.contents.isEmpty()) {
            this.pages.add(tempPage.copy());
        }
        
        // Deal with any remaining images
        tempPage = new Page();
        heightRemaining = 210;
        while (!tempImages.isEmpty()) {
            if (heightRemaining < (tempImages.get(0).adjustedHeight + 2)) {
                heightRemaining = 210;
                this.pages.add(tempPage.copy());
                tempPage = new Page();
            } else {
                heightRemaining -= (tempImages.get(0).adjustedHeight + 2);
                tempPage.contents.add(tempImages.remove(0));
            }
        }
        if (!tempPage.contents.isEmpty()) {
            this.pages.add(tempPage.copy());
        }
    }
    
    public void nextPage() {
        if (this.currentPage < this.pages.size() - 2) {
            this.currentPage += 2;
            if (this.container.getTopic() == null || this.container.getTopic() instanceof ResearchDiscipline) {
                this.initButtons();
            }
            this.updateNavButtonVisibility();
        }
    }
    
    public void prevPage() {
        if (this.currentPage >= 2) {
            this.currentPage -= 2;
            if (this.container.getTopic() == null || this.container.getTopic() instanceof ResearchDiscipline) {
                this.initButtons();
            }
            this.updateNavButtonVisibility();
        }
    }
    
    public void goBack() {
        if (!HISTORY.isEmpty()) {
            Object lastTopic = HISTORY.remove(HISTORY.size() - 1);
            this.container.setTopic(lastTopic);
            this.getMinecraft().displayGuiScreen(new GrimoireScreen(this.container, this.playerInventory, this.title));
        }
    }
    
    protected void updateNavButtonVisibility() {
        this.prevPageButton.visible = (this.currentPage >= 2);
        this.nextPageButton.visible = (this.currentPage < this.pages.size() - 2);
        this.backButton.visible = !HISTORY.isEmpty();
    }
    
    @Override
    public void onClose() {
        HISTORY.clear();
        super.onClose();
    }
    
    @Override
    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        if (p_keyPressed_1_ == GLFW.GLFW_KEY_ESCAPE) {
            HISTORY.clear();
        }
        return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
    }
    
    protected static class Page {
        public List<Object> contents = new ArrayList<>();
        public boolean hasButtons = false;
        
        private Page() {}
        
        public Page copy() {
            Page p = new Page();
            p.contents.addAll(this.contents);
            p.hasButtons = this.hasButtons;
            return p;
        }
    }
    
    protected static class PageImage {
        public int x, y, width, height, adjustedWidth, adjustedHeight;
        public float scale;
        public ResourceLocation location;
        
        @Nullable
        public static PageImage parse(String str) {
            String[] tokens = str.split(":");
            if (tokens.length != 7) {
                return null;
            }
            try {
                PageImage image = new PageImage();
                image.location = new ResourceLocation(tokens[0], tokens[1]);
                image.x = Integer.parseInt(tokens[2]);
                image.y = Integer.parseInt(tokens[3]);
                image.width = Integer.parseInt(tokens[4]);
                image.height = Integer.parseInt(tokens[5]);
                image.scale = Float.parseFloat(tokens[6]);
                image.adjustedWidth = (int)(image.width * image.scale);
                image.adjustedHeight = (int)(image.height * image.scale);
                if (image.adjustedWidth > 208 || image.adjustedHeight > 140) {
                    return null;
                }
                return image;
            } catch (Exception e) {
                return null;
            }
        }
    }
}
