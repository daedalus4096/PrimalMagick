package com.verdantartifice.primalmagic.client.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.gui.grimoire.AbstractPage;
import com.verdantartifice.primalmagic.client.gui.grimoire.AbstractRecipePage;
import com.verdantartifice.primalmagic.client.gui.grimoire.AttunementGainPage;
import com.verdantartifice.primalmagic.client.gui.grimoire.AttunementIndexPage;
import com.verdantartifice.primalmagic.client.gui.grimoire.AttunementPage;
import com.verdantartifice.primalmagic.client.gui.grimoire.DisciplineIndexPage;
import com.verdantartifice.primalmagic.client.gui.grimoire.DisciplinePage;
import com.verdantartifice.primalmagic.client.gui.grimoire.OtherIndexPage;
import com.verdantartifice.primalmagic.client.gui.grimoire.PageImage;
import com.verdantartifice.primalmagic.client.gui.grimoire.PageString;
import com.verdantartifice.primalmagic.client.gui.grimoire.RecipePageFactory;
import com.verdantartifice.primalmagic.client.gui.grimoire.RequirementsPage;
import com.verdantartifice.primalmagic.client.gui.grimoire.RuneEnchantmentIndexPage;
import com.verdantartifice.primalmagic.client.gui.grimoire.RuneEnchantmentPage;
import com.verdantartifice.primalmagic.client.gui.grimoire.StagePage;
import com.verdantartifice.primalmagic.client.gui.grimoire.StatisticsPage;
import com.verdantartifice.primalmagic.client.gui.widgets.grimoire.BackButton;
import com.verdantartifice.primalmagic.client.gui.widgets.grimoire.PageButton;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.containers.GrimoireContainer;
import com.verdantartifice.primalmagic.common.research.ResearchAddendum;
import com.verdantartifice.primalmagic.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagic.common.research.ResearchDisciplines;
import com.verdantartifice.primalmagic.common.research.ResearchEntry;
import com.verdantartifice.primalmagic.common.research.ResearchManager;
import com.verdantartifice.primalmagic.common.research.ResearchStage;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.runes.RuneManager;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;
import com.verdantartifice.primalmagic.common.stats.Stat;
import com.verdantartifice.primalmagic.common.stats.StatsManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * GUI screen for the grimoire research browser.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class GrimoireScreen extends ContainerScreen<GrimoireContainer> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/grimoire.png");
    private static final PageImage IMAGE_LINE = PageImage.parse("primalmagic:textures/gui/grimoire.png:24:184:95:6:1");
    private static final float SCALE = 1.3F;
    
    public static final List<Object> HISTORY = new ArrayList<>();
    
    protected int scaledLeft;
    protected int scaledTop;
    protected int currentPage = 0;
    protected int currentStageIndex = 0;
    protected int lastStageIndex = 0;
    protected long lastCheck = 0L;
    protected boolean progressing = false;
    protected List<AbstractPage> pages = new ArrayList<>();
    protected IPlayerKnowledge knowledge;
    
    protected PageButton nextPageButton;
    protected PageButton prevPageButton;
    protected BackButton backButton;
    
    public GrimoireScreen(GrimoireContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.xSize = 256;
        this.ySize = 181;
    }
    
    public PlayerInventory getPlayerInventory() {
        return this.playerInventory;
    }
    
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        // Determine if we need to update the GUI based on how long it's been since the last refresh
        long millis = System.currentTimeMillis();
        if (millis > this.lastCheck) {
            // Update more frequently if waiting for the server to process a progression
            this.lastCheck = this.progressing ? (millis + 250L) : (millis + 2000L);
            this.initPages();
            this.initButtons();
            
            // Reset to the first page of the entry if the current stage has advanced
            if (this.currentStageIndex > this.lastStageIndex) {
                this.progressing = false;
                this.lastStageIndex = this.currentStageIndex;
                this.currentPage = 0;
                this.updateNavButtonVisibility();
            }
        }
        
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }
    
    public boolean isProgressing() {
        return this.progressing;
    }
    
    public void setProgressing() {
        this.progressing = true;
        this.lastCheck = 0L;
        this.lastStageIndex = this.currentStageIndex;
    }
    
    @Override
    protected void init() {
        super.init();
        this.scaledLeft = (int)(this.width - this.xSize * SCALE) / 2;
        this.scaledTop = (int)(this.height - this.ySize * SCALE) / 2;
    	Minecraft mc = this.getMinecraft();
        this.knowledge = PrimalMagicCapabilities.getKnowledge(mc.player);
        if (this.knowledge == null) {
            throw new IllegalStateException("No knowledge provider found for player");
        }
        this.initPages();
        this.initButtons();
    }
    
    protected void initPages() {
        // Parse grimoire pages based on the current topic
        this.pages.clear();
        if (this.container.getTopic() == null) {
            this.parseIndexPages();
        } else if (this.container.getTopic() instanceof ResearchDiscipline) {
            this.parseDisciplinePages((ResearchDiscipline)this.container.getTopic());
        } else if (this.container.getTopic() instanceof ResearchEntry) {
            this.parseEntryPages((ResearchEntry)this.container.getTopic());
        } else if (this.container.getTopic() instanceof Source) {
            this.parseAttunementPage((Source)this.container.getTopic());
        } else if (this.container.getTopic() instanceof Enchantment) {
            this.parseRuneEnchantmentPage((Enchantment)this.container.getTopic());
        } else if (StatisticsPage.TOPIC.equals(this.container.getTopic())) {
            this.parseStatsPages();
        } else if (AttunementIndexPage.TOPIC.equals(this.container.getTopic())) {
            this.parseAttunementIndexPages();
        } else if (RuneEnchantmentIndexPage.TOPIC.equals(this.container.getTopic())) {
            this.parseRuneEnchantmentIndexPages();
        }
    }
    
    protected void initButtons() {
        int current = 0;
        this.buttons.clear();
        this.children.clear();
        
        // Initialize buttons for the two visible pages
        for (AbstractPage page : this.pages) {
            if ((current == this.currentPage || current == this.currentPage + 1) && current < this.pages.size()) {
                this.initPageButtons(page, current % 2, this.scaledLeft + 23, this.scaledTop + 9);
            }
            current++;
            if (current > this.currentPage + 1) {
                break;
            }
        }
        
        // Create navigation buttons and update their visibility
        this.nextPageButton = new PageButton(this.guiLeft + 258, this.guiTop + 172, this, true);
        this.prevPageButton = new PageButton(this.guiLeft - 12, this.guiTop + 172, this, false);
        this.backButton = new BackButton(this.guiLeft + 120, this.guiTop + 172, this);
        this.addButton(this.nextPageButton);
        this.addButton(this.prevPageButton);
        this.addButton(this.backButton);
        this.updateNavButtonVisibility();
    }
    
    public <T extends Widget> T addWidgetToScreen(T widget) {
        return this.addButton(widget);
    }

    private void initPageButtons(AbstractPage abstractPage, int side, int x, int y) {
        // Make room for page title if applicable
        if (this.currentPage == 0 && side == 0) {
            y += 53;
        }
        if (this.currentPage > 0 || side == 1) {
            y += 29;
        }
        
        // Place buttons
        abstractPage.initWidgets(this, side, x, y);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        // Render the grimoire background
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(TEXTURE);

        int unscaledLeft = (this.width - this.xSize) / 2;
        int unscaledTop = (this.height - this.ySize) / 2;
        float scaledLeft = (this.width - this.xSize * SCALE) / 2.0F;
        float scaledTop = (this.height - this.ySize * SCALE) / 2.0F;

        RenderSystem.pushMatrix();
        RenderSystem.translatef(scaledLeft, scaledTop, 0.0F);
        RenderSystem.scalef(SCALE, SCALE, 1.0F);
        this.blit(matrixStack, 0, 0, 0, 0, this.xSize, this.ySize);
        RenderSystem.popMatrix();
        
        // Render the two visible pages
        int current = 0;
        for (AbstractPage page : this.pages) {
            if ((current == this.currentPage || current == this.currentPage + 1) && current < this.pages.size()) {
                page.render(current % 2, unscaledLeft, unscaledTop - 10, mouseX, mouseY);
            }
            current++;
            if (current > this.currentPage + 1) {
                break;
            }
        }
    }
    
    private List<ResearchDiscipline> buildDisciplineList() {
        // Gather a list of all research disciplines, sorted by their display names
        return ResearchDisciplines.getAllDisciplines().stream()
                                    .sorted(Comparator.comparing(d -> (new TranslationTextComponent(d.getNameTranslationKey())).getString()))
                                    .collect(Collectors.toList());
    }
    
    private List<ResearchEntry> buildEntryList(ResearchDiscipline discipline) {
        // Gather a list of all research entries for the given discipline, sorted by their display names
        return discipline.getEntries().stream()
                .sorted(Comparator.comparing(e -> (new TranslationTextComponent(e.getNameTranslationKey())).getString()))
                .collect(Collectors.toList());
    }
    
    protected void parseIndexPages() {
        this.currentStageIndex = 0;
        List<ResearchDiscipline> disciplines = this.buildDisciplineList();
        if (disciplines.isEmpty()) {
            return;
        }
        
        // Add each unlocked research discipline to the current discipline index page
        int heightRemaining = 182;  // Leave enough room for the page header
        DisciplineIndexPage tempPage = new DisciplineIndexPage(true);
        for (ResearchDiscipline discipline : disciplines) {
            if (!discipline.getKey().equals("SCANS") && (discipline.getUnlockResearchKey() == null || discipline.getUnlockResearchKey().isKnownByStrict(Minecraft.getInstance().player))) {
                tempPage.addDiscipline(discipline);
                heightRemaining -= 12;
                
                // If there isn't enough room for another discipline, start a new page
                if (heightRemaining < 12 && !tempPage.getDisciplines().isEmpty()) {
                    heightRemaining = 210;
                    this.pages.add(tempPage);
                    tempPage = new DisciplineIndexPage();
                }
            }
        }
        
        // Add the final discipline index page to the collection, if it's not empty
        if (!tempPage.getDisciplines().isEmpty()) {
            this.pages.add(tempPage);
        }
        
        // Add the other index page after the discipline index pages
        this.pages.add(new OtherIndexPage());
    }
    
    protected void parseDisciplinePages(ResearchDiscipline discipline) {
        this.currentStageIndex = 0;
        if (discipline == null) {
            return;
        }
        List<ResearchEntry> entries = this.buildEntryList(discipline);
        if (entries.isEmpty()) {
            return;
        }
        
        // Sort the alphabetized entries into sections
        List<ResearchEntry> newList = new ArrayList<>();
        List<ResearchEntry> updatedList = new ArrayList<>();
        List<ResearchEntry> completeList = new ArrayList<>();
        List<ResearchEntry> inProgressList = new ArrayList<>();
        List<ResearchEntry> availableList = new ArrayList<>();
        for (ResearchEntry entry : entries) {
            if (this.knowledge.hasResearchFlag(entry.getKey(), IPlayerKnowledge.ResearchFlag.NEW)) {
                newList.add(entry);
            } else if (this.knowledge.hasResearchFlag(entry.getKey(), IPlayerKnowledge.ResearchFlag.UPDATED)) {
                updatedList.add(entry);
            } else if (this.knowledge.getResearchStatus(entry.getKey()) == IPlayerKnowledge.ResearchStatus.COMPLETE) {
                completeList.add(entry);
            } else if (this.knowledge.getResearchStatus(entry.getKey()) == IPlayerKnowledge.ResearchStatus.IN_PROGRESS) {
                inProgressList.add(entry);
            } else if (entry.getParentResearch() == null || entry.getParentResearch().isKnownByStrict(Minecraft.getInstance().player)) {
                availableList.add(entry);
            }
        }
        
        // Divide the sections out into pages
        DisciplinePageProperties properties = new DisciplinePageProperties();
        properties.heightRemaining = 137;
        properties.firstSection = true;
        properties.page = new DisciplinePage(discipline, true);
        if (!updatedList.isEmpty()) {
            this.parseDisciplinePageSection(updatedList, "updated", discipline, properties);
        }
        if (!newList.isEmpty()) {
            this.parseDisciplinePageSection(newList, "new", discipline, properties);
        }
        if (!inProgressList.isEmpty()) {
            this.parseDisciplinePageSection(inProgressList, "in_progress", discipline, properties);
        }
        if (!availableList.isEmpty()) {
            this.parseDisciplinePageSection(availableList, "available", discipline, properties);
        }
        if (!completeList.isEmpty()) {
            this.parseDisciplinePageSection(completeList, "complete", discipline, properties);
        }
        if (!properties.page.getContents().isEmpty()) {
            this.pages.add(properties.page);
        }
    }
    
    protected void parseDisciplinePageSection(List<ResearchEntry> researchList, String headerName, ResearchDiscipline discipline, DisciplinePageProperties properties) {
        // Append the section header and spacer
        ITextComponent headerText = new TranslationTextComponent("primalmagic.grimoire.section_header." + headerName).mergeStyle(TextFormatting.UNDERLINE);
        if (properties.heightRemaining < 36 && !properties.page.getContents().isEmpty()) {
            // If there's not room for the spacer, the header, and a first entry, skip to the next page
            properties.heightRemaining = 155;
            this.pages.add(properties.page);
            properties.page = new DisciplinePage(discipline);
            properties.page.addContent(headerText);
        } else {
            if (!properties.firstSection && !properties.page.getContents().isEmpty()) {
                properties.page.addContent(new StringTextComponent(""));
                properties.heightRemaining -= 12;
            }
            properties.page.addContent(headerText);
            properties.heightRemaining -= 12;
        }
        properties.firstSection = false;
        for (ResearchEntry entry : researchList) {
            // Append each entry from the list to the page, breaking where necessary
            properties.page.addContent(entry);
            properties.heightRemaining -= 12;
            if (properties.heightRemaining < 12 && !properties.page.getContents().isEmpty()) {
                properties.heightRemaining = 155;
                this.pages.add(properties.page);
                properties.page = new DisciplinePage(discipline);
            }
        }
    }
    
    private Tuple<List<String>, List<PageImage>> parseText(String rawText) {
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
        List<String> parsedText = new ArrayList<>();
        for (String str : firstPassText) {
            parsedText.addAll(this.font.listFormattedStringToWidth(str, 124));
        }
        
        return new Tuple<>(parsedText, images);
    }
    
    protected void parseEntryPages(ResearchEntry entry) {
        if (entry == null || entry.getStages().isEmpty()) {
            return;
        }
        
        // Determine current research stage
        boolean complete = false;
        this.currentStageIndex = this.knowledge.getResearchStage(entry.getKey());
        if (this.currentStageIndex >= entry.getStages().size()) {
            this.currentStageIndex = entry.getStages().size() - 1;
            complete = true;
        }
        if (this.currentStageIndex < 0) {
            this.currentStageIndex = 0;
        }
        ResearchStage stage = entry.getStages().get(this.currentStageIndex);
        List<ResearchAddendum> addenda = complete ? entry.getAddenda() : Collections.emptyList();
        
        String rawText = (new TranslationTextComponent(stage.getTextTranslationKey())).getString();
        
        // Append unlocked addendum text
        int addendumCount = 0;
        for (ResearchAddendum addendum : addenda) {
            if (addendum.getRequiredResearch() != null && addendum.getRequiredResearch().isKnownByStrict(this.getMinecraft().player)) {
                ITextComponent headerText = new TranslationTextComponent("primalmagic.grimoire.addendum_header", ++addendumCount);
                ITextComponent addendumText = new TranslationTextComponent(addendum.getTextTranslationKey());
                rawText += ("<PAGE>" + headerText.getString() + "<BR>" + addendumText.getString());
            }
        }
        
        // Process text
        int lineHeight = this.font.FONT_HEIGHT;
        Tuple<List<String>, List<PageImage>> parsedData = this.parseText(rawText);
        List<String> parsedText = parsedData.getA();
        List<PageImage> images = parsedData.getB();
        
        // First page has less available space to account for title
        int heightRemaining = 137;
        
        // Break parsed text into pages
        StagePage tempPage = new StagePage(stage, true);
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
                this.pages.add(tempPage);
                tempPage = new StagePage(stage);
                heightRemaining = 165;
                line = "";
            }
            if (!line.isEmpty()) {
                line = line.trim();
                tempPage.addElement(new PageString(line));
                heightRemaining -= lineHeight;
                if (line.endsWith("~B")) {
                    heightRemaining -= (int)(lineHeight * 0.66D);
                }
            }
            while (!tempImages.isEmpty() && (heightRemaining >= (tempImages.get(0).adjustedHeight + 2))) {
                heightRemaining -= (tempImages.get(0).adjustedHeight + 2);
                tempPage.addElement(tempImages.remove(0));
            }
            if ((heightRemaining < lineHeight) && !tempPage.getElements().isEmpty()) {
                heightRemaining = 165;
                this.pages.add(tempPage);
                tempPage = new StagePage(stage);
            }
        }
        if (!tempPage.getElements().isEmpty()) {
            this.pages.add(tempPage);
        }
        
        // Deal with any remaining images
        tempPage = new StagePage(stage);
        heightRemaining = 165;
        while (!tempImages.isEmpty()) {
            if (heightRemaining < (tempImages.get(0).adjustedHeight + 2)) {
                heightRemaining = 165;
                this.pages.add(tempPage);
                tempPage = new StagePage(stage);
            } else {
                heightRemaining -= (tempImages.get(0).adjustedHeight + 2);
                tempPage.addElement(tempImages.remove(0));
            }
        }
        if (!tempPage.getElements().isEmpty()) {
            this.pages.add(tempPage);
        }
        
        // Add attunement gain page if applicable
        SourceList attunements = stage.getAttunements();
        for (ResearchAddendum addendum : addenda) {
            if (addendum.getRequiredResearch() == null || addendum.getRequiredResearch().isKnownByStrict(this.getMinecraft().player)) {
                attunements = attunements.merge(addendum.getAttunements());
            }
        }
        if (!attunements.isEmpty()) {
            this.pages.add(new AttunementGainPage(attunements));
        }
        
        // Add requirements page if applicable
        if (!entry.getKey().isKnownByStrict(this.minecraft.player) && stage.hasPrerequisites()) {
            this.pages.add(new RequirementsPage(stage));
        }
        
        // Generate recipe pages from stage and addenda
        List<ResourceLocation> locList = new ArrayList<>();
        for (ResourceLocation loc : stage.getRecipes()) {
            if (!locList.contains(loc)) {
                locList.add(loc);
            }
        }
        for (ResearchAddendum addendum : addenda) {
            if (addendum.getRequiredResearch() == null || addendum.getRequiredResearch().isKnownByStrict(this.getMinecraft().player)) {
                for (ResourceLocation loc : addendum.getRecipes()) {
                    if (!locList.contains(loc)) {
                        locList.add(loc);
                    }
                }
            }
        }
        for (ResourceLocation recipeLoc : locList) {
            Optional<? extends IRecipe<?>> opt = this.minecraft.world.getRecipeManager().getRecipe(recipeLoc);
            if (opt.isPresent()) {
                AbstractRecipePage page = RecipePageFactory.createPage(opt.get());
                if (page != null) {
                    this.pages.add(page);
                }
            } else {
                PrimalMagic.LOGGER.warn("Unable to find recipe definition for {}", recipeLoc.toString());
            }
        }
    }
    
    protected void parseStatsPages() {
        // For each visible statistic, create one or more page strings for that stat's text and value
        this.currentStageIndex = 0;
        List<Stat> stats = StatsManager.getSortedStats();
        if (stats.isEmpty()) {
            return;
        }
        
        // Add each unlocked research discipline to the current discipline index page
        int heightRemaining = 137;  // Leave enough room for the page header
        int dotWidth = this.font.getStringWidth(".");
        StatisticsPage tempPage = new StatisticsPage(true);
        for (Stat stat : stats) {
            int statValue = StatsManager.getValue(this.getMinecraft().player, stat);
            if (!stat.isHidden() || statValue > 0) {
                // Join the stat text and formatted value with periods in between for spacing
                ITextComponent statText = new TranslationTextComponent(stat.getTranslationKey());
                List<String> statTextSegments = new ArrayList<>(this.font.listFormattedStringToWidth(statText.getString(), 124));
                String lastStatTextSegment = statTextSegments.get(statTextSegments.size() - 1);
                int lastStatTextSegmentWidth = this.font.getStringWidth(lastStatTextSegment);
                String statFormattedValueStr = stat.getFormatter().format(statValue);
                int statFormattedValueStrWidth = this.font.getStringWidth(statFormattedValueStr);
                int remainingWidth = 124 - lastStatTextSegmentWidth - statFormattedValueStrWidth;
                if (remainingWidth < 10) {
                    // If there isn't enough room to put them on the same line, put the last text segment and the formatted value on different lines
                    String joiner1 = String.join("", Collections.nCopies((124 - lastStatTextSegmentWidth) / dotWidth, "."));
                    String joiner2 = String.join("", Collections.nCopies((124 - statFormattedValueStrWidth) / dotWidth, "."));
                    statTextSegments.set(statTextSegments.size() - 1, lastStatTextSegment + joiner1);
                    statTextSegments.add(joiner2 + statFormattedValueStr + "~B");   // Include a section break at the end
                } else {
                    // Otherwise, join them as the last line of the block
                    String joiner = String.join("", Collections.nCopies(remainingWidth / dotWidth, "."));
                    statTextSegments.set(statTextSegments.size() - 1, lastStatTextSegment + joiner + statFormattedValueStr + "~B"); // Include a section break at the end
                }
                
                // Calculate the total height of the stat block, including spacer, and determine if it will fit on the current page
                int totalHeight = (int)(this.font.FONT_HEIGHT * statTextSegments.size());
                if (heightRemaining < totalHeight && !tempPage.getElements().isEmpty()) {
                    // If there isn't enough room for another stat block, start a new page
                    heightRemaining = 165;
                    this.pages.add(tempPage);
                    tempPage = new StatisticsPage();
                }
                
                // Add the stat block and its section break to the page
                for (String str : statTextSegments) {
                    tempPage.addElement(new PageString(str));
                    heightRemaining -= this.font.FONT_HEIGHT;
                }
                heightRemaining -= (int)(this.font.FONT_HEIGHT * 0.66D);
            }
        }
        
        // Add the final page to the collection, if it's not empty
        if (!tempPage.getElements().isEmpty()) {
            this.pages.add(tempPage);
        }
    }
    
    protected void parseAttunementIndexPages() {
        this.currentStageIndex = 0;
        this.pages.add(new AttunementIndexPage(true));
    }
    
    protected void parseAttunementPage(Source source) {
        this.currentStageIndex = 0;
        
        // Add the first page with no contents to show the meter
        this.pages.add(new AttunementPage(source, true));
        
        String rawText = (new TranslationTextComponent("primalmagic.attunement." + source.getTag() + ".text")).getString();
        
        // Process text
        int lineHeight = this.font.FONT_HEIGHT;
        Tuple<List<String>, List<PageImage>> parsedData = this.parseText(rawText);
        List<String> parsedText = parsedData.getA();
        List<PageImage> images = parsedData.getB();
        
        // Starting with the second page, so we have more space available
        int heightRemaining = 165;
        
        // Break parsed text into pages
        AttunementPage tempPage = new AttunementPage(source);
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
                this.pages.add(tempPage);
                tempPage = new AttunementPage(source);
                heightRemaining = 165;
                line = "";
            }
            if (!line.isEmpty()) {
                line = line.trim();
                tempPage.addElement(new PageString(line));
                heightRemaining -= lineHeight;
                if (line.endsWith("~B")) {
                    heightRemaining -= (int)(lineHeight * 0.66D);
                }
            }
            while (!tempImages.isEmpty() && (heightRemaining >= (tempImages.get(0).adjustedHeight + 2))) {
                heightRemaining -= (tempImages.get(0).adjustedHeight + 2);
                tempPage.addElement(tempImages.remove(0));
            }
            if ((heightRemaining < lineHeight) && !tempPage.getElements().isEmpty()) {
                heightRemaining = 165;
                this.pages.add(tempPage);
                tempPage = new AttunementPage(source);
            }
        }
        if (!tempPage.getElements().isEmpty()) {
            this.pages.add(tempPage);
        }
        
        // Deal with any remaining images
        tempPage = new AttunementPage(source);
        heightRemaining = 165;
        while (!tempImages.isEmpty()) {
            if (heightRemaining < (tempImages.get(0).adjustedHeight + 2)) {
                heightRemaining = 165;
                this.pages.add(tempPage);
                tempPage = new AttunementPage(source);
            } else {
                heightRemaining -= (tempImages.get(0).adjustedHeight + 2);
                tempPage.addElement(tempImages.remove(0));
            }
        }
        if (!tempPage.getElements().isEmpty()) {
            this.pages.add(tempPage);
        }
    }
    
    protected void parseRuneEnchantmentPage(Enchantment enchant) {
        String rawText = (new TranslationTextComponent(Util.makeTranslationKey("rune_enchantment.text", enchant.getRegistryName()))).getString();
        
        // Process text
        int lineHeight = this.font.FONT_HEIGHT;
        Tuple<List<String>, List<PageImage>> parsedData = this.parseText(rawText);
        List<String> parsedText = parsedData.getA();
        List<PageImage> images = parsedData.getB();
        
        // First page has less available space to account for title and rune combination
        int heightRemaining = 113;

        // Break parsed text into pages
        RuneEnchantmentPage tempPage = new RuneEnchantmentPage(enchant, true);
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
                this.pages.add(tempPage);
                tempPage = new RuneEnchantmentPage(enchant);
                heightRemaining = 165;
                line = "";
            }
            if (!line.isEmpty()) {
                line = line.trim();
                tempPage.addElement(new PageString(line));
                heightRemaining -= lineHeight;
                if (line.endsWith("~B")) {
                    heightRemaining -= (int)(lineHeight * 0.66D);
                }
            }
            while (!tempImages.isEmpty() && (heightRemaining >= (tempImages.get(0).adjustedHeight + 2))) {
                heightRemaining -= (tempImages.get(0).adjustedHeight + 2);
                tempPage.addElement(tempImages.remove(0));
            }
            if ((heightRemaining < lineHeight) && !tempPage.getElements().isEmpty()) {
                heightRemaining = 165;
                this.pages.add(tempPage);
                tempPage = new RuneEnchantmentPage(enchant);
            }
        }
        if (!tempPage.getElements().isEmpty()) {
            this.pages.add(tempPage);
        }
        
        // Deal with any remaining images
        tempPage = new RuneEnchantmentPage(enchant);
        heightRemaining = 165;
        while (!tempImages.isEmpty()) {
            if (heightRemaining < (tempImages.get(0).adjustedHeight + 2)) {
                heightRemaining = 165;
                this.pages.add(tempPage);
                tempPage = new RuneEnchantmentPage(enchant);
            } else {
                heightRemaining -= (tempImages.get(0).adjustedHeight + 2);
                tempPage.addElement(tempImages.remove(0));
            }
        }
        if (!tempPage.getElements().isEmpty()) {
            this.pages.add(tempPage);
        }
    }
    
    protected void parseRuneEnchantmentIndexPages() {
        this.currentStageIndex = 0;
        int heightRemaining = 137;
        RuneEnchantmentIndexPage tempPage = new RuneEnchantmentIndexPage(true);
    	Minecraft mc = Minecraft.getInstance();

        for (Enchantment enchant : RuneManager.getRuneEnchantmentsSorted()) {
            if (ResearchManager.isResearchComplete(mc.player, SimpleResearchKey.parseRuneEnchantment(enchant))) {
                tempPage.addEnchantment(enchant);
                heightRemaining -= 12;
                if (heightRemaining < 12 && !tempPage.getEnchantments().isEmpty()) {
                    heightRemaining = 155;
                    this.pages.add(tempPage);
                    tempPage = new RuneEnchantmentIndexPage();
                }
            }
        }
        if (!tempPage.getEnchantments().isEmpty()) {
            this.pages.add(tempPage);
        }
    }
    
    public void nextPage() {
        if (this.currentPage < this.pages.size() - 2) {
            this.currentPage += 2;
            this.initButtons();
        }
    }
    
    public void prevPage() {
        if (this.currentPage >= 2) {
            this.currentPage -= 2;
            this.initButtons();
        }
    }
    
    public void goBack() {
        // Pop the last viewed topic off the history stack and open a new screen for it
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
        // Clear the topic history when closing the screen
        HISTORY.clear();
        super.onClose();
    }
    
    @Override
    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        // Clear the topic history when closing the screen
        if (p_keyPressed_1_ == GLFW.GLFW_KEY_ESCAPE) {
            HISTORY.clear();
        }
        return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
    }
    
    protected static class DisciplinePageProperties {
        public DisciplinePage page;
        public int heightRemaining;
        public boolean firstSection;
    }
}
