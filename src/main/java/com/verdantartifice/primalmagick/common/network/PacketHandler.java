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
                        .addMain(SyncStatsPacket.class, SyncStatsPacket.STREAM_CODEC, SyncStatsPacket::onMessage)
                        .addMain(SyncAttunementsPacket.class, SyncAttunementsPacket.STREAM_CODEC, SyncAttunementsPacket::onMessage)
                        .addMain(PlayClientSoundPacket.class, PlayClientSoundPacket.STREAM_CODEC, PlayClientSoundPacket::onMessage)
                        .addMain(OfferingChannelPacket.class, OfferingChannelPacket.STREAM_CODEC, OfferingChannelPacket::onMessage)
                        .addMain(PropMarkerPacket.class, PropMarkerPacket.STREAM_CODEC, PropMarkerPacket::onMessage)
                        .addMain(RemovePropMarkerPacket.class, RemovePropMarkerPacket.STREAM_CODEC, RemovePropMarkerPacket::onMessage)
                        .addMain(SyncCompanionsPacket.class, SyncCompanionsPacket.STREAM_CODEC, SyncCompanionsPacket::onMessage)
                        .addMain(PotionExplosionPacket.class, PotionExplosionPacket.STREAM_CODEC, PotionExplosionPacket::onMessage)
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
    
    public static void registerMessages() {
        // The class just needs to be externally referenced by a loaded class in order to be class-loaded itself and have its SimpleChannel initialized statically
        LOGGER.debug("Registering network {} v{}", INSTANCE.getName(), INSTANCE.getProtocolVersion());
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
