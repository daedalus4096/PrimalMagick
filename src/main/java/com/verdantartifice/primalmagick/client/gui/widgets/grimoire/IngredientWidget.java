package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.util.GuiUtils;
import com.verdantartifice.primalmagick.common.research.topics.OtherResearchTopic;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * Display widget for showing all the possible itemstacks for a given crafting ingredient.  Used
 * on recipe pages.
 * 
 * @author Daedalus4096
 */
public class IngredientWidget extends Button {
    protected Ingredient ingredient;
    protected GrimoireScreen screen;

    public IngredientWidget(@Nullable Ingredient ingredient, int x, int y, GrimoireScreen screen) {
        super(Button.builder(Component.empty(), new Handler()).bounds(x, y, 16, 16));
        this.ingredient = ingredient;
        this.screen = screen;
    }

    public GrimoireScreen getScreen() {
        return this.screen;
    }
    
    @Override
    public void renderWidget(GuiGraphics guiGraphics, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        ItemStack toDisplay = this.getDisplayStack();
        if (!toDisplay.isEmpty()) {
            GuiUtils.renderItemStack(guiGraphics, toDisplay, this.getX(), this.getY(), this.getMessage().getString(), false);
        }
    }
    
    @Override
    public void renderToolTip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        ItemStack toDisplay = this.getDisplayStack();
        if (!toDisplay.isEmpty()) {
            // If hovered, show a tooltip with the display name of the current matching itemstack
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0, 0, 200);
            
            GuiUtils.renderItemTooltip(guiGraphics, toDisplay, mouseX, mouseY);
            
            guiGraphics.pose().popPose();
        }
    }

    @Override
    protected boolean isValidClickButton(int p_93652_) {
        ItemStack displayStack = this.getDisplayStack();
        return super.isValidClickButton(p_93652_) && !displayStack.isEmpty() && this.screen.isIndexKey(displayStack.getHoverName().getString());
    }

    @Override
    public void playDownSound(SoundManager handler) {
        handler.play(SimpleSoundInstance.forUI(SoundsPM.PAGE.get(), 1.0F, 1.0F));
    }
    
    @Nonnull
    protected ItemStack getDisplayStack() {
        if (this.ingredient != null) {
            ItemStack[] matching = this.ingredient.getItems();
            if (matching != null && matching.length > 0) {
                // Cycle through each matching stack of the ingredient and display them one at a time
                int index = (int)((System.currentTimeMillis() / 1000L) % matching.length);
                return matching[index];
            }
        }
        return ItemStack.EMPTY;
    }
    
    private static class Handler implements OnPress {
        @Override
        public void onPress(Button button) {
            if (button instanceof IngredientWidget iw) {
                // Push the current grimoire topic onto the history stack
                iw.getScreen().pushCurrentHistoryTopic();
                
                // Set the new grimoire topic and open a new screen for it
                iw.getScreen().getMenu().setTopic(new OtherResearchTopic(iw.getDisplayStack().getHoverName().getString(), 0));
                iw.getScreen().getMinecraft().setScreen(new GrimoireScreen(
                    iw.getScreen().getMenu(),
                    iw.getScreen().getPlayerInventory(),
                    iw.getScreen().getTitle()
                ));
            }
        }
    }
}
