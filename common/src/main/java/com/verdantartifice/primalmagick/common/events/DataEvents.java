package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.common.affinities.AffinityManager;
import com.verdantartifice.primalmagick.common.books.LinguisticsManager;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.data.UpdateAffinitiesPacket;
import com.verdantartifice.primalmagick.common.network.packets.data.UpdateLinguisticsGridsPacket;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

/**
 * Handlers for data-related events.
 * 
 * @author Daedalus4096
 */
public class DataEvents {
    public static void onDatapackSync(List<ServerPlayer> players) {
        players.forEach(DataEvents::syncToPlayer);
    }

    public static void onDatapackSync(ServerPlayer player) {
        syncToPlayer(player);
    }
    
    protected static void syncToPlayer(ServerPlayer player) {
        PacketHandler.sendToPlayer(new UpdateAffinitiesPacket(AffinityManager.getInstance().getAllAffinities()), player);
        PacketHandler.sendToPlayer(new UpdateLinguisticsGridsPacket(LinguisticsManager.getAllGridDefinitions()), player);
    }
}
