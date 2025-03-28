package com.verdantartifice.primalmagick.client.events;

import com.verdantartifice.primalmagick.client.config.KeyBindings;
import com.verdantartifice.primalmagick.client.gui.SpellSelectionRadialScreen;
import com.verdantartifice.primalmagick.common.entities.misc.FlyingCarpetEntity;
import com.verdantartifice.primalmagick.common.wands.IWand;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

/**
 * Respond to client-only input-related events.
 * 
 * @author Daedalus4096
 */
public class InputEvents {
    private static boolean SPELL_SELECT_KEY_WAS_DOWN = false;
    
    public static void wipeOpen() {
        while (KeyBindings.CHANGE_SPELL_KEY.consumeClick())
        {
        }
    }

    public static void updateFlyingCarpetInputs() {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player != null && player.getVehicle() instanceof FlyingCarpetEntity carpet) {
            carpet.updateInputs(KeyBindings.CARPET_FORWARD_KEY.isDown(), KeyBindings.CARPET_BACKWARD_KEY.isDown());
        }
    }
    
    public static void onClientTickEvent() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen == null && mc.player != null) {
            boolean spellSelectKeyIsDown = KeyBindings.CHANGE_SPELL_KEY.isDown();
            if (spellSelectKeyIsDown && !SPELL_SELECT_KEY_WAS_DOWN) {
                while (KeyBindings.CHANGE_SPELL_KEY.consumeClick()) {
                    if (mc.screen == null && (mc.player.getMainHandItem().getItem() instanceof IWand || mc.player.getOffhandItem().getItem() instanceof IWand)) {
                        mc.setScreen(new SpellSelectionRadialScreen());
                    }
                }
            }
            SPELL_SELECT_KEY_WAS_DOWN = spellSelectKeyIsDown;
        } else {
            SPELL_SELECT_KEY_WAS_DOWN = true;
        }
    }
}
