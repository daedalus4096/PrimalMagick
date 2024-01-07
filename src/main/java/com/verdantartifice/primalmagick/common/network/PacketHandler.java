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
            .messageBuilder(OfferingChannelPacket.class, OfferingChannelPacket.direction()).encoder(OfferingChannelPacket::encode).decoder(OfferingChannelPacket::decode).consumerMainThread(OfferingChannelPacket::onMessage).add()
            .messageBuilder(PropMarkerPacket.class, PropMarkerPacket.direction()).encoder(PropMarkerPacket::encode).decoder(PropMarkerPacket::decode).consumerMainThread(PropMarkerPacket::onMessage).add()
            .messageBuilder(RemovePropMarkerPacket.class, RemovePropMarkerPacket.direction()).encoder(RemovePropMarkerPacket::encode).decoder(RemovePropMarkerPacket::decode).consumerMainThread(RemovePropMarkerPacket::onMessage).add()
            .messageBuilder(SyncCompanionsPacket.class, SyncCompanionsPacket.direction()).encoder(SyncCompanionsPacket::encode).decoder(SyncCompanionsPacket::decode).consumerMainThread(SyncCompanionsPacket::onMessage).add()
            .messageBuilder(ScanEntityPacket.class, ScanEntityPacket.direction()).encoder(ScanEntityPacket::encode).decoder(ScanEntityPacket::decode).consumerMainThread(ScanEntityPacket::onMessage).add()
            .messageBuilder(PotionExplosionPacket.class, PotionExplosionPacket.direction()).encoder(PotionExplosionPacket::encode).decoder(PotionExplosionPacket::decode).consumerMainThread(PotionExplosionPacket::onMessage).add()
            .messageBuilder(UpdateResearchPacket.class, UpdateResearchPacket.direction()).encoder(UpdateResearchPacket::encode).decoder(UpdateResearchPacket::decode).consumerMainThread(UpdateResearchPacket::onMessage).add()
            .messageBuilder(UpdateAffinitiesPacket.class, UpdateAffinitiesPacket.direction()).encoder(UpdateAffinitiesPacket::encode).decoder(UpdateAffinitiesPacket::decode).consumerMainThread(UpdateAffinitiesPacket::onMessage).add()
            .messageBuilder(UpdateTheorycraftingPacket.class, UpdateTheorycraftingPacket.direction()).encoder(UpdateTheorycraftingPacket::encode).decoder(UpdateTheorycraftingPacket::decode).consumerMainThread(UpdateTheorycraftingPacket::onMessage).add()
            .messageBuilder(SyncArcaneRecipeBookPacket.class, SyncArcaneRecipeBookPacket.direction()).encoder(SyncArcaneRecipeBookPacket::encode).decoder(SyncArcaneRecipeBookPacket::decode).consumerMainThread(SyncArcaneRecipeBookPacket::onMessage).add()
            .messageBuilder(PlaceArcaneRecipePacket.class, PlaceArcaneRecipePacket.direction()).encoder(PlaceArcaneRecipePacket::encode).decoder(PlaceArcaneRecipePacket::decode).consumerMainThread(PlaceArcaneRecipePacket::onMessage).add()
            .messageBuilder(PlaceGhostArcaneRecipePacket.class, PlaceGhostArcaneRecipePacket.direction()).encoder(PlaceGhostArcaneRecipePacket::encode).decoder(PlaceGhostArcaneRecipePacket::decode).consumerMainThread(PlaceGhostArcaneRecipePacket::onMessage).add()
            .messageBuilder(SeenArcaneRecipePacket.class, SeenArcaneRecipePacket.direction()).encoder(SeenArcaneRecipePacket::encode).decoder(SeenArcaneRecipePacket::decode).consumerMainThread(SeenArcaneRecipePacket::onMessage).add()
            .messageBuilder(ChangeArcaneRecipeBookSettingsPacket.class, ChangeArcaneRecipeBookSettingsPacket.direction()).encoder(ChangeArcaneRecipeBookSettingsPacket::encode).decoder(ChangeArcaneRecipeBookSettingsPacket::decode).consumerMainThread(ChangeArcaneRecipeBookSettingsPacket::onMessage).add()
            .messageBuilder(SetResearchTopicHistoryPacket.class, SetResearchTopicHistoryPacket.direction()).encoder(SetResearchTopicHistoryPacket::encode).decoder(SetResearchTopicHistoryPacket::decode).consumerMainThread(SetResearchTopicHistoryPacket::onMessage).add()
            .messageBuilder(SpellcraftingRunePacket.class, SpellcraftingRunePacket.direction()).encoder(SpellcraftingRunePacket::encode).decoder(SpellcraftingRunePacket::decode).consumerMainThread(SpellcraftingRunePacket::onMessage).add()
            .messageBuilder(SetActiveSpellPacket.class, SetActiveSpellPacket.direction()).encoder(SetActiveSpellPacket::encode).decoder(SetActiveSpellPacket::decode).consumerMainThread(SetActiveSpellPacket::onMessage).add()
            .messageBuilder(WithdrawCaskEssencePacket.class, WithdrawCaskEssencePacket.direction()).encoder(WithdrawCaskEssencePacket::encode).decoder(WithdrawCaskEssencePacket::decode).consumerMainThread(WithdrawCaskEssencePacket::onMessage).add()
            .messageBuilder(UpdateRuneEnchantmentsPacket.class, UpdateRuneEnchantmentsPacket.direction()).encoder(UpdateRuneEnchantmentsPacket::encode).decoder(UpdateRuneEnchantmentsPacket::decode).consumerMainThread(UpdateRuneEnchantmentsPacket::onMessage).add()
            .messageBuilder(OpenGrimoireScreenPacket.class, OpenGrimoireScreenPacket.direction()).encoder(OpenGrimoireScreenPacket::encode).decoder(OpenGrimoireScreenPacket::decode).consumerMainThread(OpenGrimoireScreenPacket::onMessage).add()
            .messageBuilder(SyncWardPacket.class, SyncWardPacket.direction()).encoder(SyncWardPacket::encode).decoder(SyncWardPacket::decode).consumerMainThread(SyncWardPacket::onMessage).add()
            .messageBuilder(OpenStaticBookScreenPacket.class, OpenStaticBookScreenPacket.direction()).encoder(OpenStaticBookScreenPacket::encode).decoder(OpenStaticBookScreenPacket::decode).consumerMainThread(OpenStaticBookScreenPacket::onMessage).add()
            .messageBuilder(OpenEnchantedBookScreenPacket.class, OpenEnchantedBookScreenPacket.direction()).encoder(OpenEnchantedBookScreenPacket::encode).decoder(OpenEnchantedBookScreenPacket::decode).consumerMainThread(OpenEnchantedBookScreenPacket::onMessage).add()
            .messageBuilder(SyncLinguisticsPacket.class, SyncLinguisticsPacket.direction()).encoder(SyncLinguisticsPacket::encode).decoder(SyncLinguisticsPacket::decode).consumerMainThread(SyncLinguisticsPacket::onMessage).add()
            .messageBuilder(ContainerSetVarintDataPacket.class, ContainerSetVarintDataPacket.direction()).encoder(ContainerSetVarintDataPacket::encode).decoder(ContainerSetVarintDataPacket::decode).consumerMainThread(ContainerSetVarintDataPacket::onMessage).add()
            ;
    }
    
    public static void sendToServer(IMessageToServer message) {
        // Send a packet from a client to the server
        INSTANCE.send(message, PacketDistributor.SERVER.noArg());
    }
    
    public static void sendToPlayer(IMessageToClient message, ServerPlayer player) {
        // Send a message from the server to a specific player's client
        INSTANCE.send(message, PacketDistributor.PLAYER.with(player));
    }
    
    public static void sendToAllAround(IMessageToClient message, ResourceKey<Level> dimension, BlockPos center, double radius) {
        // Send a message to the clients of all players within a given distance of the given world position
        INSTANCE.send(message, PacketDistributor.NEAR.with(new PacketDistributor.TargetPoint(center.getX() + 0.5D, center.getY() + 0.5D, center.getZ() + 0.5D, radius, dimension)));
    }
}
