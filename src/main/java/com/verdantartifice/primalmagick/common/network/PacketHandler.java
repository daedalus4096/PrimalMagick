package com.verdantartifice.primalmagick.common.network;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.network.packets.data.ContainerSetVarintDataPacket;
import com.verdantartifice.primalmagick.common.network.packets.data.SetResearchTopicHistoryPacket;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncArcaneRecipeBookPacket;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncAttunementsPacket;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncCompanionsPacket;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncCooldownsPacket;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncKnowledgePacket;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncLinguisticsPacket;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncProgressPacket;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncResearchFlagsPacket;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncStatsPacket;
import com.verdantartifice.primalmagick.common.network.packets.data.SyncWardPacket;
import com.verdantartifice.primalmagick.common.network.packets.data.TileToClientPacket;
import com.verdantartifice.primalmagick.common.network.packets.data.TileToServerPacket;
import com.verdantartifice.primalmagick.common.network.packets.data.UpdateAffinitiesPacket;
import com.verdantartifice.primalmagick.common.network.packets.data.UpdateResearchPacket;
import com.verdantartifice.primalmagick.common.network.packets.data.UpdateRuneEnchantmentsPacket;
import com.verdantartifice.primalmagick.common.network.packets.data.UpdateTheorycraftingPacket;
import com.verdantartifice.primalmagick.common.network.packets.fx.ManaSparklePacket;
import com.verdantartifice.primalmagick.common.network.packets.fx.OfferingChannelPacket;
import com.verdantartifice.primalmagick.common.network.packets.fx.PlayClientSoundPacket;
import com.verdantartifice.primalmagick.common.network.packets.fx.PotionExplosionPacket;
import com.verdantartifice.primalmagick.common.network.packets.fx.PropMarkerPacket;
import com.verdantartifice.primalmagick.common.network.packets.fx.RemovePropMarkerPacket;
import com.verdantartifice.primalmagick.common.network.packets.fx.SpellBoltPacket;
import com.verdantartifice.primalmagick.common.network.packets.fx.SpellImpactPacket;
import com.verdantartifice.primalmagick.common.network.packets.fx.SpellTrailPacket;
import com.verdantartifice.primalmagick.common.network.packets.fx.SpellcraftingRunePacket;
import com.verdantartifice.primalmagick.common.network.packets.fx.TeleportArrivalPacket;
import com.verdantartifice.primalmagick.common.network.packets.fx.WandPoofPacket;
import com.verdantartifice.primalmagick.common.network.packets.misc.AnalysisActionPacket;
import com.verdantartifice.primalmagick.common.network.packets.misc.CycleActiveSpellPacket;
import com.verdantartifice.primalmagick.common.network.packets.misc.OpenEnchantedBookScreenPacket;
import com.verdantartifice.primalmagick.common.network.packets.misc.OpenGrimoireScreenPacket;
import com.verdantartifice.primalmagick.common.network.packets.misc.OpenStaticBookScreenPacket;
import com.verdantartifice.primalmagick.common.network.packets.misc.ResetFallDistancePacket;
import com.verdantartifice.primalmagick.common.network.packets.misc.ScanEntityPacket;
import com.verdantartifice.primalmagick.common.network.packets.misc.ScanItemPacket;
import com.verdantartifice.primalmagick.common.network.packets.misc.ScanPositionPacket;
import com.verdantartifice.primalmagick.common.network.packets.misc.SetActiveSpellPacket;
import com.verdantartifice.primalmagick.common.network.packets.misc.WithdrawCaskEssencePacket;
import com.verdantartifice.primalmagick.common.network.packets.recipe_book.ChangeArcaneRecipeBookSettingsPacket;
import com.verdantartifice.primalmagick.common.network.packets.recipe_book.PlaceArcaneRecipePacket;
import com.verdantartifice.primalmagick.common.network.packets.recipe_book.PlaceGhostArcaneRecipePacket;
import com.verdantartifice.primalmagick.common.network.packets.recipe_book.SeenArcaneRecipePacket;
import com.verdantartifice.primalmagick.common.network.packets.spellcrafting.SetSpellComponentPropertyPacket;
import com.verdantartifice.primalmagick.common.network.packets.spellcrafting.SetSpellComponentTypeIndexPacket;
import com.verdantartifice.primalmagick.common.network.packets.spellcrafting.SetSpellNamePacket;
import com.verdantartifice.primalmagick.common.network.packets.theorycrafting.CompleteProjectPacket;
import com.verdantartifice.primalmagick.common.network.packets.theorycrafting.SetProjectMaterialSelectionPacket;
import com.verdantartifice.primalmagick.common.network.packets.theorycrafting.StartProjectPacket;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.Channel.VersionTest;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

