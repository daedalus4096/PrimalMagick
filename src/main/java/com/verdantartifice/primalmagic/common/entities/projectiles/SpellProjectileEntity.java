package com.verdantartifice.primalmagic.common.entities.projectiles;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.entities.EntityTypesPM;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class SpellProjectileEntity extends ThrowableEntity {
    public SpellProjectileEntity(EntityType<? extends ThrowableEntity> type, World worldIn) {
        super(type, worldIn);
    }
    
    public SpellProjectileEntity(World world, LivingEntity thrower) {
        super(EntityTypesPM.SPELL_PROJECTILE, thrower, world);
    }
    
    public SpellProjectileEntity(World world, double x, double y, double z) {
        super(EntityTypesPM.SPELL_PROJECTILE, x, y, z, world);
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        PrimalMagic.LOGGER.debug("Spell projectile impact");
        // TODO execute spell payload on impacted target
        if (!this.world.isRemote) {
            this.remove();
        }
    }

    @Override
    protected void registerData() {
        // TODO Auto-generated method stub

    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
