package com.verdantartifice.primalmagick.client.renderers.entity.state;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PixieHouseRenderState extends LivingEntityRenderState {
    public float wiggle;
    @NotNull
    public ItemStack occupantStack = ItemStack.EMPTY;
    public boolean isOccupantPresent;
    public double occupantDx;
    public double occupantDz;
}
