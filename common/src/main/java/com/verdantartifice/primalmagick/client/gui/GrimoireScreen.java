package com.verdantartifice.primalmagick.client.gui;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.client.config.KeyBindings;
import com.verdantartifice.primalmagick.client.gui.grimoire.AbstractPage;
import com.verdantartifice.primalmagick.client.gui.grimoire.AbstractRecipePage;
import com.verdantartifice.primalmagick.client.gui.grimoire.AffinityIndexPage;
import com.verdantartifice.primalmagick.client.gui.grimoire.AffinityPage;
import com.verdantartifice.primalmagick.client.gui.grimoire.AttunementGainPage;
import com.verdantartifice.primalmagick.client.gui.grimoire.AttunementIndexPage;
import com.verdantartifice.primalmagick.client.gui.grimoire.AttunementPage;
import com.verdantartifice.primalmagick.client.gui.grimoire.DisciplineIndexPage;
import com.verdantartifice.primalmagick.client.gui.grimoire.DisciplinePage;
import com.verdantartifice.primalmagick.client.gui.grimoire.IPageElement;
import com.verdantartifice.primalmagick.client.gui.grimoire.LinguisticsDescriptionPage;
import com.verdantartifice.primalmagick.client.gui.grimoire.LinguisticsIndexPage;
import com.verdantartifice.primalmagick.client.gui.grimoire.LinguisticsScorePage;
import com.verdantartifice.primalmagick.client.gui.grimoire.OtherIndexPage;
import com.verdantartifice.primalmagick.client.gui.grimoire.PageImage;
import com.verdantartifice.primalmagick.client.gui.grimoire.PageString;
import com.verdantartifice.primalmagick.client.gui.grimoire.RecipeIndexPage;
import com.verdantartifice.primalmagick.client.gui.grimoire.RecipeMetadataPage;
import com.verdantartifice.primalmagick.client.gui.grimoire.RecipePageFactory;
import com.verdantartifice.primalmagick.client.gui.grimoire.RequirementsPage;
import com.verdantartifice.primalmagick.client.gui.grimoire.RuneEnchantmentIndexPage;
import com.verdantartifice.primalmagick.client.gui.grimoire.RuneEnchantmentPage;
import com.verdantartifice.primalmagick.client.gui.grimoire.StagePage;
import com.verdantartifice.primalmagick.client.gui.grimoire.StatisticsPage;
import com.verdantartifice.primalmagick.client.gui.grimoire.TipsPage;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.AffinityRecordWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.BackButton;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.MainIndexButton;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.PageButton;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.TopicLinkButton;
import com.verdantartifice.primalmagick.common.affinities.AffinityIndexEntry;
import com.verdantartifice.primalmagick.common.affinities.AffinityManager;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagick.common.crafting.IHasRequirement;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.data.SetResearchTopicHistoryPacket;
import com.verdantartifice.primalmagick.common.research.ResearchAddendum;
import com.verdantartifice.primalmagick.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagick.common.research.ResearchDisciplines;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.research.ResearchStage;
import com.verdantartifice.primalmagick.common.research.keys.AbstractResearchKey;
import com.verdantartifice.primalmagick.common.research.keys.ItemScanKey;
import com.verdantartifice.primalmagick.common.research.keys.ResearchDisciplineKey;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.keys.RuneEnchantmentKey;
import com.verdantartifice.primalmagick.common.research.keys.RuneEnchantmentPartialKey;
import com.verdantartifice.primalmagick.common.research.topics.AbstractResearchTopic;
import com.verdantartifice.primalmagick.common.research.topics.AffinityResearchTopic;
import com.verdantartifice.primalmagick.common.research.topics.DisciplineResearchTopic;
import com.verdantartifice.primalmagick.common.research.topics.EnchantmentResearchTopic;
import com.verdantartifice.primalmagick.common.research.topics.EntryResearchTopic;
import com.verdantartifice.primalmagick.common.research.topics.LanguageResearchTopic;
import com.verdantartifice.primalmagick.common.research.topics.MainIndexResearchTopic;
import com.verdantartifice.primalmagick.common.research.topics.OtherResearchTopic;
import com.verdantartifice.primalmagick.common.research.topics.SourceResearchTopic;
import com.verdantartifice.primalmagick.common.research.topics.TopicLink;
import com.verdantartifice.primalmagick.common.runes.RuneManager;
import com.verdantartifice.primalmagick.common.runes.RuneType;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.stats.Stat;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.tips.TipDefinition;
import com.verdantartifice.primalmagick.common.tips.TipDefinitionsPM;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringDecomposer;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * GUI screen for the grimoire research browser.
 * 
 * @author Daedalus4096
 */
public class GrimoireScreen extends Screen {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final ResourceLocation TEXTURE = ResourceUtils.loc("textures/gui/grimoire.png");
    private static final PageImage IMAGE_LINE = PageImage.parse("primalmagick:textures/gui/grimoire.png:24:184:95:6:1");
    private static final float SCALE = 1.3F;
    private static final int HISTORY_LIMIT = 64;
    private static final int BG_WIDTH = 256;
    private static final int BG_HEIGHT = 181;
    
