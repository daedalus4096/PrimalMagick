package com.verdantartifice.primalmagick.common.books.grids.rewards;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class GridRewardTypesPM {
    public static final IRegistryItem<GridRewardType<?>, GridRewardType<AttunementReward>> ATTUNEMENT = register("attunement", AttunementReward.CODEC, AttunementReward.STREAM_CODEC);
    public static final IRegistryItem<GridRewardType<?>, GridRewardType<ComprehensionReward>> COMPREHENSION = register("comprehension", ComprehensionReward.CODEC, ComprehensionReward.STREAM_CODEC);
    public static final IRegistryItem<GridRewardType<?>, GridRewardType<EmptyReward>> EMPTY = register("empty", EmptyReward.CODEC, EmptyReward.STREAM_CODEC);
    public static final IRegistryItem<GridRewardType<?>, GridRewardType<KnowledgeReward>> KNOWLEDGE = register("knowledge", KnowledgeReward.CODEC, KnowledgeReward.STREAM_CODEC);
    
    protected static <T extends AbstractReward<T>> IRegistryItem<GridRewardType<?>, GridRewardType<T>> register(String id, MapCodec<T> codec, StreamCodec<? super FriendlyByteBuf, T> streamCodec) {
        return Services.GRID_REWARD_TYPES.register(id, () -> new GridRewardType<T>(ResourceUtils.loc(id), codec, streamCodec));
    }
}
