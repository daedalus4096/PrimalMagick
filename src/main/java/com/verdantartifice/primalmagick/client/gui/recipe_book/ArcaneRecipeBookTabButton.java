package com.verdantartifice.primalmagick.client.gui.recipe_book;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.client.recipe_book.ArcaneRecipeBookCategories;
import com.verdantartifice.primalmagick.client.recipe_book.ClientArcaneRecipeBook;
import com.verdantartifice.primalmagick.common.containers.AbstractArcaneRecipeBookMenu;

import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.StateSwitchingButton;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

/**
 * Tab button for an arcane recipe book category.
 * 
 * @author Daedalus4096
 */
public class ArcaneRecipeBookTabButton extends StateSwitchingButton {
    protected static final float ANIMATION_TIME = 15.0F;

    protected final ArcaneRecipeBookCategories category;
    protected float animationTime;
    
    public ArcaneRecipeBookTabButton(ArcaneRecipeBookCategories category) {
        super(0, 0, 35, 27, false);
        this.category = category;
        this.initTextureValues(153, 2, 35, 0, ArcaneRecipeBookComponent.RECIPE_BOOK_LOCATION);
    }
    
    public void startAnimation(Minecraft mc, ClientRecipeBook vanillaBook, ClientArcaneRecipeBook arcaneBook) {
        ImmutableList.Builder<ArcaneRecipeCollection> builder = ImmutableList.builder();
        builder.addAll(arcaneBook.getCollection(this.category));
        builder.addAll(vanillaBook.getCollection(this.category.getVanillaCategory()).stream().map(ArcaneRecipeCollection::new).collect(Collectors.toList()));
        List<ArcaneRecipeCollection> list = builder.build();
        
        if (mc.player.containerMenu instanceof AbstractArcaneRecipeBookMenu<?> recipeMenu) {
            for (ArcaneRecipeCollection recipeCollection : list) {
                for (Recipe<?> recipe : recipeCollection.getRecipes(arcaneBook.getData().isFiltering(recipeMenu.getRecipeBookType()))) {
                    if (arcaneBook.getData().willHighlight(recipe) || vanillaBook.willHighlight(recipe)) {
                        this.animationTime = ANIMATION_TIME;
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void renderButton(PoseStack poseStack, int p_94632_, int p_94633_, float p_94634_) {
        if (this.animationTime > 0.0F) {
            float f = 1.0F + 0.1F * (float)Math.sin((double)(this.animationTime / 15.0F * (float)Math.PI));
            poseStack.pushPose();
            poseStack.translate((double)(this.getX() + 8), (double)(this.getY() + 12), 0.0D);
            poseStack.scale(1.0F, f, 1.0F);
            poseStack.translate((double)(-(this.getX() + 8)), (double)(-(this.getY() + 12)), 0.0D);
        }
        
        Minecraft mc = Minecraft.getInstance();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, this.resourceLocation);
        RenderSystem.disableDepthTest();
        
        int texX = this.xTexStart;
        int texY = this.yTexStart;
        if (this.isStateTriggered) {
            texX += this.xDiffTex;
        }
        if (this.isHoveredOrFocused()) {
            texY += this.yDiffTex;
        }
        
        int localX = this.getX();
        if (this.isStateTriggered) {
            localX -= 2;
        }
        
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        this.blit(poseStack, localX, this.getY(), texX, texY, this.width, this.height);
        RenderSystem.enableDepthTest();
        this.renderIcon(mc.getItemRenderer());

        if (this.animationTime > 0.0F) {
            poseStack.popPose();
            this.animationTime -= p_94634_;
        }
    }
    
    protected void renderIcon(ItemRenderer itemRenderer) {
        List<ItemStack> list = this.category.getIconItems();
        int dx = this.isStateTriggered ? -2 : 0;
        if (list.size() == 1) {
            itemRenderer.renderAndDecorateFakeItem(list.get(0), this.getX() + 9 + dx, this.getY() + 5);
        } else if (list.size() == 2) {
            itemRenderer.renderAndDecorateFakeItem(list.get(0), this.getX() + 3 + dx, this.getY() + 5);
            itemRenderer.renderAndDecorateFakeItem(list.get(1), this.getX() + 14 + dx, this.getY() + 5);
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
