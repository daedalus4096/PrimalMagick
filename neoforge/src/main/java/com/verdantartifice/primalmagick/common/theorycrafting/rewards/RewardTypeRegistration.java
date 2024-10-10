package com.verdantartifice.primalmagick.common.theorycrafting.rewards;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

/**
 * Deferred registry for mod theorycrafting reward types in Neoforge.
 *
 * @author Daedalus4096
 */
public class RewardTypeRegistration {
    public static final Registry<RewardType<?>> TYPES = new RegistryBuilder<>(RegistryKeysPM.PROJECT_REWARD_TYPES)
            .sync(true)
            .create();
    private static final DeferredRegister<RewardType<?>> DEFERRED_TYPES = DeferredRegister.create(TYPES, Constants.MOD_ID);

    public static DeferredRegister<RewardType<?>> getDeferredRegister() {
        return DEFERRED_TYPES;
    }

    public static void init() {
        DEFERRED_TYPES.register(PrimalMagick.getEventBus());
    }
}
