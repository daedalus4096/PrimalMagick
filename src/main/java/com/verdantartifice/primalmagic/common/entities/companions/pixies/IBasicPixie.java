package com.verdantartifice.primalmagic.common.entities.companions.pixies;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;

/**
 * Interface identifying a basic pixie, of the weakest stature.
 * 
 * @author Daedalus4096
 */
public interface IBasicPixie extends IPixie {
    public static AttributeModifierMap.MutableAttribute getAttributeModifiers() {
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 6.0D).createMutableAttribute(Attributes.FLYING_SPEED, 0.6D).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    public default int getSpellPower() {
        return 1;
    }
}
