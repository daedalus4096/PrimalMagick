package com.verdantartifice.primalmagick.common.commands.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.resources.ResourceKey;

/**
 * Deferred registry for command argument types.
 * 
 * @author Daedalus4096
 */
public class ArgumentTypesPM {
    public static void init() {
        // Nothing to do, other than be called for class loading
    }

    public static final IRegistryItem<ArgumentTypeInfo<?, ?>, SingletonArgumentInfo<KnowledgeTypeArgument>> KNOWLEDGE_TYPE = registerSingleton(
            "knowledge_type", KnowledgeTypeArgument.class, SingletonArgumentInfo.contextFree(KnowledgeTypeArgument::knowledgeType));
    public static final IRegistryItem<ArgumentTypeInfo<?, ?>, SingletonArgumentInfo<KnowledgeAmountArgument>> KNOWLEDGE_AMOUNT = registerSingleton(
            "knowledge_amount", KnowledgeAmountArgument.class, SingletonArgumentInfo.contextFree(KnowledgeAmountArgument::amount));
    public static final IRegistryItem<ArgumentTypeInfo<?, ?>, SingletonArgumentInfo<SourceArgument>> SOURCE = registerSingleton(
            "source", SourceArgument.class, SingletonArgumentInfo.contextFree(SourceArgument::source));
    public static final IRegistryItem<ArgumentTypeInfo<?, ?>, SingletonArgumentInfo<StatValueArgument>> STAT_VALUE = registerSingleton(
            "stat_value", StatValueArgument.class, SingletonArgumentInfo.contextFree(StatValueArgument::value));
    public static final IRegistryItem<ArgumentTypeInfo<?, ?>, SingletonArgumentInfo<AttunementTypeArgument>> ATTUNEMENT_TYPE = registerSingleton(
            "attunement_type", AttunementTypeArgument.class, SingletonArgumentInfo.contextFree(AttunementTypeArgument::attunementType));
    public static final IRegistryItem<ArgumentTypeInfo<?, ?>, SingletonArgumentInfo<AttunementValueArgument>> ATTUNEMENT_VALUE = registerSingleton(
            "attunement_value", AttunementValueArgument.class, SingletonArgumentInfo.contextFree(AttunementValueArgument::value));
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static final IRegistryItem<ArgumentTypeInfo<?, ?>, ArgumentTypeInfo<ResourceKeyArgumentPM<ResourceKey<?>>, ResourceKeyArgumentPM.Info<ResourceKey<?>>.Template>> RESOURCE_KEY = registerInfo(
            "resource_key", ResourceKeyArgumentPM.class, new ResourceKeyArgumentPM.Info());

    private static <T extends ArgumentType<?>> IRegistryItem<ArgumentTypeInfo<?, ?>, SingletonArgumentInfo<T>> registerSingleton(
            String name, Class<T> clazz, SingletonArgumentInfo<T> info) {
        return Services.ARGUMENT_TYPES_REGISTRY.registerSingleton(name, clazz, info);
    }

    private static <T extends ArgumentType<?>, M extends ArgumentTypeInfo.Template<T>> IRegistryItem<ArgumentTypeInfo<?, ?>, ArgumentTypeInfo<T, M>> registerInfo(
            String name, Class<T> clazz, ArgumentTypeInfo<T, M> info) {
        return Services.ARGUMENT_TYPES_REGISTRY.registerInfo(name, clazz, info);
    }
}
