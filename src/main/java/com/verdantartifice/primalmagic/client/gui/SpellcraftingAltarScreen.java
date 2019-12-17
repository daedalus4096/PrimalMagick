package com.verdantartifice.primalmagic.client.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.mojang.blaze3d.platform.GlStateManager;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.gui.widgets.ManaCostWidget;
import com.verdantartifice.primalmagic.common.containers.SpellcraftingAltarContainer;
import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.spellcrafting.SetSpellComponentPropertyPacket;
import com.verdantartifice.primalmagic.common.network.packets.spellcrafting.SetSpellComponentTypeIndexPacket;
import com.verdantartifice.primalmagic.common.network.packets.spellcrafting.SetSpellNamePacket;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;
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
import net.minecraft.util.text.StringTextComponent;
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
        this.xSize = 230;
        this.ySize = 222;
    }
    
    @Override
    protected void init() {
        super.init();
        this.minecraft.keyboardListener.enableRepeatEvents(true);
        
        this.nameField = new TextFieldWidget(this.font, this.guiLeft + 49, this.guiTop + 12, 103, 12, "");
        this.nameField.setCanLoseFocus(false);
        this.nameField.changeFocus(true);
        this.nameField.setTextColor(-1);
        this.nameField.setDisabledTextColour(-1);
        this.nameField.setEnableBackgroundDrawing(false);
        this.nameField.setMaxStringLength(50);
        this.nameField.func_212954_a(this::updateName);
        this.nameField.setText(this.container.getDefaultSpellName().getString());
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
        
        int vehicleMax = SpellManager.getVehicleTypes(this.container.getPlayer()).size() - 1;
        int payloadMax = SpellManager.getPayloadTypes(this.container.getPlayer()).size() - 1;
        int modMax = SpellManager.getModTypes(this.container.getPlayer()).size() - 1;

        SourceList manaCost = this.container.getSpellPackage().getManaCost();
        if (manaCost != null && !manaCost.isEmpty()) {
            Source source = manaCost.getSourcesSorted().get(0);
            this.addButton(new ManaCostWidget(source, manaCost.getAmount(source), this.guiLeft + 28, this.guiTop + 8));
        }
        
        this.texts.put(new Vec3i(x, y + 2, 106), new TranslationTextComponent("primalmagic.spell.vehicle.header"));
        
        y += 12;
        this.addButton(new CyclicBoundedSpinnerButton(x, y, false, 0, vehicleMax, this.container::getSpellPackageTypeIndex, this::updateSpellPackageTypeIndex));
        this.texts.put(new Vec3i(x + 8, y + 2, 90), this.container.getSpellPackage().getVehicle().getTypeName());
        this.addButton(new CyclicBoundedSpinnerButton(x + 99, y, true, 0, vehicleMax, this.container::getSpellPackageTypeIndex, this::updateSpellPackageTypeIndex));
        
        y = startY + 48;
        this.texts.put(new Vec3i(x, y + 2, 106), new TranslationTextComponent("primalmagic.spell.payload.header"));
        
        y += 12;
        this.addButton(new CyclicBoundedSpinnerButton(x, y, false, 0, payloadMax, this.container::getSpellPayloadTypeIndex, this::updateSpellPayloadTypeIndex));
        this.texts.put(new Vec3i(x + 8, y + 2, 90), this.container.getSpellPackage().getPayload().getTypeName());
        this.addButton(new CyclicBoundedSpinnerButton(x + 99, y, true, 0, payloadMax, this.container::getSpellPayloadTypeIndex, this::updateSpellPayloadTypeIndex));
        
        for (SpellProperty property : this.container.getSpellPackage().getPayload().getProperties()) {
            y += 12;
            this.addButton(new CyclicBoundedSpinnerButton(x + 8, y, false, property.getMin(), property.getMax(), this.container.getSpellPackage().getPayload().getProperty(property.getName())::getValue, (v) -> this.updateSpellPayloadPropertyValue(property.getName(), v)));
            this.texts.put(new Vec3i(x + 18, y + 2, 7), new StringTextComponent(Integer.toString(property.getValue())));
            this.addButton(new CyclicBoundedSpinnerButton(x + 26, y, true, property.getMin(), property.getMax(), this.container.getSpellPackage().getPayload().getProperty(property.getName())::getValue, (v) -> this.updateSpellPayloadPropertyValue(property.getName(), v)));
            this.texts.put(new Vec3i(x + 35, y + 2, Math.min(71, this.font.getStringWidth(property.getDescription().getFormattedText()))), property.getDescription());
        }
        
        x += 110;
        y = startY;
        this.texts.put(new Vec3i(x, y + 2, 106), new TranslationTextComponent("primalmagic.spell.primary_mod.header"));
        
        y += 12;
        this.addButton(new CyclicBoundedSpinnerButton(x, y, false, 0, modMax, this.container::getSpellPrimaryModTypeIndex, this::updateSpellPrimaryModTypeIndex));
        this.texts.put(new Vec3i(x + 8, y + 2, 90), this.container.getSpellPackage().getPrimaryMod().getTypeName());
        this.addButton(new CyclicBoundedSpinnerButton(x + 99, y, true, 0, modMax, this.container::getSpellPrimaryModTypeIndex, this::updateSpellPrimaryModTypeIndex));
        
        for (SpellProperty property : this.container.getSpellPackage().getPrimaryMod().getProperties()) {
            y += 12;
            this.addButton(new CyclicBoundedSpinnerButton(x + 8, y, false, property.getMin(), property.getMax(), this.container.getSpellPackage().getPrimaryMod().getProperty(property.getName())::getValue, (v) -> this.updateSpellPrimaryModPropertyValue(property.getName(), v)));
            this.texts.put(new Vec3i(x + 18, y + 2, 7), new StringTextComponent(Integer.toString(property.getValue())));
            this.addButton(new CyclicBoundedSpinnerButton(x + 26, y, true, property.getMin(), property.getMax(), this.container.getSpellPackage().getPrimaryMod().getProperty(property.getName())::getValue, (v) -> this.updateSpellPrimaryModPropertyValue(property.getName(), v)));
            this.texts.put(new Vec3i(x + 35, y + 2, Math.min(71, this.font.getStringWidth(property.getDescription().getFormattedText()))), property.getDescription());
        }
        
        y = startY + 48;
        this.texts.put(new Vec3i(x, y + 2, 106), new TranslationTextComponent("primalmagic.spell.secondary_mod.header"));
        
        y += 12;
        this.addButton(new CyclicBoundedSpinnerButton(x, y, false, 0, modMax, this.container::getSpellSecondaryModTypeIndex, this::updateSpellSecondaryModTypeIndex));
        this.texts.put(new Vec3i(x + 8, y + 2, 90), this.container.getSpellPackage().getSecondaryMod().getTypeName());
        this.addButton(new CyclicBoundedSpinnerButton(x + 99, y, true, 0, modMax, this.container::getSpellSecondaryModTypeIndex, this::updateSpellSecondaryModTypeIndex));
        
        for (SpellProperty property : this.container.getSpellPackage().getSecondaryMod().getProperties()) {
            y += 12;
            this.addButton(new CyclicBoundedSpinnerButton(x + 8, y, false, property.getMin(), property.getMax(), this.container.getSpellPackage().getSecondaryMod().getProperty(property.getName())::getValue, (v) -> this.updateSpellSecondaryModPropertyValue(property.getName(), v)));
            this.texts.put(new Vec3i(x + 18, y + 2, 7), new StringTextComponent(Integer.toString(property.getValue())));
            this.addButton(new CyclicBoundedSpinnerButton(x + 26, y, true, property.getMin(), property.getMax(), this.container.getSpellPackage().getSecondaryMod().getProperty(property.getName())::getValue, (v) -> this.updateSpellSecondaryModPropertyValue(property.getName(), v)));
            this.texts.put(new Vec3i(x + 35, y + 2, Math.min(71, this.font.getStringWidth(property.getDescription().getFormattedText()))), property.getDescription());
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
        this.blit(this.guiLeft + 46, this.guiTop + 8, 0, this.ySize, 110, 16);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        int color = 0x404040;
        String str;
        int strWidth;
        for (Map.Entry<Vec3i, ITextComponent> entry : this.texts.entrySet()) {
            str = this.font.trimStringToWidth(entry.getValue().getFormattedText(), entry.getKey().getZ());
            strWidth = this.font.getStringWidth(str);
            this.font.drawString(str, entry.getKey().getX() - this.guiLeft + ((entry.getKey().getZ() - strWidth) / 2), entry.getKey().getY() - this.guiTop, color);
        }
    }
    
    private void updateName(String name) {
        if (name.isEmpty()) {
            name = this.container.getDefaultSpellName().getString();
        }
        this.container.setSpellName(name);
        PacketHandler.sendToServer(new SetSpellNamePacket(this.container.windowId, name));
    }
    
    private void updateSpellPackageTypeIndex(int index) {
        boolean recalcName = this.nameField.getText().isEmpty() || this.nameField.getText().equals(this.container.getDefaultSpellName().getString());
        this.container.setSpellPackageTypeIndex(index);
        PacketHandler.sendToServer(new SetSpellComponentTypeIndexPacket(this.container.windowId, SpellComponent.PACKAGE, index));
        if (recalcName) {
            this.nameField.setText(this.container.getDefaultSpellName().getString());
        }
    }
    
    private void updateSpellPayloadTypeIndex(int index) {
        boolean recalcName = this.nameField.getText().isEmpty() || this.nameField.getText().equals(this.container.getDefaultSpellName().getString());
        this.container.setSpellPayloadTypeIndex(index);
        PacketHandler.sendToServer(new SetSpellComponentTypeIndexPacket(this.container.windowId, SpellComponent.PAYLOAD, index));
        if (recalcName) {
            this.nameField.setText(this.container.getDefaultSpellName().getString());
        }
    }
    
    private void updateSpellPrimaryModTypeIndex(int index) {
        boolean recalcName = this.nameField.getText().isEmpty() || this.nameField.getText().equals(this.container.getDefaultSpellName().getString());
        this.container.setSpellPrimaryModTypeIndex(index);
        PacketHandler.sendToServer(new SetSpellComponentTypeIndexPacket(this.container.windowId, SpellComponent.PRIMARY_MOD, index));
        if (recalcName) {
            this.nameField.setText(this.container.getDefaultSpellName().getString());
        }
    }
    
    private void updateSpellSecondaryModTypeIndex(int index) {
        boolean recalcName = this.nameField.getText().isEmpty() || this.nameField.getText().equals(this.container.getDefaultSpellName().getString());
        this.container.setSpellSecondaryModTypeIndex(index);
        PacketHandler.sendToServer(new SetSpellComponentTypeIndexPacket(this.container.windowId, SpellComponent.SECONDARY_MOD, index));
        if (recalcName) {
            this.nameField.setText(this.container.getDefaultSpellName().getString());
        }
    }
    
    private void updateSpellPayloadPropertyValue(String name, int value) {
        this.container.getSpellPackage().getPayload().getProperty(name).setValue(value);
        PacketHandler.sendToServer(new SetSpellComponentPropertyPacket(this.container.windowId, SpellComponent.PAYLOAD, name, value));
    }
    
    private void updateSpellPrimaryModPropertyValue(String name, int value) {
        this.container.getSpellPackage().getPrimaryMod().getProperty(name).setValue(value);
        PacketHandler.sendToServer(new SetSpellComponentPropertyPacket(this.container.windowId, SpellComponent.PRIMARY_MOD, name, value));
    }
    
    private void updateSpellSecondaryModPropertyValue(String name, int value) {
        this.container.getSpellPackage().getSecondaryMod().getProperty(name).setValue(value);
        PacketHandler.sendToServer(new SetSpellComponentPropertyPacket(this.container.windowId, SpellComponent.SECONDARY_MOD, name, value));
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
            this.blit(this.x, this.y, this.isIncrement ? 230 : 237, this.isHovered() ? 11 : 0, this.width, this.height);
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
