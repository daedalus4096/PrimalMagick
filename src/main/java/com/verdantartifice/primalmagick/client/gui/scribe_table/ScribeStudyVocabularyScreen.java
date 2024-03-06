package com.verdantartifice.primalmagick.client.gui.scribe_table;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.ScribeTableMode;
import com.verdantartifice.primalmagick.common.menus.ScribeStudyVocabularyMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EnchantmentNames;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringDecomposer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

/**
 * GUI screen for the study vocabulary mode of the scribe table block.
 * 
 * @author Daedalus4096
 */
public class ScribeStudyVocabularyScreen extends AbstractScribeTableScreen<ScribeStudyVocabularyMenu> {
    private static final ResourceLocation[] ENABLED_LEVEL_SPRITES = new ResourceLocation[]{PrimalMagick.resource("scribe_table/level_1"), PrimalMagick.resource("scribe_table/level_2"), PrimalMagick.resource("scribe_table/level_3")};
    private static final ResourceLocation[] DISABLED_LEVEL_SPRITES = new ResourceLocation[]{PrimalMagick.resource("scribe_table/level_1_disabled"), PrimalMagick.resource("scribe_table/level_2_disabled"), PrimalMagick.resource("scribe_table/level_3_disabled")};
    private static final ResourceLocation SLOT_DISABLED_SPRITE = PrimalMagick.resource("scribe_table/slot_disabled");
    private static final ResourceLocation SLOT_HIGHLIGHTED_SPRITE = PrimalMagick.resource("scribe_table/slot_highlighted");
    private static final ResourceLocation SLOT_SPRITE = PrimalMagick.resource("scribe_table/slot");
    private static final ResourceLocation TEXTURE = PrimalMagick.resource("textures/gui/scribe_study_vocabulary.png");
    /** The ResourceLocation containing the texture for the Book rendered above the enchantment table */
    private static final ResourceLocation ENCHANTING_BOOK_LOCATION = new ResourceLocation("textures/entity/enchanting_table_book.png");

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

    public ScribeStudyVocabularyScreen(ScribeStudyVocabularyMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
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
        this.bookModel = new BookModel(this.minecraft.getEntityModels().bakeLayer(ModelLayers.BOOK));
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.tickBook();
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        super.renderBg(pGuiGraphics, pPartialTick, pMouseX, pMouseY);
        this.renderBook(pGuiGraphics, this.leftPos, this.topPos, pPartialTick);
        EnchantmentNames.getInstance().initSeed((long)this.menu.getNameSeed());
        BookLanguage activeLanguage = this.menu.getBookLanguage();
        
        for (int slotIndex = 0; slotIndex < 3; slotIndex++) {
            int slotLeft = this.leftPos + 60;
            int slotTextStart = slotLeft + 20;
            int slotTop = this.topPos + 14 + (19 * slotIndex);
            int cost = this.menu.costs[slotIndex];
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
                FormattedText formattedText = this.font.getSplitter().headByWidth(Component.literal(rawText).withStyle(activeLanguage.style()), textWidth, Style.EMPTY);
                if (!this.minecraft.player.getAbilities().instabuild && this.minecraft.player.experienceLevel < cost) {
                    pGuiGraphics.blitSprite(SLOT_DISABLED_SPRITE, slotLeft, slotTop, 108, 19);
                    pGuiGraphics.blitSprite(DISABLED_LEVEL_SPRITES[slotIndex], slotLeft + 1, slotTop + 1, 16, 16);
                    pGuiGraphics.drawWordWrap(this.font, formattedText, slotTextStart, slotTop + 2, textWidth, (textColor & 16711422) >> 1);
                } else {
                    int dx = pMouseX - slotLeft;
                    int dy = pMouseY - slotTop;
                    if (dx >= 0 && dy >= 0 && dx < 108 && dy < 19) {
                        pGuiGraphics.blitSprite(SLOT_HIGHLIGHTED_SPRITE, slotLeft, slotTop, 108, 19);
                        textColor = 16777088;
                    } else {
                        pGuiGraphics.blitSprite(SLOT_SPRITE, slotLeft, slotTop, 108, 19);
                    }
                    pGuiGraphics.blitSprite(ENABLED_LEVEL_SPRITES[slotIndex], slotLeft + 1, slotTop + 1, 16, 16);
                    pGuiGraphics.drawWordWrap(this.font, formattedText, slotTextStart, slotTop + 2, textWidth, textColor);
                }
            }
        }
    }

    private void renderBook(GuiGraphics pGuiGraphics, int pX, int pY, float pPartialTick) {
        float f = Mth.lerp(pPartialTick, this.oOpen, this.open);
        float f1 = Mth.lerp(pPartialTick, this.oFlip, this.flip);
        Lighting.setupForEntityInInventory();
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate((float)pX + 33.0F, (float)pY + 31.0F, 100.0F);
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
        this.bookModel.renderToBuffer(pGuiGraphics.pose(), vertexconsumer, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
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
