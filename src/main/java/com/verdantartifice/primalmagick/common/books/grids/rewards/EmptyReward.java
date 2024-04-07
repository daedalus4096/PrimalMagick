package com.verdantartifice.primalmagick.common.books.grids.rewards;

import java.util.Optional;

import com.google.gson.JsonObject;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

/**
 * Linguistics grid reward placeholder that grants nothing.
 * 
 * @author Daedalus4096
 */
public class EmptyReward extends AbstractReward {
    public static final String TYPE = "empty";
    public static final IRewardSerializer<EmptyReward> SERIALIZER = new Serializer();
    public static final EmptyReward INSTANCE = new EmptyReward();
    
    protected static final ResourceLocation ICON_LOCATION = new ResourceLocation("textures/item/barrier.png");
    protected static final Component DESCRIPTION = Component.translatable("label.primalmagick.scribe_table.grid.reward.empty");
    
    public static void init() {
        AbstractReward.register(TYPE, EmptyReward::fromNBT, SERIALIZER);
    }
    
    protected EmptyReward() {}
    
    public static EmptyReward fromNBT(CompoundTag tag) {
        return INSTANCE;
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

    @Override
    public String getRewardType() {
        return TYPE;
    }

    @SuppressWarnings("unchecked")
    @Override
    public IRewardSerializer<EmptyReward> getSerializer() {
        return SERIALIZER;
    }

    public static class Serializer implements IRewardSerializer<EmptyReward> {
        @Override
        public EmptyReward read(ResourceLocation gridId, JsonObject json) {
            return EmptyReward.INSTANCE;
        }

        @Override
        public EmptyReward fromNetwork(FriendlyByteBuf buf) {
            return EmptyReward.INSTANCE;
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, EmptyReward reward) {
            // Nothing to do
        }
    }
}
