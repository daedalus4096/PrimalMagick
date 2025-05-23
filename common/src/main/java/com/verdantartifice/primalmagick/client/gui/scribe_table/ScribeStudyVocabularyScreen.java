package com.verdantartifice.primalmagick.client.gui.scribe_table;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.client.gui.widgets.VocabularyWidget;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.LinguisticsManager;
import com.verdantartifice.primalmagick.common.books.ScribeTableMode;
import com.verdantartifice.primalmagick.common.menus.ScribeStudyVocabularyMenu;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.scribe_table.StudyVocabularyActionPacket;
import com.verdantartifice.primalmagick.common.tags.BookLanguageTagsPM;
import com.verdantartifice.primalmagick.common.util.PlayerUtils;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EnchantmentNames;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringDecomposer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * GUI screen for the study vocabulary mode of the scribe table block.
 * 
 * @author Daedalus4096
 */
public class ScribeStudyVocabularyScreen extends AbstractScribeTableScreen<ScribeStudyVocabularyMenu> {
    private static final ResourceLocation[] ENABLED_LEVEL_SPRITES = IntStream.rangeClosed(1, 10).mapToObj("scribe_table/level_%d"::formatted).map(ResourceUtils::loc).toArray(ResourceLocation[]::new);
    private static final ResourceLocation[] DISABLED_LEVEL_SPRITES = IntStream.rangeClosed(1, 10).mapToObj("scribe_table/level_%d_disabled"::formatted).map(ResourceUtils::loc).toArray(ResourceLocation[]::new);
    private static final ResourceLocation SLOT_DISABLED_SPRITE = ResourceUtils.loc("scribe_table/slot_disabled");
    private static final ResourceLocation SLOT_HIGHLIGHTED_SPRITE = ResourceUtils.loc("scribe_table/slot_highlighted");
    private static final ResourceLocation SLOT_SPRITE = ResourceUtils.loc("scribe_table/slot");
    private static final ResourceLocation TEXTURE = ResourceUtils.loc("textures/gui/scribe_study_vocabulary.png");
    /** The ResourceLocation containing the texture for the Book rendered above the enchantment table */
    private static final ResourceLocation ENCHANTING_BOOK_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/enchanting_table_book.png");

    private final RandomSource random = RandomSource.create();
    private BookModel bookModel;
    public int time;
    public float flip;
    public float oFlip;
    public float flipT;
    public float flipA;
    public float open;
    public float oOpen;
    private ItemStack last = ItemStack.EMPTY;
    protected VocabularyWidget vocabularyWidget;

