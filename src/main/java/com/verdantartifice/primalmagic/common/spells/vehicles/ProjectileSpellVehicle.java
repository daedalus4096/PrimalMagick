package com.verdantartifice.primalmagic.common.spells.vehicles;

import com.verdantartifice.primalmagic.common.entities.projectiles.SpellProjectileEntity;
import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;

import net.minecraft.entity.player.PlayerEntity;
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
            SpellProjectileEntity projectile = new SpellProjectileEntity(world, caster, spell);
            projectile.shoot(caster, caster.rotationPitch, caster.rotationYaw, 0.0F, 1.5F, 0.0F);
            world.addEntity(projectile);
        }
    }
    
    @Override
    public int getBaseManaCostModifier() {
        return 5;
    }
}
