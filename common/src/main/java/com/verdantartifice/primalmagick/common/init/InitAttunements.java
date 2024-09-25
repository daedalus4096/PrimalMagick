package com.verdantartifice.primalmagick.common.init;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.attunements.AttunementManager;
import com.verdantartifice.primalmagick.common.attunements.AttunementThreshold;
import com.verdantartifice.primalmagick.common.sources.Sources;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;

/**
 * Point of registration for attunement related data.
 * 
 * @author Daedalus4096
 */
public class InitAttunements {
    public static void initAttunementAttributeModifiers() {
        AttunementManager.registerAttributeModifier(Sources.EARTH, AttunementThreshold.LESSER, Attributes.ATTACK_SPEED, ResourceUtils.loc("effect.attunement.earth.lesser"), 0.2D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        AttunementManager.registerAttributeModifier(Sources.SKY, AttunementThreshold.LESSER, Attributes.MOVEMENT_SPEED, ResourceUtils.loc("effect.attunement.sky.lesser"), 0.2D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        AttunementManager.registerAttributeModifier(Sources.SEA, AttunementThreshold.LESSER, ForgeMod.SWIM_SPEED.getHolder().get(), ResourceUtils.loc("effect.attunement.sea.lesser"), 0.6D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }
}
