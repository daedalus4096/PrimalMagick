package com.verdantartifice.primalmagick.platform.registries;

import com.mojang.brigadier.arguments.ArgumentType;
import com.verdantartifice.primalmagick.common.commands.arguments.ArgumentTypeRegistration;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.platform.services.registries.IArgumentTypeRegistryService;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Neoforge implementation of the command argument type registry service.
 *
 * @author Daedalus4096
 */
public class ArgumentTypeRegistryServiceNeoforge extends AbstractRegistryServiceNeoforge<ArgumentTypeInfo<?, ?>> implements IArgumentTypeRegistryService {
    @Override
    protected Supplier<DeferredRegister<ArgumentTypeInfo<?, ?>>> getDeferredRegisterSupplier() {
        return ArgumentTypeRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<ArgumentTypeInfo<?, ?>> getRegistry() {
        return BuiltInRegistries.COMMAND_ARGUMENT_TYPE;
    }

    @Override
    public <T extends ArgumentType<?>> IRegistryItem<ArgumentTypeInfo<?, ?>, SingletonArgumentInfo<T>> registerSingleton(
            String name, Class<T> clazz, SingletonArgumentInfo<T> info) {
        return this.register(name, () -> ArgumentTypeInfos.registerByClass(clazz, info));
    }

    @Override
    public <T extends ArgumentType<?>, M extends ArgumentTypeInfo.Template<T>> IRegistryItem<ArgumentTypeInfo<?, ?>, ArgumentTypeInfo<T, M>> registerInfo(
            String name, Class<T> clazz, ArgumentTypeInfo<T, M> info) {
        return this.register(name, () -> ArgumentTypeInfos.registerByClass(clazz, info));
    }
}
