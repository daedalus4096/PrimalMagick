package com.verdantartifice.primalmagick.common.commands.arguments;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Deferred registry for mod command argument types in Neoforge.
 *
 * @author Daedalus4096
 */
public class ArgumentTypeRegistration {
    private static final DeferredRegister<ArgumentTypeInfo<?, ?>> TYPES = DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE, Constants.MOD_ID);

    public static DeferredRegister<ArgumentTypeInfo<?, ?>> getDeferredRegister() {
        return TYPES;
    }

    public static void init() {
        ArgumentTypesPM.init();
        TYPES.register(PrimalMagick.getEventBus());
    }
}
