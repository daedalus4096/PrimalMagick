package com.verdantartifice.primalmagick.common.research.requirements;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Supplier;

public class RequirementsPM {
    public static final IRegistryItem<RequirementType<?>, RequirementType<ResearchRequirement>> RESEARCH = register("research", ResearchRequirement::codec, ResearchRequirement::streamCodec);
    public static final IRegistryItem<RequirementType<?>, RequirementType<KnowledgeRequirement>> KNOWLEDGE = register("knowledge", () -> KnowledgeRequirement.CODEC, () -> KnowledgeRequirement.STREAM_CODEC);
    public static final IRegistryItem<RequirementType<?>, RequirementType<ItemStackRequirement>> ITEM_STACK = register("item_stack", () -> ItemStackRequirement.CODEC, () -> ItemStackRequirement.STREAM_CODEC);
    public static final IRegistryItem<RequirementType<?>, RequirementType<ItemTagRequirement>> ITEM_TAG = register("item_tag", () -> ItemTagRequirement.CODEC, () -> ItemTagRequirement.STREAM_CODEC);
    public static final IRegistryItem<RequirementType<?>, RequirementType<StatRequirement>> STAT = register("stat", () -> StatRequirement.CODEC, () -> StatRequirement.STREAM_CODEC);
    public static final IRegistryItem<RequirementType<?>, RequirementType<ExpertiseRequirement>> EXPERTISE = register("expertise", ExpertiseRequirement::codec, ExpertiseRequirement::streamCodec);   
    public static final IRegistryItem<RequirementType<?>, RequirementType<VanillaItemUsedStatRequirement>> VANILLA_ITEM_USED_STAT = register("vanilla_item_used_stat", () -> VanillaItemUsedStatRequirement.CODEC, () -> VanillaItemUsedStatRequirement.STREAM_CODEC);
    public static final IRegistryItem<RequirementType<?>, RequirementType<VanillaCustomStatRequirement>> VANILLA_CUSTOM_STAT = register("vanilla_custom_stat", () -> VanillaCustomStatRequirement.CODEC, () -> VanillaCustomStatRequirement.STREAM_CODEC);
    public static final IRegistryItem<RequirementType<?>, RequirementType<AndRequirement>> AND = register("and", AndRequirement::codec, AndRequirement::streamCodec);
    public static final IRegistryItem<RequirementType<?>, RequirementType<OrRequirement>> OR = register("or", OrRequirement::codec, OrRequirement::streamCodec);
    public static final IRegistryItem<RequirementType<?>, RequirementType<QuorumRequirement>> QUORUM = register("quorum", QuorumRequirement::codec, QuorumRequirement::streamCodec);
    
    protected static <T extends AbstractRequirement<T>> IRegistryItem<RequirementType<?>, RequirementType<T>> register(String id, Supplier<MapCodec<T>> codecSupplier, Supplier<StreamCodec<? super RegistryFriendlyByteBuf, T>> streamCodecSupplier) {
        return Services.REQUIREMENT_TYPES_REGISTRY.register(id, () -> new RequirementType<T>(ResourceUtils.loc(id), codecSupplier, streamCodecSupplier));
    }
}
