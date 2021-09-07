package com.verdantartifice.primalmagic.common.entities.projectiles;

import java.util.Random;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.client.fx.FxDispatcher;
import com.verdantartifice.primalmagic.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagic.common.items.entities.ManaArrowItem;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
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
        if (this.level.isClientSide) {
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
            Random rng = this.level.random;
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
}
