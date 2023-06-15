package com.verdantartifice.primalmagick.common.entities.projectiles;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.effects.EffectsPM;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.items.entities.ManaArrowItem;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

/**
 * Definition for a mana-tinged arrow.  Has minor secondary effects in addition to regular arrow damage.
 * 
 * @author Daedalus4096
 */
public class ManaArrowEntity extends AbstractArrow {
    protected static final EntityDataAccessor<String> SOURCE_TAG = SynchedEntityData.defineId(ManaArrowEntity.class, EntityDataSerializers.STRING);

    public ManaArrowEntity(EntityType<? extends ManaArrowEntity> type, Level level) {
        super(type, level);
    }
    
    public ManaArrowEntity(Level level, double x, double y, double z) {
        super(EntityTypesPM.MANA_ARROW.get(), x, y, z, level);
    }
    
    public ManaArrowEntity(Level level, LivingEntity shooter) {
        super(EntityTypesPM.MANA_ARROW.get(), shooter, level);
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SOURCE_TAG, "");
    }

    public void setSource(Source source) {
        this.entityData.set(SOURCE_TAG, source.getTag());
        if (source == Source.EARTH) {
            this.setKnockback(this.getKnockback() + 2);
        } else if (source == Source.SKY) {
            this.setNoGravity(true);
        } else if (source == Source.INFERNAL) {
            this.setSecondsOnFire(100);
        } else if (source == Source.HALLOWED) {
            this.setBaseDamage(this.getBaseDamage() + 1);
        }
    }
    
    @Nullable
    public Source getSource() {
        return Source.getSource(this.entityData.get(SOURCE_TAG));
    }
    
    @Override
    protected ItemStack getPickupItem() {
        Item item = ManaArrowItem.SOURCE_MAPPING.get(this.getSource());
        return new ItemStack(item == null ? Items.ARROW : item);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getSource() == Source.SKY && this.isNoGravity() && this.isInWater()) {
            this.setNoGravity(false);
        }
        Level level = this.level();
        if (level.isClientSide) {
            if (this.inGround) {
                if (this.inGroundTime % 5 == 0) {
                    this.makeParticle(1);
                }
            } else {
                this.makeParticle(2);
            }
        }
    }

    protected void makeParticle(int count) {
        Source source = this.getSource();
        if (source != null && count > 0) {
            Level level = this.level();
            RandomSource rng = level.random;
            int color = source.getColor();
            for (int index = 0; index < count; index++) {
                // Generate mana sparkle particle in a random direction
                double dx = (rng.nextFloat() * 0.035D) * (rng.nextBoolean() ? 1 : -1);
                double dy = (rng.nextFloat() * 0.035D) * (rng.nextBoolean() ? 1 : -1);
                double dz = (rng.nextFloat() * 0.035D) * (rng.nextBoolean() ? 1 : -1);
                FxDispatcher.INSTANCE.manaArrowTrail(this.getX(), this.getY(), this.getZ(), dx, dy, dz, color);
            }
        }
    }

    @Override
    protected void doPostHurtEffects(LivingEntity target) {
        super.doPostHurtEffects(target);
        Source source = this.getSource();
        if (source == Source.SEA) {
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 50));
        } else if (source == Source.SUN) {
            target.addEffect(new MobEffectInstance(MobEffects.GLOWING, 50));
            if (target.getMobType() == MobType.UNDEAD) {
                target.setSecondsOnFire(3);
            }
        } else if (source == Source.MOON) {
            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 50));
        } else if (source == Source.BLOOD) {
            target.addEffect(new MobEffectInstance(EffectsPM.BLEEDING.get(), 50));
        } else if (source == Source.VOID) {
            target.addEffect(new MobEffectInstance(MobEffects.WITHER, 50));
        } else if (source == Source.HALLOWED) {
            if (target.getMobType() == MobType.UNDEAD) {
                target.setSecondsOnFire(3);
            }
        }
    }

    @Override
    protected float getWaterInertia() {
        Source source = this.getSource();
        return source == Source.SEA || source == Source.BLOOD ? 0.99F : super.getWaterInertia();
    }
}
