package com.verdantartifice.primalmagick.client.gui.grimoire;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.List;

/**
 * Base class for all grimoire recipe pages.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractRecipePage extends AbstractPage {
    protected static final Identifier OVERLAY = ResourceUtils.loc("textures/gui/grimoire_overlay.png");

    protected final SlotDisplay craftingStationSlotDisplay;
    protected final RegistryAccess registryAccess;

    public AbstractRecipePage(SlotDisplay craftingStationSlotDisplay, RegistryAccess registryAccess) {
        this.craftingStationSlotDisplay = craftingStationSlotDisplay;
        this.registryAccess = registryAccess;
    }
    
    @Override
    public void render(GuiGraphicsExtractor guiGraphics, int side, int x, int y, int mouseX, int mouseY) {
        this.renderTitle(guiGraphics, side, x, y, mouseX, mouseY, null);
    }

    @Override
    protected boolean renderTopTitleBar() {
        return false;
    }
    
    protected abstract String getRecipeTypeTranslationKey();

    protected abstract ItemStack getRecipeResult();

    protected abstract List<Ingredient> getRecipeIngredients();
}
