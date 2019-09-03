package com.verdantartifice.primalmagic.client.gui.grimoire;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.mojang.blaze3d.platform.GlStateManager;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.gui.grimoire.buttons.GrimoireDisciplineButton;
import com.verdantartifice.primalmagic.common.containers.GrimoireContainer;
import com.verdantartifice.primalmagic.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagic.common.research.ResearchDisciplines;
import com.verdantartifice.primalmagic.common.research.ResearchStage;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class GrimoireScreen extends ContainerScreen<GrimoireContainer> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/grimoire.png");
    private static final float SCALE = 1.3F;
    
    protected int left;
    protected int top;
    
    public GrimoireScreen(GrimoireContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.xSize = 256;
        this.ySize = 181;
    }
    
    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
    
    @Override
    protected void init() {
        super.init();
        this.left = (int)(this.width - this.xSize * SCALE) / 2;
        this.top = (int)(this.height - this.ySize * SCALE) / 2;
        this.initButtons();
    }
    
    protected void initButtons() {
        this.buttons.clear();
        if (this.container.getTopic() == null) {
            int index = 0;
            for (ResearchDiscipline discipline : this.buildDisciplineList()) {
                String text = (new TranslationTextComponent(discipline.getNameTranslationKey())).getString();
                this.addButton(new GrimoireDisciplineButton(this.left + 23, this.top + 11 + (index * 24), 135, 18, text, this, discipline));
                index++;
            }
        } else if (this.container.getTopic() instanceof ResearchDiscipline) {
            // TODO render entry index
        } else if (this.container.getTopic() instanceof ResearchStage) {
            // TODO render stage details
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(TEXTURE);

        float relX = (this.width - this.xSize * SCALE) / 2.0F;
        float relY = (this.height - this.ySize * SCALE) / 2.0F;

        GlStateManager.pushMatrix();
        GlStateManager.translatef(relX, relY, 0.0F);
        GlStateManager.scalef(SCALE, SCALE, 1.0F);
        this.blit(0, 0, 0, 0, this.xSize, this.ySize);
        GlStateManager.popMatrix();
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }
    
    private List<ResearchDiscipline> buildDisciplineList() {
        return ResearchDisciplines.getAllDisciplines().stream()
                                    .sorted(Comparator.comparing(d -> (new TranslationTextComponent(d.getNameTranslationKey())).getString()))
                                    .collect(Collectors.toList());
    }
}
