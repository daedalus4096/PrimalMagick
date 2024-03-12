package com.verdantartifice.primalmagick.common.books.grids.rewards;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

/**
 * Base class for a reward provided by a linguistics grid node upon successful unlock.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractReward implements IReward {
    protected static final Logger LOGGER = LogManager.getLogger();
    protected static final Map<String, Function<CompoundTag, IReward>> NBT_FACTORIES = new HashMap<>();
    protected static final Map<String, IRewardSerializer<?>> SERIALIZERS = new HashMap<>();
    
    protected static void register(String type, Function<CompoundTag, IReward> nbtFactory, IRewardSerializer<?> serializer) {
        if (NBT_FACTORIES.containsKey(type) || SERIALIZERS.containsKey(type)) {
            LOGGER.error("Duplicate linguistics grid reward type {}", type);
        } else {
            NBT_FACTORIES.put(type, nbtFactory);
            SERIALIZERS.put(type, serializer);
        }
    }
    
    @Nullable
    public static IReward fromNBT(@Nullable CompoundTag tag) {
        String rewardType = (tag == null) ? null : tag.getString("RewardType");
        return NBT_FACTORIES.getOrDefault(rewardType, nbt -> null).apply(tag);
    }
    
    @Nullable
    public static IReward fromJson(@Nullable JsonObject json, @Nonnull ResourceLocation gridId) {
        try {
            String rewardType = json.getAsJsonPrimitive("type").getAsString();
            IRewardSerializer<?> serializer = SERIALIZERS.get(rewardType);
            return (serializer == null) ? null : serializer.read(gridId, json);
        } catch (Exception e) {
            throw new JsonSyntaxException("Invalid reward in linguistics grid JSON for " + gridId.toString(), e);
        }
    }
    
    @Nullable
    public static IReward fromNetwork(FriendlyByteBuf buf) {
        if (buf.readBoolean()) {
            String rewardType = buf.readUtf();
            IRewardSerializer<?> serializer = SERIALIZERS.get(rewardType);
            return (serializer == null) ? null : serializer.fromNetwork(buf);
        } else {
            return null;
        }
    }
    
    public static void toNetwork(FriendlyByteBuf buf, @Nullable IReward reward) {
        if (reward == null) {
            buf.writeBoolean(false);
        } else {
            buf.writeBoolean(true);
            buf.writeUtf(reward.getRewardType());
            reward.getSerializer().toNetwork(buf, reward);
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag retVal = new CompoundTag();
        retVal.putString("RewardType", this.getRewardType());
        return retVal;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        // Nothing to do in the base class
    }
}
