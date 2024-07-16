package com.verdantartifice.primalmagick.common.rituals;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.rituals.steps.AbstractRitualStep;
import com.verdantartifice.primalmagick.common.rituals.steps.UniversalRitualStep;

import net.minecraft.nbt.CompoundTag;

/**
 * Factory class for deserializing ritual steps from saved NBT data.
 * 
 * @author Daedalus4096
 */
public class RitualStepFactory {
    @Nullable
    public static AbstractRitualStep deserializeNBT(CompoundTag nbt) {
        RitualStepType type = RitualStepType.fromName(nbt.getString("Type"));
        if (type == null) {
            return null;
        } else if (type == RitualStepType.UNIVERSAL_PROP) {
            UniversalRitualStep step = new UniversalRitualStep();
            step.deserializeNBT(nbt);
            return step;
        } else {
            RecipeRitualStep step = new RecipeRitualStep();
            step.deserializeNBT(nbt);
            return step;
        }
    }
}