    protected int leftPos;
    protected int topPos;
    protected int scaledLeft;
    protected int scaledTop;
    protected int currentPage = 0;
    protected int currentStageIndex = 0;
    protected int lastStageIndex = 0;
    protected long lastCheck = 0L;
    protected boolean progressing = false;
    protected boolean refreshing = false;
    protected List<AbstractPage> pages = new ArrayList<>();
    protected IPlayerKnowledge knowledge;
    protected NavigableMap<String, List<RecipeHolder<?>>> indexMap;
    protected Component cachedTip = null;
    protected Optional<String> lastRecipeSearch = Optional.empty();
    
    protected PageButton nextPageButton;
    protected PageButton prevPageButton;
    protected BackButton backButton;
    
    public GrimoireScreen() {
        super(Component.empty());
    }
    
    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        // Determine if we need to update the GUI based on how long it's been since the last refresh
        long millis = System.currentTimeMillis();
        if ((this.isProgressing() || this.isRefreshing() || this.currentStageIndex > this.lastStageIndex) && millis > this.lastCheck) {
            // Reset to the first page of the entry if the current stage has advanced
            if (this.currentStageIndex > this.lastStageIndex) {
                this.progressing = false;
                this.lastStageIndex = this.currentStageIndex;
                this.currentPage = 0;
                this.updateNavButtonVisibility();
            }
            
            this.lastCheck = millis + 250L;
            this.initPages();
            this.initButtons();
        }
        
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    public boolean isRefreshing() {
        return this.refreshing;
    }

    public void setRefreshing() {
        this.refreshing = true;
        this.lastCheck = 0;
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
        this.leftPos = (this.width - BG_WIDTH) / 2;
        this.topPos = (this.height - BG_HEIGHT) / 2;
        this.scaledLeft = (int)(this.width - BG_WIDTH * SCALE) / 2;
        this.scaledTop = (int)(this.height - BG_HEIGHT * SCALE) / 2;
        Minecraft mc = Minecraft.getInstance();
        this.knowledge = Services.CAPABILITIES.knowledge(mc.player).orElseThrow(() -> new IllegalStateException("No knowledge provider found for player"));
        this.minecraft.getConnection().send(new ServerboundClientCommandPacket(ServerboundClientCommandPacket.Action.REQUEST_STATS));
        this.generateIndexMap();
        this.setCurrentPage(this.knowledge.getLastResearchTopic().getPage());
        this.initPages();
        this.initButtons();
        PacketHandler.sendToServer(new SetResearchTopicHistoryPacket(this.knowledge.getLastResearchTopic(), this.getHistoryView()));
    }
    
    protected void initPages() {
        // Parse grimoire pages based on the current topic
        this.pages.clear();
        AbstractResearchTopic<?> topic = this.knowledge.getLastResearchTopic();
        if (topic instanceof MainIndexResearchTopic) {
            this.parseIndexPages();
        } else if (topic instanceof DisciplineResearchTopic discTopic) {
            this.parseDisciplinePages(discTopic.getDiscipline());
        } else if (topic instanceof EntryResearchTopic entryTopic) {
            this.parseEntryPages(entryTopic.getEntry());
        } else if (topic instanceof SourceResearchTopic sourceTopic) {
            this.parseAttunementPage(sourceTopic.getSource());
        } else if (topic instanceof EnchantmentResearchTopic enchTopic) {
            this.parseRuneEnchantmentPage(enchTopic.getEnchantment());
        } else if (topic instanceof LanguageResearchTopic langTopic) {
            this.parseLinguisticsPage(langTopic.getLanguage());
        } else if (topic instanceof AffinityResearchTopic affinityTopic) {
            this.parseAffinityPage(affinityTopic.getSource());
        } else if (topic instanceof OtherResearchTopic otherTopic) {
            String data = otherTopic.getData();
            if (this.isIndexKey(data)) {
                this.parseRecipeEntryPages(data);
            } else if (StatisticsPage.TOPIC.getData().equals(data)) {
                this.parseStatsPages();
            } else if (AttunementIndexPage.TOPIC.getData().equals(data)) {
                this.parseAttunementIndexPages();
            } else if (RuneEnchantmentIndexPage.TOPIC.getData().equals(data)) {
                this.parseRuneEnchantmentIndexPages();
            } else if (RecipeIndexPage.TOPIC.getData().equals(data)) {
                this.parseRecipeIndexPages();
            } else if (TipsPage.TOPIC.getData().equals(data)) {
                this.parseTipsPages();
            } else if (LinguisticsIndexPage.TOPIC.getData().equals(data)) {
                this.parseLinguisticsIndexPages();
            } else if (AffinityIndexPage.TOPIC.getData().equals(data)) {
                this.parseAffinityIndexPages();
            } else {
                LOGGER.warn("Unexpected OtherResearchTopic data {}", data);
            }
        } else {
            LOGGER.warn("Unexpected research topic type {}", topic.getType().toString());
        }
    }
    
