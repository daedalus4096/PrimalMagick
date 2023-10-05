package com.verdantartifice.primalmagick.client.fx;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import com.verdantartifice.primalmagick.client.fx.particles.ParticleTypesPM;
import com.verdantartifice.primalmagick.client.fx.particles.PotionExplosionParticleData;
import com.verdantartifice.primalmagick.client.fx.particles.SpellBoltParticleData;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Create client-side special effects, usually involving particles.
 * 
 * @author Daedalus4096
 */
public class FxDispatcher {
    public static final FxDispatcher INSTANCE = new FxDispatcher();
    public static final int DEFAULT_PROP_MARKER_LIFETIME = 6000;
    
    protected static final Map<BlockPos, Particle> PROP_MARKER_PARTICLES = new HashMap<>();
    
    protected Level getWorld() {
        Minecraft mc = Minecraft.getInstance();
        return mc.level;
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
        Level world = this.getWorld();
        RandomSource rng = world.random;
        if (sound) {
            this.getWorld().playLocalSound(x, y, z, SoundsPM.POOF.get(), SoundSource.BLOCKS, 1.0F, 1.0F + (float)rng.nextGaussian() * 0.05F, false);
        }
        for (int index = 0; index < 8 + rng.nextInt(3); index++) {
            double dx = (rng.nextFloat() * 0.05D) * (rng.nextBoolean() ? 1 : -1);
            double dy = (rng.nextFloat() * 0.05D) * (rng.nextBoolean() ? 1 : -1);
            double dz = (rng.nextFloat() * 0.05D) * (rng.nextBoolean() ? 1 : -1);
            if (side != null) {
                dx += (side.getStepX() * 0.1D);
                dy += (side.getStepY() * 0.1D);
                dz += (side.getStepZ() * 0.1D);
            }
            Particle p = mc.particleEngine.createParticle(ParticleTypesPM.WAND_POOF.get(), x + dx * 2.0D, y + dy * 2.0D, z + dz * 2.0D, dx / 2.0D, dy / 2.0D, dz / 2.0D);
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
        Particle p = mc.particleEngine.createParticle(ParticleTypesPM.MANA_SPARKLE.get(), x1, y1, z1, vx, vy, vz);
        if (p != null) {
            p.setColor(r, g, b);
            p.setLifetime(maxAge);
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
        Particle p = mc.particleEngine.createParticle(ParticleTypesPM.SPELL_SPARKLE.get(), x, y, z, 0.0D, 0.0D, 0.0D);
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
        Level world = this.getWorld();
        RandomSource rng = world.random;
        int count = (15 + rng.nextInt(11)) * radius;
        for (int index = 0; index < count; index++) {
            double dx = (rng.nextFloat() * 0.035D * radius) * (rng.nextBoolean() ? 1 : -1);
            double dy = (rng.nextFloat() * 0.035D * radius) * (rng.nextBoolean() ? 1 : -1);
            double dz = (rng.nextFloat() * 0.035D * radius) * (rng.nextBoolean() ? 1 : -1);
            Particle p = mc.particleEngine.createParticle(ParticleTypesPM.SPELL_SPARKLE.get(), x, y, z, dx, dy, dz);
            if (p != null) {
                p.setColor(r, g, b);
            }
        }
    }
    
    public void ritualGlow(BlockPos pos, int color) {
        Minecraft mc = Minecraft.getInstance();
        Level world = this.getWorld();
        RandomSource rng = world.random;

        Color c = new Color(color);
        float r = c.getRed() / 255.0F;
        float g = c.getGreen() / 255.0F;
        float b = c.getBlue() / 255.0F;

        int count = (10 + rng.nextInt(6));
        for (int index = 0; index < count; index++) {
            Particle p = mc.particleEngine.createParticle(ParticleTypesPM.SPELL_SPARKLE.get(), pos.getX() + rng.nextDouble(), pos.getY() + 1.0D, pos.getZ() + rng.nextDouble(), 0.0D, 0.075D, 0.0D);
            if (p != null) {
                p.setColor(r, g, b);
            }
        }
    }
    
    public void spellcraftingGlow(BlockPos pos, int color) {
        Color c = new Color(color);
        float r = c.getRed() / 255.0F;
        float g = c.getGreen() / 255.0F;
        float b = c.getBlue() / 255.0F;
        this.spellcraftingGlow(pos, r, g, b);
    }
    
    public void spellcraftingGlow(BlockPos pos, float r, float g, float b) {
        Minecraft mc = Minecraft.getInstance();
        Level world = this.getWorld();
        RandomSource rng = world.random;

        int count = (3 + rng.nextInt(3));
        for (int index = 0; index < count; index++) {
            double x = pos.getX() + 0.40625D + (rng.nextDouble() * 0.1875D);
            double y = pos.getY() + 1.125D;
            double z = pos.getZ() + 0.40625D + (rng.nextDouble() * 0.1875D);
            Particle p = mc.particleEngine.createParticle(ParticleTypesPM.SPELL_SPARKLE.get(), x, y, z, 0.0D, 0.0375D, 0.0D);
            if (p != null) {
                p.setColor(r, g, b);
            }
        }
    }
    
    public void teleportArrival(double x, double y, double z) {
        // Show a cluster of particles at the point where a player arrives from a teleport spell; similar to Ender Pearl effect
        Level world = getWorld();
        for (int i = 0; i < 32; i++) {
            world.addParticle(ParticleTypes.PORTAL, x, y + world.random.nextDouble() * 2.0D, z, world.random.nextGaussian(), 0.0D, world.random.nextGaussian());
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
        Particle p = mc.particleEngine.createParticle(new SpellBoltParticleData(tx, ty, tz), sx, sy, sz, 0.0D, 0.0D, 0.0D);
        if (p != null) {
            p.setColor(r, g, b);
        }
    }
    
    public void offeringChannel(double sx, double sy, double sz, double tx, double ty, double tz, ItemStack stack) {
        // Show a trail of particles between the ritual offering and the altar
        Minecraft mc = Minecraft.getInstance();
        mc.particleEngine.createParticle(new ItemParticleOption(ParticleTypesPM.OFFERING.get(), stack), sx, sy, sz, tx, ty, tz);
    }
    
    public void propMarker(BlockPos pos) {
        this.propMarker(pos, DEFAULT_PROP_MARKER_LIFETIME);
    }
    
    public void propMarker(BlockPos pos, int lifetimeTicks) {
        // Show a marker above a ritual prop's position and save it for later manual canceling
        Minecraft mc = Minecraft.getInstance();
        Particle p = mc.particleEngine.createParticle(ParticleTypesPM.PROP_MARKER.get(), pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
        p.setLifetime(lifetimeTicks);
        this.removePropMarker(pos);
        PROP_MARKER_PARTICLES.put(pos, p);
    }
    
    public void removePropMarker(BlockPos pos) {
        // Expire and remove the marker particle for the given position
        if (PROP_MARKER_PARTICLES.containsKey(pos)) {
            Particle oldParticle = PROP_MARKER_PARTICLES.remove(pos);
            oldParticle.remove();
        }
    }
    
    public void pixieDust(double x, double y, double z, int color) {
        Color c = new Color(color);
        float r = c.getRed() / 255.0F;
        float g = c.getGreen() / 255.0F;
        float b = c.getBlue() / 255.0F;
        this.pixieDust(x, y, z, r, g, b);
    }
    
    public void pixieDust(double x, double y, double z, float r, float g, float b) {
        Minecraft mc = Minecraft.getInstance();
        Particle p = mc.particleEngine.createParticle(ParticleTypesPM.SPELL_SPARKLE.get(), x, y, z, 0.0D, -0.1D, 0.0D);
        if (p != null) {
            p.setColor(r, g, b);
        }
    }
    
    public void crucibleBubble(double x, double y, double z, int color) {
        Color c = new Color(color);
        float r = c.getRed() / 255.0F;
        float g = c.getGreen() / 255.0F;
        float b = c.getBlue() / 255.0F;
        this.crucibleBubble(x, y, z, r, g, b);
    }
    
    public void crucibleBubble(double x, double y, double z, float r, float g, float b) {
        Minecraft mc = Minecraft.getInstance();
        Particle p = mc.particleEngine.createParticle(ParticleTypes.BUBBLE, x, y, z, 0.0D, 0.0D, 0.0D);
        if (p != null) {
            p.setColor(r, g, b);
        }
    }
    
    public void potionExplosion(double x, double y, double z, int color, boolean isInstant) {
        Color c = new Color(color);
        float r = c.getRed() / 255.0F;
        float g = c.getGreen() / 255.0F;
        float b = c.getBlue() / 255.0F;
        this.potionExplosion(x, y, z, r, g, b, isInstant);
    }
    
    public void potionExplosion(double x, double y, double z, float r, float g, float b, boolean isInstant) {
        Minecraft mc = Minecraft.getInstance();
        Level world = this.getWorld();
        world.playLocalSound(x, y, z, SoundEvents.GENERIC_EXPLODE, SoundSource.NEUTRAL, 4.0F, (1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.2F) * 0.7F, false);
        world.addParticle(ParticleTypes.EXPLOSION_EMITTER, x, y, z, 1.0D, 0.0D, 0.0D);
        Particle p = mc.particleEngine.createParticle(new PotionExplosionParticleData(isInstant), x, y, z, 1.0D, 0.0D, 0.0D);
        if (p != null) {
            p.setColor(r, g, b);
        }
    }
    
    public void manaArrowTrail(double x, double y, double z, double dx, double dy, double dz, int color) {
        Color c = new Color(color);
        float r = c.getRed() / 255.0F;
        float g = c.getGreen() / 255.0F;
        float b = c.getBlue() / 255.0F;
        this.manaArrowTrail(x, y, z, dx, dy, dz, r, g, b);
    }
    
    public void manaArrowTrail(double x, double y, double z, double dx, double dy, double dz, float r, float g, float b) {
        Minecraft mc = Minecraft.getInstance();
        Particle p = mc.particleEngine.createParticle(ParticleTypesPM.SPELL_SPARKLE.get(), x, y, z, dx, dy, dz);
        if (p != null) {
            p.setColor(r, g, b);
        }
    }
    
    public void spellcraftingRuneU(double x, double y, double z, double dx, double dy, double dz, int color) {
        Color c = new Color(color);
        float r = c.getRed() / 255.0F;
        float g = c.getGreen() / 255.0F;
        float b = c.getBlue() / 255.0F;
        this.spellcraftingRuneU(x, y, z, dx, dy, dz, r, g, b);
    }
    
    public void spellcraftingRuneU(double x, double y, double z, double dx, double dy, double dz, float r, float g, float b) {
        Minecraft mc = Minecraft.getInstance();
        Particle p = mc.particleEngine.createParticle(ParticleTypesPM.SPELLCRAFTING_RUNE_U.get(), x, y, z, dx, dy, dz);
        if (p != null) {
            p.setColor(r, g, b);
        }
    }
    
    public void spellcraftingRuneV(double x, double y, double z, double dx, double dy, double dz, int color) {
        Color c = new Color(color);
        float r = c.getRed() / 255.0F;
        float g = c.getGreen() / 255.0F;
        float b = c.getBlue() / 255.0F;
        this.spellcraftingRuneV(x, y, z, dx, dy, dz, r, g, b);
    }
    
    public void spellcraftingRuneV(double x, double y, double z, double dx, double dy, double dz, float r, float g, float b) {
        Minecraft mc = Minecraft.getInstance();
        Particle p = mc.particleEngine.createParticle(ParticleTypesPM.SPELLCRAFTING_RUNE_V.get(), x, y, z, dx, dy, dz);
        if (p != null) {
            p.setColor(r, g, b);
        }
    }
    
    public void spellcraftingRuneT(double x, double y, double z, double dx, double dy, double dz, int color) {
        Color c = new Color(color);
        float r = c.getRed() / 255.0F;
        float g = c.getGreen() / 255.0F;
        float b = c.getBlue() / 255.0F;
        this.spellcraftingRuneT(x, y, z, dx, dy, dz, r, g, b);
    }
    
    public void spellcraftingRuneT(double x, double y, double z, double dx, double dy, double dz, float r, float g, float b) {
        Minecraft mc = Minecraft.getInstance();
        Particle p = mc.particleEngine.createParticle(ParticleTypesPM.SPELLCRAFTING_RUNE_T.get(), x, y, z, dx, dy, dz);
        if (p != null) {
            p.setColor(r, g, b);
        }
    }
    
    public void spellcraftingRuneD(double x, double y, double z, double dx, double dy, double dz, int color) {
        Color c = new Color(color);
        float r = c.getRed() / 255.0F;
        float g = c.getGreen() / 255.0F;
        float b = c.getBlue() / 255.0F;
        this.spellcraftingRuneD(x, y, z, dx, dy, dz, r, g, b);
    }
    
    public void spellcraftingRuneD(double x, double y, double z, double dx, double dy, double dz, float r, float g, float b) {
        Minecraft mc = Minecraft.getInstance();
        Particle p = mc.particleEngine.createParticle(ParticleTypesPM.SPELLCRAFTING_RUNE_D.get(), x, y, z, dx, dy, dz);
        if (p != null) {
            p.setColor(r, g, b);
        }
    }
    
    public void bloodDrop(double x, double y, double z) {
        Minecraft mc = Minecraft.getInstance();
        mc.particleEngine.createParticle(ParticleTypesPM.DRIPPING_BLOOD_DROP.get(), x, y, z, 0, 0, 0);
    }
}
