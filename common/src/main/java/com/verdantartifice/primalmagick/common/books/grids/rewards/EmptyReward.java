package com.verdantartifice.primalmagick.common.books.grids.rewards;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

/**
 * Linguistics grid reward placeholder that grants nothing.
 * 
 * @author Daedalus4096
 */
public class EmptyReward extends AbstractReward<EmptyReward> {
    public static final EmptyReward INSTANCE = new EmptyReward();
    
    protected static final ResourceLocation ICON_LOCATION = ResourceLocation.withDefaultNamespace("textures/item/barrier.png");
    protected static final Component DESCRIPTION = Component.translatable("label.primalmagick.scribe_table.grid.reward.empty");
    
    public static final MapCodec<EmptyReward> CODEC = MapCodec.unit(EmptyReward.INSTANCE);
    public static final StreamCodec<ByteBuf, EmptyReward> STREAM_CODEC = StreamCodec.unit(EmptyReward.INSTANCE);
    
    protected EmptyReward() {}
    
    @Override
    protected GridRewardType<EmptyReward> getType() {
        return GridRewardTypesPM.EMPTY.get();
    }

    @Override
    public void grant(ServerPlayer player, RegistryAccess registryAccess) {
        // Nothing to do
    }

    @Override
    public Component getDescription(Player player, RegistryAccess registryAccess) {
        return DESCRIPTION;
    }

    @Override
    public ResourceLocation getIconLocation(Player player) {
        return ICON_LOCATION;
    }

    @Override
    public Optional<Component> getAmountText() {
        return Optional.empty();
    }
}
