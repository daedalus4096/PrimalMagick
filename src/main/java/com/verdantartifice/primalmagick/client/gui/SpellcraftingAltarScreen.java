package com.verdantartifice.primalmagick.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.gui.widgets.ManaCostWidget;
import com.verdantartifice.primalmagick.common.menus.SpellcraftingAltarMenu;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.spellcrafting.SetSpellComponentPropertyPacket;
import com.verdantartifice.primalmagick.common.network.packets.spellcrafting.SetSpellComponentTypeIndexPacket;
import com.verdantartifice.primalmagick.common.network.packets.spellcrafting.SetSpellNamePacket;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.spells.SpellComponent;
import com.verdantartifice.primalmagick.common.spells.SpellManager;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * GUI screen for the spellcrafting altar block.
 * 
 * @author Daedalus4096
 */
public class SpellcraftingAltarScreen extends AbstractContainerScreen<SpellcraftingAltarMenu> {
    private static final ResourceLocation TEXTURE = PrimalMagick.resource("textures/gui/spellcrafting_altar.png");
    
    private final Map<Vec3i, Component> texts = new HashMap<>();
    private final List<GuiEventListener> localWidgets = new ArrayList<>();
    
    private EditBox nameField;

    public SpellcraftingAltarScreen(SpellcraftingAltarMenu screenMenu, Inventory inv, Component titleIn) {
        super(screenMenu, inv, titleIn);
        this.imageWidth = 230;
        this.imageHeight = 222;
    }
    
    @Override
    protected void init() {
        super.init();
        
        // Set up the spell name text entry widget
        this.nameField = new EditBox(this.font, this.leftPos + 49, this.topPos + 12, 103, 12, Component.empty());
        this.nameField.setCanLoseFocus(false);
        this.nameField.setFocused(true);
        this.nameField.setTextColor(-1);
        this.nameField.setTextColorUneditable(-1);
        this.nameField.setBordered(false);
        this.nameField.setMaxLength(50);
        this.nameField.setResponder(this::updateName);
        this.nameField.setValue(this.menu.getDefaultSpellName().getString());
        this.addWidget(this.nameField);
        this.setInitialFocus(this.nameField);
    }
    
    @Override
    public void resize(Minecraft p_resize_1_, int p_resize_2_, int p_resize_3_) {
        // Preserve spell name text upon GUI re-initialization
        String str = this.nameField.getValue();
        this.init(p_resize_1_, p_resize_2_, p_resize_3_);
        this.nameField.setValue(str);
    }
    
    @Override
    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        // Close the screen if Escape was pressed
        if (p_keyPressed_1_ == GLFW.GLFW_KEY_ESCAPE) {
            this.minecraft.player.closeContainer();
        }
        