    protected void initButtons() {
        int current = 0;
        this.clearWidgets();
        
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
        this.nextPageButton = new PageButton(this.leftPos + 258, this.topPos + 172, this, true);
        this.prevPageButton = new PageButton(this.leftPos - 12, this.topPos + 172, this, false);
        this.backButton = new BackButton(this.leftPos + 120, this.topPos + 172, this);
        this.addRenderableWidget(this.nextPageButton);
        this.addRenderableWidget(this.prevPageButton);
        this.addRenderableWidget(this.backButton);
        this.addRenderableWidget(new MainIndexButton(this.leftPos + 142, this.topPos + 177, this));
        this.updateNavButtonVisibility();
    }
    
    public <T extends AbstractWidget> T addWidgetToScreen(T widget) {
        return this.addRenderableWidget(widget);
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
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        // Render the grimoire background
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(this.scaledLeft, this.scaledTop, 0.0F);
        guiGraphics.pose().scale(SCALE, SCALE, 1.0F);
        guiGraphics.blit(TEXTURE, 0, 0, 0, 0, BG_WIDTH, BG_HEIGHT);
        guiGraphics.pose().popPose();
        
        // Render the two visible pages
        int current = 0;
        for (AbstractPage page : this.pages) {
            if ((current == this.currentPage || current == this.currentPage + 1) && current < this.pages.size()) {
                page.render(guiGraphics, current % 2, this.leftPos, this.topPos - 10, mouseX, mouseY);
            }
            current++;
            if (current > this.currentPage + 1) {
                break;
            }
        }
    }
    
    @Override
    public void tick() {
        super.tick();
        this.pages.forEach(p -> p.tick());
    }

    private List<ResearchDiscipline> buildDisciplineList() {
        // Gather a list of all research disciplines, sorted by their registration order, that appear in the main index
        return ResearchDisciplines.streamIndexDisciplines(this.minecraft.level.registryAccess()).toList();
    }
    
