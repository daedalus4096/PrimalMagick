package com.verdantartifice.primalmagick.common.books.grids.rewards;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Base class for a reward provided by a linguistics grid node upon successful unlock.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractReward<T extends AbstractReward<T>> implements IReward {
    protected static final Logger LOGGER = LogManager.getLogger();
    
    public static Codec<AbstractReward<?>> dispatchCodec() {
        return Services.GRID_REWARD_TYPES_REGISTRY.codec().dispatch("reward_type", AbstractReward::getType, GridRewardType::codec);
    }
    
    public static StreamCodec<FriendlyByteBuf, AbstractReward<?>> dispatchStreamCodec() {
        return Services.GRID_REWARD_TYPES_REGISTRY.friendlyStreamCodec().dispatch(AbstractReward::getType, GridRewardType::streamCodec);
    }
    
    protected abstract GridRewardType<T> getType();

    public static AbstractReward<?> fromNetwork(RegistryFriendlyByteBuf buf) {
        return AbstractReward.dispatchStreamCodec().decode(buf);
    }
    
    public void toNetwork(RegistryFriendlyByteBuf buf) {
        AbstractReward.dispatchStreamCodec().encode(buf, this);
    }
}
