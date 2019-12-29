package com.verdantartifice.primalmagic.common.entities.projectiles;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.fx.SpellImpactPacket;
import com.verdantartifice.primalmagic.common.network.packets.fx.SpellTrailPacket;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;
import com.verdantartifice.primalmagic.common.spells.mods.BlastSpellMod;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class SpellProjectileEntity extends ThrowableEntity {
    protected static final DataParameter<Integer> COLOR = EntityDataManager.createKey(SpellProjectileEntity.class, DataSerializers.VARINT);
    
    protected final SpellPackage spell;
    
    public SpellProjectileEntity(EntityType<? extends ThrowableEntity> type, World worldIn) {
        super(type, worldIn);
        this.spell = null;
    }
    
    public SpellProjectileEntity(World world, LivingEntity thrower, SpellPackage spell) {
        super(EntityTypesPM.SPELL_PROJECTILE, thrower, world);
        this.spell = spell;
        if (spell != null && spell.getPayload() != null) {
            this.setColor(spell.getPayload().getSource().getColor());
        }
    }
    
    public SpellProjectileEntity(World world, double x, double y, double z, SpellPackage spell) {
        super(EntityTypesPM.SPELL_PROJECTILE, x, y, z, world);
        this.spell = spell;
        if (spell != null && spell.getPayload() != null) {
            this.setColor(spell.getPayload().getSource().getColor());
        }
    }
    
    @Nullable
    public SpellPackage getSpell() {
        return this.spell;
    }
    
    public int getColor() {
        return this.getDataManager().get(COLOR).intValue();
    }
    
    protected void setColor(int color) {
        this.getDataManager().set(COLOR, Integer.valueOf(color));
    }
    
    @Override
    public void tick() {
        super.tick();
        if (!this.world.isRemote && this.isAlive() && this.ticksExisted % 2 == 0 && this.spell != null && this.spell.getPayload() != null) {
            PacketHandler.sendToAllAround(
                    new SpellTrailPacket(this.posX, this.posY, this.posZ, this.spell.getPayload().getSource().getColor()), 
                    this.dimension, 
                    this.getPosition(), 
                    64.0D);
        }
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (!this.world.isRemote) {
            if (this.spell != null && this.spell.getPayload() != null) {
                BlastSpellMod blastMod = this.spell.getMod(BlastSpellMod.class, "power");
                int radius = blastMod == null ? 1 : blastMod.getPropertyValue("power");
                PacketHandler.sendToAllAround(
                        new SpellImpactPacket(this.posX, this.posY, this.posZ, radius, this.spell.getPayload().getSource().getColor()), 
                        this.dimension, 
                        new BlockPos(result.getHitVec()), 
                        64.0D);

                if (blastMod != null) {
                    for (RayTraceResult target : blastMod.getBlastTargets(result, this.world)) {
                        this.spell.getPayload().execute(target, result.getHitVec(), this.spell, this.world, this.getThrower());
                    }
                } else {
                    this.spell.getPayload().execute(result, null, this.spell, this.world, this.getThrower());
                }
            }
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
