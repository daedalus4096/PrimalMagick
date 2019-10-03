package com.verdantartifice.primalmagic.client.fx;

import java.awt.Color;
import java.util.Random;

import com.verdantartifice.primalmagic.common.sounds.SoundsPM;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FxDispatcher {
    public static final FxDispatcher INSTANCE = new FxDispatcher();
    
    protected World getWorld() {
        return Minecraft.getInstance().world;
    }
    
    public void poof(double x, double y, double z, int color, boolean sound, Direction side) {
        Color c = new Color(color);
        float r = c.getRed() / 255.0F;
        float g = c.getGreen() / 255.0F;
        float b = c.getBlue() / 255.0F;
        this.poof(x, y, z, r, g, b, sound, side);
    }
    
    public void poof(double x, double y, double z, float r, float g, float b, boolean sound, Direction side) {
        Random rng = getWorld().rand;
        if (sound) {
            getWorld().playSound(x, y, z, SoundsPM.POOF, SoundCategory.BLOCKS, 1.0F, 1.0F + (float)rng.nextGaussian() * 0.05F, false);
        }
    }
}
