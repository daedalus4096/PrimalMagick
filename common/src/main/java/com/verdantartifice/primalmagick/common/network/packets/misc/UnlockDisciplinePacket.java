package com.verdantartifice.primalmagick.common.network.packets.misc;

import com.verdantartifice.primalmagick.client.toast.ToastManager;
import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagick.common.research.ResearchDisciplines;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class UnlockDisciplinePacket implements IMessageToClient {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("unlock_discipline");
    public static final StreamCodec<RegistryFriendlyByteBuf, UnlockDisciplinePacket> STREAM_CODEC = StreamCodec.ofMember(UnlockDisciplinePacket::encode, UnlockDisciplinePacket::decode);

    protected final ResourceKey<ResearchDiscipline> disciplineKey;

    public UnlockDisciplinePacket(ResearchDiscipline discipline) {
        this.disciplineKey = discipline.key().getRootKey();
    }

    protected UnlockDisciplinePacket(ResourceKey<ResearchDiscipline> disciplineKey) {
        this.disciplineKey = disciplineKey;
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void encode(UnlockDisciplinePacket message, RegistryFriendlyByteBuf buf) {
        buf.writeResourceKey(message.disciplineKey);
    }

    public static UnlockDisciplinePacket decode(RegistryFriendlyByteBuf buf) {
        return new UnlockDisciplinePacket(buf.readResourceKey(RegistryKeysPM.RESEARCH_DISCIPLINES));
    }

    public static void onMessage(PacketContext<UnlockDisciplinePacket> ctx) {
        Player player = Side.CLIENT.equals(ctx.side()) ? ClientUtils.getCurrentPlayer() : null;
        if (player != null) {
            RegistryAccess registryAccess = player.level().registryAccess();
            ResearchDiscipline discipline = ResearchDisciplines.getDiscipline(registryAccess, ctx.message().disciplineKey);
            if (discipline != null) {
                ToastManager.showDisciplineUnlockToast(discipline);
            }
        }
    }
}
