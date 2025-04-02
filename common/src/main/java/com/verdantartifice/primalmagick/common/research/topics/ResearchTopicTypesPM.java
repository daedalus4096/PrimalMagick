package com.verdantartifice.primalmagick.common.research.topics;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class ResearchTopicTypesPM {
    public static void init() {
        // Pass the service initialization through this class so it gets class loaded and fields registered
        Services.RESEARCH_TOPIC_TYPES_REGISTRY.init();
    }

    public static final IRegistryItem<ResearchTopicType<?>, ResearchTopicType<DisciplineResearchTopic>> DISCIPLINE = register("discipline", DisciplineResearchTopic.CODEC, DisciplineResearchTopic.STREAM_CODEC);
    public static final IRegistryItem<ResearchTopicType<?>, ResearchTopicType<EnchantmentResearchTopic>> ENCHANTMENT = register("enchantment", EnchantmentResearchTopic.CODEC, EnchantmentResearchTopic.STREAM_CODEC);
    public static final IRegistryItem<ResearchTopicType<?>, ResearchTopicType<EntryResearchTopic>> RESEARCH_ENTRY = register("research_entry", EntryResearchTopic.CODEC, EntryResearchTopic.STREAM_CODEC);
    public static final IRegistryItem<ResearchTopicType<?>, ResearchTopicType<LanguageResearchTopic>> LANGUAGE = register("language", LanguageResearchTopic.CODEC, LanguageResearchTopic.STREAM_CODEC);
    public static final IRegistryItem<ResearchTopicType<?>, ResearchTopicType<MainIndexResearchTopic>> MAIN_INDEX = register("main_index", MainIndexResearchTopic.CODEC, MainIndexResearchTopic.STREAM_CODEC);
    public static final IRegistryItem<ResearchTopicType<?>, ResearchTopicType<OtherResearchTopic>> OTHER = register("other", OtherResearchTopic.CODEC, OtherResearchTopic.STREAM_CODEC);
    public static final IRegistryItem<ResearchTopicType<?>, ResearchTopicType<SourceResearchTopic>> SOURCE = register("source", SourceResearchTopic.CODEC, SourceResearchTopic.STREAM_CODEC);
    public static final IRegistryItem<ResearchTopicType<?>, ResearchTopicType<AffinityResearchTopic>> AFFINITY = register("affinity", AffinityResearchTopic.CODEC, AffinityResearchTopic.STREAM_CODEC);
    
    protected static <T extends AbstractResearchTopic<T>> IRegistryItem<ResearchTopicType<?>, ResearchTopicType<T>> register(String id, MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return Services.RESEARCH_TOPIC_TYPES_REGISTRY.register(id, () -> new ResearchTopicType<T>(ResourceUtils.loc(id), codec, streamCodec));
    }
}
