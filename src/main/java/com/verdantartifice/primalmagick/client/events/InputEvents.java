package com.verdantartifice.primalmagick.client.events;

import org.lwjgl.glfw.GLFW;

import com.verdantartifice.primalmagick.PrimalMagic;
import com.verdantartifice.primalmagick.client.config.KeyBindings;
import com.verdantartifice.primalmagick.common.entities.misc.FlyingCarpetEntity;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.misc.CycleActiveSpellPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Respond to client-only input-related events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagic.MODID, value=Dist.CLIENT)
public class InputEvents {
    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (KeyBindings.changeSpellKey.consumeClick()) {
            boolean shift = (event.getModifiers() & GLFW.GLFW_MOD_SHIFT) != 0;  // Cycle spells in reverse if shift is pressed as well
            PacketHandler.sendToServer(new CycleActiveSpellPacket(shift));
        }
        
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player != null) {
            Entity ridingEntity = player.getVehicle();
            if (ridingEntity != null && ridingEntity instanceof FlyingCarpetEntity) {
                ((FlyingCarpetEntity)ridingEntity).updateInputs(KeyBindings.carpetForwardKey.isDown(), KeyBindings.carpetBackwardKey.isDown());
            }
        }
    }
}
