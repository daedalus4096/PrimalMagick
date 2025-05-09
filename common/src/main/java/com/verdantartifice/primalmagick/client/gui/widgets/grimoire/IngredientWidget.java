package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.util.GuiUtils;
import com.verdantartifice.primalmagick.common.research.topics.OtherResearchTopic;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Display widget for showing all the possible itemstacks for a given crafting ingredient.  Used
 * on recipe pages.
 * 
 * @author Daedalus4096
 */
public class IngredientWidget extends Button {
    protected static final Logger LOGGER = LogManager.getLogger();

    protected final Ingredient ingredient;
    protected final GrimoireScreen screen;
    protected ItemStack lastStack = ItemStack.EMPTY;
    protected ItemStack currentStack = ItemStack.EMPTY;

    public IngredientWidget(@Nullable Ingredient ingredient, int x, int y, GrimoireScreen screen) {
        super(x, y, 16, 16, Component.empty(), new Handler(), Button.DEFAULT_NARRATION);
        this.ingredient = ingredient;
        this.screen = screen;
    }

    public GrimoireScreen getScreen() {
        return this.screen;
    }
    
    @Override
    public void renderWidget(GuiGraphics guiGraphics, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        this.lastStack = this.currentStack;
        this.currentStack = this.getDisplayStack();
        if (!this.currentStack.isEmpty()) {
            GuiUtils.renderItemStack(guiGraphics, this.currentStack, this.getX(), this.getY(), this.getMessage().getString(), false);

            // Update the widget's tooltip if necessary
            this.updateTooltip();
        }
        
        // Don't allow the widget to become focused, to prevent keyboard navigation from moving the active tooltip
        this.setFocused(false);
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
    
    protected void updateTooltip() {
        if (!ItemStack.isSameItemSameComponents(this.currentStack, this.lastStack)) {
            Minecraft mc = Minecraft.getInstance();
            this.setTooltip(Tooltip.create(CommonComponents.joinLines(this.currentStack.getTooltipLines(Item.TooltipContext.of(mc.level), mc.player, 
                    mc.options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL))));
        }
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
                // Set the new grimoire topic and open a new screen for it
                iw.getScreen().gotoTopic(new OtherResearchTopic(iw.getDisplayStack().getHoverName().getString(), 0));
            }
        }
    }
}
