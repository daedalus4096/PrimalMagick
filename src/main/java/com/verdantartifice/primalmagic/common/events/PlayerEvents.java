package com.verdantartifice.primalmagic.common.events;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class PlayerEvents {
    @SubscribeEvent
    public static void playerJoinEvent(EntityJoinWorldEvent event) {
        if (!event.getWorld().isRemote && (event.getEntity() instanceof ServerPlayerEntity)) {
            ServerPlayerEntity player = (ServerPlayerEntity)event.getEntity();
            IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
            if (knowledge != null) {
                knowledge.sync(player);
            }
        }
    }
    
    @SubscribeEvent
    public static void playerCloneEvent(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            try {
                CompoundNBT nbtKnowledge = PrimalMagicCapabilities.getKnowledge(event.getOriginal()).serializeNBT();
                PrimalMagicCapabilities.getKnowledge(event.getPlayer()).deserializeNBT(nbtKnowledge);
            } catch (Exception e) {
                PrimalMagic.LOGGER.error("Failed to clone player {} knowledge", event.getOriginal().getName().getString());
            }
        }
    }
}
