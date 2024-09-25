package com.verdantartifice.primalmagick.common.commands.arguments;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Deferred registry for command argument types.
 * 
 * @author Daedalus4096
 */
public class ArgumentTypesPM {
    private static final DeferredRegister<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENT_TYPES = DeferredRegister.create(ForgeRegistries.COMMAND_ARGUMENT_TYPES, Constants.MOD_ID);

    public static void init() {
        COMMAND_ARGUMENT_TYPES.register(PrimalMagick.getModLoadingContext().getModEventBus());
    }
    
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
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static final RegistryObject<ResourceKeyArgumentPM.Info> RESOURCE_KEY = COMMAND_ARGUMENT_TYPES.register("resource_key", 
            () -> ArgumentTypeInfos.registerByClass(ResourceKeyArgumentPM.class, new ResourceKeyArgumentPM.Info()));
}
