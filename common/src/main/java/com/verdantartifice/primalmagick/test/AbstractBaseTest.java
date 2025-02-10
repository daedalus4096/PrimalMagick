package com.verdantartifice.primalmagick.test;

import com.mojang.authlib.GameProfile;
import com.verdantartifice.primalmagick.platform.Services;
import io.netty.channel.ChannelHandler;
import io.netty.channel.embedded.EmbeddedChannel;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.Connection;
import net.minecraft.network.ProtocolInfo;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.GameProtocols;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import java.util.UUID;

public abstract class AbstractBaseTest {
    protected ServerPlayer makeMockServerPlayer(GameTestHelper helper) {
        return this.makeMockServerPlayer(helper, false);
    }

    protected ServerPlayer makeMockServerPlayer(GameTestHelper helper, boolean joinLevel) {
        ServerLevel level = helper.getLevel();
        CommonListenerCookie cookie = CommonListenerCookie.createInitial(new GameProfile(UUID.randomUUID(), "test-mock-player"), false);
        ServerPlayer player = new ServerPlayer(level.getServer(), level, cookie.gameProfile(), cookie.clientInformation()) {
            public boolean isSpectator() {
                return false;
            }

            public boolean isCreative() {
                return true;
            }
        };
        Connection connection = new Connection(PacketFlow.SERVERBOUND);
        new EmbeddedChannel(new ChannelHandler[]{connection});
        MinecraftServer server = level.getServer();
        ServerGamePacketListenerImpl listener = new ServerGamePacketListenerImpl(server, connection, player, cookie);
        ProtocolInfo<ServerGamePacketListener> info = GameProtocols.SERVERBOUND_TEMPLATE.bind(RegistryFriendlyByteBuf.decorator(server.registryAccess()));
        connection.setupInboundProtocol(info, listener);
        Services.TEST.configureMockConnection(connection);
        if (joinLevel) {
            helper.getLevel().getServer().getPlayerList().placeNewPlayer(connection, player, cookie);
        }
        return player;
    }

    @SuppressWarnings("unchecked")
    protected static <T> T assertInstanceOf(GameTestHelper helper, Object obj, Class<T> clazz, String failureMessage) {
        helper.assertTrue(clazz.isInstance(obj), failureMessage);
        return (T)obj;
    }
}
