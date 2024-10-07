package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.effects.MobEffectRegistration;
import com.verdantartifice.primalmagick.platform.services.IMobEffectService;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Forge implementation of the mob effect registry service.
 *
 * @author Daedalus4096
 */
public class MobEffectServiceForge extends AbstractRegistryServiceForge<MobEffect> implements IMobEffectService {
    @Override
    protected Supplier<DeferredRegister<MobEffect>> getDeferredRegisterSupplier() {
        return MobEffectRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<MobEffect> getRegistry() {
        return BuiltInRegistries.MOB_EFFECT;
    }
}
