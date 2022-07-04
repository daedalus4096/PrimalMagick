package com.verdantartifice.primalmagick.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.blaze3d.vertex.PoseStack;
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
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class SpellSelectionRadialScreen extends Screen {
    private static final Logger LOGGER = LogManager.getLogger();
    
    private ItemStack stackEquipped = ItemStack.EMPTY;
    private boolean needsRecheckSpells = true;
    
    private final GenericRadialMenu menu;
    private final List<RadialMenuItem> cachedMenuItems = new ArrayList<>();
    private final ImageRadialMenuItem noSpellMenuItem;

    public SpellSelectionRadialScreen() {
        super(Component.empty());
        Minecraft mc = Minecraft.getInstance();
        this.stackEquipped = mc.player.getMainHandItem();
        this.menu = new GenericRadialMenu(mc, new IRadialMenuHost() {
            @Override
            public void renderTooltip(PoseStack matrixStack, ItemStack stack, int mouseX, int mouseY)
            {
                SpellSelectionRadialScreen.this.renderTooltip(matrixStack, stack, mouseX, mouseY);
            }

            @Override
            public void renderTooltip(PoseStack matrixStack, List<Component> textComponents, int mouseX, int mouseY)
            {
                SpellSelectionRadialScreen.this.renderTooltip(matrixStack, textComponents, Optional.empty(), mouseX, mouseY);
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

            @Override
            public ItemRenderer getItemRenderer()
            {
                return SpellSelectionRadialScreen.this.getItemRenderer();
            }
        })
        {
            @Override
            public void onClickOutside()
            {
                LOGGER.info("Clicked outside, closing underlying radial menu");
                close();
            }
        };
        this.noSpellMenuItem = new ImageRadialMenuItem(this.menu, -1, new ResourceLocation("textures/item/barrier.png"), Component.translatable("primalmagick.spells.no_spell_selection")) {
            @Override
            public boolean onClick() {
                return SpellSelectionRadialScreen.this.trySwitch(getSlot());
            }
        };
    }

    protected ItemRenderer getItemRenderer() {
        return this.itemRenderer;
    }

    @Override
    public void removed() {
        super.removed();
        LOGGER.info("Removed spell selection radial screen");
        InputEvents.wipeOpen();
    }

    @Override
    public void tick() {
        super.tick();
        this.menu.tick();
        
        if (this.menu.isClosed()) {
            LOGGER.info("Closing radial screen because underlying menu is closed");
            this.minecraft.setScreen(null);
            InputEvents.wipeOpen();
        }
        if (!this.menu.isReady()) {
            return;
        }
        
        ItemStack inHand = this.minecraft.player.getMainHandItem();
        if (inHand.getItem() instanceof IWand wand) {
            if (this.stackEquipped != inHand) {     // Reference comparison intended
                this.stackEquipped = inHand;
                this.needsRecheckSpells = true;
            }
        } else {
            this.stackEquipped = ItemStack.EMPTY;
        }
        
        if (this.stackEquipped.isEmpty()) {
            LOGGER.info("Closing radial screen because equipped item stack is empty");
            this.minecraft.setScreen(null);
        } else if (!InputEvents.isKeyDown(KeyBindings.changeSpellKey)) {
            if (Config.RADIAL_RELEASE_TO_SWITCH.get()) {
                LOGGER.info("Menu key release detected, processing click");
                this.processClick(false);
            } else {
                LOGGER.info("Closing underlying radial menu because key is no longer down");
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
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTick) {
        matrixStack.pushPose();
        super.render(matrixStack, mouseX, mouseY, partialTick);
        matrixStack.popPose();
        
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
        
        this.menu.draw(matrixStack, partialTick, mouseX, mouseY);
    }
    
    private boolean trySwitch(int slotNumber) {
        ItemStack inHand = this.minecraft.player.getMainHandItem();
        if (!(inHand.getItem() instanceof IWand)) {
            return false;
        }
        
        // Send packet to server signaling a spell switch
        LOGGER.info("Switching to {} and closing underlying radial menu", slotNumber);
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