/**
 * Handler class for processing packets.  Responsible for all custom communication between the client and the server.
 * 
 * @author Daedalus4096
 */
public class PacketHandler {
    private static final int PROTOCOL_VERSION = 1;
    
    private static final SimpleChannel INSTANCE = ChannelBuilder
            .named(PrimalMagick.resource("main_channel"))
            .clientAcceptedVersions(VersionTest.exact(PROTOCOL_VERSION))
            .serverAcceptedVersions(VersionTest.exact(PROTOCOL_VERSION))
            .networkProtocolVersion(PROTOCOL_VERSION)
            .simpleChannel();
    
    public static void registerMessages() {
        INSTANCE
            .messageBuilder(SyncKnowledgePacket.class, SyncKnowledgePacket.direction()).encoder(SyncKnowledgePacket::encode).decoder(SyncKnowledgePacket::decode).consumerMainThread(SyncKnowledgePacket::onMessage).add()
            .messageBuilder(SyncProgressPacket.class, SyncProgressPacket.direction()).encoder(SyncProgressPacket::encode).decoder(SyncProgressPacket::decode).consumerMainThread(SyncProgressPacket::onMessage).add()
            .messageBuilder(SyncResearchFlagsPacket.class, SyncResearchFlagsPacket.direction()).encoder(SyncResearchFlagsPacket::encode).decoder(SyncResearchFlagsPacket::decode).consumerMainThread(SyncResearchFlagsPacket::onMessage).add()
            .messageBuilder(WandPoofPacket.class, WandPoofPacket.direction()).encoder(WandPoofPacket::encode).decoder(WandPoofPacket::decode).consumerMainThread(WandPoofPacket::onMessage).add()
            .messageBuilder(ManaSparklePacket.class, ManaSparklePacket.direction()).encoder(ManaSparklePacket::encode).decoder(ManaSparklePacket::decode).consumerMainThread(ManaSparklePacket::onMessage).add()
            .messageBuilder(ScanItemPacket.class, ScanItemPacket.direction()).encoder(ScanItemPacket::encode).decoder(ScanItemPacket::decode).consumerMainThread(ScanItemPacket::onMessage).add()
            .messageBuilder(ScanPositionPacket.class, ScanPositionPacket.direction()).encoder(ScanPositionPacket::encode).decoder(ScanPositionPacket::decode).consumerMainThread(ScanPositionPacket::onMessage).add()
            .messageBuilder(AnalysisActionPacket.class, AnalysisActionPacket.direction()).encoder(AnalysisActionPacket::encode).decoder(AnalysisActionPacket::decode).consumerMainThread(AnalysisActionPacket::onMessage).add()
            .messageBuilder(SyncCooldownsPacket.class, SyncCooldownsPacket.direction()).encoder(SyncCooldownsPacket::encode).decoder(SyncCooldownsPacket::decode).consumerMainThread(SyncCooldownsPacket::onMessage).add()
            .messageBuilder(CycleActiveSpellPacket.class, CycleActiveSpellPacket.direction()).encoder(CycleActiveSpellPacket::encode).decoder(CycleActiveSpellPacket::decode).consumerMainThread(CycleActiveSpellPacket::onMessage).add()
            .messageBuilder(SetSpellNamePacket.class, SetSpellNamePacket.direction()).encoder(SetSpellNamePacket::encode).decoder(SetSpellNamePacket::decode).consumerMainThread(SetSpellNamePacket::onMessage).add()
            .messageBuilder(SetSpellComponentTypeIndexPacket.class, SetSpellComponentTypeIndexPacket.direction()).encoder(SetSpellComponentTypeIndexPacket::encode).decoder(SetSpellComponentTypeIndexPacket::decode).consumerMainThread(SetSpellComponentTypeIndexPacket::onMessage).add()
            .messageBuilder(SetSpellComponentPropertyPacket.class, SetSpellComponentPropertyPacket.direction()).encoder(SetSpellComponentPropertyPacket::encode).decoder(SetSpellComponentPropertyPacket::decode).consumerMainThread(SetSpellComponentPropertyPacket::onMessage).add()
            .messageBuilder(SpellTrailPacket.class, SpellTrailPacket.direction()).encoder(SpellTrailPacket::encode).decoder(SpellTrailPacket::decode).consumerMainThread(SpellTrailPacket::onMessage).add()
            .messageBuilder(SpellImpactPacket.class, SpellImpactPacket.direction()).encoder(SpellImpactPacket::encode).decoder(SpellImpactPacket::decode).consumerMainThread(SpellImpactPacket::onMessage).add()
            .messageBuilder(TileToClientPacket.class, TileToClientPacket.direction()).encoder(TileToClientPacket::encode).decoder(TileToClientPacket::decode).consumerMainThread(TileToClientPacket::onMessage).add()
            .messageBuilder(TileToServerPacket.class, TileToServerPacket.direction()).encoder(TileToServerPacket::encode).decoder(TileToServerPacket::decode).consumerMainThread(TileToServerPacket::onMessage).add()
            .messageBuilder(TeleportArrivalPacket.class, TeleportArrivalPacket.direction()).encoder(TeleportArrivalPacket::encode).decoder(TeleportArrivalPacket::decode).consumerMainThread(TeleportArrivalPacket::onMessage).add()
            .messageBuilder(SpellBoltPacket.class, SpellBoltPacket.direction()).encoder(SpellBoltPacket::encode).decoder(SpellBoltPacket::decode).consumerMainThread(SpellBoltPacket::onMessage).add()
            .messageBuilder(SyncStatsPacket.class, SyncStatsPacket.direction()).encoder(SyncStatsPacket::encode).decoder(SyncStatsPacket::decode).consumerMainThread(SyncStatsPacket::onMessage).add()
            .messageBuilder(SyncAttunementsPacket.class, SyncAttunementsPacket.direction()).encoder(SyncAttunementsPacket::encode).decoder(SyncAttunementsPacket::decode).consumerMainThread(SyncAttunementsPacket::onMessage).add()
            .messageBuilder(ResetFallDistancePacket.class, ResetFallDistancePacket.direction()).encoder(ResetFallDistancePacket::encode).decoder(ResetFallDistancePacket::decode).consumerMainThread(ResetFallDistancePacket::onMessage).add()
            .messageBuilder(StartProjectPacket.class, StartProjectPacket.direction()).encoder(StartProjectPacket::encode).decoder(StartProjectPacket::decode).consumerMainThread(StartProjectPacket::onMessage).add()
            .messageBuilder(CompleteProjectPacket.class, CompleteProjectPacket.direction()).encoder(CompleteProjectPacket::encode).decoder(CompleteProjectPacket::decode).consumerMainThread(CompleteProjectPacket::onMessage).add()
            .messageBuilder(SetProjectMaterialSelectionPacket.class, SetProjectMaterialSelectionPacket.direction()).encoder(SetProjectMaterialSelectionPacket::encode).decoder(SetProjectMaterialSelectionPacket::decode).consumerMainThread(SetProjectMaterialSelectionPacket::onMessage).add()
            .messageBuilder(PlayClientSoundPacket.class, PlayClientSoundPacket.direction()).encoder(PlayClientSoundPacket::encode).decoder(PlayClientSoundPacket::decode).consumerMainThread(PlayClientSoundPacket::onMessage).add()
            
            .messageBuilder(ContainerSetVarintDataPacket.class, ContainerSetVarintDataPacket.direction()).encoder(ContainerSetVarintDataPacket::encode).decoder(ContainerSetVarintDataPacket::decode).consumerMainThread(ContainerSetVarintDataPacket::onMessage).add()
            ;
            
        int disc = 0;
        
        INSTANCE.registerMessage(disc++, OfferingChannelPacket.class, OfferingChannelPacket::encode, OfferingChannelPacket::decode, OfferingChannelPacket.Handler::onMessage);
        INSTANCE.registerMessage(disc++, PropMarkerPacket.class, PropMarkerPacket::encode, PropMarkerPacket::decode, PropMarkerPacket.Handler::onMessage);
        INSTANCE.registerMessage(disc++, RemovePropMarkerPacket.class, RemovePropMarkerPacket::encode, RemovePropMarkerPacket::decode, RemovePropMarkerPacket.Handler::onMessage);
        INSTANCE.registerMessage(disc++, SyncCompanionsPacket.class, SyncCompanionsPacket::encode, SyncCompanionsPacket::decode, SyncCompanionsPacket.Handler::onMessage);
        INSTANCE.registerMessage(disc++, ScanEntityPacket.class, ScanEntityPacket::encode, ScanEntityPacket::decode, ScanEntityPacket.Handler::onMessage);
        INSTANCE.registerMessage(disc++, PotionExplosionPacket.class, PotionExplosionPacket::encode, PotionExplosionPacket::decode, PotionExplosionPacket.Handler::onMessage);
        INSTANCE.registerMessage(disc++, UpdateResearchPacket.class, UpdateResearchPacket::encode, UpdateResearchPacket::decode, UpdateResearchPacket.Handler::onMessage);
        INSTANCE.registerMessage(disc++, UpdateAffinitiesPacket.class, UpdateAffinitiesPacket::encode, UpdateAffinitiesPacket::decode, UpdateAffinitiesPacket.Handler::onMessage);
        INSTANCE.registerMessage(disc++, UpdateTheorycraftingPacket.class, UpdateTheorycraftingPacket::encode, UpdateTheorycraftingPacket::decode, UpdateTheorycraftingPacket.Handler::onMessage);
        INSTANCE.registerMessage(disc++, SyncArcaneRecipeBookPacket.class, SyncArcaneRecipeBookPacket::encode, SyncArcaneRecipeBookPacket::decode, SyncArcaneRecipeBookPacket.Handler::onMessage);
        INSTANCE.registerMessage(disc++, PlaceArcaneRecipePacket.class, PlaceArcaneRecipePacket::encode, PlaceArcaneRecipePacket::decode, PlaceArcaneRecipePacket.Handler::onMessage);
        INSTANCE.registerMessage(disc++, PlaceGhostArcaneRecipePacket.class, PlaceGhostArcaneRecipePacket::encode, PlaceGhostArcaneRecipePacket::decode, PlaceGhostArcaneRecipePacket.Handler::onMessage);
        INSTANCE.registerMessage(disc++, SeenArcaneRecipePacket.class, SeenArcaneRecipePacket::encode, SeenArcaneRecipePacket::decode, SeenArcaneRecipePacket.Handler::onMessage);
        INSTANCE.registerMessage(disc++, ChangeArcaneRecipeBookSettingsPacket.class, ChangeArcaneRecipeBookSettingsPacket::encode, ChangeArcaneRecipeBookSettingsPacket::decode, ChangeArcaneRecipeBookSettingsPacket.Handler::onMessage);
        INSTANCE.registerMessage(disc++, SetResearchTopicHistoryPacket.class, SetResearchTopicHistoryPacket::encode, SetResearchTopicHistoryPacket::decode, SetResearchTopicHistoryPacket.Handler::onMessage);
        INSTANCE.registerMessage(disc++, SpellcraftingRunePacket.class, SpellcraftingRunePacket::encode, SpellcraftingRunePacket::decode, SpellcraftingRunePacket.Handler::onMessage);
        INSTANCE.registerMessage(disc++, SetActiveSpellPacket.class, SetActiveSpellPacket::encode, SetActiveSpellPacket::decode, SetActiveSpellPacket.Handler::onMessage);
        INSTANCE.registerMessage(disc++, WithdrawCaskEssencePacket.class, WithdrawCaskEssencePacket::encode, WithdrawCaskEssencePacket::decode, WithdrawCaskEssencePacket.Handler::onMessage);
        INSTANCE.registerMessage(disc++, UpdateRuneEnchantmentsPacket.class, UpdateRuneEnchantmentsPacket::encode, UpdateRuneEnchantmentsPacket::decode, UpdateRuneEnchantmentsPacket.Handler::onMessage);
        INSTANCE.registerMessage(disc++, OpenGrimoireScreenPacket.class, OpenGrimoireScreenPacket::encode, OpenGrimoireScreenPacket::decode, OpenGrimoireScreenPacket.Handler::onMessage);
        INSTANCE.registerMessage(disc++, SyncWardPacket.class, SyncWardPacket::encode, SyncWardPacket::decode, SyncWardPacket.Handler::onMessage);
        INSTANCE.registerMessage(disc++, OpenStaticBookScreenPacket.class, OpenStaticBookScreenPacket::encode, OpenStaticBookScreenPacket::decode, OpenStaticBookScreenPacket.Handler::onMessage);
        INSTANCE.registerMessage(disc++, OpenEnchantedBookScreenPacket.class, OpenEnchantedBookScreenPacket::encode, OpenEnchantedBookScreenPacket::decode, OpenEnchantedBookScreenPacket.Handler::onMessage);
        INSTANCE.registerMessage(disc++, SyncLinguisticsPacket.class, SyncLinguisticsPacket::encode, SyncLinguisticsPacket::decode, SyncLinguisticsPacket.Handler::onMessage);
    }
    
    public static void sendToServer(IMessageToServer message) {
        // Send a packet from a client to the server
        INSTANCE.sendToServer(message);
    }
    
    public static void sendToPlayer(IMessageToClient message, ServerPlayer player) {
        // Send a message from the server to a specific player's client
        INSTANCE.sendTo(message, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
    
    public static void sendToAllAround(IMessageToClient message, ResourceKey<Level> dimension, BlockPos center, double radius) {
        // Send a message to the clients of all players within a given distance of the given world position
        INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(center.getX() + 0.5D, center.getY() + 0.5D, center.getZ() + 0.5D, radius, dimension)), message);
    }
}