        // Otherwise, process the text entry
        return !this.nameField.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_) && !this.nameField.canConsumeInput() ? super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_) : true;
    }
    
    protected void regenerateWidgets() {
        for (GuiEventListener widget : this.localWidgets) {
            this.removeWidget(widget);
        }
        this.localWidgets.clear();
        this.texts.clear();
        
        if (!this.nameField.isFocused()) {
            this.setInitialFocus(this.nameField);
        }
        
        int startX = this.leftPos + 7;
        int startY = this.topPos + 32;
        int x = startX;
        int y = startY;
        
        int vehicleMax = SpellManager.getVehicleTypes(this.menu.getPlayer()).size() - 1;
        int payloadMax = SpellManager.getPayloadTypes(this.menu.getPlayer()).size() - 1;
        int modMax = SpellManager.getModTypes(this.menu.getPlayer()).size() - 1;

        // Init mana cost indicator
        SourceList manaCost = this.menu.getSpellPackage().getManaCost();
        if (manaCost != null && !manaCost.isEmpty()) {
            Source source = manaCost.getSourcesSorted().get(0);
            this.localWidgets.add(this.addRenderableWidget(new ManaCostWidget(source, manaCost.getAmount(source), this.leftPos + 28, this.topPos + 8, this.menu::getWand, this.menu.getPlayer())));
        }
        
        this.texts.put(new Vec3i(x, y + 2, 106), Component.translatable("spells.primalmagick.vehicle.header"));
        
        // Init spell vehicle type selector
        y += 12;
        this.localWidgets.add(this.addRenderableWidget(new CyclicBoundedSpinnerButton(x, y, false, 0, vehicleMax, this.menu::getSpellVehicleTypeIndex, this::updateSpellVehicleTypeIndex)));
        this.texts.put(new Vec3i(x + 8, y + 2, 90), this.menu.getSpellPackage().getVehicle().getTypeName());
        this.localWidgets.add(this.addRenderableWidget(new CyclicBoundedSpinnerButton(x + 99, y, true, 0, vehicleMax, this.menu::getSpellVehicleTypeIndex, this::updateSpellVehicleTypeIndex)));
        
        // Init spell vehicle property selectors, if any
        for (SpellProperty property : this.menu.getSpellPackage().getVehicle().getProperties()) {
            y += 12;
            this.localWidgets.add(this.addRenderableWidget(new CyclicBoundedSpinnerButton(x + 8, y, false, property.getMin(), property.getMax(), this.menu.getSpellPackage().getVehicle().getProperty(property.getName())::getValue, (v) -> this.updateSpellPropertyValue(SpellComponent.VEHICLE, property.getName(), v))));
            this.texts.put(new Vec3i(x + 18, y + 2, 7), Component.literal(Integer.toString(property.getValue())));
            this.localWidgets.add(this.addRenderableWidget(new CyclicBoundedSpinnerButton(x + 26, y, true, property.getMin(), property.getMax(), this.menu.getSpellPackage().getVehicle().getProperty(property.getName())::getValue, (v) -> this.updateSpellPropertyValue(SpellComponent.VEHICLE, property.getName(), v))));
            this.texts.put(new Vec3i(x + 35, y + 2, Math.min(71, this.font.width(property.getDescription().getString()))), property.getDescription());
        }
        
        y = startY + 48;
        this.texts.put(new Vec3i(x, y + 2, 106), Component.translatable("spells.primalmagick.payload.header"));
        
        // Init spell payload type selector
        y += 12;
        this.localWidgets.add(this.addRenderableWidget(new CyclicBoundedSpinnerButton(x, y, false, 0, payloadMax, this.menu::getSpellPayloadTypeIndex, this::updateSpellPayloadTypeIndex)));
        this.texts.put(new Vec3i(x + 8, y + 2, 90), this.menu.getSpellPackage().getPayload().getTypeName());
        this.localWidgets.add(this.addRenderableWidget(new CyclicBoundedSpinnerButton(x + 99, y, true, 0, payloadMax, this.menu::getSpellPayloadTypeIndex, this::updateSpellPayloadTypeIndex)));
        
        // Init spell payload property selectors, if any
        for (SpellProperty property : this.menu.getSpellPackage().getPayload().getProperties()) {
            y += 12;
            this.localWidgets.add(this.addRenderableWidget(new CyclicBoundedSpinnerButton(x + 8, y, false, property.getMin(), property.getMax(), this.menu.getSpellPackage().getPayload().getProperty(property.getName())::getValue, (v) -> this.updateSpellPropertyValue(SpellComponent.PAYLOAD, property.getName(), v))));
            this.texts.put(new Vec3i(x + 18, y + 2, 7), Component.literal(Integer.toString(property.getValue())));
            this.localWidgets.add(this.addRenderableWidget(new CyclicBoundedSpinnerButton(x + 26, y, true, property.getMin(), property.getMax(), this.menu.getSpellPackage().getPayload().getProperty(property.getName())::getValue, (v) -> this.updateSpellPropertyValue(SpellComponent.PAYLOAD, property.getName(), v))));
            this.texts.put(new Vec3i(x + 35, y + 2, Math.min(71, this.font.width(property.getDescription().getString()))), property.getDescription());
        }
        
        // Move to the top of the right-hand column
        x += 110;
        y = startY;
        this.texts.put(new Vec3i(x, y + 2, 106), Component.translatable("spells.primalmagick.primary_mod.header"));
        
        // Init primary spell mod type selector
        y += 12;
        this.localWidgets.add(this.addRenderableWidget(new CyclicBoundedSpinnerButton(x, y, false, 0, modMax, this.menu::getSpellPrimaryModTypeIndex, this::updateSpellPrimaryModTypeIndex)));
        this.texts.put(new Vec3i(x + 8, y + 2, 90), this.menu.getSpellPackage().getPrimaryMod().getTypeName());
        this.localWidgets.add(this.addRenderableWidget(new CyclicBoundedSpinnerButton(x + 99, y, true, 0, modMax, this.menu::getSpellPrimaryModTypeIndex, this::updateSpellPrimaryModTypeIndex)));
        
        // Init primary spell mod property selectors, if any
        for (SpellProperty property : this.menu.getSpellPackage().getPrimaryMod().getProperties()) {
            y += 12;
            this.localWidgets.add(this.addRenderableWidget(new CyclicBoundedSpinnerButton(x + 8, y, false, property.getMin(), property.getMax(), this.menu.getSpellPackage().getPrimaryMod().getProperty(property.getName())::getValue, (v) -> this.updateSpellPropertyValue(SpellComponent.PRIMARY_MOD, property.getName(), v))));
            this.texts.put(new Vec3i(x + 18, y + 2, 7), Component.literal(Integer.toString(property.getValue())));
            this.localWidgets.add(this.addRenderableWidget(new CyclicBoundedSpinnerButton(x + 26, y, true, property.getMin(), property.getMax(), this.menu.getSpellPackage().getPrimaryMod().getProperty(property.getName())::getValue, (v) -> this.updateSpellPropertyValue(SpellComponent.PRIMARY_MOD, property.getName(), v))));
            this.texts.put(new Vec3i(x + 35, y + 2, Math.min(71, this.font.width(property.getDescription().getString()))), property.getDescription());
        }
        
        y = startY + 48;
        this.texts.put(new Vec3i(x, y + 2, 106), Component.translatable("spells.primalmagick.secondary_mod.header"));
        
        // Init secondary spell mod type selector
        y += 12;
        this.localWidgets.add(this.addRenderableWidget(new CyclicBoundedSpinnerButton(x, y, false, 0, modMax, this.menu::getSpellSecondaryModTypeIndex, this::updateSpellSecondaryModTypeIndex)));
        this.texts.put(new Vec3i(x + 8, y + 2, 90), this.menu.getSpellPackage().getSecondaryMod().getTypeName());
        this.localWidgets.add(this.addRenderableWidget(new CyclicBoundedSpinnerButton(x + 99, y, true, 0, modMax, this.menu::getSpellSecondaryModTypeIndex, this::updateSpellSecondaryModTypeIndex)));
        
        // Init secondary spell mod property selectors, if any
        for (SpellProperty property : this.menu.getSpellPackage().getSecondaryMod().getProperties()) {
            y += 12;
            this.localWidgets.add(this.addRenderableWidget(new CyclicBoundedSpinnerButton(x + 8, y, false, property.getMin(), property.getMax(), this.menu.getSpellPackage().getSecondaryMod().getProperty(property.getName())::getValue, (v) -> this.updateSpellPropertyValue(SpellComponent.SECONDARY_MOD, property.getName(), v))));
            this.texts.put(new Vec3i(x + 18, y + 2, 7), Component.literal(Integer.toString(property.getValue())));
            this.localWidgets.add(this.addRenderableWidget(new CyclicBoundedSpinnerButton(x + 26, y, true, property.getMin(), property.getMax(), this.menu.getSpellPackage().getSecondaryMod().getProperty(property.getName())::getValue, (v) -> this.updateSpellPropertyValue(SpellComponent.SECONDARY_MOD, property.getName(), v))));
            this.texts.put(new Vec3i(x + 35, y + 2, Math.min(71, this.font.width(property.getDescription().getString()))), property.getDescription());
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.regenerateWidgets();
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        RenderSystem.disableBlend();
        this.nameField.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        // Render the GUI background
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        
        // Render the text entry widget's background
        guiGraphics.blit(TEXTURE, this.leftPos + 46, this.topPos + 8, 0, this.imageHeight, 110, 16);
    }
    
    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // Render any text entries generated during initWidgets
        int color = 0x404040;
        String str;
        int strWidth;
        for (Map.Entry<Vec3i, Component> entry : this.texts.entrySet()) {
            str = this.font.plainSubstrByWidth(entry.getValue().getString(), entry.getKey().getZ());
            strWidth = this.font.width(str);
            guiGraphics.drawString(this.minecraft.font, str, entry.getKey().getX() - this.leftPos + ((entry.getKey().getZ() - strWidth) / 2), entry.getKey().getY() - this.topPos, color, false);
        }
    }
    
    private void updateName(String name) {
        // Use the default name for the selected spell component types if no name has been entered
        if (name.isEmpty()) {
            name = this.menu.getDefaultSpellName().getString();
        }
        
        // Update the spell name on both client and server sides
        this.menu.setSpellName(name);
        PacketHandler.sendToServer(new SetSpellNamePacket(this.menu.containerId, name));
    }
    
    private void updateSpellVehicleTypeIndex(int index) {
        boolean recalcName = this.nameField.getValue().isEmpty() || this.nameField.getValue().equals(this.menu.getDefaultSpellName().getString());
        
        // Update the spell vehicle type on both client and server sides
        this.menu.setSpellVehicleTypeIndex(index);
        PacketHandler.sendToServer(new SetSpellComponentTypeIndexPacket(this.menu.containerId, SpellComponent.VEHICLE, index));
        
        // Recalculate the spell name if using the default or the spell name is empty
        if (recalcName) {
            this.nameField.setValue(this.menu.getDefaultSpellName().getString());
        }
    }
    
    private void updateSpellPayloadTypeIndex(int index) {
        boolean recalcName = this.nameField.getValue().isEmpty() || this.nameField.getValue().equals(this.menu.getDefaultSpellName().getString());
        
        // Update the spell payload type on both client and server sides
        this.menu.setSpellPayloadTypeIndex(index);
        PacketHandler.sendToServer(new SetSpellComponentTypeIndexPacket(this.menu.containerId, SpellComponent.PAYLOAD, index));
        
        // Recalculate the spell name if using the default or the spell name is empty
        if (recalcName) {
            this.nameField.setValue(this.menu.getDefaultSpellName().getString());
        }
    }
    
    private void updateSpellPrimaryModTypeIndex(int index) {
        boolean recalcName = this.nameField.getValue().isEmpty() || this.nameField.getValue().equals(this.menu.getDefaultSpellName().getString());
        
        // Update the spell's primary mod type on both client and server sides
        this.menu.setSpellPrimaryModTypeIndex(index);
        PacketHandler.sendToServer(new SetSpellComponentTypeIndexPacket(this.menu.containerId, SpellComponent.PRIMARY_MOD, index));

        // Recalculate the spell name if using the default or the spell name is empty
        if (recalcName) {
            this.nameField.setValue(this.menu.getDefaultSpellName().getString());
        }
    }
    
    private void updateSpellSecondaryModTypeIndex(int index) {
        boolean recalcName = this.nameField.getValue().isEmpty() || this.nameField.getValue().equals(this.menu.getDefaultSpellName().getString());
        
        // Update the spell's secondary mod type on both client and server sides
        this.menu.setSpellSecondaryModTypeIndex(index);
        PacketHandler.sendToServer(new SetSpellComponentTypeIndexPacket(this.menu.containerId, SpellComponent.SECONDARY_MOD, index));

        // Recalculate the spell name if using the default or the spell name is empty
        if (recalcName) {
            this.nameField.setValue(this.menu.getDefaultSpellName().getString());
        }
    }
    
    private void updateSpellPropertyValue(SpellComponent component, String name, int value) {
        // Update the property value for the given spell component on both client and server sides
        this.menu.setSpellPropertyValue(component, name, value);
        PacketHandler.sendToServer(new SetSpellComponentPropertyPacket(this.menu.containerId, component, name, value));
    }
    
    /**
     * Spinner button that updates an int value.  Has a minimum and maximum value, and wraps the value around.
     * 
     * @author Daedalus4096
     */
    protected static class CyclicBoundedSpinnerButton extends Button {
        protected final boolean isIncrement;
        protected final int min;
        protected final int max;
        protected final Supplier<Integer> getter;
        protected final Consumer<Integer> setter;
        
        public CyclicBoundedSpinnerButton(int x, int y, boolean increment, int min, int max, Supplier<Integer> getter, Consumer<Integer> setter) {
            super(Button.builder(Component.empty(), new Handler()).bounds(x, y, 7, 11));
            this.isIncrement = increment;
            this.min = min;
            this.max = max;
            this.getter = getter;
            this.setter = setter;
        }
        
        @Override
        public void renderWidget(GuiGraphics guiGraphics, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
            guiGraphics.blit(TEXTURE, this.getX(), this.getY(), this.isIncrement ? 230 : 237, this.isHoveredOrFocused() ? 11 : 0, this.width, this.height);
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

        private static class Handler implements OnPress {
            @Override
            public void onPress(Button button) {
                if (button instanceof CyclicBoundedSpinnerButton) {
                    CyclicBoundedSpinnerButton spinner = (CyclicBoundedSpinnerButton)button;
                    
                    // Caluclate the new value
                    int newVal = spinner.getGetter().get().intValue() + (spinner.isIncrement() ? 1 : -1);
                    
                    // Wrap the value around if beyond min or max
                    if (newVal < spinner.getMin()) {
                        newVal = spinner.getMax();
                    } else if (newVal > spinner.getMax()) {
                        newVal = spinner.getMin();
                    }
                    
                    // Update the owner with the new value
                    spinner.getSetter().accept(Integer.valueOf(newVal));
                }
            }
        }
    }
}
