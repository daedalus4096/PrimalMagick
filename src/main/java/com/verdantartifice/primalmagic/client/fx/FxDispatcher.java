package com.verdantartifice.primalmagic.client.fx;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.verdantartifice.primalmagic.client.fx.particles.ParticleTypesPM;
import com.verdantartifice.primalmagic.client.fx.particles.SpellBoltParticleData;
import com.verdantartifice.primalmagic.common.sounds.SoundsPM;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Create client-side special effects, usually involving particles.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class FxDispatcher {
    public static final FxDispatcher INSTANCE = new FxDispatcher();
    
    protected static final Map<BlockPos, Particle> PROP_MARKER_PARTICLES = new HashMap<>();
    
    protected World getWorld() {
    	Minecraft mc = Minecraft.getInstance();
        return mc.world;
    }
    
    public void wandPoof(double x, double y, double z, int color, boolean sound, Direction side) {
        Color c = new Color(color);
        float r = c.getRed() / 255.0F;
        float g = c.getGreen() / 255.0F;
        float b = c.getBlue() / 255.0F;
        this.wandPoof(x, y, z, r, g, b, sound, side);
    }
    
    public void wandPoof(double x, double y, double z, float r, float g, float b, boolean sound, Direction side) {
        // Release a cluster of poof clouds when transforming a block with a wand
    	Minecraft mc = Minecraft.getInstance();
    	World world = this.getWorld();
        Random rng = world.rand;
        if (sound) {
            getWorld().playSound(x, y, z, SoundsPM.POOF.get(), SoundCategory.BLOCKS, 1.0F, 1.0F + (float)rng.nextGaussian() * 0.05F, false);
        }
        for (int index = 0; index < 8 + rng.nextInt(3); index++) {
            double dx = (rng.nextFloat() * 0.05D) * (rng.nextBoolean() ? 1 : -1);
            double dy = (rng.nextFloat() * 0.05D) * (rng.nextBoolean() ? 1 : -1);
            double dz = (rng.nextFloat() * 0.05D) * (rng.nextBoolean() ? 1 : -1);
            if (side != null) {
                dx += (side.getXOffset() * 0.1D);
                dy += (side.getYOffset() * 0.1D);
                dz += (side.getZOffset() * 0.1D);
            }
            Particle p = mc.particles.addParticle(ParticleTypesPM.WAND_POOF.get(), x + dx * 2.0D, y + dy * 2.0D, z + dz * 2.0D, dx / 2.0D, dy / 2.0D, dz / 2.0D);
            if (p != null) {
                p.setColor(r, g, b);
            }
        }
    }
    
    public void manaSparkle(double x1, double y1, double z1, double x2, double y2, double z2, int maxAge, int color) {
        Color c = new Color(color);
        float r = c.getRed() / 255.0F;
        float g = c.getGreen() / 255.0F;
        float b = c.getBlue() / 255.0F;
        this.manaSparkle(x1, y1, z1, x2, y2, z2, maxAge, r, g, b);
    }
    
    public void manaSparkle(double x1, double y1, double z1, double x2, double y2, double z2, int maxAge, float r, float g, float b) {
        // Show a particle when draining a mana font with a wand
    	Minecraft mc = Minecraft.getInstance();
        double vx = (x2 - x1) / (double)maxAge;
        double vy = (y2 - y1) / (double)maxAge;
        double vz = (z2 - z1) / (double)maxAge;
        Particle p = mc.particles.addParticle(ParticleTypesPM.MANA_SPARKLE.get(), x1, y1, z1, vx, vy, vz);
        if (p != null) {
            p.setColor(r, g, b);
            p.setMaxAge(maxAge);
        }
    }
    
    public void spellTrail(double x, double y, double z, int color) {
        Color c = new Color(color);
        float r = c.getRed() / 255.0F;
        float g = c.getGreen() / 255.0F;
        float b = c.getBlue() / 255.0F;
        this.spellTrail(x, y, z, r, g, b);
    }
    
    public void spellTrail(double x, double y, double z, float r, float g, float b) {
        // Show a particle trailing behind a spell projectile
    	Minecraft mc = Minecraft.getInstance();
        Particle p = mc.particles.addParticle(ParticleTypesPM.SPELL_SPARKLE.get(), x, y, z, 0.0D, 0.0D, 0.0D);
        if (p != null) {
            p.setColor(r, g, b);
        }
    }
    
    public void spellImpact(double x, double y, double z, int radius, int color) {
        Color c = new Color(color);
        float r = c.getRed() / 255.0F;
        float g = c.getGreen() / 255.0F;
        float b = c.getBlue() / 255.0F;
        this.spellImpact(x, y, z, radius, r, g, b);
    }
    
    public void spellImpact(double x, double y, double z, int radius, float r, float g, float b) {
        // Show a cluster of particles at the impact point of a spell
    	Minecraft mc = Minecraft.getInstance();
    	World world = this.getWorld();
        Random rng = world.rand;
        int count = (15 + rng.nextInt(11)) * radius;
        for (int index = 0; index < count; index++) {
            double dx = (rng.nextFloat() * 0.035D * radius) * (rng.nextBoolean() ? 1 : -1);
            double dy = (rng.nextFloat() * 0.035D * radius) * (rng.nextBoolean() ? 1 : -1);
            double dz = (rng.nextFloat() * 0.035D * radius) * (rng.nextBoolean() ? 1 : -1);
            Particle p = mc.particles.addParticle(ParticleTypesPM.SPELL_SPARKLE.get(), x, y, z, dx, dy, dz);
            if (p != null) {
                p.setColor(r, g, b);
            }
        }
    }
    
    public void teleportArrival(double x, double y, double z) {
        // Show a cluster of particles at the point where a player arrives from a teleport spell; similar to Ender Pearl effect
        World world = getWorld();
        for (int i = 0; i < 32; i++) {
            world.addParticle(ParticleTypes.PORTAL, x, y + world.rand.nextDouble() * 2.0D, z, world.rand.nextGaussian(), 0.0D, world.rand.nextGaussian());
        }
    }
    
    public void spellBolt(double sx, double sy, double sz, double tx, double ty, double tz, int color) {
        Color c = new Color(color);
        float r = c.getRed() / 255.0F;
        float g = c.getGreen() / 255.0F;
        float b = c.getBlue() / 255.0F;
        this.spellBolt(sx, sy, sz, tx, ty, tz, r, g, b);
    }
    
    public void spellBolt(double sx, double sy, double sz, double tx, double ty, double tz, float r, float g, float b) {
        // Show a spell bolt "particle"
    	Minecraft mc = Minecraft.getInstance();
        Particle p = mc.particles.addParticle(new SpellBoltParticleData(tx, ty, tz), sx, sy, sz, 0.0D, 0.0D, 0.0D);
        if (p != null) {
            p.setColor(r, g, b);
        }
    }
    
    public void offeringChannel(double sx, double sy, double sz, double tx, double ty, double tz, ItemStack stack) {
        // Show a trail of particles between the ritual offering and the altar
    	Minecraft mc = Minecraft.getInstance();
        mc.particles.addParticle(new ItemParticleData(ParticleTypesPM.OFFERING.get(), stack), sx, sy, sz, tx, ty, tz);
    }
    
    public void propMarker(BlockPos pos) {
        // Show a marker above a ritual prop's position and save it for later manual canceling
    	Minecraft mc = Minecraft.getInstance();
        Particle p = mc.particles.addParticle(ParticleTypesPM.PROP_MARKER.get(), pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
        this.removePropMarker(pos);
        PROP_MARKER_PARTICLES.put(pos, p);
    }
    
    public void removePropMarker(BlockPos pos) {
        // Expire and remove the marker particle for the given position
        if (PROP_MARKER_PARTICLES.containsKey(pos)) {
            Particle oldParticle = PROP_MARKER_PARTICLES.remove(pos);
            oldParticle.setExpired();
        }
    }
}
