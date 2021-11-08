package com.verdantartifice.primalmagick.common.entities.companions.pixies;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;

/**
 * Interface identifying a basic pixie, of the weakest stature.
 * 
 * @author Daedalus4096
 */
public interface IBasicPixie extends IPixie {
    public static AttributeSupplier.Builder getAttributeModifiers() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 6.0D).add(Attributes.FLYING_SPEED, 0.6D).add(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    public default int getSpellPower() {
        return 1;
    }
}
