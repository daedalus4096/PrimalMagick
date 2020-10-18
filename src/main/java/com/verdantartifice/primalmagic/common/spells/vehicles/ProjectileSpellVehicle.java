package com.verdantartifice.primalmagic.common.spells.vehicles;

import java.util.Arrays;
import java.util.List;

import com.verdantartifice.primalmagic.common.entities.projectiles.SpellProjectileEntity;
import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;
import com.verdantartifice.primalmagic.common.spells.mods.ForkSpellMod;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

/**
 * Definition of a projectile spell vehicle.  Projectiles are long range vehicles that have a travel
 * time and are affected by gravity.  They're like throwing a magic snowball.
 * 
 * @author Daedalus4096
 */
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
    public void execute(SpellPackage spell, World world, PlayerEntity caster, ItemStack spellSource) {
        if (spell.getPayload() != null) {
            ForkSpellMod forkMod = spell.getMod(ForkSpellMod.class, "forks");
            Vector3d baseLookVector = caster.getLook(1.0F);
            List<Vector3d> lookVectors;
            if (forkMod == null) {
                // If no Fork mod is in the spell package, use the caster's line of sight for the direction vector
                lookVectors = Arrays.asList(baseLookVector.normalize());
            } else {
                // If a Fork mod is in the spell package, calculate a direction vector for each fork, based on the caster's line of sight
                lookVectors = forkMod.getDirectionUnitVectors(baseLookVector, world.rand);
            }
            
            for (Vector3d lookVector : lookVectors) {
                // Instantiate the projectile entity and launch it into the world
                SpellProjectileEntity projectile = new SpellProjectileEntity(world, caster, spell, spellSource);
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
