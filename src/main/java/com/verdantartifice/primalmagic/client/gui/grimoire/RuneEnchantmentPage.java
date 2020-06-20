package com.verdantartifice.primalmagic.client.gui.grimoire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.client.gui.GrimoireScreen;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Grimoire page showing the page elements for a rune enchantment.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class RuneEnchantmentPage extends AbstractPage {
    protected Enchantment enchant;
    protected List<IPageElement> contents = new ArrayList<>();
    protected boolean firstPage;
    
    public RuneEnchantmentPage(Enchantment enchant) {
        this(enchant, false);
    }
    
    public RuneEnchantmentPage(Enchantment enchant, boolean first) {
        this.enchant = enchant;
        this.firstPage = first;
    }
    
    @Nonnull
    public List<IPageElement> getElements() {
        return Collections.unmodifiableList(this.contents);
    }
    
    public boolean addElement(IPageElement element) {
        return this.contents.add(element);
    }
    
    public boolean isFirstPage() {
        return this.firstPage;
    }
    
    @Override
    protected String getTitleTranslationKey() {
        return this.enchant.getName();
    }
    
    @Override
    public void render(int side, int x, int y, int mouseX, int mouseY) {
        // Draw title page if applicable
        if (this.isFirstPage() && side == 0) {
            this.renderTitle(side, x, y, mouseX, mouseY, null);
            y += 77;
        } else {
            y += 25;
        }
        
        // Render page contents
        for (IPageElement content : this.contents) {
            content.render(side, x, y);
            y = content.getNextY(y);
        }
    }
    
    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {}
}
