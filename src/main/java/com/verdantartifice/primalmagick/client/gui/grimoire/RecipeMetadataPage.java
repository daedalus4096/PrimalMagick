package com.verdantartifice.primalmagick.client.gui.grimoire;

import java.awt.Color;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.DisciplineButton;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.EntryButton;
import com.verdantartifice.primalmagick.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagick.common.research.ResearchDisciplines;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.ResearchManager;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.crafting.Recipe;

/**
 * GUI page to show recipe metadata in the grimoire.
 * 
 * @author Daedalus4096
 */
public class RecipeMetadataPage extends AbstractPage {
    protected final Recipe<?> recipe;
    protected final boolean firstPage;
    
    public RecipeMetadataPage(Recipe<?> recipe) {
        this(recipe, false);
    }
    
    public RecipeMetadataPage(Recipe<?> recipe, boolean firstPage) {
        this.recipe = recipe;
        this.firstPage = firstPage;
    }
    
    public boolean isFirstPage() {
        return this.firstPage;
    }
    
    @Override
    protected boolean renderTopTitleBar() {
        return false;
    }

    @Override
    public void render(PoseStack matrixStack, int side, int x, int y, int mouseX, int mouseY) {
        // Draw title
        this.renderTitle(matrixStack, side, x, y, mouseX, mouseY, null);
        y += 53;
        
        Minecraft mc = Minecraft.getInstance();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        
        ResearchEntry entry = ResearchManager.getEntryForRecipe(this.recipe.getId());
        Component noneComponent = new TranslatableComponent("tooltip.primalmagick.none");

        // Render the metadata's discipline header
        mc.font.draw(matrixStack, new TranslatableComponent("primalmagick.grimoire.recipe_metadata.discipline").withStyle(ChatFormatting.UNDERLINE), x - 3 + (side * 138), y - 6, Color.BLACK.getRGB());
        y += mc.font.lineHeight;
        
        // Render a label if the recipe has no associated research discipline
        if (entry == null) {
            mc.font.draw(matrixStack, noneComponent, x - 3 + (side * 138), y - 4, Color.BLACK.getRGB());
        }
        y += 2 * mc.font.lineHeight;
        
        // Render the metadata's entry header
        mc.font.draw(matrixStack, new TranslatableComponent("primalmagick.grimoire.recipe_metadata.entry").withStyle(ChatFormatting.UNDERLINE), x - 3 + (side * 138), y - 6, Color.BLACK.getRGB());
        y += mc.font.lineHeight;
        
        // Render a label if the recipe has no associated research entry
        if (entry == null) {
            mc.font.draw(matrixStack, noneComponent, x - 3 + (side * 138), y - 4, Color.BLACK.getRGB());
        }
    }

    @Override
    protected String getTitleTranslationKey() {
        return this.recipe.getResultItem().getDescriptionId();
    }

    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        Minecraft mc = screen.getMinecraft();
        ResearchEntry entry = ResearchManager.getEntryForRecipe(this.recipe.getId());
        if (!this.isFirstPage()) {
            y += 24;
        }
        if (entry != null) {
            y += mc.font.lineHeight + 3;
            ResearchDiscipline discipline = ResearchDisciplines.getDiscipline(entry.getDisciplineKey());
            if (discipline != null) {
                screen.addWidgetToScreen(new DisciplineButton(x + 12 + (side * 140), y, new TranslatableComponent(discipline.getNameTranslationKey()), screen, discipline));
            }
            
            y += 3 * mc.font.lineHeight;
            screen.addWidgetToScreen(new EntryButton(x + 12 + (side * 140), y, new TranslatableComponent(entry.getNameTranslationKey()), screen, entry));
        }
    }
}
