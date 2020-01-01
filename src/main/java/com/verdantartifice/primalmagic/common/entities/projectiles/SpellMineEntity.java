package com.verdantartifice.primalmagic.common.entities.projectiles;

import java.util.UUID;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

public class SpellMineEntity extends Entity {
    protected static final DataParameter<Integer> COLOR = EntityDataManager.createKey(SpellMineEntity.class, DataSerializers.VARINT);
    protected static final DataParameter<Boolean> ARMED = EntityDataManager.createKey(SpellMineEntity.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Integer> LIFESPAN = EntityDataManager.createKey(SpellMineEntity.class, DataSerializers.VARINT);
    
    protected static final int DURATION_FACTOR = 4800;
    protected static final int ARMING_TIME = 60;
    
    protected SpellPackage spell;
    protected LivingEntity caster;
    protected UUID casterId;
    
    public SpellMineEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
        this.spell = null;
    }
    
    public SpellMineEntity(World world, Vec3d pos, LivingEntity caster, SpellPackage spell, int duration) {
        super(EntityTypesPM.SPELL_MINE, world);
        this.setPosition(pos.x, pos.y, pos.z);
        this.spell = spell;
        this.caster = caster;
        this.casterId = caster.getUniqueID();
        this.setLifespan(duration * DURATION_FACTOR);
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
    
    public boolean isArmed() {
        return this.getDataManager().get(ARMED).booleanValue();
    }
    
    protected void setArmed(boolean armed) {
        this.getDataManager().set(ARMED, Boolean.valueOf(armed));
    }
    
    protected int getLifespan() {
        return this.getDataManager().get(LIFESPAN).intValue();
    }
    
    protected void setLifespan(int ticks) {
        this.getDataManager().set(LIFESPAN, Integer.valueOf(ticks));
    }
    
    @Nullable
    public LivingEntity getCaster() {
        if (this.caster == null && this.casterId != null && this.world instanceof ServerWorld) {
            Entity entity = ((ServerWorld)this.world).getEntityByUuid(this.casterId);
            if (entity instanceof LivingEntity) {
                this.caster = (LivingEntity)entity;
            } else {
                this.casterId = null;
            }
        }
        return this.caster;
    }

    @Override
    protected void registerData() {
        this.getDataManager().register(COLOR, 0xFFFFFF);
        this.getDataManager().register(ARMED, Boolean.FALSE);
        this.getDataManager().register(LIFESPAN, 0);
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        this.caster = null;
        if (compound.contains("Caster", 10)) {
            this.casterId = NBTUtil.readUniqueId(compound.getCompound("Caster"));
        }
        this.spell = null;
        if (compound.contains("Spell", 10)) {
            this.spell = new SpellPackage(compound.getCompound("Spell"));
        }
        if (this.spell != null && !this.spell.isValid()) {
            this.spell = null;
        }
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        if (this.casterId != null) {
            compound.put("Caster", NBTUtil.writeUniqueId(this.casterId));
        }
        if (this.spell != null) {
            compound.put("Spell", this.spell.serializeNBT());
        }
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        // TODO Auto-generated method stub
        super.tick();
        if (!this.world.isRemote && (this.spell == null || !this.spell.isValid() || this.getCaster() == null)) {
            this.remove();
        }
        if (this.ticksExisted > this.getLifespan()) {
            this.remove();
        }
        if (this.isAlive()) {
            if (!this.isArmed() && this.ticksExisted >= ARMING_TIME) {
                this.setArmed(true);
            }
            if (this.isArmed() && this.ticksExisted % 5 == 0) {
                // TODO scan for living entities within a block
                // TODO if found, execute the spell payload on them then remove self
            }
        }
    }
}
