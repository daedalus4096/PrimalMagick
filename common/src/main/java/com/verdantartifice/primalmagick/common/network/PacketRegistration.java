package com.verdantartifice.primalmagick.common.network;

import com.verdantartifice.primalmagick.common.network.packets.config.AcknowledgementPacket;
import com.verdantartifice.primalmagick.common.network.packets.config.UpdateAffinitiesConfigPacket;
import com.verdantartifice.primalmagick.common.network.packets.config.UpdateLinguisticsGridsConfigPacket;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncCooldownsPacket;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncKnowledgePacket;
import com.verdantartifice.primalmagick.common.network.packets.data.TileToClientPacket;
import com.verdantartifice.primalmagick.common.network.packets.fx.ManaSparklePacket;
import com.verdantartifice.primalmagick.common.network.packets.fx.SpellBoltPacket;
import com.verdantartifice.primalmagick.common.network.packets.fx.SpellImpactPacket;
import com.verdantartifice.primalmagick.common.network.packets.fx.SpellTrailPacket;
import com.verdantartifice.primalmagick.common.network.packets.fx.TeleportArrivalPacket;
import com.verdantartifice.primalmagick.common.network.packets.fx.WandPoofPacket;
import commonnetwork.api.Network;

public class PacketRegistration {
    public static void registerMessages() {
        Network
                // Client-bound configuration channel packets
                .registerConfigurationPacket(UpdateAffinitiesConfigPacket.type(), UpdateAffinitiesConfigPacket.class, UpdateAffinitiesConfigPacket.STREAM_CODEC, UpdateAffinitiesConfigPacket::onMessage)
                .registerConfigurationPacket(UpdateLinguisticsGridsConfigPacket.type(), UpdateLinguisticsGridsConfigPacket.class, UpdateLinguisticsGridsConfigPacket.STREAM_CODEC, UpdateLinguisticsGridsConfigPacket::onMessage)
                // Server-bound configuration channel packets
                .registerConfigurationPacket(AcknowledgementPacket.type(), AcknowledgementPacket.class, AcknowledgementPacket.STREAM_CODEC, AcknowledgementPacket::onMessage)
                // Client-bound play channel packets
                .registerPacket(SyncKnowledgePacket.type(), SyncKnowledgePacket.class, SyncKnowledgePacket.STREAM_CODEC, SyncKnowledgePacket::onMessage)
                .registerPacket(WandPoofPacket.type(), WandPoofPacket.class, WandPoofPacket.STREAM_CODEC, WandPoofPacket::onMessage)
                .registerPacket(ManaSparklePacket.type(), ManaSparklePacket.class, ManaSparklePacket.STREAM_CODEC, ManaSparklePacket::onMessage)
                .registerPacket(SyncCooldownsPacket.type(), SyncCooldownsPacket.class, SyncCooldownsPacket.STREAM_CODEC, SyncCooldownsPacket::onMessage)
                .registerPacket(SpellTrailPacket.type(), SpellTrailPacket.class, SpellTrailPacket.STREAM_CODEC, SpellTrailPacket::onMessage)
                .registerPacket(SpellImpactPacket.type(), SpellImpactPacket.class, SpellImpactPacket.STREAM_CODEC, SpellImpactPacket::onMessage)
                .registerPacket(TileToClientPacket.type(), TileToClientPacket.class, TileToClientPacket.STREAM_CODEC, TileToClientPacket::onMessage)
                .registerPacket(TeleportArrivalPacket.type(), TeleportArrivalPacket.class, TeleportArrivalPacket.STREAM_CODEC, TeleportArrivalPacket::onMessage)
                .registerPacket(SpellBoltPacket.type(), SpellBoltPacket.class, SpellBoltPacket.STREAM_CODEC, SpellBoltPacket::onMessage)
                ;
    }
}
