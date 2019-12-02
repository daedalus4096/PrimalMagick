package com.verdantartifice.primalmagic.common.spells.packages;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class SelfSpellPackage extends AbstractSpellPackage {
    public static final String TYPE = "self";
    
    public SelfSpellPackage() {
        super();
    }
    
    public SelfSpellPackage(String name) {
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
            RayTraceResult result = new EntityRayTraceResult(caster);
            this.payload.execute(result, this, world, caster);
        }
    }
}
