package com.verdantartifice.primalmagick.client.gui.grimoire;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Objects;

/**
 * Base class for all grimoire recipe pages.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractRecipePage<T extends RecipeDisplay> extends AbstractPage {
    protected static final Identifier OVERLAY = ResourceUtils.loc("textures/gui/grimoire_overlay.png");

    protected final T display;
    protected final ContextMap contextMap;
    protected final RegistryAccess registryAccess;

    public AbstractRecipePage(T display) {
        this.display = display;
        Minecraft mc = Minecraft.getInstance();
        Level level = Objects.requireNonNull(mc.level);
        this.contextMap = SlotDisplayContext.fromLevel(level);
        this.registryAccess = level.registryAccess();
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

    protected ItemStack getRecipeResult() {
        return this.display.result().resolveForFirstStack(this.contextMap);
    }

    protected abstract List<SlotDisplay> getRecipeIngredients();
}
