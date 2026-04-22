package com.verdantartifice.primalmagick.common.affinities;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class AffinityTypesPM {
    public static void init() {
        // Pass the service initialization through this class so it gets class loaded and fields registered
        Services.AFFINITY_TYPES_REGISTRY.init();
    }

    public static final IRegistryItem<AffinityType<?>, AffinityType<ItemAffinity>> ITEM = register("item", ItemAffinity.CODEC, ItemAffinity.STREAM_CODEC, "items");
    public static final IRegistryItem<AffinityType<?>, AffinityType<PotionBonusAffinity>> POTION_BONUS = register("potion_bonus", PotionBonusAffinity.CODEC, PotionBonusAffinity.STREAM_CODEC, "potions");
    public static final IRegistryItem<AffinityType<?>, AffinityType<EnchantmentBonusAffinity>> ENCHANTMENT_BONUS = register("enchantment_bonus", EnchantmentBonusAffinity.CODEC, EnchantmentBonusAffinity.STREAM_CODEC, "enchantments");
    public static final IRegistryItem<AffinityType<?>, AffinityType<EntityTypeAffinity>> ENTITY_TYPE = register("entity_type", EntityTypeAffinity.CODEC, EntityTypeAffinity.STREAM_CODEC, "entity_types");

    protected static <T extends AbstractAffinity<T>> IRegistryItem<AffinityType<?>, AffinityType<T>> register(String id, MapCodec<T> codec, StreamCodec<? super FriendlyByteBuf, T> streamCodec, String folder) {
        return Services.AFFINITY_TYPES_REGISTRY.register(id, () -> new AffinityType<>(ResourceUtils.loc(id), codec, streamCodec, folder));
    }
}
