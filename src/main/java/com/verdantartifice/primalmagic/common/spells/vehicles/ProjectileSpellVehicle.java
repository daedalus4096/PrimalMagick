package com.verdantartifice.primalmagic.common.spells.vehicles;

import com.verdantartifice.primalmagic.common.entities.projectiles.SpellProjectileEntity;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class ProjectileSpellVehicle extends AbstractSpellVehicle {
    public static final String TYPE = "projectile";
    
    @Override
    protected String getPackageType() {
        return TYPE;
    }

    @Override
    public void execute(SpellPackage spell, World world, PlayerEntity caster) {
        if (spell.getPayload() != null) {
            SpellProjectileEntity projectile = new SpellProjectileEntity(world, caster, spell);
            projectile.shoot(caster, caster.rotationPitch, caster.rotationYaw, 0.0F, 1.5F, 0.0F);
            world.addEntity(projectile);
        }
    }
}