    public ScribeStudyVocabularyScreen(ScribeStudyVocabularyMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 222;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected ScribeTableMode getMode() {
        return ScribeTableMode.STUDY_VOCABULARY;
    }

    @Override
    protected ResourceLocation getBgTexture() {
        return TEXTURE;
    }

    @Override
    protected void init() {
        super.init();
        Holder.Reference<BookLanguage> lang = this.menu.getBookLanguage();
        this.vocabularyWidget = this.addRenderableWidget(new VocabularyWidget(lang, this.menu.getVocabularyCount(), this.leftPos + 35, this.topPos + 75));
        this.bookModel = new BookModel(this.minecraft.getEntityModels().bakeLayer(ModelLayers.BOOK));
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.tickBook();
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        int slotLeft = this.leftPos + 60;
        for (int slotIndex = 0; slotIndex < 3; slotIndex++) {
            int slotTop = this.topPos + 42 + (19 * slotIndex);
            double dx = pMouseX - (double)slotLeft;
            double dy = pMouseY - (double)slotTop;
            if (dx >= 0 && dy >= 0 && dx < 108 && dy < 19 && this.menu.checkStudyClick(this.minecraft.player, slotIndex)) {
                PacketHandler.sendToServer(new StudyVocabularyActionPacket(this.menu.containerId, slotIndex));
                return true;
            }
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        super.renderBg(pGuiGraphics, pPartialTick, pMouseX, pMouseY);
        this.renderBook(pGuiGraphics, this.leftPos, this.topPos, pPartialTick);
        EnchantmentNames.getInstance().initSeed(this.menu.getNameSeed());
        Holder.Reference<BookLanguage> activeLanguage = this.menu.getBookLanguage();
        
        // Update the vocabulary widget based on the current language in the menu
        this.vocabularyWidget.visible = activeLanguage.is(BookLanguageTagsPM.ANCIENT);
        this.vocabularyWidget.setLanguage(activeLanguage);
        this.vocabularyWidget.setAmount(this.menu.getVocabularyCount());
        
        int slotLeft = this.leftPos + 60;
        int slotTextStart = slotLeft + 20;
        int hoveredSlotIndex = Integer.MIN_VALUE;
        
        // First, determine which, if any, slot is being hovered over
        for (int slotIndex = 0; slotIndex < 3; slotIndex++) {
            int slotTop = this.topPos + 42 + (19 * slotIndex);
            int dx = pMouseX - slotLeft;
            int dy = pMouseY - slotTop;
            if (dx >= 0 && dy >= 0 && dx < 108 && dy < 19) {
                hoveredSlotIndex = slotIndex;
                break;
            }
        }

        // Then render the slots: background, sprites, and text
        for (int slotIndex = 0; slotIndex < 3; slotIndex++) {
            int slotTop = this.topPos + 42 + (19 * slotIndex);
            int cost = this.menu.costs[slotIndex];
            int minLevels = this.menu.minLevels[slotIndex];
            int textColor = 6839882;
            int textWidth = 86;
            if (cost <= 0) {
                pGuiGraphics.blitSprite(SLOT_DISABLED_SPRITE, slotLeft, slotTop, 108, 19);
                if (cost < 0) {
                    // The drawCenteredString method doesn't have an option to omit the drop shadow, alas, so we do it manually
                    Component text = Component.translatable("tooltip.primalmagick.scribe_table.button.study_vocabulary.already_studied");
                    pGuiGraphics.drawString(this.font, text, slotLeft + 54 - this.font.width(text) / 2, slotTop + 5, (textColor & 16711422) >> 1, false);
                }
            } else {
                String rawText = StringDecomposer.getPlainText(EnchantmentNames.getInstance().getRandomName(this.font, textWidth));
                FormattedText formattedText = this.font.getSplitter().headByWidth(Component.literal(rawText).withStyle(activeLanguage.value().style()), textWidth, Style.EMPTY);
                int levelCount = this.menu.levelCostClues[slotIndex];
                if (!this.minecraft.player.getAbilities().instabuild && (this.minecraft.player.experienceLevel < minLevels || !PlayerUtils.canAffordXp(this.minecraft.player, cost))) {
                    pGuiGraphics.blitSprite(SLOT_DISABLED_SPRITE, slotLeft, slotTop, 108, 19);
                    if (levelCount > 0 && levelCount <= DISABLED_LEVEL_SPRITES.length) {
                        pGuiGraphics.blitSprite(DISABLED_LEVEL_SPRITES[levelCount - 1], slotLeft + 1, slotTop + 1, 16, 16);
                    }
                    pGuiGraphics.drawWordWrap(this.font, formattedText, slotTextStart, slotTop + 2, textWidth, (textColor & 16711422) >> 1);
                } else {
                    // Highlight all non-disabled slots up to and including the hovered slot
                    if (slotIndex <= hoveredSlotIndex) {
                        pGuiGraphics.blitSprite(SLOT_HIGHLIGHTED_SPRITE, slotLeft, slotTop, 108, 19);
                        textColor = 16777088;
                    } else {
                        pGuiGraphics.blitSprite(SLOT_SPRITE, slotLeft, slotTop, 108, 19);
                    }

                    // Render a level cost indicator for the number of full or partial levels that will be deducted
                    if (levelCount > 0 && levelCount <= ENABLED_LEVEL_SPRITES.length) {
                        pGuiGraphics.blitSprite(ENABLED_LEVEL_SPRITES[levelCount - 1], slotLeft + 1, slotTop + 1, 16, 16);
                    }

                    pGuiGraphics.drawWordWrap(this.font, formattedText, slotTextStart, slotTop + 2, textWidth, textColor);
                    textColor = 8453920;
                }

                // Draw the total number of levels required to choose the option
                if (minLevels > 0) {
                    String costStr = "" + minLevels;
                    pGuiGraphics.drawString(this.font, costStr, slotTextStart + 86 - this.font.width(costStr), slotTop + 9, textColor);
                }
            }
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        for (int slotIndex = 0; slotIndex < 3; slotIndex++) {
            int cost = this.menu.costs[slotIndex];
            int minLevels = this.menu.minLevels[slotIndex];
            if (this.isHovering(60, 42 + 19 * slotIndex, 108, 17, (double)pMouseX, (double)pMouseY) && cost > 0) {
                // Determine how many vocabulary levels are awarded by the slot, if any
                int studyDelta = 0;
                for (int costIndex = 0; costIndex <= slotIndex && costIndex < this.menu.costs.length; costIndex++) {
                    if (this.menu.costs[costIndex] > 0) {
                        studyDelta++;
                    }
                }

                if (studyDelta > 0) {
                    Holder.Reference<BookLanguage> activeLanguage = this.menu.getBookLanguage();
                    List<Component> tooltips = new ArrayList<>();
                    
                    // Add the vocabulary gain tooltip line to the output
                    tooltips.add(Component.translatable("tooltip.primalmagick.scribe_table.button.study_vocabulary.study_count", activeLanguage.value().getName(), studyDelta).withStyle(ChatFormatting.WHITE));
                    
                    // Only process experience level costs if not in creative mode
                    if (!this.minecraft.player.getAbilities().instabuild) {
                        tooltips.add(CommonComponents.EMPTY);

                        // Add the experience cost tooltip line to the output
                        ChatFormatting costColor = PlayerUtils.canAffordXp(this.minecraft.player, cost) ? ChatFormatting.GRAY : ChatFormatting.RED;
                        MutableComponent costLine = cost == 1 ? Component.translatable("tooltip.primalmagick.experience.one") : Component.translatable("tooltip.primalmagick.experience.many", cost);
                        tooltips.add(Component.translatable("tooltip.primalmagick.scribe_table.button.study_vocabulary.experience_cost", costLine).withStyle(costColor));

                        // Add the level requirement tooltip line to the output
                        if (minLevels > 0) {
                            ChatFormatting levelColor = this.minecraft.player.experienceLevel < minLevels ? ChatFormatting.RED : ChatFormatting.GRAY;
                            tooltips.add(Component.translatable("container.enchant.level.requirement", minLevels).withStyle(levelColor));
                        }
                    }
                    
                    // Show a warning if the player is trying to study vocabulary they don't need
                    int totalNeeded = LinguisticsManager.getTotalRemainingVocabularyRequired(this.minecraft.player, activeLanguage);
                    int currentVocab = LinguisticsManager.getVocabulary(this.minecraft.player, activeLanguage);
                    if (currentVocab + studyDelta > totalNeeded) {
                        tooltips.add(CommonComponents.EMPTY);
                        tooltips.add(Component.translatable("tooltip.primalmagick.scribe_table.button.study_vocabulary.no_more_needed").withStyle(ChatFormatting.RED));
                    }
                    
                    // Render the output tooltip lines
                    pGuiGraphics.renderComponentTooltip(this.font, tooltips, pMouseX, pMouseY);
                    break;
                }
            }
        }
    }

    private void renderBook(GuiGraphics pGuiGraphics, int pX, int pY, float pPartialTick) {
        float f = Mth.lerp(pPartialTick, this.oOpen, this.open);
        float f1 = Mth.lerp(pPartialTick, this.oFlip, this.flip);
        Lighting.setupForEntityInInventory();
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate((float)pX + 33.0F, (float)pY + 59.0F, 100.0F);
        float f2 = 40.0F;
        pGuiGraphics.pose().scale(-f2, f2, f2);
        pGuiGraphics.pose().mulPose(Axis.XP.rotationDegrees(25.0F));
        pGuiGraphics.pose().translate((1.0F - f) * 0.2F, (1.0F - f) * 0.1F, (1.0F - f) * 0.25F);
        float f3 = -(1.0F - f) * 90.0F - 90.0F;
        pGuiGraphics.pose().mulPose(Axis.YP.rotationDegrees(f3));
        pGuiGraphics.pose().mulPose(Axis.XP.rotationDegrees(180.0F));
        float f4 = Mth.clamp(Mth.frac(f1 + 0.25F) * 1.6F - 0.3F, 0.0F, 1.0F);
        float f5 = Mth.clamp(Mth.frac(f1 + 0.75F) * 1.6F - 0.3F, 0.0F, 1.0F);
        this.bookModel.setupAnim(0.0F, f4, f5, f);
        VertexConsumer vertexconsumer = pGuiGraphics.bufferSource().getBuffer(this.bookModel.renderType(ENCHANTING_BOOK_LOCATION));
        this.bookModel.renderToBuffer(pGuiGraphics.pose(), vertexconsumer, 15728880, OverlayTexture.NO_OVERLAY, -1);
        pGuiGraphics.flush();
        pGuiGraphics.pose().popPose();
        Lighting.setupFor3DItems();
    }
    
    public void tickBook() {
        ItemStack itemstack = this.menu.getSlot(0).getItem();
        if (!ItemStack.matches(itemstack, this.last)) {
            this.last = itemstack;

            do {
                this.flipT += (float)(this.random.nextInt(4) - this.random.nextInt(4));
            } while(this.flip <= this.flipT + 1.0F && this.flip >= this.flipT - 1.0F);
        }

        ++this.time;
        this.oFlip = this.flip;
        this.oOpen = this.open;
        boolean flag = false;

        for (int i = 0; i < 3; ++i) {
            if (this.menu.costs[i] != 0) {
                flag = true;
            }
        }

        if (flag) {
            this.open += 0.2F;
        } else {
            this.open -= 0.2F;
        }

        this.open = Mth.clamp(this.open, 0.0F, 1.0F);
        float f1 = (this.flipT - this.flip) * 0.4F;
        float f = 0.2F;
        f1 = Mth.clamp(f1, -f, f);
        this.flipA += (f1 - this.flipA) * 0.9F;
        this.flip += this.flipA;
    }
}
