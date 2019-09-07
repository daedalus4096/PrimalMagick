package com.verdantartifice.primalmagic.client.gui.grimoire.pages;

import com.verdantartifice.primalmagic.common.research.ResearchStage;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RequirementsPage extends AbstractPage {
    protected ResearchStage stage;
    
    public RequirementsPage(ResearchStage stage) {
        this.stage = stage;
    }
    
    @Override
    protected boolean renderTopTitleBar() {
        return false;
    }

    @Override
    protected String getTitleTranslationKey() {
        return "primalmagic.grimoire.requirements_header";
    }

    @Override
    public void render(int side, int x, int y, int mouseX, int mouseY) {
        // Render page title
        this.renderTitle(side, x, y, mouseX, mouseY);
        
        // TODO Render requirements widgets
    }
}
