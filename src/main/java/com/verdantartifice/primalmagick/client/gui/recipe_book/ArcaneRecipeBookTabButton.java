package com.verdantartifice.primalmagick.client.gui.recipe_book;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagick.client.recipe_book.ArcaneRecipeBookCategories;
import com.verdantartifice.primalmagick.client.recipe_book.ClientArcaneRecipeBook;
import com.verdantartifice.primalmagick.common.menus.base.IArcaneRecipeBookMenu;

import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.StateSwitchingButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

/**
 * Tab button for an arcane recipe book category.
 * 
 * @author Daedalus4096
 */
public class ArcaneRecipeBookTabButton extends StateSwitchingButton {
    protected static final float ANIMATION_TIME_TOTAL = 15.0F;
    protected static final WidgetSprites SPRITES = new WidgetSprites(new ResourceLocation("recipe_book/tab"), new ResourceLocation("recipe_book/tab_selected"));

    protected final ArcaneRecipeBookCategories category;
    protected float animationTime;
    
    public ArcaneRecipeBookTabButton(ArcaneRecipeBookCategories category) {
        super(0, 0, 35, 27, false);
        this.category = category;
        this.initTextureValues(SPRITES);
    }
    
    public void startAnimation(Minecraft mc, ClientRecipeBook vanillaBook, ClientArcaneRecipeBook arcaneBook) {
        ImmutableList.Builder<ArcaneRecipeCollection> builder = ImmutableList.builder();
        builder.addAll(arcaneBook.getCollection(this.category));
        builder.addAll(vanillaBook.getCollection(this.category.getVanillaCategory()).stream().map(ArcaneRecipeCollection::new).collect(Collectors.toList()));
        List<ArcaneRecipeCollection> list = builder.build();
        
        if (mc.player.containerMenu instanceof IArcaneRecipeBookMenu<?> recipeMenu) {
            for (ArcaneRecipeCollection recipeCollection : list) {
                for (RecipeHolder<?> recipe : recipeCollection.getRecipes(arcaneBook.getData().isFiltering(recipeMenu.getRecipeBookType()))) {
                    if (arcaneBook.getData().willHighlight(recipe) || vanillaBook.willHighlight(recipe)) {
                        this.animationTime = ANIMATION_TIME_TOTAL;
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (this.sprites != null) {
            if (this.animationTime > 0.0F) {
                float f = 1.0F + 0.1F * (float)Math.sin((double)(this.animationTime / ANIMATION_TIME_TOTAL * (float)Math.PI));
                pGuiGraphics.pose().pushPose();
                pGuiGraphics.pose().translate((double)(this.getX() + 8), (double)(this.getY() + 12), 0.0D);
                pGuiGraphics.pose().scale(1.0F, f, 1.0F);
                pGuiGraphics.pose().translate((double)(-(this.getX() + 8)), (double)(-(this.getY() + 12)), 0.0D);
            }
            
            Minecraft mc = Minecraft.getInstance();
            RenderSystem.disableDepthTest();
            ResourceLocation spriteLoc = this.sprites.get(true, this.isStateTriggered);
            int x = this.getX();
            if (this.isStateTriggered) {
                x -= 2;
            }
            
            pGuiGraphics.blitSprite(spriteLoc, x, this.getY(), this.width, this.height);
            RenderSystem.enableDepthTest();
            this.renderIcon(pGuiGraphics, mc.getItemRenderer());
            if (this.animationTime > 0.0F) {
                pGuiGraphics.pose().popPose();
                this.animationTime -= pPartialTick;
            }
        }
    }
    
    protected void renderIcon(GuiGraphics guiGraphics, ItemRenderer pItemRenderer) {
        List<ItemStack> list = this.category.getIconItems();
        int dx = this.isStateTriggered ? -2 : 0;
        if (list.size() == 1) {
            guiGraphics.renderFakeItem(list.get(0), this.getX() + 9 + dx, this.getY() + 5);
        } else if (list.size() == 2) {
            guiGraphics.renderFakeItem(list.get(0), this.getX() + 3 + dx, this.getY() + 5);
            guiGraphics.renderFakeItem(list.get(1), this.getX() + 14 + dx, this.getY() + 5);
        }
    }
    
    public ArcaneRecipeBookCategories getCategory() {
        return this.category;
    }
    
    public boolean updateVisibility(ClientRecipeBook vanillaBook, ClientArcaneRecipeBook arcaneBook) {
        ImmutableList.Builder<ArcaneRecipeCollection> builder = ImmutableList.builder();
        builder.addAll(arcaneBook.getCollection(this.category));
        builder.addAll(vanillaBook.getCollection(this.category.getVanillaCategory()).stream().map(ArcaneRecipeCollection::new).collect(Collectors.toList()));
        List<ArcaneRecipeCollection> list = builder.build();
        this.visible = false;
        for (ArcaneRecipeCollection collection : list) {
            if (collection.hasKnownRecipes() && collection.hasFitting()) {
                this.visible = true;
                break;
            }
        }
        return this.visible;
    }
}
