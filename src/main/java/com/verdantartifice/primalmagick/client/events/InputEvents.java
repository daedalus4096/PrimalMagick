package com.verdantartifice.primalmagick.client.events;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.config.KeyBindings;
import com.verdantartifice.primalmagick.client.gui.SpellSelectionRadialScreen;
import com.verdantartifice.primalmagick.common.entities.misc.FlyingCarpetEntity;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.misc.CycleActiveSpellPacket;
import com.verdantartifice.primalmagick.common.wands.IWand;

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
@Mod.EventBusSubscriber(modid=PrimalMagick.MODID, value=Dist.CLIENT)
public class InputEvents {
    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (KeyBindings.changeSpellKey.isDown() && mc.screen == null && mc.player.getMainHandItem().getItem() instanceof IWand) {
//            boolean shift = (event.getModifiers() & GLFW.GLFW_MOD_SHIFT) != 0;  // Cycle spells in reverse if shift is pressed as well
//            PacketHandler.sendToServer(new CycleActiveSpellPacket(shift));
            LOGGER.info("Opening spell selection radial screen");
            mc.setScreen(new SpellSelectionRadialScreen());
        }
        
        Player player = mc.player;
        if (player != null) {
            Entity ridingEntity = player.getVehicle();
            if (ridingEntity != null && ridingEntity instanceof FlyingCarpetEntity) {
                ((FlyingCarpetEntity)ridingEntity).updateInputs(KeyBindings.carpetForwardKey.isDown(), KeyBindings.carpetBackwardKey.isDown());
            }
        }
    }
}
