package com.verdantartifice.primalmagic.common.entities.misc;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.common.entities.ai.goals.LongDistanceRangedAttackGoal;
import com.verdantartifice.primalmagic.common.entities.projectiles.SinCrashEntity;
import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.sounds.SoundsPM;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;
import com.verdantartifice.primalmagic.common.spells.mods.BurstSpellMod;
import com.verdantartifice.primalmagic.common.spells.payloads.BloodDamageSpellPayload;
import com.verdantartifice.primalmagic.common.spells.vehicles.ProjectileSpellVehicle;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IChargeableMob;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.Difficulty;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;

/**
 * Definition of an inner demon entity, a boss monster summoned via a sanguine crucible.
 * 
 * @author Daedalus4096
 */
@OnlyIn(value = Dist.CLIENT, _interface = IChargeableMob.class)
public class InnerDemonEntity extends MonsterEntity implements IRangedAttackMob, IChargeableMob {
    protected final ServerBossInfo bossInfo = (ServerBossInfo)(new ServerBossInfo(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS)).setDarkenSky(true);
    protected boolean isSuffocating = false;
    protected SpellPackage spellCache;

    public InnerDemonEntity(EntityType<? extends InnerDemonEntity> type, World worldIn) {
        super(type, worldIn);
        this.getNavigator().setCanSwim(true);
        this.experienceValue = 50;
    }
    
    public static AttributeModifierMap.MutableAttribute getAttributeModifiers() {
        return MonsterEntity.func_234295_eP_().createMutableAttribute(Attributes.MAX_HEALTH, 400.0D).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.23D).createMutableAttribute(Attributes.FOLLOW_RANGE, 40.0D).createMutableAttribute(Attributes.ARMOR, 4.0D).createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0.5D).createMutableAttribute(Attributes.ATTACK_DAMAGE, 12.0D).createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 1.5D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new InnerDemonEntity.SinCrashGoal(this, 100, 600));
        this.goalSelector.addGoal(3, new LongDistanceRangedAttackGoal<>(this, 1.0D, 30, 4.0F, 16.0F, true));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 0, false, false, null));
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
    }

    @Override
    public void setCustomName(ITextComponent name) {
        super.setCustomName(name);
        this.bossInfo.setName(this.getDisplayName());
    }
    
    @Nonnull
    protected SpellPackage createSpellPackage() {
        SpellPackage spell = new SpellPackage("Blood Ball");
        ProjectileSpellVehicle vehicle = new ProjectileSpellVehicle();
        spell.setVehicle(vehicle);
        BloodDamageSpellPayload payload = new BloodDamageSpellPayload();
        payload.getProperty("power").setValue(4);
        spell.setPayload(payload);
        spell.setPrimaryMod(new BurstSpellMod(2, 2));
        return spell;
    }

    @Nonnull
    protected SpellPackage getSpellPackage() {
        if (this.spellCache == null) {
            this.spellCache = this.createSpellPackage();
        }
        return this.spellCache;
    }

    @Override
    public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor) {
        this.getSpellPackage().cast(this.world, this, null);
    }

    @Override
    public boolean isCharged() {
        return true;
    }

    @Override
    protected void updateAITasks() {
        if (!this.world.isRemote) {
            // Explode if suffocating
            if (this.isSuffocating && this.ticksExisted % 20 == 0) {
                Explosion.Mode mode = ForgeEventFactory.getMobGriefingEvent(this.world, this) ? Explosion.Mode.DESTROY : Explosion.Mode.NONE;
                this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), 7.0F, false, mode);
                this.isSuffocating = false;
            }
            
            this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        }
        super.updateAITasks();
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        boolean retVal = super.attackEntityAsMob(entityIn);
        entityIn.setFire(5);
        return retVal;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source == DamageSource.DROWN || source.getTrueSource() instanceof InnerDemonEntity) {
            return false;
        } else {
            if (source == DamageSource.IN_WALL) {
                this.isSuffocating = true;
            }
            return super.attackEntityFrom(source, amount);
        }
    }

    @Override
    public void addTrackingPlayer(ServerPlayerEntity player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void removeTrackingPlayer(ServerPlayerEntity player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    protected void dropSpecialItems(DamageSource source, int looting, boolean recentlyHitIn) {
        super.dropSpecialItems(source, looting, recentlyHitIn);
        ItemEntity itemEntity = this.entityDropItem(ItemsPM.HALLOWED_ORB.get());
        if (itemEntity != null) {
            itemEntity.setNoDespawn();
        }
    }

    @Override
    public void checkDespawn() {
        if (this.world.getDifficulty() == Difficulty.PEACEFUL && this.isDespawnPeaceful()) {
            this.remove();
        } else {
            this.idleTime = 0;
        }
    }

    @Override
    public boolean onLivingFall(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    public boolean isPotionApplicable(EffectInstance potioneffectIn) {
        return false;
    }

    @Override
    protected boolean canBeRidden(Entity entityIn) {
        return false;
    }

    @Override
    public boolean canChangeDimension() {
        return false;
    }
    
    public void doSinCrash() {
        if (!this.world.isRemote) {
            double demonPosX = this.getPosX();
            double demonPosY = this.getPosYEye();
            double demonPosZ = this.getPosZ();
            Difficulty difficulty = this.world.getDifficulty();
            int crashCount = difficulty == Difficulty.EASY ? 1 : (difficulty == Difficulty.HARD ? 3 : 2);
            for (int index = 0; index < crashCount; index++) {
                double dx = this.world.rand.nextGaussian() * 16.0D * (this.world.rand.nextBoolean() ? 1.0D : -1.0D);
                double dy = -1.0D * (double)this.getEyeHeight();
                double dz = this.world.rand.nextGaussian() * 16.0D * (this.world.rand.nextBoolean() ? 1.0D : -1.0D);
                SinCrashEntity crash = new SinCrashEntity(this.world, this, dx, dy, dz);
                crash.setLocationAndAngles(demonPosX, demonPosY, demonPosZ, 0.0F, 0.0F);
                this.world.addEntity(crash);
            }
            this.world.playSound(null, demonPosX, demonPosY, demonPosZ, SoundsPM.WHISPERS.get(), SoundCategory.HOSTILE, 1.0F, 1.0F);
        }
    }
    
    public static class SinCrashGoal extends Goal {
        protected final InnerDemonEntity demon;
        protected final int startDelayTicks;
        protected final int cooldownTicks;
        protected int timer = -1;
        
        public SinCrashGoal(InnerDemonEntity demon, int startDelayTicks, int cooldownTicks) {
            this.demon = demon;
            this.startDelayTicks = startDelayTicks;
            this.cooldownTicks = cooldownTicks;
            this.timer = this.startDelayTicks;
        }

        @Override
        public boolean shouldExecute() {
            LivingEntity target = this.demon.getAttackTarget();
            return (target != null && target.isAlive());
        }

        @Override
        public void resetTask() {
            this.timer = this.startDelayTicks;
        }

        @Override
        public void tick() {
            if (--this.timer == 0) {
                this.demon.doSinCrash();
                this.timer = this.cooldownTicks;
            } else if (this.timer < 0) {
                this.timer = this.cooldownTicks;
            }
        }
    }
}