    private List<ResearchEntry> buildEntryList(ResearchDiscipline discipline) {
        // Gather a list of all research entries for the given discipline, sorted by their display names
        return discipline.getEntryStream(this.minecraft.level.registryAccess())
                .sorted(Comparator.comparing(e -> (Component.translatable(e.getNameTranslationKey())).getString()))
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
            if (discipline.unlockRequirementOpt().isEmpty() || discipline.unlockRequirementOpt().get().isMetBy(Minecraft.getInstance().player)) {
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
    
    protected void parseDisciplinePages(ResearchDisciplineKey disciplineKey) {
        Minecraft mc = this.minecraft;
        RegistryAccess registryAccess = mc.level.registryAccess();
        this.currentStageIndex = 0;
        if (disciplineKey == null) {
            return;
        }
        ResearchDiscipline discipline = ResearchDisciplines.getDiscipline(registryAccess, disciplineKey);
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
        List<ResearchEntry> upcomingList = new ArrayList<>();
        for (ResearchEntry entry : entries) {
            if (entry.isNew(mc.player)) {
                newList.add(entry);
            } else if (entry.isUpdated(mc.player)) {
                updatedList.add(entry);
            } else if (entry.isComplete(mc.player)) {
                completeList.add(entry);
            } else if (entry.isInProgress(mc.player)) {
                inProgressList.add(entry);
            } else if (!entry.flags().hidden() && entry.isAvailable(mc.player)) {
                availableList.add(entry);
            } else if (!entry.flags().hidden() && entry.isUpcoming(mc.player)) {
                upcomingList.add(entry);
            }
        }

        // If no upcoming entries are visible, but the player isn't done with the discipline, add a placeholder
        if (upcomingList.isEmpty()) {
            final List<ResearchEntry> discFinales = new ArrayList<>(discipline.getFinaleEntries(registryAccess));
            if (!entries.stream().filter(e -> !discFinales.contains(e)).allMatch(e -> e.isVisible(mc.player))) {
                final ResearchEntry unknownEntry = ResearchEntries.getEntry(registryAccess, ResearchEntries.UNKNOWN_RESEARCH);
                if (unknownEntry != null) {
                    upcomingList.add(unknownEntry);
                }
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
        if (!upcomingList.isEmpty()) {
            this.parseDisciplinePageSection(upcomingList, "upcoming", discipline, properties);
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
        Component headerText = Component.translatable("grimoire.primalmagick.section_header." + headerName).withStyle(ChatFormatting.UNDERLINE);
        if (properties.heightRemaining < 36 && !properties.page.getContents().isEmpty()) {
            // If there's not room for the spacer, the header, and a first entry, skip to the next page
            properties.heightRemaining = 156;
            this.pages.add(properties.page);
            properties.page = new DisciplinePage(discipline);
            properties.page.addContent(headerText);
        } else {
            if (!properties.firstSection && !properties.page.getContents().isEmpty()) {
                properties.page.addContent(Component.literal(""));
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
            if (properties.heightRemaining <= 12 && !properties.page.getContents().isEmpty()) {
                properties.heightRemaining = 156;
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
        List<FormattedText> parsedText = new ArrayList<>();
        for (String str : firstPassText) {
            parsedText.addAll(this.font.getSplitter().splitLines(FormattedText.of(str), 124, Style.EMPTY));   // list formatted string to width
        }
        
        return new Tuple<>(parsedText.stream().map((t) -> t.getString()).collect(Collectors.toList()), images);
    }
    
    protected void parseEntryPages(ResearchEntryKey entryKey) {
        if (entryKey == null) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        RegistryAccess registryAccess = mc.level.registryAccess();
        ResearchEntry entry = ResearchEntries.getEntry(registryAccess, entryKey);
        if (entry == null || entry.stages().isEmpty()) {
            return;
        }
        
        Player player = this.minecraft.player;
        Level level = this.minecraft.level;
        
        // Determine current research stage
        boolean complete = false;
        this.currentStageIndex = this.knowledge.getResearchStage(entry.key());
        if (this.currentStageIndex >= entry.stages().size()) {
            this.currentStageIndex = entry.stages().size() - 1;
            complete = true;
        }
        if (this.currentStageIndex < 0) {
            this.currentStageIndex = 0;
        }
        ResearchStage stage = entry.stages().get(this.currentStageIndex);
        List<ResearchAddendum> addenda = complete ? entry.addenda() : Collections.emptyList();
        
        String rawText = (Component.translatable(stage.textTranslationKey())).getString();
        
        // Append unlocked addendum text
        int addendumCount = 0;
        for (ResearchAddendum addendum : addenda) {
            if (addendum.completionRequirementOpt().isPresent() && addendum.completionRequirementOpt().get().isMetBy(player)) {
                Component headerText = Component.translatable("grimoire.primalmagick.addendum_header", ++addendumCount);
                Component addendumText = Component.translatable(addendum.textTranslationKey());
                rawText += ("<PAGE>" + headerText.getString() + "<BR>" + addendumText.getString());
            }
        }
        
        // Process text
        int lineHeight = this.font.lineHeight;
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
        if (!tempImages.isEmpty()) {
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
        }

        // If the stage has a CTA link, add it to the bottom of the final stage page, adding a new one if there's no room
        if (stage.ctaLinkOpt().isPresent()) {
            TopicLink link = stage.ctaLinkOpt().get();
            if (heightRemaining < TopicLinkButton.HEIGHT + 2) {
                tempPage = new StagePage(stage);
                tempPage.setTopicLink(link);
                this.pages.add(tempPage);
            } else {
                tempPage.setTopicLink(link);
            }
        }
        
        // Add attunement gain page if applicable
        SourceList attunements = stage.attunements();
        for (ResearchAddendum addendum : addenda) {
            if (addendum.completionRequirementOpt().isEmpty() || addendum.completionRequirementOpt().get().isMetBy(player)) {
                attunements = attunements.merge(addendum.attunements());
            }
        }
        if (!attunements.isEmpty()) {
            this.pages.add(new AttunementGainPage(attunements));
        }
        
        // Add requirements page if applicable
        if (!entry.key().isKnownBy(player) && stage.hasPrerequisites()) {
            this.pages.add(new RequirementsPage(stage));
        }
        
        // Generate recipe pages from stage and addenda
        List<ResourceLocation> locList = new ArrayList<>();
        for (ResourceLocation loc : stage.recipes()) {
            if (!locList.contains(loc)) {
                locList.add(loc);
            }
        }
        for (ResearchAddendum addendum : addenda) {
            if (addendum.completionRequirementOpt().isEmpty() || addendum.completionRequirementOpt().get().isMetBy(player)) {
                for (ResourceLocation loc : addendum.recipes()) {
                    if (!locList.contains(loc)) {
                        locList.add(loc);
                    }
                }
            }
        }
        for (ResourceLocation recipeLoc : locList) {
            Optional<RecipeHolder<?>> opt = level.getRecipeManager().byKey(recipeLoc);
            opt.ifPresent(recipe -> {
                AbstractRecipePage page = RecipePageFactory.createPage(recipe, level.registryAccess());
                if (page != null) {
                    this.pages.add(page);
                }
            });
        }
    }
    
    protected void parseStatsPages() {
        // For each visible statistic, create one or more page strings for that stat's text and value
        this.currentStageIndex = 0;
        List<Stat> stats = StatsManager.getDisplayStats();
        if (stats.isEmpty()) {
            return;
        }
        
        // Add each unlocked research discipline to the current discipline index page
        int heightRemaining = 137;  // Leave enough room for the page header
        int dotWidth = this.font.width(".");
        StatisticsPage tempPage = new StatisticsPage(true);
        Minecraft mc = this.minecraft;
        for (Stat stat : stats) {
            int statValue = StatsManager.getValue(mc.player, stat);
            if (!stat.hidden() || statValue > 0) {
                // Join the stat text and formatted value with periods in between for spacing
                Component statText = Component.translatable(stat.getTranslationKey());
                List<FormattedText> statTextSegments = new ArrayList<>(this.font.getSplitter().splitLines(statText, 124, Style.EMPTY));
                FormattedText lastStatTextSegment = statTextSegments.get(statTextSegments.size() - 1);
                int lastStatTextSegmentWidth = this.font.width(lastStatTextSegment);
                String statFormattedValueStr = stat.formatter().format(statValue);
                int statFormattedValueStrWidth = this.font.width(statFormattedValueStr);
                int remainingWidth = 124 - lastStatTextSegmentWidth - statFormattedValueStrWidth;
                if (remainingWidth < 10) {
                    // If there isn't enough room to put them on the same line, put the last text segment and the formatted value on different lines
                    String joiner1 = String.join("", Collections.nCopies((124 - lastStatTextSegmentWidth) / dotWidth, "."));
                    String joiner2 = String.join("", Collections.nCopies((124 - statFormattedValueStrWidth) / dotWidth, "."));
                    statTextSegments.set(statTextSegments.size() - 1, FormattedText.of(lastStatTextSegment.getString() + joiner1));
                    statTextSegments.add(FormattedText.of(joiner2 + statFormattedValueStr + "~B"));   // Include a section break at the end
                } else {
                    // Otherwise, join them as the last line of the block
                    String joiner = String.join("", Collections.nCopies(remainingWidth / dotWidth, "."));
                    statTextSegments.set(statTextSegments.size() - 1, FormattedText.of(lastStatTextSegment.getString() + joiner + statFormattedValueStr + "~B")); // Include a section break at the end
                }
                
                // Calculate the total height of the stat block, including spacer, and determine if it will fit on the current page
                int totalHeight = (int)(this.font.lineHeight * statTextSegments.size());
                if (heightRemaining < totalHeight && !tempPage.getElements().isEmpty()) {
                    // If there isn't enough room for another stat block, start a new page
                    heightRemaining = 165;
                    this.pages.add(tempPage);
                    tempPage = new StatisticsPage();
                }
                
                // Add the stat block and its section break to the page
                for (FormattedText str : statTextSegments) {
                    tempPage.addElement(new PageString(str.getString()));
                    heightRemaining -= this.font.lineHeight;
                }
                heightRemaining -= (int)(this.font.lineHeight * 0.66D);
            }
        }
        
        // Add the final page to the collection, if it's not empty
        if (!tempPage.getElements().isEmpty()) {
            this.pages.add(tempPage);
        }
    }
    
    protected void parseLinguisticsIndexPages() {
        this.currentStageIndex = 0;
        this.pages.add(new LinguisticsIndexPage(true));
    }
    
    protected void parseLinguisticsPage(ResourceKey<BookLanguage> languageKey) {
        this.currentStageIndex = 0;
        
        // Fetch the language itself
        Holder.Reference<BookLanguage> language = BookLanguagesPM.getLanguageOrDefault(languageKey, this.minecraft.level.registryAccess(), BookLanguagesPM.DEFAULT);

        // Add the first page with just the comprehension and vocabulary trackers
        this.pages.add(new LinguisticsScorePage(language));
        
        // Add subsequent pages with language description
        String rawText = language.value().getDescription().getString();
        
        // Process text
        int lineHeight = this.font.lineHeight;
        Tuple<List<String>, List<PageImage>> parsedData = this.parseText(rawText);
        List<String> parsedText = parsedData.getA();
        List<PageImage> images = parsedData.getB();
        
        // Starting with the second page, so we have more space available
        int heightRemaining = 165;
        
        // Break parsed text into pages
        LinguisticsDescriptionPage tempPage = new LinguisticsDescriptionPage(language);
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
                tempPage = new LinguisticsDescriptionPage(language);
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
                tempPage = new LinguisticsDescriptionPage(language);
            }
        }
        if (!tempPage.getElements().isEmpty()) {
            this.pages.add(tempPage);
        }
        
        // Deal with any remaining images
        tempPage = new LinguisticsDescriptionPage(language);
        heightRemaining = 165;
        while (!tempImages.isEmpty()) {
            if (heightRemaining < (tempImages.get(0).adjustedHeight + 2)) {
                heightRemaining = 165;
                this.pages.add(tempPage);
                tempPage = new LinguisticsDescriptionPage(language);
            } else {
                heightRemaining -= (tempImages.get(0).adjustedHeight + 2);
                tempPage.addElement(tempImages.remove(0));
            }
        }
        if (!tempPage.getElements().isEmpty()) {
            this.pages.add(tempPage);
        }
    }

    protected void parseAffinityIndexPages() {
        this.currentStageIndex = 0;
        this.pages.add(new AffinityIndexPage(true));
    }

    protected void parseAffinityPage(Source source) {
        this.currentStageIndex = 0;

        Minecraft mc = Minecraft.getInstance();
        List<AffinityIndexEntry> entries = this.knowledge.getResearchSet().stream()
                .map(k -> k instanceof ItemScanKey scanKey ? scanKey : null)
                .filter(Objects::nonNull)
                .map(k -> new AffinityIndexEntry(k.getStack(), AffinityManager.getInstance().getAffinityValuesAsync(k.getStack(), mc.level)))
                .toList();
        CompletableFuture<Void> loadedFuture = CompletableFuture.allOf(entries.stream().map(AffinityIndexEntry::affinities).toArray(CompletableFuture[]::new));

        // Wait just the barest beat for any completed futures to register as done so that we don't end up in a refresh loop
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
        }

        if (loadedFuture.isDone()) {
            // Sort the entries by relevant affinity then name
            List<AffinityIndexEntry> sortedEntries = entries.stream().filter(e -> e.affinities().join().getAmount(source) > 0).sorted(
                    Comparator.<AffinityIndexEntry, Integer>comparing(e -> e.affinities().join().getAmount(source)).reversed()
                            .thenComparing(e -> e.stack().getDisplayName().getString())).toList();
            AffinityPage tempPage = new AffinityPage(source, loadedFuture, true);
            final int lineHeight = AffinityRecordWidget.WIDGET_HEIGHT;
            int heightRemaining = 137;  // First page has less available height to account for title
            for (AffinityIndexEntry entry : sortedEntries) {
                tempPage.addElement(entry);
                heightRemaining -= lineHeight;
                if ((heightRemaining < lineHeight) && !tempPage.getElements().isEmpty()) {
                    heightRemaining = 165;
                    this.pages.add(tempPage);
                    tempPage = new AffinityPage(source, loadedFuture, false);
                }
            }
            if (sortedEntries.isEmpty() || !tempPage.getElements().isEmpty()) {
                this.pages.add(tempPage);
            }
        } else {
            // Trigger a page refresh when calculation is complete
            loadedFuture.thenAccept($ -> this.setRefreshing());
            this.pages.add(new AffinityPage(source, loadedFuture, true));
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
        
        ResourceLocation sourceId = source.getId();
        String componentKey = String.join(".", "source", sourceId.getNamespace(), sourceId.getPath(), "attunement", "text");
        String rawText = (Component.translatable(componentKey)).getString();
        
        // Process text
        int lineHeight = this.font.lineHeight;
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
    
    protected void parseRuneEnchantmentPage(Holder<Enchantment> enchant) {
        Minecraft mc = Minecraft.getInstance();
        ResourceLocation enchantKey = enchant.unwrapKey().get().location();
        String rawText = ResearchManager.isResearchComplete(mc.player, new RuneEnchantmentKey(enchant)) ?
                (Component.translatable(String.join(".", "enchantment", enchantKey.getNamespace(), enchantKey.getPath(), "rune_enchantment", "text"))).getString() :
                (Component.translatable(String.join(".", "enchantment", enchantKey.getNamespace(), enchantKey.getPath(), "rune_enchantment", "partial_text"))).getString();
        
        // Process text
        int lineHeight = this.font.lineHeight;
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

        for (Holder<Enchantment> enchant : RuneManager.getRuneEnchantmentsSorted(mc.level.registryAccess())) {
            List<AbstractResearchKey<?>> researchKeys = List.of(
                    new RuneEnchantmentKey(enchant), 
                    new RuneEnchantmentPartialKey(enchant, RuneType.VERB),
                    new RuneEnchantmentPartialKey(enchant, RuneType.NOUN),
                    new RuneEnchantmentPartialKey(enchant, RuneType.SOURCE));
            if (researchKeys.stream().anyMatch(key -> ResearchManager.isResearchComplete(mc.player, key))) {
                tempPage.addEnchantment(enchant);
                heightRemaining -= 12;
                if (heightRemaining < 12 && !tempPage.getEnchantments().isEmpty()) {
                    heightRemaining = 156;
                    this.pages.add(tempPage);
                    tempPage = new RuneEnchantmentIndexPage();
                }
            }
        }
        if (!tempPage.getEnchantments().isEmpty()) {
            this.pages.add(tempPage);
        }
    }
    
    protected void generateIndexMap() {
        Minecraft mc = this.minecraft;
        Comparator<RecipeHolder<?>> displayNameComparator = Comparator.comparing(r -> r.value().getResultItem(mc.level.registryAccess()).getHoverName().getString());
        Comparator<RecipeHolder<?>> recipeIdComparator = Comparator.comparing(r -> r.id());
        List<RecipeHolder<?>> processedRecipes = mc.level.getRecipeManager().getRecipes().stream().filter(GrimoireScreen::isValidRecipeIndexEntry)
                .sorted(displayNameComparator.thenComparing(recipeIdComparator)).collect(Collectors.toList());

        this.indexMap = new TreeMap<>();
        for (RecipeHolder<?> recipe : processedRecipes) {
            String recipeName = recipe.value().getResultItem(mc.level.registryAccess()).getHoverName().getString();
            if (!this.indexMap.containsKey(recipeName)) {
                this.indexMap.put(recipeName, new ArrayList<>());
            }
            this.indexMap.get(recipeName).add(recipe);
        }
    }
    
    public boolean isIndexKey(String name) {
        return this.indexMap.containsKey(name);
    }
    
    public void checkRecipeSearchStringUpdate(String newString) {
        if (!newString.equals(this.lastRecipeSearch.orElse(""))) {
            this.lastRecipeSearch = Optional.ofNullable(newString);
            this.initPages();
            this.initButtons();
        }
    }
    
    protected void parseRecipeIndexPages() {
        this.currentStageIndex = 0;
        
        Minecraft mc = this.minecraft;
        int heightRemaining = 113;
        RecipeIndexPage tempPage = new RecipeIndexPage(true, this.lastRecipeSearch);
        
        for (String recipeName : this.indexMap.navigableKeySet()) {
            if (recipeName.toLowerCase(Locale.ROOT).contains(this.lastRecipeSearch.orElse("").toLowerCase(Locale.ROOT))) {
                tempPage.addContent(recipeName, this.indexMap.get(recipeName).get(0).value().getResultItem(mc.level.registryAccess()));
                heightRemaining -= 12;
                if (heightRemaining < 12 && !tempPage.getContents().isEmpty()) {
                    heightRemaining = 156;
                    this.pages.add(tempPage);
                    tempPage = new RecipeIndexPage();
                }
            }
        }
        if (!tempPage.getContents().isEmpty() || (tempPage.getContents().isEmpty() && tempPage.isFirstPage())) {
            this.pages.add(tempPage);
        }
        if (this.pages.isEmpty()) {
            LOGGER.warn("Finished parsing recipe index pages without adding any!");
        }
    }
    
    protected static boolean isValidRecipeIndexEntry(RecipeHolder<?> recipe) {
        Minecraft mc = Minecraft.getInstance();
        RegistryAccess registryAccess = mc.level.registryAccess();
        if (!recipe.id().getNamespace().equals(Constants.MOD_ID) || recipe.value().getResultItem(registryAccess).isEmpty()) {
            return false;
        }
        if (recipe.value() instanceof IHasRequirement hrr) {
            return hrr.getRequirement().isEmpty() || hrr.getRequirement().get().isMetBy(mc.player);
        } else {
            return ResearchManager.isRecipeVisible(recipe.id(), mc.player);
        }
    }
    
    protected void parseRecipeEntryPages(String recipeName) {
        Minecraft mc = Minecraft.getInstance();
        this.currentStageIndex = 0;
        boolean firstPage = true;
        List<RecipeHolder<?>> recipes = this.indexMap.getOrDefault(recipeName, Collections.emptyList());
        for (RecipeHolder<?> recipe : recipes) {
            AbstractRecipePage page = RecipePageFactory.createPage(recipe, mc.level.registryAccess());
            if (page != null) {
                this.pages.add(new RecipeMetadataPage(recipe, mc.level.registryAccess(), firstPage));
                this.pages.add(page);
                firstPage = false;
            }
        }
    }
    
    protected Component getCurrentTip() {
        if (this.cachedTip == null) {
            Minecraft mc = Minecraft.getInstance();
            this.cachedTip = TipDefinitionsPM.getRandom(mc.player, mc.player.getRandom()).map(TipDefinition::getText).orElse(Component.empty());
        }
        return this.cachedTip;
    }
    
    public void invalidateCurrentTip() {
        this.cachedTip = null;
    }
    
    protected void parseTipsPages() {
        String rawText = StringDecomposer.getPlainText(this.getCurrentTip());
        
        // Process text
        int lineHeight = this.font.lineHeight;
        Tuple<List<String>, List<PageImage>> parsedData = this.parseText(rawText);
        List<String> parsedText = parsedData.getA();
        List<PageImage> images = parsedData.getB();
        
        // First page has less available space to account for title
        int heightRemaining = 137;

        // Break parsed text into pages
        TipsPage tempPage = new TipsPage(true);
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
                tempPage = new TipsPage();
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
                tempPage = new TipsPage();
            }
        }
        if (!tempPage.getElements().isEmpty()) {
            this.pages.add(tempPage);
        }
        
        // Deal with any remaining images
        tempPage = new TipsPage();
        heightRemaining = 165;
        while (!tempImages.isEmpty()) {
            if (heightRemaining < (tempImages.get(0).adjustedHeight + 2)) {
                heightRemaining = 165;
                this.pages.add(tempPage);
                tempPage = new TipsPage();
            } else {
                heightRemaining -= (tempImages.get(0).adjustedHeight + 2);
                tempPage.addElement(tempImages.remove(0));
            }
        }
        if (!tempPage.getElements().isEmpty()) {
            this.pages.add(tempPage);
        }
        
        // Flag the last page as such to show the refresh button
        TipsPage lastPage = ((TipsPage)this.pages.get(this.pages.size() - 1));
        int contentHeight = lastPage.getElements().stream().mapToInt(IPageElement::getHeight).sum();
        if (contentHeight > (lastPage.isFirstPage() ? 112 : 140)) {
            // If the last page's contents would overlap the next tip button, add an extra page
            lastPage = new TipsPage();
            this.pages.add(lastPage);
        }
        lastPage.setLastPage(true);
    }
    
    public int getCurrentPage() {
        return this.currentPage;
    }
    
    public void setCurrentPage(int newPage) {
        this.currentPage = newPage;
        this.lastStageIndex = this.currentStageIndex;   // Prevent current page from resetting when transitioning to a recipe entry topic
    }
    
    public boolean nextPage() {
        if (this.currentPage < this.pages.size() - 2) {
            this.currentPage += 2;
            this.initButtons();
            return true;
        }
        return false;
    }
    
    public boolean prevPage() {
        if (this.currentPage >= 2) {
            this.currentPage -= 2;
            this.initButtons();
            return true;
        }
        return false;
    }
    
    public boolean goBack() {
        // Pop the last viewed topic off the history stack and open a new screen for it
        if (!this.knowledge.getResearchTopicHistory().isEmpty()) {
            AbstractResearchTopic<?> lastTopic = this.knowledge.getResearchTopicHistory().pop();
            this.knowledge.setLastResearchTopic(lastTopic);
            this.minecraft.setScreen(new GrimoireScreen());
            return true;
        }
        return false;
    }

    public Optional<AbstractResearchTopic<?>> getPreviousTopic() {
        return Optional.ofNullable(this.knowledge.getResearchTopicHistory().peek());
    }
    
    public void gotoTopic(AbstractResearchTopic<?> newTopic) {
        this.gotoTopic(newTopic, true);
    }
    
    public void gotoTopic(AbstractResearchTopic<?> newTopic, boolean allowRepeatInHistory) {
        if (allowRepeatInHistory || !this.knowledge.getLastResearchTopic().equals(newTopic)) {
            this.pushCurrentHistoryTopic();
        }
        this.setTopic(newTopic);
        this.minecraft.setScreen(new GrimoireScreen());
    }
    
    protected void updateNavButtonVisibility() {
        this.prevPageButton.visible = (this.currentPage >= 2);
        this.nextPageButton.visible = (this.currentPage < this.pages.size() - 2);
        this.backButton.visible = !this.knowledge.getResearchTopicHistory().isEmpty();
    }
    
    @Override
    public boolean charTyped(char pCodePoint, int pModifiers) {
        for (AbstractPage page : this.pages) {
            if (page.charTyped(pCodePoint, pModifiers)) {
                return true;
            }
        }
        return super.charTyped(pCodePoint, pModifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (AbstractPage page : this.pages) {
            if (page.keyPressed(keyCode, scanCode, modifiers)) {
                return true;
            }
        }
        
        if (keyCode == Services.INPUT.getKey(KeyBindings.GRIMOIRE_PREV_TOPIC).getValue()) {
            if (this.goBack()) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundsPM.PAGE.get(), 1.0F, 1.0F));
                return true;
            }
        } else if (keyCode == Services.INPUT.getKey(KeyBindings.GRIMOIRE_PREV_PAGE).getValue()) {
            if (this.prevPage()) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundsPM.PAGE.get(), 1.0F, 1.0F));
                return true;
            }
        } else if (keyCode == Services.INPUT.getKey(KeyBindings.GRIMOIRE_NEXT_PAGE).getValue()) {
            if (this.nextPage()) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundsPM.PAGE.get(), 1.0F, 1.0F));
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    
    @Override
    public boolean mouseClicked(double xPos, double yPos, int keyCode) {
        boolean retVal = super.mouseClicked(xPos, yPos, keyCode);
        if (keyCode == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            if (this.goBack()) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundsPM.PAGE.get(), 1.0F, 1.0F));
            }
        } else if (keyCode == GLFW.GLFW_MOUSE_BUTTON_4) {
            if (this.prevPage()) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundsPM.PAGE.get(), 1.0F, 1.0F));
            }
        } else if (keyCode == GLFW.GLFW_MOUSE_BUTTON_5) {
            if (this.nextPage()) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundsPM.PAGE.get(), 1.0F, 1.0F));
            }
        } else {
            for (AbstractPage page : this.pages) {
                if (page.mouseClicked(xPos, yPos, keyCode)) {
                    this.setFocused(page);
                    return true;
                }
            }
            this.setFocused(null);
        }
        return retVal;
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pScrollX, double pScrollY) {
        if (pScrollY > 0) {
            if (this.prevPage()) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundsPM.PAGE.get(), 1.0F, 1.0F));
            }
        } else if (pScrollY < 0) {
            if (this.nextPage()) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundsPM.PAGE.get(), 1.0F, 1.0F));
            }
        }

        return true;
    }

    public void pushCurrentHistoryTopic() {
        this.knowledge.getResearchTopicHistory().push(this.knowledge.getLastResearchTopic().withPage(this.getCurrentPage()));
    }
    
    public void setTopic(AbstractResearchTopic<?> newTopic) {
        this.knowledge.setLastResearchTopic(newTopic);
    }
    
    public List<AbstractResearchTopic<?>> getHistoryView() {
        return this.knowledge.getResearchTopicHistory().subList(0, Math.min(this.knowledge.getResearchTopicHistory().size(), HISTORY_LIMIT));
    }
    
    protected static class DisciplinePageProperties {
        public DisciplinePage page;
        public int heightRemaining;
        public boolean firstSection;
    }
}
