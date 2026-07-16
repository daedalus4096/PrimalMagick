package com.verdantartifice.primalmagick.client.fx;

import com.verdantartifice.primalmagick.client.fx.particles.ManaSparkleParticleData;
import com.verdantartifice.primalmagick.client.fx.particles.ParticleTypesPM;
import com.verdantartifice.primalmagick.client.fx.particles.PotionExplosionParticleData;
import com.verdantartifice.primalmagick.client.fx.particles.SpellBoltParticleData;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.BubbleParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ARGB;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Create client-side special effects, usually involving particles.
 * 
 * @author Daedalus4096
 */
public class FxDispatcher {
    public static final FxDispatcher INSTANCE = new FxDispatcher();
    public static final int DEFAULT_PROP_MARKER_LIFETIME = 6000;
    
    protected static final Map<BlockPos, Particle> PROP_MARKER_PARTICLES = new HashMap<>();

    @NotNull
    protected Level getWorld() {
        Minecraft mc = Minecraft.getInstance();
        return Objects.requireNonNull(mc.level);
    }
    
    public void wandPoof(double x, double y, double z, int color, boolean sound, Direction side) {
        // Release a cluster of poof clouds when transforming a block with a wand
        Level level = this.getWorld();
        RandomSource rng = level.getRandom();
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
            level.addParticle(ColorParticleOption.create(ParticleTypesPM.WAND_POOF.get(), color), x + dx * 2.0D, y + dy * 2.0D, z + dz * 2.0D, dx / 2.0D, dy / 2.0D, dz / 2.0D);
        }
    }
    
    public void manaSparkle(double x1, double y1, double z1, double x2, double y2, double z2, int maxAge, int color, double phase) {
        // Show a particle when draining a mana font with a wand
        double vx = (x2 - x1) / (double)maxAge;
        double vy = (y2 - y1) / (double)maxAge;
        double vz = (z2 - z1) / (double)maxAge;
        getWorld().addParticle(new ManaSparkleParticleData(color, maxAge, phase), x1, y1, z1, vx, vy, vz);
    }
    
    public void spellTrail(double x, double y, double z, int color) {
        // Show a particle trailing behind a spell projectile
        getWorld().addParticle(ColorParticleOption.create(ParticleTypesPM.SPELL_SPARKLE.get(), color), x, y, z, 0.0D, 0.0D, 0.0D);
    }
    
    public void spellImpact(double x, double y, double z, int radius, int color) {
        // Show a cluster of particles at the impact point of a spell
        Level level = this.getWorld();
        RandomSource rng = level.getRandom();
        int count = (15 + rng.nextInt(11)) * radius;
        ColorParticleOption options = ColorParticleOption.create(ParticleTypesPM.SPELL_SPARKLE.get(), color);
        for (int index = 0; index < count; index++) {
            double dx = (rng.nextFloat() * 0.035D * radius) * (rng.nextBoolean() ? 1 : -1);
            double dy = (rng.nextFloat() * 0.035D * radius) * (rng.nextBoolean() ? 1 : -1);
            double dz = (rng.nextFloat() * 0.035D * radius) * (rng.nextBoolean() ? 1 : -1);
            level.addParticle(options, x, y, z, dx, dy, dz);
        }
    }
    
    public void ritualGlow(BlockPos pos, int color) {
        Level level = this.getWorld();
        RandomSource rng = level.getRandom();
        ColorParticleOption options = ColorParticleOption.create(ParticleTypesPM.SPELL_SPARKLE.get(), color);

        int count = (10 + rng.nextInt(6));
        for (int index = 0; index < count; index++) {
            level.addParticle(options, pos.getX() + rng.nextDouble(), pos.getY() + 1.0D, pos.getZ() + rng.nextDouble(), 0.0D, 0.075D, 0.0D);
        }
    }
    
    public void spellcraftingGlow(BlockPos pos, double dy, int color) {
        float r = ARGB.red(color) / 255.0F;
        float g = ARGB.green(color) / 255.0F;
        float b = ARGB.blue(color) / 255.0F;
        this.spellcraftingGlow(pos, dy, r, g, b);
    }
    
    public void spellcraftingGlow(BlockPos pos, double dy, float r, float g, float b) {
        Level world = this.getWorld();
        RandomSource rng = world.getRandom();
        ColorParticleOption options = ColorParticleOption.create(ParticleTypesPM.SPELL_SPARKLE.get(), r, g, b);

        int count = (3 + rng.nextInt(3));
        for (int index = 0; index < count; index++) {
            double x = pos.getX() + 0.40625D + (rng.nextDouble() * 0.1875D);
            double y = pos.getY() + dy;
            double z = pos.getZ() + 0.40625D + (rng.nextDouble() * 0.1875D);
            world.addParticle(options, x, y, z, 0.0D, 0.0375D, 0.0D);
        }
    }
    
    public void teleportArrival(double x, double y, double z) {
        // Show a cluster of particles at the point where a player arrives from a teleport spell; similar to Ender Pearl effect
        Level world = getWorld();
        RandomSource random = world.getRandom();
        for (int i = 0; i < 32; i++) {
            world.addParticle(ParticleTypes.PORTAL, x, y + random.nextDouble() * 2.0D, z, random.nextGaussian(), 0.0D, random.nextGaussian());
        }
    }

    public void spellBolt(Vec3 source, Vec3 target, int color) {
        // Show a translucent spell bolt "particle"
        getWorld().addParticle(new SpellBoltParticleData(target, ARGB.color(0.5F, color)), source.x(), source.y(), source.z(), 0D, 0D, 0D);
    }
    
    public void offeringChannel(double sx, double sy, double sz, double tx, double ty, double tz, ItemStack stack) {
        // Show a trail of particles between the ritual offering and the altar
        Minecraft mc = Minecraft.getInstance();
        mc.particleEngine.createParticle(new ItemParticleOption(ParticleTypesPM.OFFERING.get(), ItemStackTemplate.fromNonEmptyStack(stack)), sx, sy, sz, tx, ty, tz);
    }
    
    public void propMarker(BlockPos pos, int lifetimeTicks) {
        // Show a marker above a ritual prop's position and save it for later manual canceling
        Minecraft mc = Minecraft.getInstance();
        Particle p = mc.particleEngine.createParticle(ParticleTypesPM.PROP_MARKER.get(), pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
        if (p != null) {
            p.setLifetime(lifetimeTicks);
        }
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
        // Create a gently falling particle of pixie dust
        getWorld().addParticle(ColorParticleOption.create(ParticleTypesPM.SPELL_SPARKLE.get(), color), x, y, z, 0.0D, -0.1D, 0.0D);
    }
    
    public void crucibleBubble(double x, double y, double z, float r, float g, float b) {
        Minecraft mc = Minecraft.getInstance();
        Particle p = mc.particleEngine.createParticle(ParticleTypes.BUBBLE, x, y, z, 0.0D, 0.0D, 0.0D);
        if (p instanceof BubbleParticle bubbleParticle) {
            bubbleParticle.setColor(r, g, b);
        }
    }
    
    public void potionExplosion(double x, double y, double z, int color, boolean isInstant) {
        Minecraft mc = Minecraft.getInstance();
        Level world = this.getWorld();
        world.playLocalSound(x, y, z, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.NEUTRAL, 4.0F, (1.0F + (world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.2F) * 0.7F, false);
        world.addParticle(ParticleTypes.EXPLOSION_EMITTER, x, y, z, 1.0D, 0.0D, 0.0D);
        if (mc.level != null) {
            mc.level.addParticle(new PotionExplosionParticleData(color, isInstant), x, y, z, 1.0D, 0.0D, 0.0D);
        }
    }
    
    public void manaArrowTrail(double x, double y, double z, double dx, double dy, double dz, int color) {
        // Draw a trail of mana sparkles in the flight path of a mana-tinged arrow
        getWorld().addParticle(ColorParticleOption.create(ParticleTypesPM.SPELL_SPARKLE.get(), color), x, y, z, dx, dy, dz);
    }
    
    public void spellcraftingRuneU(double x, double y, double z, double dx, double dy, double dz, int color) {
        getWorld().addParticle(ColorParticleOption.create(ParticleTypesPM.SPELLCRAFTING_RUNE_U.get(), color), x, y, z, dx, dy, dz);
    }
    
    public void spellcraftingRuneV(double x, double y, double z, double dx, double dy, double dz, int color) {
        getWorld().addParticle(ColorParticleOption.create(ParticleTypesPM.SPELLCRAFTING_RUNE_V.get(), color), x, y, z, dx, dy, dz);
    }
    
    public void spellcraftingRuneT(double x, double y, double z, double dx, double dy, double dz, int color) {
        getWorld().addParticle(ColorParticleOption.create(ParticleTypesPM.SPELLCRAFTING_RUNE_T.get(), color), x, y, z, dx, dy, dz);
    }
    
    public void spellcraftingRuneD(double x, double y, double z, double dx, double dy, double dz, int color) {
        getWorld().addParticle(ColorParticleOption.create(ParticleTypesPM.SPELLCRAFTING_RUNE_D.get(), color), x, y, z, dx, dy, dz);
    }
    
    public void bloodDrop(double x, double y, double z) {
        Minecraft mc = Minecraft.getInstance();
        mc.particleEngine.createParticle(ParticleTypesPM.DRIPPING_BLOOD_DROP.get(), x, y, z, 0, 0, 0);
    }
}
