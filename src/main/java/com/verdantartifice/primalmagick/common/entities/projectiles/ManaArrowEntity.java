package com.verdantartifice.primalmagick.common.entities.projectiles;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.effects.EffectsPM;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.items.entities.ManaArrowItem;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Definition for a mana-tinged arrow.  Has minor secondary effects in addition to regular arrow damage.
 * 
 * @author Daedalus4096
 */
public class ManaArrowEntity extends AbstractArrow {
    protected static final EntityDataAccessor<String> SOURCE_TAG = SynchedEntityData.defineId(ManaArrowEntity.class, EntityDataSerializers.STRING);
    private static final ItemStack DEFAULT_ARROW_STACK = new ItemStack(Items.ARROW);

    public ManaArrowEntity(EntityType<? extends ManaArrowEntity> type, Level level) {
        super(type, level);
    }
    
    public ManaArrowEntity(Level level, LivingEntity shooter, Source source, ItemStack pickupItem, ItemStack weapon) {
        super(EntityTypesPM.MANA_ARROW.get(), shooter, level, pickupItem, weapon);
        this.setSource(source);
    }
    
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(SOURCE_TAG, "");
    }

    public void setSource(Source source) {
        this.entityData.set(SOURCE_TAG, source.getId().toString());
        if (source == Sources.SKY) {
            this.setNoGravity(true);
        } else if (source == Sources.INFERNAL) {
            this.igniteForSeconds(100);
        } else if (source == Sources.HALLOWED) {
            this.setBaseDamage(this.getBaseDamage() + 1);
        }
    }
    
    @Nullable
    public Source getSource() {
        return Sources.get(ResourceLocation.parse(this.entityData.get(SOURCE_TAG)));
    }
    
    @Override
    protected ItemStack getPickupItem() {
        Item item = ManaArrowItem.SOURCE_MAPPING.get(this.getSource());
        return new ItemStack(item == null ? Items.ARROW : item);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getSource() == Sources.SKY && this.isNoGravity() && this.isInWater()) {
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
        if (source == Sources.SEA) {
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 50));
        } else if (source == Sources.SUN) {
            target.addEffect(new MobEffectInstance(MobEffects.GLOWING, 50));
            if (target.isInvertedHealAndHarm()) {
                target.igniteForSeconds(3);
            }
        } else if (source == Sources.MOON) {
            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 50));
        } else if (source == Sources.BLOOD) {
            target.addEffect(new MobEffectInstance(EffectsPM.BLEEDING.getHolder().get(), 50));
        } else if (source == Sources.VOID) {
            target.addEffect(new MobEffectInstance(MobEffects.WITHER, 50));
        } else if (source == Sources.HALLOWED) {
            if (target.isInvertedHealAndHarm()) {
                target.igniteForSeconds(3);
            }
        }
    }

    @Override
    protected float getWaterInertia() {
        Source source = this.getSource();
        return source == Sources.SEA || source == Sources.BLOOD ? 0.99F : super.getWaterInertia();
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return DEFAULT_ARROW_STACK;
    }

    @Override
    protected void doKnockback(LivingEntity pEntity, DamageSource pDamageSource) {
        float baseForce = this.getSource() == Sources.EARTH ? 2.0F : 0.0F;
        double force = (double)(
            this.getWeaponItem() != null && this.level() instanceof ServerLevel serverlevel
                ? EnchantmentHelper.modifyKnockback(serverlevel, this.getWeaponItem(), pEntity, pDamageSource, baseForce)
                : baseForce
        );
        if (force > 0.0) {
            double inertia = Math.max(0.0, 1.0 - pEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
            Vec3 pushVec = this.getDeltaMovement().multiply(1.0, 0.0, 1.0).normalize().scale(force * 0.6 * inertia);
            if (pushVec.lengthSqr() > 0.0) {
                pEntity.push(pushVec.x, 0.1, pushVec.z);
            }
        }
    }
}
