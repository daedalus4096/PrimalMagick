package com.verdantartifice.primalmagick.common.research.requirements;

import java.util.function.Supplier;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

public class RequirementsPM {
    private static final DeferredRegister<RequirementType<?>> DEFERRED_TYPES = DeferredRegister.create(RegistryKeysPM.REQUIREMENT_TYPES, Constants.MOD_ID);
    public static final Supplier<IForgeRegistry<RequirementType<?>>> TYPES = DEFERRED_TYPES.makeRegistry(RegistryBuilder::new);
    
    public static void init() {
        DEFERRED_TYPES.register(PrimalMagick.getModLoadingContext().getModEventBus());
    }
    
    public static final RegistryObject<RequirementType<ResearchRequirement>> RESEARCH = register("research", ResearchRequirement::codec, ResearchRequirement::streamCodec);
    public static final RegistryObject<RequirementType<KnowledgeRequirement>> KNOWLEDGE = register("knowledge", () -> KnowledgeRequirement.CODEC, () -> KnowledgeRequirement.STREAM_CODEC);
    public static final RegistryObject<RequirementType<ItemStackRequirement>> ITEM_STACK = register("item_stack", () -> ItemStackRequirement.CODEC, () -> ItemStackRequirement.STREAM_CODEC);
    public static final RegistryObject<RequirementType<ItemTagRequirement>> ITEM_TAG = register("item_tag", () -> ItemTagRequirement.CODEC, () -> ItemTagRequirement.STREAM_CODEC);
    public static final RegistryObject<RequirementType<StatRequirement>> STAT = register("stat", () -> StatRequirement.CODEC, () -> StatRequirement.STREAM_CODEC);
    public static final RegistryObject<RequirementType<ExpertiseRequirement>> EXPERTISE = register("expertise", ExpertiseRequirement::codec, ExpertiseRequirement::streamCodec);   
    public static final RegistryObject<RequirementType<VanillaItemUsedStatRequirement>> VANILLA_ITEM_USED_STAT = register("vanilla_item_used_stat", () -> VanillaItemUsedStatRequirement.CODEC, () -> VanillaItemUsedStatRequirement.STREAM_CODEC);
    public static final RegistryObject<RequirementType<VanillaCustomStatRequirement>> VANILLA_CUSTOM_STAT = register("vanilla_custom_stat", () -> VanillaCustomStatRequirement.CODEC, () -> VanillaCustomStatRequirement.STREAM_CODEC);
    public static final RegistryObject<RequirementType<AndRequirement>> AND = register("and", AndRequirement::codec, AndRequirement::streamCodec);
    public static final RegistryObject<RequirementType<OrRequirement>> OR = register("or", OrRequirement::codec, OrRequirement::streamCodec);
    public static final RegistryObject<RequirementType<QuorumRequirement>> QUORUM = register("quorum", QuorumRequirement::codec, QuorumRequirement::streamCodec);
    
    protected static <T extends AbstractRequirement<T>> RegistryObject<RequirementType<T>> register(String id, Supplier<MapCodec<T>> codecSupplier, Supplier<StreamCodec<? super RegistryFriendlyByteBuf, T>> streamCodecSupplier) {
        return DEFERRED_TYPES.register(id, () -> new RequirementType<T>(ResourceUtils.loc(id), codecSupplier, streamCodecSupplier));
    }
}
