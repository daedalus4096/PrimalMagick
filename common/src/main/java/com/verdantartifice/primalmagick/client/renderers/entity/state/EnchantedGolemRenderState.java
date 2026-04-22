package com.verdantartifice.primalmagick.client.renderers.entity.state;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.Crackiness;

public class EnchantedGolemRenderState extends LivingEntityRenderState {
    public float attackTicksRemaining;
    public Crackiness.Level crackiness;

    public EnchantedGolemRenderState() {
        this.crackiness = Crackiness.Level.NONE;
    }
}
