package com.verdantartifice.primalmagick.common.commands.arguments;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Deferred registry for command argument types.
 * 
 * @author Daedalus4096
 */
public class ArgumentTypesPM {
    private static final DeferredRegister<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENT_TYPES = DeferredRegister.create(ForgeRegistries.COMMAND_ARGUMENT_TYPES, PrimalMagick.MODID);

    public static void init() {
        COMMAND_ARGUMENT_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static final RegistryObject<SingletonArgumentInfo<ResearchArgument>> RESEARCH = COMMAND_ARGUMENT_TYPES.register("research", 
            () -> ArgumentTypeInfos.registerByClass(ResearchArgument.class, SingletonArgumentInfo.contextFree(ResearchArgument::research)));
    public static final RegistryObject<SingletonArgumentInfo<DisciplineArgument>> DISCIPLINE = COMMAND_ARGUMENT_TYPES.register("discipline", 
            () -> ArgumentTypeInfos.registerByClass(DisciplineArgument.class, SingletonArgumentInfo.contextFree(DisciplineArgument::discipline)));
    public static final RegistryObject<SingletonArgumentInfo<KnowledgeTypeArgument>> KNOWLEDGE_TYPE = COMMAND_ARGUMENT_TYPES.register("knowledge_type", 
            () -> ArgumentTypeInfos.registerByClass(KnowledgeTypeArgument.class, SingletonArgumentInfo.contextFree(KnowledgeTypeArgument::knowledgeType)));
    public static final RegistryObject<SingletonArgumentInfo<KnowledgeAmountArgument>> KNOWLEDGE_AMOUNT = COMMAND_ARGUMENT_TYPES.register("knowledge_amount", 
            () -> ArgumentTypeInfos.registerByClass(KnowledgeAmountArgument.class, SingletonArgumentInfo.contextFree(KnowledgeAmountArgument::amount)));
    public static final RegistryObject<SingletonArgumentInfo<SourceArgument>> SOURCE = COMMAND_ARGUMENT_TYPES.register("source", 
            () -> ArgumentTypeInfos.registerByClass(SourceArgument.class, SingletonArgumentInfo.contextFree(SourceArgument::source)));
    public static final RegistryObject<SingletonArgumentInfo<StatValueArgument>> STAT_VALUE = COMMAND_ARGUMENT_TYPES.register("stat_value", 
            () -> ArgumentTypeInfos.registerByClass(StatValueArgument.class, SingletonArgumentInfo.contextFree(StatValueArgument::value)));
    public static final RegistryObject<SingletonArgumentInfo<AttunementTypeArgument>> ATTUNEMENT_TYPE = COMMAND_ARGUMENT_TYPES.register("attunement_type", 
            () -> ArgumentTypeInfos.registerByClass(AttunementTypeArgument.class, SingletonArgumentInfo.contextFree(AttunementTypeArgument::attunementType)));
    public static final RegistryObject<SingletonArgumentInfo<AttunementValueArgument>> ATTUNEMENT_VALUE = COMMAND_ARGUMENT_TYPES.register("attunement_value", 
            () -> ArgumentTypeInfos.registerByClass(AttunementValueArgument.class, SingletonArgumentInfo.contextFree(AttunementValueArgument::value)));
    public static final RegistryObject<SingletonArgumentInfo<LanguageComprehensionArgument>> LANGUAGE_COMPREHENSION = COMMAND_ARGUMENT_TYPES.register("language_comprehension", 
            () -> ArgumentTypeInfos.registerByClass(LanguageComprehensionArgument.class, SingletonArgumentInfo.contextFree(LanguageComprehensionArgument::value)));
}
