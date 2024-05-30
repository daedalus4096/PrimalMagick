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
    
    public static final RegistryObject<RequirementType<ResearchRequirement>> RESEARCH = register("research", ResearchRequirement.codec(), ResearchRequirement::fromNetwork);
    public static final RegistryObject<RequirementType<KnowledgeRequirement>> KNOWLEDGE = register("knowledge", KnowledgeRequirement.CODEC, KnowledgeRequirement::fromNetwork);
    public static final RegistryObject<RequirementType<ItemStackRequirement>> ITEM_STACK = register("item_stack", ItemStackRequirement.CODEC, ItemStackRequirement::fromNetwork);
    public static final RegistryObject<RequirementType<ItemTagRequirement>> ITEM_TAG = register("item_tag", ItemTagRequirement.CODEC, ItemTagRequirement::fromNetwork);
    public static final RegistryObject<RequirementType<StatRequirement>> STAT = register("stat", StatRequirement.CODEC, StatRequirement::fromNetwork);
    public static final RegistryObject<RequirementType<VanillaItemUsedStatRequirement>> VANILLA_ITEM_USED_STAT = register("vanilla_item_used_stat", VanillaItemUsedStatRequirement.CODEC, VanillaItemUsedStatRequirement::fromNetwork);
    public static final RegistryObject<RequirementType<VanillaCustomStatRequirement>> VANILLA_CUSTOM_STAT = register("vanilla_custom_stat", VanillaCustomStatRequirement.CODEC, VanillaCustomStatRequirement::fromNetwork);
    public static final RegistryObject<RequirementType<AndRequirement>> AND = register("and", AndRequirement.codec(), AndRequirement::fromNetwork);
    public static final RegistryObject<RequirementType<OrRequirement>> OR = register("or", OrRequirement.codec(), OrRequirement::fromNetwork);
    public static final RegistryObject<RequirementType<QuorumRequirement>> QUORUM = register("quorum", QuorumRequirement.codec(), QuorumRequirement::fromNetwork);
    
    protected static <T extends AbstractRequirement<T>> RegistryObject<RequirementType<T>> register(String id, Codec<T> codec, FriendlyByteBuf.Reader<T> networkReader) {
        return DEFERRED_TYPES.register(id, () -> new RequirementType<T>(PrimalMagick.resource(id), codec, networkReader));
    }
}
