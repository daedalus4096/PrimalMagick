package com.verdantartifice.primalmagic.common.spells.vehicles;

import java.util.Arrays;
import java.util.List;

import com.verdantartifice.primalmagic.common.entities.projectiles.SpellProjectileEntity;
import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;
import com.verdantartifice.primalmagic.common.spells.mods.ForkSpellMod;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ProjectileSpellVehicle extends AbstractSpellVehicle {
    public static final String TYPE = "projectile";
    protected static final CompoundResearchKey RESEARCH = CompoundResearchKey.from(SimpleResearchKey.parse("SPELL_VEHICLE_PROJECTILE"));
    
    public static CompoundResearchKey getResearch() {
        return RESEARCH;
    }
    
    @Override
    protected String getVehicleType() {
        return TYPE;
    }

    @Override
    public void execute(SpellPackage spell, World world, PlayerEntity caster) {
        if (spell.getPayload() != null) {
            ForkSpellMod forkMod = spell.getMod(ForkSpellMod.class, "forks");
            Vec3d baseLookVector = caster.getLook(1.0F);
            List<Vec3d> lookVectors;
            if (forkMod == null) {
                lookVectors = Arrays.asList(baseLookVector.normalize());
            } else {
                lookVectors = forkMod.getDirectionUnitVectors(baseLookVector, world.rand);
            }
            
            for (Vec3d lookVector : lookVectors) {
                SpellProjectileEntity projectile = new SpellProjectileEntity(world, caster, spell);
                projectile.shoot(lookVector.x, lookVector.y, lookVector.z, 1.5F, 0.0F);
                world.addEntity(projectile);
            }
        }
    }
    
    @Override
    public int getBaseManaCostModifier() {
        return 5;
    }
}
