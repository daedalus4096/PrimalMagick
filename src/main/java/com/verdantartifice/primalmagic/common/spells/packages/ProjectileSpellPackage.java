package com.verdantartifice.primalmagic.common.spells.packages;

import com.verdantartifice.primalmagic.common.entities.projectiles.SpellProjectileEntity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class ProjectileSpellPackage extends AbstractSpellPackage {
    public static final String TYPE = "projectile";
    
    public ProjectileSpellPackage() {
        super();
    }
    
    public ProjectileSpellPackage(String name) {
        super(name);
    }

    @Override
    protected String getPackageType() {
        return TYPE;
    }

    @Override
    public void cast(World world, PlayerEntity caster) {
        super.cast(world, caster);
        if (this.payload != null) {
            SpellProjectileEntity projectile = new SpellProjectileEntity(world, caster, this);
            projectile.shoot(caster, caster.rotationPitch, caster.rotationYaw, 0.0F, 1.5F, 0.0F);
            world.addEntity(projectile);
        }
    }
}
