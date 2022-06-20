package com.verdantartifice.primalmagick.client.compat.jei;

import javax.annotation.Nullable;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/**
 * Base class for mod JEI recipe categories.
 * 
 * @author Daedalus4096
 */
public abstract class RecipeCategoryPM<T> implements IRecipeCategory<T> {
    protected final ResourceLocation uid;
    protected final IGuiHelper guiHelper;
    
    protected Component title;
    private IDrawable background;
    private IDrawable icon;
    
    public RecipeCategoryPM(IGuiHelper guiHelper, ResourceLocation uid, String titleTranslationKey) {
        this.guiHelper = guiHelper;
        this.uid = uid;
        this.title = Component.translatable(titleTranslationKey);
    }

    @Nullable
    @Override
    public IDrawable getBackground() {
        return this.background;
    }
    
    protected void setBackground(IDrawable background) {
        this.background = background;
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return this.icon;
    }
    
    protected void setIcon(IDrawable icon) {
        this.icon = icon;
    }
    
    protected void setIcon(ItemStack stack) {
        this.setIcon(this.guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, stack));
    }

    @Override
    public Component getTitle() {
        return this.title;
    }
}
