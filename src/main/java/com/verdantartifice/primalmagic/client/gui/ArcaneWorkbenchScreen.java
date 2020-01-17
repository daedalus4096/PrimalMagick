package com.verdantartifice.primalmagic.client.gui;

import java.awt.Color;

import com.mojang.blaze3d.platform.GlStateManager;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.gui.widgets.ManaCostWidget;
import com.verdantartifice.primalmagic.common.containers.ArcaneWorkbenchContainer;
import com.verdantartifice.primalmagic.common.crafting.IArcaneRecipe;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * GUI screen for arcane workbench block.
 * 
 * @author Michael Bunting
 */
@OnlyIn(Dist.CLIENT)
public class ArcaneWorkbenchScreen extends ContainerScreen<ArcaneWorkbenchContainer> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/arcane_workbench.png");

    public ArcaneWorkbenchScreen(ArcaneWorkbenchContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.ySize = 183;
    }
    
    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.initWidgets();
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        this.blit(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        // Generate text in the case that the current recipe, or lack there of, does not have a mana cost
        IArcaneRecipe activeArcaneRecipe = this.container.getActiveArcaneRecipe();
        if (activeArcaneRecipe == null || activeArcaneRecipe.getManaCosts() == null || activeArcaneRecipe.getManaCosts().isEmpty()) {
            ITextComponent text = new TranslationTextComponent("primalmagic.crafting.no_mana");
            int width = this.font.getStringWidth(text.getFormattedText());
            int x = 1 + (this.getXSize() - width) / 2;
            int y = 10 + (16 - this.font.FONT_HEIGHT) / 2;
            this.font.drawString(text.getFormattedText(), x, y, Color.BLACK.getRGB());
        }
    }

    protected void initWidgets() {
        this.buttons.clear();
        this.children.clear();
        
        // Show mana cost widgets, if the active recipe has a mana cost
        IArcaneRecipe activeArcaneRecipe = this.container.getActiveArcaneRecipe();
        if (activeArcaneRecipe != null) {
            SourceList manaCosts = activeArcaneRecipe.getManaCosts();
            if (!manaCosts.isEmpty()) {
                int widgetSetWidth = manaCosts.getSourcesSorted().size() * 18;
                int x = this.guiLeft + 1 + (this.getXSize() - widgetSetWidth) / 2;
                int y = this.guiTop + 10;
                for (Source source : manaCosts.getSourcesSorted()) {
                    this.addButton(new ManaCostWidget(source, manaCosts.getAmount(source), x, y));
                    x += 18;
                }
            }
        }
    }
}
