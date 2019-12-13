package com.verdantartifice.primalmagic.common.entities.projectiles;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagic.common.spells.packages.ISpellPackage;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class SpellProjectileEntity extends ThrowableEntity {
    protected static final DataParameter<Integer> COLOR = EntityDataManager.createKey(SpellProjectileEntity.class, DataSerializers.VARINT);
    
    protected final ISpellPackage spell;
    
    public SpellProjectileEntity(EntityType<? extends ThrowableEntity> type, World worldIn) {
        super(type, worldIn);
        this.spell = null;
    }
    
    public SpellProjectileEntity(World world, LivingEntity thrower, ISpellPackage spell) {
        super(EntityTypesPM.SPELL_PROJECTILE, thrower, world);
        this.spell = spell;
        if (spell != null && spell.getPayload() != null) {
            this.setColor(spell.getPayload().getSource().getColor());
        }
    }
    
    public SpellProjectileEntity(World world, double x, double y, double z, ISpellPackage spell) {
        super(EntityTypesPM.SPELL_PROJECTILE, x, y, z, world);
        this.spell = spell;
        if (spell != null && spell.getPayload() != null) {
            this.setColor(spell.getPayload().getSource().getColor());
        }
    }
    
    @Nullable
    public ISpellPackage getSpell() {
        return this.spell;
    }
    
    public int getColor() {
        return this.getDataManager().get(COLOR).intValue();
    }
    
    protected void setColor(int color) {
        this.getDataManager().set(COLOR, Integer.valueOf(color));
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        // TODO execute spell payload on impacted target
        if (!this.world.isRemote) {
            this.remove();
        }
    }

    @Override
    protected void registerData() {
        this.getDataManager().register(COLOR, 0xFFFFFF);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
