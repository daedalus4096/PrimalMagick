package com.verdantartifice.primalmagic.client.gui.grimoire.widgets;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.util.GuiUtils;

import net.minecraft.client.gui.widget.Widget;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class IngredientWidget extends Widget {
    protected static final ResourceLocation GRIMOIRE_TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/grimoire.png");
    
    protected Ingredient ingredient;

    public IngredientWidget(@Nullable Ingredient ingredient, int xIn, int yIn) {
        super(xIn, yIn, 16, 16, "");
        this.ingredient = ingredient;
    }

    @Override
    public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        if (this.ingredient != null) {
            ItemStack[] matching = this.ingredient.getMatchingStacks();
            if (matching != null && matching.length > 0) {
                int index = (int)((System.currentTimeMillis() / 1000L) % matching.length);
                ItemStack toDisplay = matching[index];
                GuiUtils.renderItemStack(toDisplay, this.x, this.y, this.getMessage(), false);
                if (this.isHovered()) {
                    List<ITextComponent> textList = Collections.singletonList(toDisplay.getDisplayName());
                    GuiUtils.renderCustomTooltip(textList, this.x, this.y);
                }
            }
        }
    }
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }
}
