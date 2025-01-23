package com.verdantartifice.primalmagick.common.theorycrafting.rewards;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class RewardTypesPM {
    public static void init() {
        // Pass the service initialization through this class so it gets class loaded and fields registered
        Services.REWARD_TYPES_REGISTRY.init();
    }

    public static final IRegistryItem<RewardType<?>, RewardType<ExperienceReward>> EXPERIENCE = register("experience", ExperienceReward.CODEC, ExperienceReward.STREAM_CODEC);
    public static final IRegistryItem<RewardType<?>, RewardType<ItemReward>> ITEM = register("item", ItemReward.CODEC, ItemReward.STREAM_CODEC);
    public static final IRegistryItem<RewardType<?>, RewardType<LootTableReward>> LOOT_TABLE = register("loot_table", LootTableReward.CODEC, LootTableReward.STREAM_CODEC);

    protected static <T extends AbstractReward<T>> IRegistryItem<RewardType<?>, RewardType<T>> register(String id, MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return Services.REWARD_TYPES_REGISTRY.register(id, () -> new RewardType<>(ResourceUtils.loc(id), codec, streamCodec));
    }
}
