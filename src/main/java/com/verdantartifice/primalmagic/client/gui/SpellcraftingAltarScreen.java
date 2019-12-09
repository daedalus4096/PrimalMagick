package com.verdantartifice.primalmagic.client.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.mojang.blaze3d.platform.GlStateManager;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.containers.SpellcraftingAltarContainer;
import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.spellcrafting.SetSpellComponentTypeIndexPacket;
import com.verdantartifice.primalmagic.common.network.packets.spellcrafting.SetSpellNamePacket;
import com.verdantartifice.primalmagic.common.spells.SpellComponent;
import com.verdantartifice.primalmagic.common.spells.SpellManager;
import com.verdantartifice.primalmagic.common.spells.SpellProperty;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SpellcraftingAltarScreen extends ContainerScreen<SpellcraftingAltarContainer> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/spellcrafting_altar.png");
    
    private final Map<Vec3i, ITextComponent> texts = new HashMap<>();
    
    private TextFieldWidget nameField;

    public SpellcraftingAltarScreen(SpellcraftingAltarContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.xSize = 212;
        this.ySize = 222;
    }
    
    @Override
    protected void init() {
        super.init();
        this.minecraft.keyboardListener.enableRepeatEvents(true);
        
        this.nameField = new TextFieldWidget(this.font, this.guiLeft + 31, this.guiTop + 12, 103, 12, "");
        this.nameField.setCanLoseFocus(false);
        this.nameField.changeFocus(true);
        this.nameField.setTextColor(-1);
        this.nameField.setDisabledTextColour(-1);
        this.nameField.setEnableBackgroundDrawing(false);
        this.nameField.setMaxStringLength(35);
        this.nameField.func_212954_a(this::updateName);
    }
    
    @Override
    public void resize(Minecraft p_resize_1_, int p_resize_2_, int p_resize_3_) {
        String str = this.nameField.getText();
        this.init(p_resize_1_, p_resize_2_, p_resize_3_);
        this.nameField.setText(str);
    }
    
    @Override
    public void removed() {
        super.removed();
        this.minecraft.keyboardListener.enableRepeatEvents(false);
    }
    
    @Override
    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        if (p_keyPressed_1_ == 256) {
            this.minecraft.player.closeScreen();
        }
        return !this.nameField.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_) && !this.nameField.func_212955_f() ? super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_) : true;
    }
    
    protected void initWidgets() {
        this.buttons.clear();
        this.children.clear();
        this.texts.clear();
        
        this.children.add(this.nameField);
        this.func_212928_a(this.nameField);
        
        int startX = this.guiLeft + 7;
        int startY = this.guiTop + 32;
        int x = startX;
        int y = startY;
        
        this.texts.put(new Vec3i(x, y + 2, 97), new TranslationTextComponent("primalmagic.spell.package.header"));
        
        y += 12;
        this.addButton(new CyclicBoundedSpinnerButton(x, y, false, 0, SpellManager.getPackageTypes().size() - 1, this.container::getSpellPackageTypeIndex, this::updateSpellPackageTypeIndex));
        this.texts.put(new Vec3i(x + 8, y + 2, 81), this.container.getSpellPackageComponent().getTypeName());
        this.addButton(new CyclicBoundedSpinnerButton(x + 90, y, true, 0, SpellManager.getPackageTypes().size() - 1, this.container::getSpellPackageTypeIndex, this::updateSpellPackageTypeIndex));
        
        y = startY + 48;
        this.texts.put(new Vec3i(x, y + 2, 97), new TranslationTextComponent("primalmagic.spell.payload.header"));
        
        y += 12;
        this.addButton(new CyclicBoundedSpinnerButton(x, y, false, 0, SpellManager.getPayloadTypes().size() - 1, this.container::getSpellPayloadTypeIndex, this::updateSpellPayloadTypeIndex));
        this.texts.put(new Vec3i(x + 8, y + 2, 81), this.container.getSpellPayloadComponent().getTypeName());
        this.addButton(new CyclicBoundedSpinnerButton(x + 90, y, true, 0, SpellManager.getPayloadTypes().size() - 1, this.container::getSpellPayloadTypeIndex, this::updateSpellPayloadTypeIndex));
        
        for (SpellProperty property : this.container.getSpellPayloadComponent().getProperties()) {
            y += 12;
            // TODO add decrement spinner
            // TODO add property value label
            // TODO add increment spinner
            this.texts.put(new Vec3i(x + 16, y + 2, 73), property.getDescription());
        }
        
        x += 101;
        y = startY;
        this.texts.put(new Vec3i(x, y + 2, 97), new TranslationTextComponent("primalmagic.spell.primary_mod.header"));
        
        y += 12;
        this.addButton(new CyclicBoundedSpinnerButton(x, y, false, 0, SpellManager.getModTypes().size() - 1, this.container::getSpellPrimaryModTypeIndex, this::updateSpellPrimaryModTypeIndex));
        this.texts.put(new Vec3i(x + 8, y + 2, 81), this.container.getSpellPrimaryModComponent().getTypeName());
        this.addButton(new CyclicBoundedSpinnerButton(x + 90, y, true, 0, SpellManager.getModTypes().size() - 1, this.container::getSpellPrimaryModTypeIndex, this::updateSpellPrimaryModTypeIndex));
        
        for (SpellProperty property : this.container.getSpellPrimaryModComponent().getProperties()) {
            y += 12;
            // TODO add decrement spinner
            // TODO add property value label
            // TODO add increment spinner
            this.texts.put(new Vec3i(x + 16, y + 2, 73), property.getDescription());
        }
        
        y = startY + 48;
        this.texts.put(new Vec3i(x, y + 2, 97), new TranslationTextComponent("primalmagic.spell.secondary_mod.header"));
        
        y += 12;
        this.addButton(new CyclicBoundedSpinnerButton(x, y, false, 0, SpellManager.getModTypes().size() - 1, this.container::getSpellSecondaryModTypeIndex, this::updateSpellSecondaryModTypeIndex));
        this.texts.put(new Vec3i(x + 8, y + 2, 81), this.container.getSpellSecondaryModComponent().getTypeName());
        this.addButton(new CyclicBoundedSpinnerButton(x + 90, y, true, 0, SpellManager.getModTypes().size() - 1, this.container::getSpellSecondaryModTypeIndex, this::updateSpellSecondaryModTypeIndex));
        
        for (SpellProperty property : this.container.getSpellSecondaryModComponent().getProperties()) {
            y += 12;
            // TODO add decrement spinner
            // TODO add property value label
            // TODO add increment spinner
            this.texts.put(new Vec3i(x + 16, y + 2, 73), property.getDescription());
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.initWidgets();
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        this.nameField.render(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        this.blit(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        this.blit(this.guiLeft + 28, this.guiTop + 8, 0, this.ySize, 110, 16);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        int color = 0x404040;
        String str;
        for (Map.Entry<Vec3i, ITextComponent> entry : this.texts.entrySet()) {
            str = this.font.trimStringToWidth(entry.getValue().getFormattedText(), entry.getKey().getZ());
            this.font.drawString(str, entry.getKey().getX() - this.guiLeft, entry.getKey().getY() - this.guiTop, color);
        }
    }
    
    private void updateName(String name) {
        if (!name.isEmpty()) {
            this.container.setSpellName(name);
            PacketHandler.sendToServer(new SetSpellNamePacket(this.container.windowId, name));
        }
    }
    
    private void updateSpellPackageTypeIndex(int index) {
        this.container.setSpellPackageTypeIndex(index);
        PacketHandler.sendToServer(new SetSpellComponentTypeIndexPacket(this.container.windowId, SpellComponent.PACKAGE, index));
    }
    
    private void updateSpellPayloadTypeIndex(int index) {
        this.container.setSpellPayloadTypeIndex(index);
        PacketHandler.sendToServer(new SetSpellComponentTypeIndexPacket(this.container.windowId, SpellComponent.PAYLOAD, index));
    }
    
    private void updateSpellPrimaryModTypeIndex(int index) {
        this.container.setSpellPrimaryModTypeIndex(index);
        PacketHandler.sendToServer(new SetSpellComponentTypeIndexPacket(this.container.windowId, SpellComponent.PRIMARY_MOD, index));
    }
    
    private void updateSpellSecondaryModTypeIndex(int index) {
        this.container.setSpellSecondaryModTypeIndex(index);
        PacketHandler.sendToServer(new SetSpellComponentTypeIndexPacket(this.container.windowId, SpellComponent.SECONDARY_MOD, index));
    }
    
    protected static class CyclicBoundedSpinnerButton extends Button {
        protected final boolean isIncrement;
        protected final int min;
        protected final int max;
        protected final Supplier<Integer> getter;
        protected final Consumer<Integer> setter;
        
        public CyclicBoundedSpinnerButton(int xPos, int yPos, boolean increment, int min, int max, Supplier<Integer> getter, Consumer<Integer> setter) {
            super(xPos, yPos, 7, 11, "", new Handler());
            this.isIncrement = increment;
            this.min = min;
            this.max = max;
            this.getter = getter;
            this.setter = setter;
        }
        
        @Override
        public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
            Minecraft mc = Minecraft.getInstance();
            mc.getTextureManager().bindTexture(TEXTURE);
            this.blit(this.x, this.y, this.isIncrement ? 212 : 219, this.isHovered() ? 11 : 0, this.width, this.height);
        }
        
        public boolean isIncrement() {
            return isIncrement;
        }

        public int getMin() {
            return min;
        }

        public int getMax() {
            return max;
        }

        public Supplier<Integer> getGetter() {
            return getter;
        }

        public Consumer<Integer> getSetter() {
            return setter;
        }

        private static class Handler implements IPressable {
            @Override
            public void onPress(Button button) {
                if (button instanceof CyclicBoundedSpinnerButton) {
                    CyclicBoundedSpinnerButton spinner = (CyclicBoundedSpinnerButton)button;
                    int newVal = spinner.getGetter().get().intValue() + (spinner.isIncrement() ? 1 : -1);
                    if (newVal < spinner.getMin()) {
                        newVal = spinner.getMax();
                    } else if (newVal > spinner.getMax()) {
                        newVal = spinner.getMin();
                    }
                    spinner.getSetter().accept(Integer.valueOf(newVal));
                }
            }
        }
    }
}
