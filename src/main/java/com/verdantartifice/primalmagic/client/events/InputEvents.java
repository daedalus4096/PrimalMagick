package com.verdantartifice.primalmagic.client.events;

import org.lwjgl.glfw.GLFW;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.config.KeyBindings;
import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.misc.CycleActiveSpellPacket;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Respond to client-only input-related events.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid=PrimalMagic.MODID, value=Dist.CLIENT)
public class InputEvents {
    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (KeyBindings.changeSpellKey.isPressed()) {
            boolean shift = (event.getModifiers() & GLFW.GLFW_MOD_SHIFT) != 0;  // Cycle spells in reverse if shift is pressed as well
            PacketHandler.sendToServer(new CycleActiveSpellPacket(shift));
        }
    }
}
