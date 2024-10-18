package com.verdantartifice.primalmagick.common.network;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.network.packets.config.AcknowledgementPacket;
import com.verdantartifice.primalmagick.common.network.packets.config.UpdateAffinitiesConfigPacket;
import com.verdantartifice.primalmagick.common.network.packets.config.UpdateLinguisticsGridsConfigPacket;
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
import net.minecraft.network.Connection;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.network.CustomPayloadEvent;
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
    private static final ResourceLocation CHANNEL_NAME = ResourceUtils.loc("main_channel");
    private static final int PROTOCOL_VERSION = 1;
    
    private static final SimpleChannel CHANNEL = ChannelBuilder
            .named(CHANNEL_NAME)
            .clientAcceptedVersions(VersionTest.exact(PROTOCOL_VERSION))
            .serverAcceptedVersions(VersionTest.exact(PROTOCOL_VERSION))
            .networkProtocolVersion(PROTOCOL_VERSION)
            .simpleChannel()
                .play()
                    .clientbound()
                        .addMain(UpdateAffinitiesPacket.class, UpdateAffinitiesPacket.STREAM_CODEC, UpdateAffinitiesPacket::onMessage)
                        .addMain(SyncArcaneRecipeBookPacket.class, SyncArcaneRecipeBookPacket.STREAM_CODEC, SyncArcaneRecipeBookPacket::onMessage)
                        .addMain(PlaceGhostArcaneRecipePacket.class, PlaceGhostArcaneRecipePacket.STREAM_CODEC, PlaceGhostArcaneRecipePacket::onMessage)
                        .addMain(SpellcraftingRunePacket.class, SpellcraftingRunePacket.STREAM_CODEC, SpellcraftingRunePacket::onMessage)
                        .addMain(OpenGrimoireScreenPacket.class, OpenGrimoireScreenPacket.STREAM_CODEC, OpenGrimoireScreenPacket::onMessage)
                        .addMain(SyncWardPacket.class, SyncWardPacket.STREAM_CODEC, SyncWardPacket::onMessage)
                        .addMain(OpenStaticBookScreenPacket.class, OpenStaticBookScreenPacket.STREAM_CODEC, OpenStaticBookScreenPacket::onMessage)
                        .addMain(OpenEnchantedBookScreenPacket.class, OpenEnchantedBookScreenPacket.STREAM_CODEC, OpenEnchantedBookScreenPacket::onMessage)
                        .addMain(SyncLinguisticsPacket.class, SyncLinguisticsPacket.STREAM_CODEC, SyncLinguisticsPacket::onMessage)
                        .addMain(ContainerSetVarintDataPacket.class, ContainerSetVarintDataPacket.STREAM_CODEC, ContainerSetVarintDataPacket::onMessage)
                        .addMain(UpdateLinguisticsGridsPacket.class, UpdateLinguisticsGridsPacket.STREAM_CODEC, UpdateLinguisticsGridsPacket::onMessage)
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
                        .addMain(ResetFallDistancePacket.class, ResetFallDistancePacket.STREAM_CODEC, ResetFallDistancePacket::onMessage)
                        .addMain(StartProjectPacket.class, StartProjectPacket.STREAM_CODEC, StartProjectPacket::onMessage)
                        .addMain(CompleteProjectPacket.class, CompleteProjectPacket.STREAM_CODEC, CompleteProjectPacket::onMessage)
                        .addMain(SetProjectMaterialSelectionPacket.class, SetProjectMaterialSelectionPacket.STREAM_CODEC, SetProjectMaterialSelectionPacket::onMessage)
                        .addMain(ScanEntityPacket.class, ScanEntityPacket.STREAM_CODEC, ScanEntityPacket::onMessage)
                        .addMain(PlaceArcaneRecipePacket.class, PlaceArcaneRecipePacket.STREAM_CODEC, PlaceArcaneRecipePacket::onMessage)
                        .addMain(SeenArcaneRecipePacket.class, SeenArcaneRecipePacket.STREAM_CODEC, SeenArcaneRecipePacket::onMessage)
                        .addMain(ChangeArcaneRecipeBookSettingsPacket.class, ChangeArcaneRecipeBookSettingsPacket.STREAM_CODEC, ChangeArcaneRecipeBookSettingsPacket::onMessage)
                        .addMain(SetResearchTopicHistoryPacket.class, SetResearchTopicHistoryPacket.STREAM_CODEC, SetResearchTopicHistoryPacket::onMessage)
                        .addMain(SetActiveSpellPacket.class, SetActiveSpellPacket.STREAM_CODEC, SetActiveSpellPacket::onMessage)
                        .addMain(WithdrawCaskEssencePacket.class, WithdrawCaskEssencePacket.STREAM_CODEC, WithdrawCaskEssencePacket::onMessage)
                        .addMain(ChangeScribeTableModePacket.class, ChangeScribeTableModePacket.STREAM_CODEC, ChangeScribeTableModePacket::onMessage)
                        .addMain(TranscribeActionPacket.class, TranscribeActionPacket.STREAM_CODEC, TranscribeActionPacket::onMessage)
                        .addMain(StudyVocabularyActionPacket.class, StudyVocabularyActionPacket.STREAM_CODEC, StudyVocabularyActionPacket::onMessage)
                        .addMain(UnlockGridNodeActionPacket.class, UnlockGridNodeActionPacket.STREAM_CODEC, UnlockGridNodeActionPacket::onMessage)
            .build();

    public static void sendToServer(IMessageToServer message) {
        // Send a packet from a client to the server
        CHANNEL.send(message, PacketDistributor.SERVER.noArg());
    }
    
    public static void sendToPlayer(IMessageToClient message, ServerPlayer player) {
        // Send a message from the server to a specific player's client
        CHANNEL.send(message, PacketDistributor.PLAYER.with(player));
    }
    
    public static void sendToAllAround(IMessageToClient message, ResourceKey<Level> dimension, BlockPos center, double radius) {
        // Send a message to the clients of all players within a given distance of the given world position
        CHANNEL.send(message, PacketDistributor.NEAR.with(new PacketDistributor.TargetPoint(center.getX() + 0.5D, center.getY() + 0.5D, center.getZ() + 0.5D, radius, dimension)));
    }
    
    public static void sendOverConnection(Object message, Connection conn) {
        // Send a message over the given connection
        CHANNEL.send(message, conn);
    }
    
    public static void reply(Object replyMessage, CustomPayloadEvent.Context ctx) {
        // Send a reply message in response to a previous message
        CHANNEL.reply(replyMessage, ctx);
    }
}
