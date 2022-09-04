package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.affinities.AffinityManager;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.data.UpdateAffinitiesPacket;
import com.verdantartifice.primalmagick.common.network.packets.data.UpdateResearchPacket;
import com.verdantartifice.primalmagick.common.network.packets.data.UpdateRuneEnchantmentsPacket;
import com.verdantartifice.primalmagick.common.network.packets.data.UpdateTheorycraftingPacket;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.runes.RuneManager;
import com.verdantartifice.primalmagick.common.theorycrafting.TheorycraftManager;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handlers for data-related events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagick.MODID)
public class DataEvents {
    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        if (event.getPlayer() == null) {
            for (ServerPlayer player : event.getPlayerList().getPlayers()) {
                syncToPlayer(player);
            }
        } else {
            syncToPlayer(event.getPlayer());
        }
    }
    
    protected static void syncToPlayer(ServerPlayer player) {
        PacketHandler.sendToPlayer(new UpdateAffinitiesPacket(AffinityManager.getInstance().getAllAffinities()), player);
        PacketHandler.sendToPlayer(new UpdateResearchPacket(ResearchEntries.getAllEntries()), player);
        PacketHandler.sendToPlayer(new UpdateTheorycraftingPacket(TheorycraftManager.getAllTemplates()), player);
        PacketHandler.sendToPlayer(new UpdateRuneEnchantmentsPacket(RuneManager.getAllDefinitions()), player);
    }
}
