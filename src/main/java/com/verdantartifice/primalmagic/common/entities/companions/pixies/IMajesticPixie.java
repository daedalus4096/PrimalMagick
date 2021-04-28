package com.verdantartifice.primalmagic.common.entities.companions.pixies;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;

/**
 * Interface identifying a majestic pixie, of the highest stature.
 * 
 * @author Daedalus4096
 */
public interface IMajesticPixie extends IPixie {
    public static AttributeModifierMap.MutableAttribute getAttributeModifiers() {
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 12.0D).createMutableAttribute(Attributes.FLYING_SPEED, 0.6D).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3D);
    }
    
    public default int getSpellPower() {
        return 5;
    }
}
