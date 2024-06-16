package com.verdantartifice.primalmagick.common.research.requirements;

import java.util.function.Supplier;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

public class RequirementsPM {
    private static final DeferredRegister<RequirementType<?>> DEFERRED_TYPES = DeferredRegister.create(RegistryKeysPM.REQUIREMENT_TYPES, PrimalMagick.MODID);
    public static final Supplier<IForgeRegistry<RequirementType<?>>> TYPES = DEFERRED_TYPES.makeRegistry(RegistryBuilder::new);
    
    public static void init() {
        DEFERRED_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static final RegistryObject<RequirementType<ResearchRequirement>> RESEARCH = register("research", ResearchRequirement::codec, ResearchRequirement::fromNetworkInner);
    public static final RegistryObject<RequirementType<KnowledgeRequirement>> KNOWLEDGE = register("knowledge", () -> KnowledgeRequirement.CODEC, KnowledgeRequirement::fromNetworkInner);
    public static final RegistryObject<RequirementType<ItemStackRequirement>> ITEM_STACK = register("item_stack", () -> ItemStackRequirement.CODEC, ItemStackRequirement::fromNetworkInner);
    public static final RegistryObject<RequirementType<ItemTagRequirement>> ITEM_TAG = register("item_tag", () -> ItemTagRequirement.CODEC, ItemTagRequirement::fromNetworkInner);
    public static final RegistryObject<RequirementType<StatRequirement>> STAT = register("stat", () -> StatRequirement.CODEC, StatRequirement::fromNetworkInner);
    public static final RegistryObject<RequirementType<ExpertiseRequirement>> EXPERTISE = register("expertise", ExpertiseRequirement::codec, ExpertiseRequirement::fromNetworkInner);   
    public static final RegistryObject<RequirementType<VanillaItemUsedStatRequirement>> VANILLA_ITEM_USED_STAT = register("vanilla_item_used_stat", () -> VanillaItemUsedStatRequirement.CODEC, VanillaItemUsedStatRequirement::fromNetworkInner);
    public static final RegistryObject<RequirementType<VanillaCustomStatRequirement>> VANILLA_CUSTOM_STAT = register("vanilla_custom_stat", () -> VanillaCustomStatRequirement.CODEC, VanillaCustomStatRequirement::fromNetworkInner);
    public static final RegistryObject<RequirementType<AndRequirement>> AND = register("and", AndRequirement::codec, AndRequirement::fromNetworkInner);
    public static final RegistryObject<RequirementType<OrRequirement>> OR = register("or", OrRequirement::codec, OrRequirement::fromNetworkInner);
    public static final RegistryObject<RequirementType<QuorumRequirement>> QUORUM = register("quorum", QuorumRequirement::codec, QuorumRequirement::fromNetworkInner);
    
    protected static <T extends AbstractRequirement<T>> RegistryObject<RequirementType<T>> register(String id, Supplier<Codec<T>> codecSupplier, FriendlyByteBuf.Reader<T> networkReader) {
        return DEFERRED_TYPES.register(id, () -> new RequirementType<T>(PrimalMagick.resource(id), codecSupplier, networkReader));
    }
}
