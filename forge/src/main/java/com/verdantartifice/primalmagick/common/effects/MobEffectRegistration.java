package com.verdantartifice.primalmagick.common.effects;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;

/**
 * Deferred registry for mod mob effects in Forge.
 * 
 * @author Daedalus4096
 */
public class MobEffectRegistration {
    private static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, Constants.MOD_ID);

    public static DeferredRegister<MobEffect> getDeferredRegister() {
        return EFFECTS;
    }
    
    public static void init() {
        EFFECTS.register(PrimalMagick.getModLoadingContext().getModEventBus());
    }
}
