package com.verdantartifice.primalmagick.common.network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
import com.verdantartifice.primalmagick.common.network.packets.data.UpdateLinguisticsGridsPacket;
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
import com.verdantartifice.primalmagick.common.network.packets.scribe_table.ChangeScribeTableModePacket;
import com.verdantartifice.primalmagick.common.network.packets.scribe_table.StudyVocabularyActionPacket;
import com.verdantartifice.primalmagick.common.network.packets.scribe_table.TranscribeActionPacket;
import com.verdantartifice.primalmagick.common.network.packets.scribe_table.UnlockGridNodeActionPacket;
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
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int PROTOCOL_VERSION = 1;
    
    private static final SimpleChannel INSTANCE = ChannelBuilder
            .named(PrimalMagick.resource("main_channel"))
            .clientAcceptedVersions(VersionTest.exact(PROTOCOL_VERSION))
            .serverAcceptedVersions(VersionTest.exact(PROTOCOL_VERSION))
            .networkProtocolVersion(PROTOCOL_VERSION)
            .simpleChannel()
                .play()
                    .clientbound()
                        .addMain(SyncKnowledgePacket.class, SyncKnowledgePacket.STREAM_CODEC, SyncKnowledgePacket::onMessage)
                        .addMain(WandPoofPacket.class, WandPoofPacket.STREAM_CODEC, WandPoofPacket::onMessage)
                        .addMain(ManaSparklePacket.class, ManaSparklePacket.STREAM_CODEC, ManaSparklePacket::onMessage)
                        .addMain(SyncCooldownsPacket.class, SyncCooldownsPacket.STREAM_CODEC, SyncCooldownsPacket::onMessage)
                        .addMain(SpellTrailPacket.class, SpellTrailPacket.STREAM_CODEC, SpellTrailPacket::onMessage)
                        .addMain(SpellImpactPacket.class, SpellImpactPacket.STREAM_CODEC, SpellImpactPacket::onMessage)
                        .addMain(TileToClientPacket.class, TileToClientPacket.STREAM_CODEC, TileToClientPacket::onMessage)
                        .addMain(TeleportArrivalPacket.class, TeleportArrivalPacket.STREAM_CODEC, TeleportArrivalPacket::onMessage)
                        .addMain(SpellBoltPacket.class, SpellBoltPacket.STREAM_CODEC, SpellBoltPacket::onMessage)
                    .serverbound()
                        .addMain(SyncProgressPacket.class, SyncProgressPacket.STREAM_CODEC, SyncProgressPacket::onMessage)
                        .addMain(SyncResearchFlagsPacket.class, SyncResearchFlagsPacket.STREAM_CODEC, SyncResearchFlagsPacket::onMessage)
                        .addMain(ScanItemPacket.class, ScanItemPacket.STREAM_CODEC, ScanItemPacket::onMessage)
                        .addMain(ScanPositionPacket.class, ScanPositionPacket.STREAM_CODEC, ScanPositionPacket::onMessage)
                        .addMain(AnalysisActionPacket.class, AnalysisActionPacket.STREAM_CODEC, AnalysisActionPacket::onMessage)
                        .addMain(CycleActiveSpellPacket.class, CycleActiveSpellPacket.STREAM_CODEC, CycleActiveSpellPacket::onMessage)
                        .addMain(SetSpellNamePacket.class, SetSpellNamePacket.STREAM_CODEC, SetSpellNamePacket::onMessage)
                        .addMain(SetSpellComponentTypeIndexPacket.class, SetSpellComponentTypeIndexPacket.STREAM_CODEC, SetSpellComponentTypeIndexPacket::onMessage)
                        .addMain(SetSpellComponentPropertyPacket.class, SetSpellComponentPropertyPacket.STREAM_CODEC, SetSpellComponentPropertyPacket::onMessage)
                        .addMain(TileToServerPacket.class, TileToServerPacket.STREAM_CODEC, TileToServerPacket::onMessage)
            .build();
    
    public static void registerMessages() {
        // The class just needs to be externally referenced by a loaded class in order to be class-loaded itself and have its SimpleChannel initialized statically
        LOGGER.debug("Registering network {} v{}", INSTANCE.getName(), INSTANCE.getProtocolVersion());
        INSTANCE
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
            .messageBuilder(UpdateAffinitiesPacket.class, UpdateAffinitiesPacket.direction()).encoder(UpdateAffinitiesPacket::encode).decoder(UpdateAffinitiesPacket::decode).consumerMainThread(UpdateAffinitiesPacket::onMessage).add()
            .messageBuilder(SyncArcaneRecipeBookPacket.class, SyncArcaneRecipeBookPacket.direction()).encoder(SyncArcaneRecipeBookPacket::encode).decoder(SyncArcaneRecipeBookPacket::decode).consumerMainThread(SyncArcaneRecipeBookPacket::onMessage).add()
            .messageBuilder(PlaceArcaneRecipePacket.class, PlaceArcaneRecipePacket.direction()).encoder(PlaceArcaneRecipePacket::encode).decoder(PlaceArcaneRecipePacket::decode).consumerMainThread(PlaceArcaneRecipePacket::onMessage).add()
            .messageBuilder(PlaceGhostArcaneRecipePacket.class, PlaceGhostArcaneRecipePacket.direction()).encoder(PlaceGhostArcaneRecipePacket::encode).decoder(PlaceGhostArcaneRecipePacket::decode).consumerMainThread(PlaceGhostArcaneRecipePacket::onMessage).add()
            .messageBuilder(SeenArcaneRecipePacket.class, SeenArcaneRecipePacket.direction()).encoder(SeenArcaneRecipePacket::encode).decoder(SeenArcaneRecipePacket::decode).consumerMainThread(SeenArcaneRecipePacket::onMessage).add()
            .messageBuilder(ChangeArcaneRecipeBookSettingsPacket.class, ChangeArcaneRecipeBookSettingsPacket.direction()).encoder(ChangeArcaneRecipeBookSettingsPacket::encode).decoder(ChangeArcaneRecipeBookSettingsPacket::decode).consumerMainThread(ChangeArcaneRecipeBookSettingsPacket::onMessage).add()
            .messageBuilder(SetResearchTopicHistoryPacket.class, SetResearchTopicHistoryPacket.direction()).encoder(SetResearchTopicHistoryPacket::encode).decoder(SetResearchTopicHistoryPacket::decode).consumerMainThread(SetResearchTopicHistoryPacket::onMessage).add()
            .messageBuilder(SpellcraftingRunePacket.class, SpellcraftingRunePacket.direction()).encoder(SpellcraftingRunePacket::encode).decoder(SpellcraftingRunePacket::decode).consumerMainThread(SpellcraftingRunePacket::onMessage).add()
            .messageBuilder(SetActiveSpellPacket.class, SetActiveSpellPacket.direction()).encoder(SetActiveSpellPacket::encode).decoder(SetActiveSpellPacket::decode).consumerMainThread(SetActiveSpellPacket::onMessage).add()
            .messageBuilder(WithdrawCaskEssencePacket.class, WithdrawCaskEssencePacket.direction()).encoder(WithdrawCaskEssencePacket::encode).decoder(WithdrawCaskEssencePacket::decode).consumerMainThread(WithdrawCaskEssencePacket::onMessage).add()
            .messageBuilder(OpenGrimoireScreenPacket.class, OpenGrimoireScreenPacket.direction()).encoder(OpenGrimoireScreenPacket::encode).decoder(OpenGrimoireScreenPacket::decode).consumerMainThread(OpenGrimoireScreenPacket::onMessage).add()
            .messageBuilder(SyncWardPacket.class, SyncWardPacket.direction()).encoder(SyncWardPacket::encode).decoder(SyncWardPacket::decode).consumerMainThread(SyncWardPacket::onMessage).add()
            .messageBuilder(OpenStaticBookScreenPacket.class, OpenStaticBookScreenPacket.direction()).encoder(OpenStaticBookScreenPacket::encode).decoder(OpenStaticBookScreenPacket::decode).consumerMainThread(OpenStaticBookScreenPacket::onMessage).add()
            .messageBuilder(OpenEnchantedBookScreenPacket.class, OpenEnchantedBookScreenPacket.direction()).encoder(OpenEnchantedBookScreenPacket::encode).decoder(OpenEnchantedBookScreenPacket::decode).consumerMainThread(OpenEnchantedBookScreenPacket::onMessage).add()
            .messageBuilder(SyncLinguisticsPacket.class, SyncLinguisticsPacket.direction()).encoder(SyncLinguisticsPacket::encode).decoder(SyncLinguisticsPacket::decode).consumerMainThread(SyncLinguisticsPacket::onMessage).add()
            .messageBuilder(ContainerSetVarintDataPacket.class, ContainerSetVarintDataPacket.direction()).encoder(ContainerSetVarintDataPacket::encode).decoder(ContainerSetVarintDataPacket::decode).consumerMainThread(ContainerSetVarintDataPacket::onMessage).add()
            .messageBuilder(ChangeScribeTableModePacket.class, ChangeScribeTableModePacket.direction()).encoder(ChangeScribeTableModePacket::encode).decoder(ChangeScribeTableModePacket::decode).consumerMainThread(ChangeScribeTableModePacket::onMessage).add()
            .messageBuilder(TranscribeActionPacket.class, TranscribeActionPacket.direction()).encoder(TranscribeActionPacket::encode).decoder(TranscribeActionPacket::decode).consumerMainThread(TranscribeActionPacket::onMessage).add()
            .messageBuilder(StudyVocabularyActionPacket.class, StudyVocabularyActionPacket.direction()).encoder(StudyVocabularyActionPacket::encode).decoder(StudyVocabularyActionPacket::decode).consumerMainThread(StudyVocabularyActionPacket::onMessage).add()
            .messageBuilder(UpdateLinguisticsGridsPacket.class, UpdateLinguisticsGridsPacket.direction()).encoder(UpdateLinguisticsGridsPacket::encode).decoder(UpdateLinguisticsGridsPacket::decode).consumerMainThread(UpdateLinguisticsGridsPacket::onMessage).add()
            .messageBuilder(UnlockGridNodeActionPacket.class, UnlockGridNodeActionPacket.direction()).encoder(UnlockGridNodeActionPacket::encode).decoder(UnlockGridNodeActionPacket::decode).consumerMainThread(UnlockGridNodeActionPacket::onMessage).add()
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
