package com.verdantartifice.primalmagick.client.compat.jei;

import javax.annotation.Nullable;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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
    private final Class<? extends T> recipeClass;
    
    protected Component title;
    private IDrawable background;
    private IDrawable icon;
    
    public RecipeCategoryPM(Class<? extends T> recipeClass, IGuiHelper guiHelper, ResourceLocation uid, String titleTranslationKey) {
        this.recipeClass = recipeClass;
        this.guiHelper = guiHelper;
        this.uid = uid;
        this.title = new TranslatableComponent(titleTranslationKey);
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
        this.setIcon(this.guiHelper.createDrawableIngredient(VanillaTypes.ITEM, stack));
    }

    @Override
    public ResourceLocation getUid() {
        return this.uid;
    }

    @Override
    public Class<? extends T> getRecipeClass() {
        return this.recipeClass;
    }

    @Override
    public Component getTitle() {
        return this.title;
    }
}
