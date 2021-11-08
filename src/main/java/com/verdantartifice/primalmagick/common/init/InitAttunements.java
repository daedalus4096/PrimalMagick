package com.verdantartifice.primalmagick.common.init;

import com.verdantartifice.primalmagick.common.attunements.AttunementManager;
import com.verdantartifice.primalmagick.common.attunements.AttunementThreshold;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

/**
 * Point of registration for attunement related data.
 * 
 * @author Daedalus4096
 */
public class InitAttunements {
    public static void initAttunementAttributeModifiers() {
        AttunementManager.registerAttributeModifier(Source.EARTH, AttunementThreshold.LESSER, Attributes.ATTACK_SPEED, "43bdb563-6871-44a8-8326-71f6224944bf", 0.2D, AttributeModifier.Operation.MULTIPLY_TOTAL);
        AttunementManager.registerAttributeModifier(Source.SKY, AttunementThreshold.LESSER, Attributes.MOVEMENT_SPEED, "066743c3-eef5-476c-9a76-20badb1c5934", 0.2D, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}
