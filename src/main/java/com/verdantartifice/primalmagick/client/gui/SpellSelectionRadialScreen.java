package com.verdantartifice.primalmagick.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.verdantartifice.primalmagick.client.config.KeyBindings;
import com.verdantartifice.primalmagick.client.events.InputEvents;
import com.verdantartifice.primalmagick.client.gui.radial.GenericRadialMenu;
import com.verdantartifice.primalmagick.client.gui.radial.IRadialMenuHost;
import com.verdantartifice.primalmagick.client.gui.radial.ImageRadialMenuItem;
import com.verdantartifice.primalmagick.client.gui.radial.RadialMenuItem;
import com.verdantartifice.primalmagick.client.gui.radial.SpellPackageRadialMenuItem;
import com.verdantartifice.primalmagick.common.config.Config;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.misc.SetActiveSpellPacket;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.wands.IWand;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class SpellSelectionRadialScreen extends Screen {
    private ItemStack stackEquipped = ItemStack.EMPTY;
    private boolean needsRecheckSpells = true;
    
    private final GenericRadialMenu menu;
    private final List<RadialMenuItem> cachedMenuItems = new ArrayList<>();
    private final ImageRadialMenuItem noSpellMenuItem;

    public SpellSelectionRadialScreen() {
        super(Component.empty());
        Minecraft mc = Minecraft.getInstance();
        if (mc.player.getMainHandItem().getItem() instanceof IWand) {
            this.stackEquipped = mc.player.getMainHandItem();
        } else if (mc.player.getOffhandItem().getItem() instanceof IWand) {
            this.stackEquipped = mc.player.getOffhandItem();
        }
        this.menu = new GenericRadialMenu(mc, new IRadialMenuHost() {
            @Override
            public void renderTooltip(GuiGraphics guiGraphics, ItemStack stack, int mouseX, int mouseY)
            {
                guiGraphics.renderTooltip(font, stack, mouseX, mouseY);
            }

            @Override
            public void renderTooltip(GuiGraphics guiGraphics, List<Component> textComponents, int mouseX, int mouseY)
            {
                guiGraphics.renderTooltip(font, textComponents, Optional.empty(), mouseX, mouseY);
            }

            @Override
            public Screen getScreen()
            {
                return SpellSelectionRadialScreen.this;
            }

            @Override
            public Font getFontRenderer()
            {
                return font;
            }
        })
        {
            @Override
            public void onClickOutside()
            {
                close();
            }
        };
        this.noSpellMenuItem = new ImageRadialMenuItem(this.menu, -1, new ResourceLocation("textures/item/barrier.png"), Component.translatable("tooltip.primalmagick.spells.no_spell_selection")) {
            @Override
            public boolean onClick() {
                return SpellSelectionRadialScreen.this.trySwitch(getSlot());
            }
        };
    }

    @Override
    public void removed() {
        super.removed();
        InputEvents.wipeOpen();
    }

    @Override
    public void tick() {
        super.tick();
        this.menu.tick();
        
        if (this.menu.isClosed()) {
            this.minecraft.setScreen(null);
            InputEvents.wipeOpen();
        }
        if (!this.menu.isReady()) {
            return;
        }
        
        ItemStack inMainHand = this.minecraft.player.getMainHandItem();
        ItemStack inOffHand = this.minecraft.player.getOffhandItem();
        if (inMainHand.getItem() instanceof IWand) {
            if (this.stackEquipped != inMainHand) {     // Reference comparison intended
                this.stackEquipped = inMainHand;
                this.needsRecheckSpells = true;
            }
        } else if (inOffHand.getItem() instanceof IWand) {
            if (this.stackEquipped != inOffHand) {      // Reference comparison intended
                this.stackEquipped = inOffHand;
                this.needsRecheckSpells = true;
            }
        } else {
            this.stackEquipped = ItemStack.EMPTY;
        }
        
        if (this.stackEquipped.isEmpty()) {
            this.minecraft.setScreen(null);
        } else if (!InputEvents.isKeyDown(KeyBindings.CHANGE_SPELL_KEY)) {
            if (Config.RADIAL_RELEASE_TO_SWITCH.get()) {
                this.processClick(false);
            } else {
                this.menu.close();
            }
        }
    }

    @Override
    public boolean mouseReleased(double p_94722_, double p_94723_, int p_94724_) {
        this.processClick(true);
        return super.mouseReleased(p_94722_, p_94723_, p_94724_);
    }
    
    protected void processClick(boolean triggeredByMouse) {
        this.menu.clickItem();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.pose().pushPose();
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.pose().popPose();
        
        if (this.stackEquipped.isEmpty()) {
            return;
        }
        
        IWand wand = (IWand)this.stackEquipped.getItem();
        if (this.needsRecheckSpells) {
            // Create and add radial menu items
            this.cachedMenuItems.clear();
            List<SpellPackage> spells = wand.getSpells(this.stackEquipped);
            for (int index = 0; index < spells.size(); index++) {
                SpellPackage spell = spells.get(index);
                SpellPackageRadialMenuItem item = new SpellPackageRadialMenuItem(menu, index, spell) {
                    @Override
                    public boolean onClick() {
                        return SpellSelectionRadialScreen.this.trySwitch(getSlot());
                    }
                };
                item.setVisible(true);
                this.cachedMenuItems.add(item);
            }
            
            this.menu.clear();
            this.menu.addAll(this.cachedMenuItems);
            this.noSpellMenuItem.setVisible(true);
            this.menu.add(this.noSpellMenuItem);
            
            this.needsRecheckSpells = false;
        }
        
        this.menu.draw(guiGraphics, partialTick, mouseX, mouseY);
    }
    
    private boolean trySwitch(int slotNumber) {
        ItemStack inMainHand = this.minecraft.player.getMainHandItem();
        ItemStack inOffHand = this.minecraft.player.getOffhandItem();
        if (!(inMainHand.getItem() instanceof IWand) && !(inOffHand.getItem() instanceof IWand)) {
            return false;
        }
        
        // Send packet to server signaling a spell switch
        PacketHandler.sendToServer(new SetActiveSpellPacket(slotNumber));
        
        this.menu.close();
        return true;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}

/*
 Note: This code was adapted from David Quintana's implementation in Tool Belt.
 Below is the copyright notice.
 
 Copyright (c) 2015, David Quintana <gigaherz@gmail.com>
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
     * Redistributions of source code must retain the above copyright
       notice, this list of conditions and the following disclaimer.
     * Redistributions in binary form must reproduce the above copyright
       notice, this list of conditions and the following disclaimer in the
       documentation and/or other materials provided with the distribution.
     * Neither the name of the author nor the
       names of the contributors may be used to endorse or promote products
       derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
